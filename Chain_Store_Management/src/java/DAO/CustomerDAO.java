package DAO;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.Customer;
import model.Invoice;

public class CustomerDAO {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAO.class.getName());
    private DBContext dbContext;

    public CustomerDAO() {
        dbContext = new DBContext();
    }

    public DBContext getDBContext() {
        return dbContext;
    }

    // Normalize Vietnamese text by removing diacritics and converting to uppercase (as per your INSERT example)
    private String normalizeVietnamese(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toUpperCase().trim();
    }

    // Check if email exists
    public boolean isEmailExists(String email, int excludeCustomerID) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sql = "SELECT COUNT(*) FROM [Shop].[dbo].[Customers] WHERE Gmail = ? AND IsActive = 1 AND CustomerID != ?";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, excludeCustomerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Check if phone exists
    public boolean isPhoneExists(String phone, int excludeCustomerID) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sql = "SELECT COUNT(*) FROM [Shop].[dbo].[Customers] WHERE Phone = ? AND IsActive = 1 AND CustomerID != ?";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            stmt.setString(1, phone);
            stmt.setInt(2, excludeCustomerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Check if UserID exists in Customers
    private boolean isUserIdExists(int userId) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sql = "SELECT COUNT(*) FROM [Shop].[dbo].[Customers] WHERE UserID = ? AND IsActive = 1";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Get the maximum UserID from Users
    private int getMaxUserId() throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sql = "SELECT MAX(UserID) FROM [Shop].[dbo].[Users]";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int maxUserId = rs.getInt(1);
                    return maxUserId > 0 ? maxUserId : 1; // Default to 1 if no records
                }
            }
        }
        return 1; // Fallback if query fails
    }

    // Get the first available UserID from Users (increasing from max)
    private int getAvailableUserId() throws SQLException {
        int maxUserId = getMaxUserId();
        LOGGER.log(Level.INFO, "Searching for available UserID, starting from max: {0}", maxUserId);
        for (int i = maxUserId; i <= maxUserId + 10; i++) { // Tìm lên đến 10 ID mới để tránh lặp vô hạn
            // Kiểm tra UserID có tồn tại trong Users
            String sqlCheckUser = "SELECT COUNT(*) FROM [Shop].[dbo].[Users] WHERE UserID = ?";
            try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlCheckUser)) {
                stmt.setInt(1, i);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) { // UserID tồn tại trong Users
                        if (!isUserIdExists(i)) {
                            LOGGER.log(Level.INFO, "Found available UserID: {0}", i);
                            return i; // Trả về UserID hợp lệ và chưa sử dụng trong Customers
                        } else {
                            LOGGER.log(Level.INFO, "UserID {0} exists in Customers, skipping", i);
                        }
                    }
                }
            }
        }
        throw new SQLException("No available UserID found in the range of existing Users (up to " + (maxUserId + 10) + "). Please add more users or free up an existing UserID.");
    }

    // Get all customers (with pagination support)
    public List<Customer> getAllCustomers() throws SQLException {
        return searchCustomers(null, null, null, 0, Integer.MAX_VALUE);
    }

    // Get customer by ID
    public Customer getCustomerById(int customerID) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sql = "SELECT c.CustomerID, c.FullName, c.FullNameNormalized, c.Phone, c.Gmail, c.Gender, c.BirthDate, c.CreatedAt, c.Address, c.IsActive, c.UserID, c.img, "
                + "COALESCE(SUM(i.TotalAmount), 0) AS TotalSpent, lc.TierLevel AS MembershipLevel "
                + "FROM [Shop].[dbo].[Customers] c "
                + "LEFT JOIN Invoices i ON c.CustomerID = i.CustomerID "
                + "LEFT JOIN LoyaltyCustomers lc ON c.CustomerID = lc.CustomerID "
                + "WHERE c.CustomerID = ? AND c.IsActive = 1 "
                + "GROUP BY c.CustomerID, c.FullName, c.FullNameNormalized, c.Phone, c.Gmail, c.Gender, c.BirthDate, c.CreatedAt, c.Address, c.IsActive, c.UserID, c.img, lc.TierLevel";

        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerID(rs.getInt("CustomerID"));
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhone(rs.getString("Phone"));
                    customer.setGmail(rs.getString("Gmail"));
                    customer.setGender(rs.getString("Gender"));
                    customer.setBirthDate(rs.getDate("BirthDate"));
                    customer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setActive(rs.getBoolean("IsActive"));
                    customer.setUserId(rs.getInt("UserID"));
                    customer.setImg(rs.getString("img"));
                    customer.setTotalSpent(rs.getDouble("TotalSpent"));
                    customer.setMembershipLevel(rs.getString("MembershipLevel"));
                    LOGGER.log(Level.INFO, "Retrieved customer: ID={0}, FullName={1}, Normalized FullName={2}", 
                               new Object[]{customer.getCustomerID(), customer.getFullName(), rs.getString("FullNameNormalized")});
                    return customer;
                }
            }
        }
        return null;
    }

    // Get invoices for a customer by ID
    public List<Invoice> getCustomerInvoices(int customerID) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT i.InvoiceID, i.InvoiceDate, p.ProductName, id.Quantity, id.UnitPrice, id.DiscountPercent, "
                + "id.Quantity * id.UnitPrice * (1 - COALESCE(id.DiscountPercent, 0) / 100) AS LineTotal "
                + "FROM Invoices i "
                + "JOIN InvoiceDetails id ON i.InvoiceID = id.InvoiceID "
                + "JOIN Products p ON id.ProductID = p.ProductID "
                + "WHERE i.CustomerID = ? "
                + "ORDER BY i.InvoiceDate DESC";

        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceID(rs.getInt("InvoiceID"));
                    invoice.setInvoiceDate(rs.getTimestamp("InvoiceDate"));
                    invoice.setProductName(rs.getString("ProductName"));
                    invoice.setQuantity(rs.getDouble("Quantity"));
                    invoice.setUnitPrice(rs.getDouble("UnitPrice"));
                    invoice.setDiscountPercent(rs.getDouble("DiscountPercent"));
                    invoice.setLineTotal(rs.getDouble("LineTotal"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    // Search customers with optional filters and pagination
    public List<Customer> searchCustomers(String keyword, String gender, String membershipLevel, int offset, int pageSize) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        if (offset < 0) {
            LOGGER.log(Level.WARNING, "Negative offset provided: {0}, setting to 0", offset);
            offset = 0;
        }
        if (pageSize < 1) {
            LOGGER.log(Level.WARNING, "Invalid pageSize provided: {0}, setting to 3", pageSize);
            pageSize = 2;
        }

        LOGGER.log(Level.INFO, "Executing searchCustomers with keyword: {0}, gender: {1}, membershipLevel: {2}, offset: {3}, pageSize: {4}", 
                   new Object[]{keyword, gender, membershipLevel, offset, pageSize});

        List<Customer> customers = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.CustomerID, c.FullName, c.FullNameNormalized, c.Phone, c.Gmail, c.Gender, c.BirthDate, c.CreatedAt, c.Address, c.IsActive, c.UserID, c.img, "
                + "COALESCE(SUM(i.TotalAmount), 0) AS TotalSpent, lc.TierLevel AS MembershipLevel "
                + "FROM [Shop].[dbo].[Customers] c "
                + "LEFT JOIN Invoices i ON c.CustomerID = i.CustomerID "
                + "LEFT JOIN LoyaltyCustomers lc ON c.CustomerID = lc.CustomerID "
                + "WHERE c.IsActive = 1 "
        );
        List<Object> params = new ArrayList<>();

        // Chỉ áp dụng gender filter nếu không phải trường hợp đặc biệt (ví dụ: muốn tất cả)
        if (gender != null && !gender.trim().isEmpty() && !"all".equalsIgnoreCase(gender)) { 
            if (!List.of("Male", "Female", "Other").contains(gender)) {
                throw new IllegalArgumentException("Invalid gender value.");
            }
            sql.append(" AND c.Gender = ?");
            params.add(gender);
        }

        // Apply membership level filter
        if (membershipLevel != null && !membershipLevel.trim().isEmpty()) {
            if (!List.of("Bronze", "Silver", "Gold", "Platinum", "Diamond").contains(membershipLevel)) {
                throw new IllegalArgumentException("Invalid membership level value.");
            }
            sql.append(" AND lc.TierLevel = ?");
            params.add(membershipLevel);
        }

        // Apply keyword filter with flexible word matching using OR within each field
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (keyword.length() > 100) {
                throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
            }
            String normalizedKeyword = normalizeVietnamese(keyword);
            String[] keywords = normalizedKeyword.split("\\s+");
            sql.append(" AND (");
            boolean firstKeyword = true;
            for (String kw : keywords) {
                if (!firstKeyword) sql.append(" OR ");
                sql.append("(LOWER(c.FullNameNormalized) LIKE ? OR LOWER(c.Gmail) LIKE ? OR LOWER(c.Phone) LIKE ?)");
                String keywordPart = "%" + kw + "%";
                params.add(keywordPart);
                params.add(keywordPart);
                params.add(keywordPart);
                firstKeyword = false;
            }
            sql.append(")");
        }

        sql.append(" GROUP BY c.CustomerID, c.FullName, c.FullNameNormalized, c.Phone, c.Gmail, c.Gender, c.BirthDate, c.CreatedAt, c.Address, c.IsActive, c.UserID, c.img, lc.TierLevel ");
        sql.append(" ORDER BY c.CustomerID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageSize);

        LOGGER.log(Level.INFO, "Executing SQL: {0}", sql.toString());
        LOGGER.log(Level.INFO, "Parameters: {0}", params.stream().map(Object::toString).collect(Collectors.joining(", ")));

        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerID(rs.getInt("CustomerID"));
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhone(rs.getString("Phone"));
                    customer.setGmail(rs.getString("Gmail"));
                    customer.setGender(rs.getString("Gender"));
                    customer.setBirthDate(rs.getDate("BirthDate"));
                    customer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setActive(rs.getBoolean("IsActive"));
                    customer.setUserId(rs.getInt("UserID"));
                    customer.setImg(rs.getString("img"));
                    customer.setTotalSpent(rs.getDouble("TotalSpent"));
                    customer.setMembershipLevel(rs.getString("MembershipLevel"));
                    customers.add(customer);
                    LOGGER.log(Level.INFO, "Found customer: ID={0}, FullName={1}, Normalized FullName={2}", 
                               new Object[]{customer.getCustomerID(), customer.getFullName(), rs.getString("FullNameNormalized")});
                }
            }
        }
        LOGGER.log(Level.INFO, "Returning {0} customers for keyword: {1}", new Object[]{customers.size(), keyword});
        return customers;
    }

    // Get total customer count for pagination
    public int getTotalCustomerCount(String keyword, String gender, String membershipLevel) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT c.CustomerID) AS Total "
                + "FROM [Shop].[dbo].[Customers] c "
                + "LEFT JOIN LoyaltyCustomers lc ON c.CustomerID = lc.CustomerID "
                + "WHERE c.IsActive = 1 "
        );
        List<Object> params = new ArrayList<>();

        // Chỉ áp dụng gender filter nếu không phải trường hợp đặc biệt
        if (gender != null && !gender.trim().isEmpty() && !"all".equalsIgnoreCase(gender)) {
            if (!List.of("Male", "Female", "Other").contains(gender)) {
                throw new IllegalArgumentException("Invalid gender value.");
            }
            sql.append(" AND c.Gender = ?");
            params.add(gender);
        }

        // Apply membership level filter
        if (membershipLevel != null && !membershipLevel.trim().isEmpty()) {
            if (!List.of("Bronze", "Silver", "Gold", "Platinum", "Diamond").contains(membershipLevel)) {
                throw new IllegalArgumentException("Invalid membership level value.");
            }
            sql.append(" AND lc.TierLevel = ?");
            params.add(membershipLevel);
        }

        // Apply keyword filter with flexible word matching using OR within each field
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (keyword.length() > 100) {
                throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
            }
            String normalizedKeyword = normalizeVietnamese(keyword);
            String[] keywords = normalizedKeyword.split("\\s+");
            sql.append(" AND (");
            boolean firstKeyword = true;
            for (String kw : keywords) {
                if (!firstKeyword) sql.append(" OR ");
                sql.append("(LOWER(c.FullNameNormalized) LIKE ? OR LOWER(c.Gmail) LIKE ? OR LOWER(c.Phone) LIKE ?)");
                String keywordPart = "%" + kw + "%";
                params.add(keywordPart);
                params.add(keywordPart);
                params.add(keywordPart);
                firstKeyword = false;
            }
            sql.append(")");
        }

        LOGGER.log(Level.INFO, "Executing SQL for count: {0}", sql.toString());
        LOGGER.log(Level.INFO, "Parameters for count: {0}", params.stream().map(Object::toString).collect(Collectors.joining(", ")));

        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Total");
                }
            }
        }
        return 0;
    }

    // Insert new customer (no Users table insertion, auto-select UserID)
   // Trong CustomerDAO.java

