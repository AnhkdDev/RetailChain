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
import model.Customer;
import model.Invoice;
import org.mindrot.jbcrypt.BCrypt;

public class CustomerDAO {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAO.class.getName());
    private DBContext dbContext;

    public CustomerDAO() {
        dbContext = new DBContext();
    }

    public DBContext getDBContext() {
        return dbContext;
    }

    // Normalize Vietnamese text by removing diacritics and converting to lowercase
    private String normalizeVietnamese(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
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
        String sql = "SELECT c.CustomerID, c.FullName, c.Phone, u.Email, c.Gender, c.BirthDate, c.CreatedAt, c.Address, "
                + "COALESCE(SUM(i.TotalAmount), 0) AS TotalSpent, lc.TierLevel AS MembershipLevel "
                + "FROM Customers c "
                + "INNER JOIN Users u ON c.UserID = u.UserID "
                + "LEFT JOIN Invoices i ON c.CustomerID = i.CustomerID "
                + "LEFT JOIN LoyaltyCustomers lc ON c.CustomerID = lc.CustomerID "
                + "WHERE c.CustomerID = ? AND c.IsActive = 1 "
                + "GROUP BY c.CustomerID, c.FullName, c.Phone, u.Email, c.Gender, c.BirthDate, c.CreatedAt, c.Address, lc.TierLevel";

        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerID(rs.getInt("CustomerID"));
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhone(rs.getString("Phone"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setGender(rs.getString("Gender"));
                    customer.setBirthDate(rs.getDate("BirthDate"));
                    customer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setTotalSpent(rs.getDouble("TotalSpent"));
                    customer.setMembershipLevel(rs.getString("MembershipLevel"));
                    LOGGER.log(Level.INFO, "Retrieved customer: ID={0}, FullName={1}", 
                               new Object[]{customer.getCustomerID(), customer.getFullName()});
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
            pageSize = 3;
        }

        LOGGER.log(Level.INFO, "Executing searchCustomers with keyword: {0}, gender: {1}, membershipLevel: {2}, offset: {3}, pageSize: {4}", 
                   new Object[]{keyword, gender, membershipLevel, offset, pageSize});

        List<Customer> customers = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.CustomerID, c.FullName, c.Phone, u.Email, c.Gender, c.BirthDate, c.CreatedAt, c.Address, "
                + "COALESCE(SUM(i.TotalAmount), 0) AS TotalSpent, lc.TierLevel AS MembershipLevel "
                + "FROM Customers c "
                + "INNER JOIN Users u ON c.UserID = u.UserID "
                + "LEFT JOIN Invoices i ON c.CustomerID = i.CustomerID "
                + "LEFT JOIN LoyaltyCustomers lc ON c.CustomerID = lc.CustomerID "
                + "WHERE c.IsActive = 1 "
        );
        List<Object> params = new ArrayList<>();

        // Apply gender filter
        if (gender != null && !gender.trim().isEmpty()) {
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

        // Apply keyword filter
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (keyword.length() > 100) {
                throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
            }
            String normalizedKeyword = normalizeVietnamese(keyword);
            String[] keywords = normalizedKeyword.trim().split("\\s+");
            sql.append(" AND (");
            for (int i = 0; i < keywords.length; i++) {
                if (i > 0) sql.append(" OR ");
                sql.append("(LOWER(c.FullName) LIKE ? OR LOWER(u.Email) LIKE ? OR LOWER(c.Phone) LIKE ?)");
                params.add("%" + keywords[i] + "%");
                params.add("%" + keywords[i] + "%");
                params.add("%" + keywords[i] + "%");
            }
            sql.append(")");
        }

        sql.append(" GROUP BY c.CustomerID, c.FullName, c.Phone, u.Email, c.Gender, c.BirthDate, c.CreatedAt, c.Address, lc.TierLevel ");
        sql.append(" ORDER BY c.CustomerID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageSize);

        LOGGER.log(Level.INFO, "Executing SQL: {0}", sql.toString());
        LOGGER.log(Level.INFO, "Parameters: {0}", params.toString());

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
                    customer.setEmail(rs.getString("Email"));
                    customer.setGender(rs.getString("Gender"));
                    customer.setBirthDate(rs.getDate("BirthDate"));
                    customer.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setTotalSpent(rs.getDouble("TotalSpent"));
                    customer.setMembershipLevel(rs.getString("MembershipLevel"));
                    customers.add(customer);
                    LOGGER.log(Level.INFO, "Found customer: ID={0}, FullName={1}", 
                               new Object[]{customer.getCustomerID(), customer.getFullName()});
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
                + "FROM Customers c "
                + "INNER JOIN Users u ON c.UserID = u.UserID "
                + "LEFT JOIN LoyaltyCustomers lc ON c.CustomerID = lc.CustomerID "
                + "WHERE c.IsActive = 1 "
        );
        List<Object> params = new ArrayList<>();

        // Apply gender filter
        if (gender != null && !gender.trim().isEmpty()) {
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

        // Apply keyword filter
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (keyword.length() > 100) {
                throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
            }
            String normalizedKeyword = normalizeVietnamese(keyword);
            String[] keywords = normalizedKeyword.trim().split("\\s+");
            sql.append(" AND (");
            for (int i = 0; i < keywords.length; i++) {
                if (i > 0) sql.append(" OR ");
                sql.append("(LOWER(c.FullName) LIKE ? OR LOWER(u.Email) LIKE ? OR LOWER(c.Phone) LIKE ?)");
                params.add("%" + keywords[i] + "%");
                params.add("%" + keywords[i] + "%");
                params.add("%" + keywords[i] + "%");
            }
            sql.append(")");
        }

        LOGGER.log(Level.INFO, "Executing SQL for count: {0}", sql.toString());
        LOGGER.log(Level.INFO, "Parameters for count: {0}", params.toString());

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

    public void insertCustomer(Customer customer) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sqlUsers = "INSERT INTO Users (Email, PasswordHash, RoleID, IsActive) VALUES (?, ?, ?, 1)";
        int userID;
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlUsers, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getEmail());
            String hashedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
            stmt.setString(2, hashedPassword);
            stmt.setInt(3, 2); // Assuming RoleID 2 is for customers
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    userID = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve UserID.");
                }
            }
        }

        String sqlCustomers = "INSERT INTO Customers (FullName, Phone, Gender, BirthDate, CreatedAt, Address, UserID, IsActive) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlCustomers)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getGender());
            stmt.setDate(4, customer.getBirthDate());
            stmt.setTimestamp(5, customer.getCreatedAt());
            stmt.setString(6, customer.getAddress());
            stmt.setInt(7, userID);
            stmt.executeUpdate();
        }
    }

    public void updateCustomer(Customer customer) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sqlCustomers = "UPDATE Customers SET FullName = ?, Phone = ?, Gender = ?, BirthDate = ?, Address = ? WHERE CustomerID = ? AND IsActive = 1";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlCustomers)) {
            stmt.setString(1, customer.getFullName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getGender());
            stmt.setDate(4, customer.getBirthDate());
            stmt.setString(5, customer.getAddress());
            stmt.setInt(6, customer.getCustomerID());
            stmt.executeUpdate();
        }

        String sqlUsers = "UPDATE Users SET Email = ? WHERE UserID = (SELECT UserID FROM Customers WHERE CustomerID = ? AND IsActive = 1)";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlUsers)) {
            stmt.setString(1, customer.getEmail());
            stmt.setInt(2, customer.getCustomerID());
            stmt.executeUpdate();
        }
    }

    public void deleteCustomer(int customerID) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sqlCustomers = "UPDATE Customers SET IsActive = 0 WHERE CustomerID = ?";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlCustomers)) {
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        }

        String sqlUsers = "UPDATE Users SET IsActive = 0 WHERE UserID = (SELECT UserID FROM Customers WHERE CustomerID = ?)";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sqlUsers)) {
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        }
    }
}