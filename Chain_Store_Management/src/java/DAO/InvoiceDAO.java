package DAO;
import Model.BankTransaction;
import java.sql.Timestamp;
import Model.Customer;
import Model.Employee;
import dal.DBContext;
import Model.Invoice;
import Model.InvoiceItem;
import Model.PaymentMethod;
import Model.Product;
import Model.Store;
import static dal.DBContext.getConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT i.InvoiceID, i.InvoiceDate, i.Notes, i.Status, i.StoreID, i.EmployeeID, i.CustomerID, " +
                     "i.TotalAmount, i.PaymentMethodID, s.StoreName, e.FullName AS EmployeeName, " +
                     "c.FullName AS CustomerName, pm.MethodName " +
                     "FROM Invoices i " +
                     "JOIN Stores s ON i.StoreID = s.StoreID " +
                     "JOIN Employees e ON i.EmployeeID = e.EmployeeID " +
                     "JOIN Customers c ON i.CustomerID = c.CustomerID " +
                     "LEFT JOIN PaymentMethods pm ON i.PaymentMethodID = pm.PaymentMethodID " +
                     "ORDER BY i.InvoiceID DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("[InvoiceDAO] [getAllInvoices] Thực thi truy vấn danh sách hóa đơn...");
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceID(rs.getString("InvoiceID"));
                invoice.setInvoiceDate(rs.getTimestamp("InvoiceDate"));
                invoice.setNote(rs.getString("Notes"));
                invoice.setStatus(rs.getString("Status"));
                invoice.setStoreID(rs.getInt("StoreID"));
                invoice.setEmployeeID(rs.getInt("EmployeeID"));
                invoice.setCustomerID(rs.getInt("CustomerID"));
                invoice.setTotalAmount(rs.getBigDecimal("TotalAmount").doubleValue());
                if (!rs.wasNull()) {
                    invoice.setPaymentMethodID(rs.getInt("PaymentMethodID"));
                }
                invoice.setStoreName(rs.getString("StoreName"));
                invoice.setEmployeeName(rs.getString("EmployeeName"));
                invoice.setCustomerName(rs.getString("CustomerName"));
                invoice.setPaymentMethodName(rs.getString("MethodName"));
                invoices.add(invoice);
                System.out.println("[InvoiceDAO] Lấy hóa đơn ID: " + invoice.getInvoiceID());
            }
            System.out.println("[InvoiceDAO] Tổng số hóa đơn: " + invoices.size());
        } catch (SQLException e) {
            System.err.println("[InvoiceDAO] Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
            throw e;
        }
        return invoices;
    }

    public Invoice getInvoiceById(String invoiceId) {
    Invoice invoice = null;
    String sql = "SELECT i.InvoiceID, i.InvoiceDate, i.Notes, i.StoreID, i.EmployeeID, i.CustomerID, i.TotalAmount, i.Status, " +
                 "ISNULL(c.FullName, 'N/A') AS CustomerName, " +
                 "s.StoreName, " +
                 "e.FullName AS EmployeeName, " +
                 "ISNULL(pm.MethodName, 'N/A') AS PaymentMethod "  +
                 "FROM Invoices i " +
                 "LEFT JOIN Customers c ON i.CustomerID = c.CustomerID " +
                 "JOIN Stores s ON i.StoreID = s.StoreID " +
                 "JOIN Employees e ON i.EmployeeID = e.EmployeeID " +
                 "LEFT JOIN PaymentMethods pm ON i.PaymentMethodID = pm.PaymentMethodID " +
                 "WHERE i.InvoiceID = ?";

    String sqlItems = "SELECT id.ProductID, p.ProductName, id.Quantity, id.UnitPrice, id.DiscountPercent, " +
                     "id.Quantity * id.UnitPrice * (1 - id.DiscountPercent / 100) AS Subtotal, " +
                     "p.Size AS ProductSize, p.Color AS ProductColor " +
                     "FROM InvoiceDetails id " +
                     "JOIN Products p ON id.ProductID = p.ProductID " +
                     "WHERE id.InvoiceID = ?";
    System.out.println("Executing SQL query for single invoice: " + sql + " with ID: " + invoiceId);

    try (Connection conn = DBContext.getConnection()) {
        // Lấy thông tin hóa đơn chính
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoiceId); // Sử dụng setString nếu InvoiceID là String
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    invoice = new Invoice();
                    invoice.setInvoiceID(String.valueOf(rs.getInt("InvoiceID"))); // Chuyển int thành String
                    invoice.setInvoiceDate(rs.getTimestamp("InvoiceDate"));
                    invoice.setStoreID(rs.getInt("StoreID"));
                    invoice.setNote(rs.getString("Notes"));
                    invoice.setEmployeeID(rs.getInt("EmployeeID"));
                    invoice.setCustomerID(rs.getObject("CustomerID") != null ? rs.getInt("CustomerID") : null); // Xử lý NULL
                    invoice.setTotalAmount(rs.getDouble("TotalAmount"));
                    invoice.setCustomerName(rs.getString("CustomerName"));
                    invoice.setStoreName(rs.getString("StoreName"));
                    invoice.setEmployeeName(rs.getString("EmployeeName"));
                    invoice.setStatus(rs.getString("Status")); // Lấy Status từ bảng Invoices
                    invoice.setPaymentMethod(rs.getString("PaymentMethod"));
                    
                }
            }
        }

        // Lấy các mặt hàng của hóa đơn (nếu tìm thấy hóa đơn)
        if (invoice != null) {
            List<InvoiceItem> items = new ArrayList<>();
            try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                psItems.setString(1, invoiceId); // Sử dụng setString nếu InvoiceID là String
                System.out.println("Executing SQL query for invoice items: " + sqlItems + " for InvoiceID: " + invoiceId);
                try (ResultSet rsItems = psItems.executeQuery()) {
                    while (rsItems.next()) {
                        InvoiceItem item = new InvoiceItem();
                        item.setInvoiceID(invoiceId);
                        item.setProductName(rsItems.getString("ProductName"));
                        item.setQuantity(rsItems.getInt("Quantity"));
                        item.setUnitPrice(rsItems.getDouble("UnitPrice"));
                        item.setSubtotal(rsItems.getDouble("Subtotal"));
                        item.setProductColor(rsItems.getString("ProductColor"));
                        item.setProductSize(rsItems.getString("ProductSize"));
                        items.add(item);
                    }
                }
            }
            invoice.setItems(items);
            System.out.println("Number of items fetched for invoice " + invoiceId + ": " + items.size());
        }

    } catch (SQLException e) {
        System.err.println("SQL Exception in getInvoiceById: " + e.getMessage());
        e.printStackTrace();
        // throw new RuntimeException("Error loading invoice details", e);
    }
    return invoice;
}
    public String createInvoice(Invoice invoice, List<InvoiceItem> items) throws SQLException {
        String generatedInvoiceID = null;
        Connection conn = null;

        // Câu lệnh SQL cho bảng Invoices
        String sqlInvoice = "INSERT INTO Invoices (InvoiceDate, Notes, Status, StoreID, EmployeeID, CustomerID, TotalAmount, PaymentMethodID) " +
                           "OUTPUT INSERTED.InvoiceID " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // Câu lệnh SQL cho bảng InvoiceDetails
        String sqlInvoiceDetails = "INSERT INTO InvoiceDetails (InvoiceID, ProductID, Quantity, UnitPrice, DiscountPercent) " +
                                  "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch
            System.out.printf("[InvoiceDAO] [createInvoice] Bắt đầu giao dịch lúc %s%n", new Timestamp(System.currentTimeMillis()));

            // Chuẩn bị và thực thi chèn vào Invoices
            try (PreparedStatement psInvoice = conn.prepareStatement(sqlInvoice)) {
                psInvoice.setTimestamp(1, invoice.getInvoiceDate());
                psInvoice.setString(2, invoice.getNote() != null ? invoice.getNote().trim() : "");
                psInvoice.setString(3, invoice.getStatus());
                psInvoice.setInt(4, invoice.getStoreID());
                psInvoice.setInt(5, invoice.getEmployeeID());
                psInvoice.setInt(6, invoice.getCustomerID()); // CustomerID NOT NULL
                psInvoice.setBigDecimal(7, BigDecimal.valueOf(invoice.getTotalAmount()));
                if (invoice.getPaymentMethodID() != null) {
                    psInvoice.setInt(8, invoice.getPaymentMethodID());
                } else {
                    psInvoice.setNull(8, java.sql.Types.INTEGER);
                }

                System.out.println("[InvoiceDAO] Thực thi chèn Invoices: StoreID=" + invoice.getStoreID() +
                                   ", EmployeeID=" + invoice.getEmployeeID() + ", CustomerID=" + invoice.getCustomerID());
                try (ResultSet rs = psInvoice.executeQuery()) {
                    if (rs.next()) {
                        generatedInvoiceID = String.valueOf(rs.getInt("InvoiceID"));
                        System.out.println("[InvoiceDAO] Đã tạo InvoiceID: " + generatedInvoiceID);
                    } else {
                        throw new SQLException("Không thể lấy InvoiceID sau khi chèn.");
                    }
                }
            }

            // Chuẩn bị và thực thi chèn vào InvoiceDetails
            if (generatedInvoiceID != null && !items.isEmpty()) {
                try (PreparedStatement psDetails = conn.prepareStatement(sqlInvoiceDetails)) {
                    System.out.println("[InvoiceDAO] Chèn " + items.size() + " chi tiết hóa đơn...");
                    for (InvoiceItem item : items) {
                        psDetails.setInt(1, Integer.parseInt(generatedInvoiceID));
                        psDetails.setInt(2, Integer.parseInt(item.getProductID()));
                        psDetails.setBigDecimal(3, BigDecimal.valueOf(item.getQuantity()));
                        psDetails.setBigDecimal(4, BigDecimal.valueOf(item.getUnitPrice()));
                        psDetails.setBigDecimal(5, BigDecimal.valueOf(item.getDiscountPercent()));
                        psDetails.addBatch();
                        System.out.printf("[InvoiceDAO] Thêm batch: ProductID=%s, Quantity=%.2f, UnitPrice=%.2f%n",
                                item.getProductID(), item.getQuantity(), item.getUnitPrice());
                    }
                    int[] batchResults = psDetails.executeBatch();
                    System.out.println("[InvoiceDAO] Kết quả batch: " + java.util.Arrays.toString(batchResults));
                    for (int result : batchResults) {
                        if (result == PreparedStatement.EXECUTE_FAILED) {
                            throw new SQLException("Chèn chi tiết hóa đơn thất bại.");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Không có chi tiết hóa đơn để chèn.");
            }

            conn.commit();
            System.out.println("[InvoiceDAO] Đã commit giao dịch. InvoiceID: " + generatedInvoiceID);
            return generatedInvoiceID;

        } catch (SQLException e) {
            System.err.println("[InvoiceDAO] Lỗi SQL: " + e.getMessage() + " - SQLState: " + e.getSQLState());
            if (conn != null) {
                try {
                    System.out.println("[InvoiceDAO] Đang rollback giao dịch...");
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[InvoiceDAO] Rollback thất bại: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("[InvoiceDAO] Lỗi dữ liệu: " + e.getMessage());
            if (conn != null) {
                try {
                    System.out.println("[InvoiceDAO] Đang rollback giao dịch do lỗi dữ liệu...");
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("[InvoiceDAO] Rollback thất bại: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("[InvoiceDAO] Đã đóng kết nối.");
                } catch (SQLException e) {
                    System.err.println("[InvoiceDAO] Lỗi khi đóng kết nối: " + e.getMessage());
                }
            }
        }
    }

    // Fetch all stores
    public List<Store> getAllStores() throws SQLException {
        List<Store> stores = new ArrayList<>();
        String sql = "SELECT StoreID, StoreName FROM Stores WHERE IsActive = 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Store store = new Store();
                store.setStoreID(rs.getInt("StoreID"));
                store.setStoreName(rs.getString("StoreName"));
                stores.add(store);
            }
        }
        return stores;
    }

    // Fetch all employees
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT EmployeeID, FullName FROM Employees WHERE IsActive = 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("EmployeeID"));
                employee.setFullName(rs.getString("FullName"));
                employees.add(employee);
            }
        }
        return employees;
    }

    // Fetch all customers
    public List<Customer> getAllCustomers() throws SQLException {
    List<Customer> customers = new ArrayList<>();
    String sql = "SELECT CustomerID, FullName FROM Customers WHERE IsActive = 1";
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Customer customer = new Customer();
            customer.setCustomerID(rs.getInt("CustomerID"));
            customer.setFullName(rs.getString("FullName"));
            customers.add(customer);
        }
    }
    return customers;
}

    // Fetch all payment methods
    public List<PaymentMethod> getAllPaymentMethods() throws SQLException {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        String sql = "SELECT PaymentMethodID, MethodName FROM PaymentMethods WHERE IsActive = 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PaymentMethod pm = new PaymentMethod();
                pm.setPaymentMethodID(rs.getInt("PaymentMethodID"));
                pm.setMethodName(rs.getString("MethodName"));
                paymentMethods.add(pm);
            }
        }
        return paymentMethods;
    }

    // Fetch all products
    public List<Product> getAllProducts() throws SQLException {
    List<Product> products = new ArrayList<>();
    String query = "SELECT ProductID, ProductName, Price, Size FROM Products WHERE IsActive = 1";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Product product = new Product();
            product.setProductID(rs.getInt("ProductID"));
            product.setProductName(rs.getString("ProductName"));
            product.setPrice(rs.getBigDecimal("Price").floatValue());
            product.setSize(rs.getString("Size")); // Lấy Size
            products.add(product);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
    return products;
}
    public float getProductPriceById(int productID) throws SQLException {
        String query = "SELECT Price FROM Products WHERE ProductID = ? AND IsActive = 1";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("Price").floatValue();
                } else {
                    throw new SQLException("Product not found or inactive");
                }
            }
        }
    }
    public List<BankTransaction> getAllBankTransactions() throws SQLException {
    List<BankTransaction> transactions = new ArrayList<>();
    String sql = "SELECT bt.TransactionID, bt.InvoiceID, bt.PaymentMethodID, bt.BankName, bt.AccountNumber, " +
                 "bt.TransactionCode, bt.Amount, bt.Status, bt.TransactionDate, pm.MethodName " +
                 "FROM BankTransactions bt " +
                 "LEFT JOIN PaymentMethods pm ON bt.PaymentMethodID = pm.PaymentMethodID " +
                 "ORDER BY bt.TransactionDate DESC";

    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        System.out.println("[InvoiceDAO] [getAllBankTransactions] Thực thi truy vấn danh sách giao dịch ngân hàng...");
        while (rs.next()) {
            BankTransaction transaction = new BankTransaction();
            transaction.setTransactionID(rs.getInt("TransactionID"));
            transaction.setInvoiceID(rs.getInt("InvoiceID"));
            transaction.setPaymentMethodID(rs.getInt("PaymentMethodID"));
            transaction.setBankName(rs.getString("BankName"));
            transaction.setAccountNumber(rs.getString("AccountNumber"));
            transaction.setTransactionCode(rs.getString("TransactionCode"));
            transaction.setAmount(rs.getDouble("Amount"));
            transaction.setStatus(rs.getString("Status"));
            transaction.setTransactionDate(rs.getTimestamp("TransactionDate"));
            transaction.setPaymentMethodName(rs.getString("MethodName"));
            transactions.add(transaction);
            System.out.println("[InvoiceDAO] Lấy giao dịch ID: " + transaction.getTransactionID());
        }
        // Thêm dữ liệu mẫu nếu danh sách rỗng
//        if (transactions.isEmpty()) {
//            System.out.println("[InvoiceDAO] Không có dữ liệu, thêm giao dịch mẫu...");
//            BankTransaction sample = new BankTransaction();
//            sample.setTransactionID(1);
//            sample.setInvoiceID(1);
//            sample.setPaymentMethodID(1);
//            sample.setBankName("Vietcombank");
//            sample.setAccountNumber("1234567890");
//            sample.setTransactionCode("TEST001");
//            sample.setAmount(500.00);
//            sample.setStatus("Pending");
//            sample.setTransactionDate(new Timestamp(System.currentTimeMillis()));
//            transactions.add(sample);
//        }
        System.out.println("[InvoiceDAO] Tổng số giao dịch: " + transactions.size());
    } catch (SQLException e) {
        System.err.println("[InvoiceDAO] Lỗi khi lấy danh sách giao dịch ngân hàng: " + e.getMessage());
        throw e;
    }
    return transactions;
}

