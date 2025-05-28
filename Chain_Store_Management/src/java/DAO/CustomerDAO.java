package DAO;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Invoice;
import org.mindrot.jbcrypt.BCrypt;

public class CustomerDAO {

    private DBContext dbContext;

    public CustomerDAO() {
        dbContext = new DBContext();
    }

    public DBContext getDBContext() {
        return dbContext;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT c.CustomerID, c.FullName, c.Phone, u.Email, c.Gender, c.BirthDate, c.CreatedAt, c.Address, "
                + "COALESCE(SUM(i.TotalAmount), 0) AS TotalSpent, lc.TierLevel AS MembershipLevel "
                + "FROM Customers c "
                + "INNER JOIN Users u ON c.UserID = u.UserID "
                + "LEFT JOIN Invoices i ON c.CustomerID = i.CustomerID "
                + "LEFT JOIN LoyaltyCustomers lc ON c.CustomerID = lc.CustomerID "
                + "WHERE c.IsActive = 1 "
                + "GROUP BY c.CustomerID, c.FullName, c.Phone, u.Email, c.Gender, c.BirthDate, c.CreatedAt, c.Address, lc.TierLevel "
                + "ORDER BY c.CustomerID";

        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
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
            }
        }
        return customers;
    }

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
                    return customer;
                }
            }
        }
        return null;
    }

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

    public List<Customer> searchCustomers(String keyword, String gender, String membershipLevel) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
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

        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            conditions.add("(c.FullName LIKE ? OR c.Phone LIKE ? OR u.Email LIKE ?)");
            String likeKeyword = "%" + keyword + "%";
            parameters.add(likeKeyword);
            parameters.add(likeKeyword);
            parameters.add(likeKeyword);
        }

        if (gender != null && !gender.trim().isEmpty()) {
            conditions.add("c.Gender = ?");
            parameters.add(gender);
        }

        if (membershipLevel != null && !membershipLevel.trim().isEmpty()) {
            conditions.add("lc.TierLevel = ?");
            parameters.add(membershipLevel);
        }

        if (!conditions.isEmpty()) {
            sql.append(" AND ");
            sql.append(String.join(" AND ", conditions));
        }

        sql.append(" GROUP BY c.CustomerID, c.FullName, c.Phone, u.Email, c.Gender, c.BirthDate, c.CreatedAt, c.Address, lc.TierLevel ");
        sql.append(" ORDER BY c.CustomerID");

        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
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
                }
            }
        }
        return customers;
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

    public boolean verifyPassword(String email, String password) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        String sql = "SELECT PasswordHash FROM Users WHERE Email = ? AND IsActive = 1";
        try (PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("PasswordHash");
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        }
        return false;
    }

    public void deleteCustomer(int customerID) throws SQLException {
        if (dbContext.getConnection() == null) {
            throw new SQLException("Database connection is not established.");
        }
        // Soft delete by setting IsActive to 0
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