// Insert new customer
public void insertCustomer(Customer customer) throws SQLException {
    if (dbContext.getConnection() == null) {
        throw new SQLException("Database connection is not established.");
    }

    try {
        if (isEmailExists(customer.getGmail(), 0)) {
            throw new SQLException("Email already exists.");
        }
        if (isPhoneExists(customer.getPhone(), 0)) {
            throw new SQLException("Phone number already exists.");
        }

        int userId = getAvailableUserId();

        String sqlCustomers = "INSERT INTO [Shop].[dbo].[Customers] (FullName, Phone, Address, Gender, BirthDate, CreatedAt, IsActive, UserID, FullNameNormalized, gmail, img) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlCustomers)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getGender());
            stmt.setDate(5, customer.getBirthDate());
            stmt.setTimestamp(6, customer.getCreatedAt());
            stmt.setBoolean(7, customer.isActive());
            stmt.setInt(8, userId);
            stmt.setString(9, normalizeVietnamese(customer.getFullName()).toUpperCase());
            stmt.setString(10, customer.getGmail());
            stmt.setString(11, customer.getImg()); // Lưu cột img
            stmt.executeUpdate();
        }
    } catch (SQLException e) {
        throw e;
    }
}

// Update existing customer
public void updateCustomer(Customer customer) throws SQLException {
    if (dbContext.getConnection() == null) {
        throw new SQLException("Database connection is not established.");
    }

    try {
        if (isEmailExists(customer.getGmail(), customer.getCustomerID())) {
            throw new SQLException("Email already exists.");
        }
        if (isPhoneExists(customer.getPhone(), customer.getCustomerID())) {
            throw new SQLException("Phone number already exists.");
        }

        String sqlCustomers = "UPDATE [Shop].[dbo].[Customers] SET FullName = ?, Phone = ?, Address = ?, Gender = ?, BirthDate = ?, CreatedAt = ?, IsActive = ?, UserID = ?, FullNameNormalized = ?, gmail = ?, img = ? " +
                             "WHERE CustomerID = ? AND IsActive = 1";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlCustomers)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getGender());
            stmt.setDate(5, customer.getBirthDate());
            stmt.setTimestamp(6, customer.getCreatedAt());
            stmt.setBoolean(7, customer.isActive());
            stmt.setInt(8, customer.getUserId());
            stmt.setString(9, normalizeVietnamese(customer.getFullName()).toUpperCase());
            stmt.setString(10, customer.getGmail());
            stmt.setString(11, customer.getImg()); // Cập nhật cột img
            stmt.setInt(12, customer.getCustomerID());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Customer not found or inactive.");
            }
        }
    } catch (SQLException e) {
        throw e;
    }
}

    // Delete customer (soft delete)
    public void deleteCustomer(int customerID) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sqlCustomers = "UPDATE [Shop].[dbo].[Customers] SET IsActive = 0 WHERE CustomerID = ?";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlCustomers)) {
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        }
    }
}