// Tạo giao dịch ngân hàng mới
public int createBankTransaction(BankTransaction transaction) throws SQLException {
    String sql = "INSERT INTO BankTransactions (InvoiceID, PaymentMethodID, BankName, AccountNumber, TransactionCode, Amount, Status, TransactionDate) " +
                 "OUTPUT INSERTED.TransactionID " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    int generatedTransactionID = -1;

    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        if (transaction.getInvoiceID() != null) {
            ps.setInt(1, transaction.getInvoiceID());
        } else {
            ps.setNull(1, java.sql.Types.INTEGER);
        }
        ps.setInt(2, transaction.getPaymentMethodID());
        ps.setString(3, transaction.getBankName());
        ps.setString(4, transaction.getAccountNumber());
        ps.setString(5, transaction.getTransactionCode());
        ps.setBigDecimal(6, BigDecimal.valueOf(transaction.getAmount()));
        ps.setString(7, transaction.getStatus());
        ps.setTimestamp(8, new Timestamp(transaction.getTransactionDate().getTime()));

        System.out.println("[InvoiceDAO] Thực thi chèn giao dịch ngân hàng: InvoiceID=" + transaction.getInvoiceID());
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                generatedTransactionID = rs.getInt("TransactionID");
                System.out.println("[InvoiceDAO] Đã tạo TransactionID: " + generatedTransactionID);
            } else {
                throw new SQLException("Không thể lấy TransactionID sau khi chèn.");
            }
        }
    } catch (SQLException e) {
        System.err.println("[InvoiceDAO] Lỗi khi tạo giao dịch ngân hàng: " + e.getMessage());
        throw e;
    }
    return generatedTransactionID;
}
}
