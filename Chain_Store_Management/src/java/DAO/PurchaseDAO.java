////
////package dao;
////
////import dal.DBContext;
////import java.sql.Connection;
////import java.sql.PreparedStatement;
////import java.sql.ResultSet;
////import java.sql.SQLException;
////import java.sql.Statement;
////import java.sql.Timestamp;
////import java.util.ArrayList;
////import java.util.List;
////import java.math.BigDecimal;
////import model.Purchase;
////import model.Warehouse;
////import model.Product;
////
////public class PurchaseDAO extends DBContext {
////
////    public List<Purchase> getAllPurchases() {
////        List<Purchase> purchases = new ArrayList<>();
////        String sql = "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, " +
////                "s.SupplierName, w.WarehouseName " +
////                "FROM Purchases p " +
////                "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
////                "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID ";
////
////        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
////            while (rs.next()) {
////                purchases.add(new Purchase(rs.getInt("PurchaseID"), rs.getTimestamp("PurchaseDate"),
////                        rs.getBigDecimal("TotalAmount"), rs.getString("SupplierName"), rs.getString("WarehouseName")));
////            }
////        } catch (SQLException e) {
////            System.err.println("Error in getAllPurchases: " + e.getMessage());
////        }
////        System.out.println("Number of purchases retrieved: " + purchases.size());
////        return purchases;
////    }
////
////    public Purchase getPurchaseById(int purchaseID) {
////        Purchase purchase = null;
////        String sql = "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, s.SupplierName, w.WarehouseName " +
////                "FROM Purchases p " +
////                "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
////                "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID " +
////                "WHERE p.PurchaseID = ?";
////
////        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
////            stmt.setInt(1, purchaseID);
////            ResultSet rs = stmt.executeQuery();
////            if (rs.next()) {
////                purchase = new Purchase(rs.getInt("PurchaseID"), rs.getTimestamp("PurchaseDate"),
////                        rs.getBigDecimal("TotalAmount"), rs.getString("SupplierName"), rs.getString("WarehouseName"));
////            }
////        } catch (SQLException e) {
////            System.err.println("Error in getPurchaseById: " + e.getMessage());
////        }
////        return purchase;
////    }
////
////    public boolean deletePurchase(int purchaseID) {
////        String deleteDetailsSql = "DELETE FROM PurchaseDetails WHERE PurchaseID = ?";
////        String deletePurchaseSql = "DELETE FROM Purchases WHERE PurchaseID = ?";
////
////        try {
////            connection.setAutoCommit(false);
////
////            try (PreparedStatement stmtDetails = connection.prepareStatement(deleteDetailsSql)) {
////                stmtDetails.setInt(1, purchaseID);
////                stmtDetails.executeUpdate();
////            }
////
////            try (PreparedStatement stmtPurchase = connection.prepareStatement(deletePurchaseSql)) {
////                stmtPurchase.setInt(1, purchaseID);
////                int rowsAffected = stmtPurchase.executeUpdate();
////                if (rowsAffected > 0) {
////                    System.out.println("Purchase ID " + purchaseID + " deleted successfully.");
////                    connection.commit();
////                    return true;
////                } else {
////                    System.out.println("No purchase found with ID " + purchaseID + ".");
////                    connection.rollback();
////                    return false;
////                }
////            }
////        } catch (SQLException e) {
////            System.err.println("Error in deletePurchase: " + e.getMessage());
////            try {
////                connection.rollback();
////            } catch (SQLException rollbackEx) {
////                System.err.println("Rollback failed: " + rollbackEx.getMessage());
////            }
////            return false;
////        } finally {
////            try {
////                connection.setAutoCommit(true);
////            } catch (SQLException e) {
////                System.err.println("Error resetting auto-commit: " + e.getMessage());
////            }
////        }
////    }
////
////    public boolean updatePurchase(int purchaseID, Timestamp purchaseDate, BigDecimal totalAmount, String supplierName, int warehouseID) {
////        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ? AND IsActive = 1";
////        String updateSql = "UPDATE Purchases SET PurchaseDate = ?, TotalAmount = ?, SupplierID = ?, WarehouseID = ? WHERE PurchaseID = ?";
////
////        try {
////            connection.setAutoCommit(false);
////
////            // Lấy SupplierID
////            int supplierID = -1;
////            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
////                stmtSupplier.setString(1, supplierName);
////                ResultSet rs = stmtSupplier.executeQuery();
////                if (rs.next()) {
////                    supplierID = rs.getInt("SupplierID");
////                } else {
////                    System.out.println("Error: Supplier not found - " + supplierName);
////                    connection.rollback();
////                    return false;
////                }
////            }
////
////            // Cập nhật Purchases
////            try (PreparedStatement stmtUpdate = connection.prepareStatement(updateSql)) {
////                stmtUpdate.setTimestamp(1, purchaseDate);
////                stmtUpdate.setBigDecimal(2, totalAmount);
////                stmtUpdate.setInt(3, supplierID);
////                stmtUpdate.setInt(4, warehouseID);
////                stmtUpdate.setInt(5, purchaseID);
////
////                int rowsAffected = stmtUpdate.executeUpdate();
////                if (rowsAffected > 0) {
////                    System.out.println("Success: Purchase ID " + purchaseID + " updated successfully.");
////                    connection.commit();
////                    return true;
////                } else {
////                    System.out.println("Error: No purchase found with ID " + purchaseID + ".");
////                    connection.rollback();
////                    return false;
////                }
////            }
////        } catch (SQLException e) {
////            System.err.println("SQL Error in updatePurchase: " + e.getMessage());
////            try {
////                connection.rollback();
////            } catch (SQLException rollbackEx) {
////                System.err.println("Rollback failed: " + rollbackEx.getMessage());
////            }
////            return false;
////        } finally {
////            try {
////                connection.setAutoCommit(true);
////            } catch (SQLException e) {
////                System.err.println("Error resetting auto-commit: " + e.getMessage());
////            }
////        }
////    }
////
////    public boolean addPurchase(String supplierName, int warehouseID, List<Integer> productIDs, List<Integer> quantities, List<BigDecimal> costPrices) {
////        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ? AND IsActive = 1";
////        String insertPurchaseSql = "INSERT INTO Purchases (PurchaseDate, TotalAmount, SupplierID, WarehouseID) VALUES (?, ?, ?, ?)";
////        String insertDetailSql = "INSERT INTO PurchaseDetails (PurchaseID, ProductID, Quantity, CostPrice) VALUES (?, ?, ?, ?)";
////
////        try {
////            connection.setAutoCommit(false);
////
////            // Lấy SupplierID
////            int supplierID = -1;
////            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
////                stmtSupplier.setString(1, supplierName);
////                try (ResultSet rs = stmtSupplier.executeQuery()) {
////                    if (rs.next()) {
////                        supplierID = rs.getInt("SupplierID");
////                    } else {
////                        System.out.println("Error: Supplier '" + supplierName + "' not found in database.");
////                        connection.rollback();
////                        return false;
////                    }
////                }
////            }
////
////            // Kiểm tra WarehouseID
////            String checkWarehouseSql = "SELECT WarehouseID FROM Warehouses WHERE WarehouseID = ? AND IsActive = 1";
////            try (PreparedStatement stmtWarehouse = connection.prepareStatement(checkWarehouseSql)) {
////                stmtWarehouse.setInt(1, warehouseID);
////                try (ResultSet rs = stmtWarehouse.executeQuery()) {
////                    if (!rs.next()) {
////                        System.out.println("Error: Warehouse ID '" + warehouseID + "' not found in database.");
////                        connection.rollback();
////                        return false;
////                    }
////                }
////            }
////
////            // Kiểm tra ProductIDs
////            String checkProductSql = "SELECT ProductID FROM Products WHERE ProductID = ? AND IsActive = 1";
////            try (PreparedStatement stmtProduct = connection.prepareStatement(checkProductSql)) {
////                for (Integer productID : productIDs) {
////                    stmtProduct.setInt(1, productID);
////                    try (ResultSet rs = stmtProduct.executeQuery()) {
////                        if (!rs.next()) {
////                            System.out.println("Error: Product ID '" + productID + "' not found in database.");
////                            connection.rollback();
////                            return false;
////                        }
////                    }
////                }
////            }
////
////            // Kiểm tra số lượng danh sách
////            if (productIDs.size() != quantities.size() || quantities.size() != costPrices.size()) {
////                System.out.println("Error: Mismatch in product details lists.");
////                connection.rollback();
////                return false;
////            }
////
////            // Tính TotalAmount
////            BigDecimal totalAmount = BigDecimal.ZERO;
////            for (int i = 0; i < quantities.size(); i++) {
////                BigDecimal quantity = new BigDecimal(quantities.get(i));
////                BigDecimal costPrice = costPrices.get(i);
////                totalAmount = totalAmount.add(quantity.multiply(costPrice));
////            }
////
////            // Thêm Purchase
////            int purchaseID;
////            try (PreparedStatement stmtInsert = connection.prepareStatement(insertPurchaseSql, Statement.RETURN_GENERATED_KEYS)) {
////                stmtInsert.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
////                stmtInsert.setBigDecimal(2, totalAmount);
////                stmtInsert.setInt(3, supplierID);
////                stmtInsert.setInt(4, warehouseID);
////
////                int rowsAffected = stmtInsert.executeUpdate();
////                if (rowsAffected == 0) {
////                    System.out.println("Error: Failed to add new purchase.");
////                    connection.rollback();
////                    return false;
////                }
////
////                try (ResultSet generatedKeys = stmtInsert.getGeneratedKeys()) {
////                    if (generatedKeys.next()) {
////                        purchaseID = generatedKeys.getInt(1);
////                    } else {
////                        System.out.println("Error: Failed to retrieve generated PurchaseID.");
////                        connection.rollback();
////                        return false;
////                    }
////                }
////            }
////
////            // Thêm PurchaseDetails
////            try (PreparedStatement stmtDetail = connection.prepareStatement(insertDetailSql)) {
////                for (int i = 0; i < productIDs.size(); i++) {
////                    stmtDetail.setInt(1, purchaseID);
////                    stmtDetail.setInt(2, productIDs.get(i));
////                    stmtDetail.setInt(3, quantities.get(i));
////                    stmtDetail.setBigDecimal(4, costPrices.get(i));
////                    stmtDetail.addBatch();
////                }
////                int[] batchResults = stmtDetail.executeBatch();
////                for (int result : batchResults) {
////                    if (result <= 0) {
////                        System.out.println("Error: Failed to add some purchase details.");
////                        connection.rollback();
////                        return false;
////                    }
////                }
////            }
////
////            System.out.println("Success: New Purchase (ID: " + purchaseID + ") added successfully with " + productIDs.size() + " details.");
////            connection.commit();
////            return true;
////
////        } catch (SQLException e) {
////            System.err.println("SQL Error in addPurchase: " + e.getMessage());
////            try {
////                connection.rollback();
////            } catch (SQLException rollbackEx) {
////                System.err.println("Rollback failed: " + rollbackEx.getMessage());
////            }
////            return false;
////        } finally {
////            try {
////                connection.setAutoCommit(true);
////            } catch (SQLException e) {
////                System.err.println("Error resetting auto-commit: " + e.getMessage());
////            }
////        }
////    }
////
////    public List<Warehouse> getAllWarehouses() {
////        List<Warehouse> warehouses = new ArrayList<>();
////        String sql = "SELECT WarehouseID, WarehouseName FROM Warehouses WHERE IsActive = 1";
////        try (PreparedStatement stmt = connection.prepareStatement(sql); 
////             ResultSet rs = stmt.executeQuery()) {
////            while (rs.next()) {
////                Warehouse warehouse = new Warehouse();
////                warehouse.setWarehouseID(rs.getInt("WarehouseID"));
////                warehouse.setWarehouseName(rs.getString("WarehouseName"));
////                warehouses.add(warehouse);
////            }
////        } catch (SQLException e) {
////            System.err.println("Error in getAllWarehouses: " + e.getMessage());
////        }
////        return warehouses;
////    }
////
////    public List<Product> getAllProducts() {
////        List<Product> products = new ArrayList<>();
////        String sql = "SELECT ProductID, ProductName FROM Products WHERE IsActive = 1";
////        try (PreparedStatement stmt = connection.prepareStatement(sql); 
////             ResultSet rs = stmt.executeQuery()) {
////            while (rs.next()) {
////                Product product = new Product();
////                product.setProductID(rs.getInt("ProductID"));
////                product.setProductName(rs.getString("ProductName"));
////                products.add(product);
////            }
////        } catch (SQLException e) {
////            System.err.println("Error in getAllProducts: " + e.getMessage());
////        }
////        return products;
////    }
////    
////     public static void main(String[] args) {
////        PurchaseDAO purchaseDAO = new PurchaseDAO();
////
////        // Test getAllWarehouses
////        System.out.println("=== List of Warehouses ===");
////        List<Warehouse> warehouses = purchaseDAO.getAllWarehouses();
////        for (Warehouse warehouse : warehouses) {
////            System.out.println("ID: " + warehouse.getWarehouseID() + ", Name: " + warehouse.getWarehouseName());
////        }
////
////        // Test getAllPurchases
//////        System.out.println("\n=== List of Purchases ===");
//////        List<Purchase> purchases = purchaseDAO.getAllPurchases();
//////        for (Purchase purchase : purchases) {
//////            System.out.println("ID: " + purchase.getPurchaseID()
//////                    + ", Date: " + purchase.getPurchaseDate()
//////                    + ", Total: " + purchase.getTotalAmount()
//////                    + ", Supplier: " + purchase.getSupplierName()
//////                    + ", Warehouse: " + purchase.getWarehouseName());
//////        }
////        
////         System.out.println("\n=== List of Products ===");
////        List<Product> products = purchaseDAO.getAllProducts();
////        for (Product product : products) {
////            System.out.println("ID: " + product.getProductID() + ", Name: " + product.getProductName());
////        }
////    }
////    
////}
////
//
//
//
//package dao;
//
//import dal.DBContext;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.math.BigDecimal;
//import model.Purchase;
//import model.Warehouse;
//import model.Product;
//import model.PurchaseDetail;
//
//public class PurchaseDAO extends DBContext {
//
//    public List<Purchase> getAllPurchases() {
//        List<Purchase> purchases = new ArrayList<>();
//        String sql = "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, " +
//                "s.SupplierName, w.WarehouseName " +
//                "FROM Purchases p " +
//                "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
//                "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID ";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                purchases.add(new Purchase(rs.getInt("PurchaseID"), rs.getTimestamp("PurchaseDate"),
//                        rs.getBigDecimal("TotalAmount"), rs.getString("SupplierName"), rs.getString("WarehouseName")));
//            }
//        } catch (SQLException e) {
//            System.err.println("Error in getAllPurchases: " + e.getMessage());
//        }
//        System.out.println("Number of purchases retrieved: " + purchases.size());
//        return purchases;
//    }
//
//    public Purchase getPurchaseById(int purchaseID) {
//        Purchase purchase = null;
//        String sql = "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, s.SupplierName, w.WarehouseName " +
//                "FROM Purchases p " +
//                "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
//                "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID " +
//                "WHERE p.PurchaseID = ?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, purchaseID);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                purchase = new Purchase(rs.getInt("PurchaseID"), rs.getTimestamp("PurchaseDate"),
//                        rs.getBigDecimal("TotalAmount"), rs.getString("SupplierName"), rs.getString("WarehouseName"));
//            }
//        } catch (SQLException e) {
//            System.err.println("Error in getPurchaseById: " + e.getMessage());
//        }
//        return purchase;
//    }
//
//    public List<PurchaseDetail> getPurchaseDetails(int purchaseID) {
//        List<PurchaseDetail> details = new ArrayList<>();
//        String sql = "SELECT pd.PurchaseID, pd.ProductID, p.ProductName, pd.Quantity, pd.CostPrice " +
//                     "FROM PurchaseDetails pd " +
//                     "JOIN Products p ON pd.ProductID = p.ProductID " +
//                     "WHERE pd.PurchaseID = ?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, purchaseID);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                PurchaseDetail detail = new PurchaseDetail(
//                    rs.getInt("PurchaseID"),
//                    rs.getInt("ProductID"),
//                    rs.getString("ProductName"),
//                    rs.getInt("Quantity"),
//                    rs.getBigDecimal("CostPrice")
//                );
//                details.add(detail);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error in getPurchaseDetails: " + e.getMessage());
//        }
//        System.out.println("Number of purchase details retrieved for PurchaseID " + purchaseID + ": " + details.size());
//        return details;
//    }
//
//    public boolean deletePurchase(int purchaseID) {
//        String deleteDetailsSql = "DELETE FROM PurchaseDetails WHERE PurchaseID = ?";
//        String deletePurchaseSql = "DELETE FROM Purchases WHERE PurchaseID = ?";
//
//        try {
//            connection.setAutoCommit(false);
//
//            try (PreparedStatement stmtDetails = connection.prepareStatement(deleteDetailsSql)) {
//                stmtDetails.setInt(1, purchaseID);
//                stmtDetails.executeUpdate();
//            }
//
//            try (PreparedStatement stmtPurchase = connection.prepareStatement(deletePurchaseSql)) {
//                stmtPurchase.setInt(1, purchaseID);
//                int rowsAffected = stmtPurchase.executeUpdate();
//                if (rowsAffected > 0) {
//                    System.out.println("Purchase ID " + purchaseID + " deleted successfully.");
//                    connection.commit();
//                    return true;
//                } else {
//                    System.out.println("No purchase found with ID " + purchaseID + ".");
//                    connection.rollback();
//                    return false;
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Error in deletePurchase: " + e.getMessage());
//            try {
//                connection.rollback();
//            } catch (SQLException rollbackEx) {
//                System.err.println("Rollback failed: " + rollbackEx.getMessage());
//            }
//            return false;
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                System.err.println("Error resetting auto-commit: " + e.getMessage());
//            }
//        }
//    }
//
//    public boolean updatePurchase(int purchaseID, Timestamp purchaseDate, BigDecimal totalAmount, String supplierName, int warehouseID) {
//        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ? AND IsActive = 1";
//        String updateSql = "UPDATE Purchases SET PurchaseDate = ?, TotalAmount = ?, SupplierID = ?, WarehouseID = ? WHERE PurchaseID = ?";
//
//        try {
//            connection.setAutoCommit(false);
//
//            // Lấy SupplierID
//            int supplierID = -1;
//            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
//                stmtSupplier.setString(1, supplierName);
//                ResultSet rs = stmtSupplier.executeQuery();
//                if (rs.next()) {
//                    supplierID = rs.getInt("SupplierID");
//                } else {
//                    System.out.println("Error: Supplier not found - " + supplierName);
//                    connection.rollback();
//                    return false;
//                }
//            }
//
//            // Cập nhật Purchases
//            try (PreparedStatement stmtUpdate = connection.prepareStatement(updateSql)) {
//                stmtUpdate.setTimestamp(1, purchaseDate);
//                stmtUpdate.setBigDecimal(2, totalAmount);
//                stmtUpdate.setInt(3, supplierID);
//                stmtUpdate.setInt(4, warehouseID);
//                stmtUpdate.setInt(5, purchaseID);
//
//                int rowsAffected = stmtUpdate.executeUpdate();
//                if (rowsAffected > 0) {
//                    System.out.println("Success: Purchase ID " + purchaseID + " updated successfully.");
//                    connection.commit();
//                    return true;
//                } else {
//                    System.out.println("Error: No purchase found with ID " + purchaseID + ".");
//                    connection.rollback();
//                    return false;
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("SQL Error in updatePurchase: " + e.getMessage());
//            try {
//                connection.rollback();
//            } catch (SQLException rollbackEx) {
//                System.err.println("Rollback failed: " + rollbackEx.getMessage());
//            }
//            return false;
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                System.err.println("Error resetting auto-commit: " + e.getMessage());
//            }
//        }
//    }
//
//    public boolean addPurchase(String supplierName, int warehouseID, List<Integer> productIDs, List<Integer> quantities, List<BigDecimal> costPrices) {
//        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ? AND IsActive = 1";
//        String insertPurchaseSql = "INSERT INTO Purchases (PurchaseDate, TotalAmount, SupplierID, WarehouseID) VALUES (?, ?, ?, ?)";
//        String insertDetailSql = "INSERT INTO PurchaseDetails (PurchaseID, ProductID, Quantity, CostPrice) VALUES (?, ?, ?, ?)";
//
//        try {
//            connection.setAutoCommit(false);
//
//            // Lấy SupplierID
//            int supplierID = -1;
//            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
//                stmtSupplier.setString(1, supplierName);
//                try (ResultSet rs = stmtSupplier.executeQuery()) {
//                    if (rs.next()) {
//                        supplierID = rs.getInt("SupplierID");
//                    } else {
//                        System.out.println("Error: Supplier '" + supplierName + "' not found in database.");
//                        connection.rollback();
//                        return false;
//                    }
//                }
//            }
//
//            // Kiểm tra WarehouseID
//            String checkWarehouseSql = "SELECT WarehouseID FROM Warehouses WHERE WarehouseID = ? AND IsActive = 1";
//            try (PreparedStatement stmtWarehouse = connection.prepareStatement(checkWarehouseSql)) {
//                stmtWarehouse.setInt(1, warehouseID);
//                try (ResultSet rs = stmtWarehouse.executeQuery()) {
//                    if (!rs.next()) {
//                        System.out.println("Error: Warehouse ID '" + warehouseID + "' not found in database.");
//                        connection.rollback();
//                        return false;
//                    }
//                }
//            }
//
//            // Kiểm tra ProductIDs
//            String checkProductSql = "SELECT ProductID FROM Products WHERE ProductID = ? AND IsActive = 1";
//            try (PreparedStatement stmtProduct = connection.prepareStatement(checkProductSql)) {
//                for (Integer productID : productIDs) {
//                    stmtProduct.setInt(1, productID);
//                    try (ResultSet rs = stmtProduct.executeQuery()) {
//                        if (!rs.next()) {
//                            System.out.println("Error: Product ID '" + productID + "' not found in database.");
//                            connection.rollback();
//                            return false;
//                        }
//                    }
//                }
//            }
//
//            // Kiểm tra số lượng danh sách
//            if (productIDs.size() != quantities.size() || quantities.size() != costPrices.size()) {
//                System.out.println("Error: Mismatch in product details lists.");
//                connection.rollback();
//                return false;
//            }
//
//            // Tính TotalAmount
//            BigDecimal totalAmount = BigDecimal.ZERO;
//            for (int i = 0; i < quantities.size(); i++) {
//                BigDecimal quantity = new BigDecimal(quantities.get(i));
//                BigDecimal costPrice = costPrices.get(i);
//                totalAmount = totalAmount.add(quantity.multiply(costPrice));
//            }
//
//            // Thêm Purchase
//            int purchaseID;
//            try (PreparedStatement stmtInsert = connection.prepareStatement(insertPurchaseSql, Statement.RETURN_GENERATED_KEYS)) {
//                stmtInsert.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
//                stmtInsert.setBigDecimal(2, totalAmount);
//                stmtInsert.setInt(3, supplierID);
//                stmtInsert.setInt(4, warehouseID);
//
//                int rowsAffected = stmtInsert.executeUpdate();
//                if (rowsAffected == 0) {
//                    System.out.println("Error: Failed to add new purchase.");
//                    connection.rollback();
//                    return false;
//                }
//
//                try (ResultSet generatedKeys = stmtInsert.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        purchaseID = generatedKeys.getInt(1);
//                    } else {
//                        System.out.println("Error: Failed to retrieve generated PurchaseID.");
//                        connection.rollback();
//                        return false;
//                    }
//                }
//            }
//
//            // Thêm PurchaseDetails
//            try (PreparedStatement stmtDetail = connection.prepareStatement(insertDetailSql)) {
//                for (int i = 0; i < productIDs.size(); i++) {
//                    stmtDetail.setInt(1, purchaseID);
//                    stmtDetail.setInt(2, productIDs.get(i));
//                    stmtDetail.setInt(3, quantities.get(i));
//                    stmtDetail.setBigDecimal(4, costPrices.get(i));
//                    stmtDetail.addBatch();
//                }
//                int[] batchResults = stmtDetail.executeBatch();
//                for (int result : batchResults) {
//                    if (result <= 0) {
//                        System.out.println("Error: Failed to add some purchase details.");
//                        connection.rollback();
//                        return false;
//                    }
//                }
//            }
//
//            System.out.println("Success: New Purchase (ID: " + purchaseID + ") added successfully with " + productIDs.size() + " details.");
//            connection.commit();
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("SQL Error in addPurchase: " + e.getMessage());
//            try {
//                connection.rollback();
//            } catch (SQLException rollbackEx) {
//                System.err.println("Rollback failed: " + rollbackEx.getMessage());
//            }
//            return false;
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                System.err.println("Error resetting auto-commit: " + e.getMessage());
//            }
//        }
//    }
//
//    public List<Warehouse> getAllWarehouses() {
//        List<Warehouse> warehouses = new ArrayList<>();
//        String sql = "SELECT WarehouseID, WarehouseName FROM Warehouses WHERE IsActive = 1";
//        try (PreparedStatement stmt = connection.prepareStatement(sql); 
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                Warehouse warehouse = new Warehouse();
//                warehouse.setWarehouseID(rs.getInt("WarehouseID"));
//                warehouse.setWarehouseName(rs.getString("WarehouseName"));
//                warehouses.add(warehouse);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error in getAllWarehouses: " + e.getMessage());
//        }
//        return warehouses;
//    }
//
//    public List<Product> getAllProducts() {
//        List<Product> products = new ArrayList<>();
//        String sql = "SELECT ProductID, ProductName FROM Products WHERE IsActive = 1";
//        try (PreparedStatement stmt = connection.prepareStatement(sql); 
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                Product product = new Product();
//                product.setProductID(rs.getInt("ProductID"));
//                product.setProductName(rs.getString("ProductName"));
//                products.add(product);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error in getAllProducts: " + e.getMessage());
//        }
//        return products;
//    }
//}

package dao;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.Purchase;
import model.Warehouse;
import model.Product;
import model.PurchaseDetail;

public class PurchaseDAO extends DBContext {

    // Phương thức lấy danh sách đơn mua hàng với tìm kiếm, lọc, và phân trang
    public List<Purchase> getAllPurchases(String search, Timestamp startDate, Timestamp endDate, BigDecimal minTotalAmount, BigDecimal maxTotalAmount, int page, int pageSize) {
        List<Purchase> purchases = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, s.SupplierName, w.WarehouseName " +
            "FROM Purchases p " +
            "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
            "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID " +
            "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        // Thêm điều kiện tìm kiếm theo SupplierName
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND s.SupplierName LIKE ?");
            params.add("%" + search.trim() + "%");
        }

        // Thêm điều kiện lọc theo PurchaseDate
        if (startDate != null) {
            sql.append(" AND p.PurchaseDate >= ?");
            params.add(startDate);
        }
        if (endDate != null) {
            sql.append(" AND p.PurchaseDate <= ?");
            params.add(endDate);
        }

        // Thêm điều kiện lọc theo TotalAmount
        if (minTotalAmount != null) {
            sql.append(" AND p.TotalAmount >= ?");
            params.add(minTotalAmount);
        }
        if (maxTotalAmount != null) {
            sql.append(" AND p.TotalAmount <= ?");
            params.add(maxTotalAmount);
        }

        // Thêm phân trang
        int offset = (page - 1) * pageSize;
        sql.append(" ORDER BY p.PurchaseID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageSize);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    purchases.add(new Purchase(
                        rs.getInt("PurchaseID"),
                        rs.getTimestamp("PurchaseDate"),
                        rs.getBigDecimal("TotalAmount"),
                        rs.getString("SupplierName"),
                        rs.getString("WarehouseName")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllPurchases: " + e.getMessage());
        }
        return purchases;
    }

    // Phương thức đếm tổng số đơn mua hàng để tính số trang
    public int getTotalPurchases(String search, Timestamp startDate, Timestamp endDate, BigDecimal minTotalAmount, BigDecimal maxTotalAmount) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) " +
            "FROM Purchases p " +
            "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
            "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID " +
            "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND s.SupplierName LIKE ?");
            params.add("%" + search.trim() + "%");
        }

        if (startDate != null) {
            sql.append(" AND p.PurchaseDate >= ?");
            params.add(startDate);
        }
        if (endDate != null) {
            sql.append(" AND p.PurchaseDate <= ?");
            params.add(endDate);
        }

        if (minTotalAmount != null) {
            sql.append(" AND p.TotalAmount >= ?");
            params.add(minTotalAmount);
        }
        if (maxTotalAmount != null) {
            sql.append(" AND p.TotalAmount <= ?");
            params.add(maxTotalAmount);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalPurchases: " + e.getMessage());
        }
        return 0;
    }

    // Các phương thức khác giữ nguyên
    public Purchase getPurchaseById(int purchaseID) {
        Purchase purchase = null;
        String sql = "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, s.SupplierName, w.WarehouseName " +
                "FROM Purchases p " +
                "JOIN Suppliers s ON p.SupplierID = s.SupplierID " +
                "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID " +
                "WHERE p.PurchaseID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purchaseID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                purchase = new Purchase(rs.getInt("PurchaseID"), rs.getTimestamp("PurchaseDate"),
                        rs.getBigDecimal("TotalAmount"), rs.getString("SupplierName"), rs.getString("WarehouseName"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getPurchaseById: " + e.getMessage());
        }
        return purchase;
    }

    public List<PurchaseDetail> getPurchaseDetails(int purchaseID) {
        List<PurchaseDetail> details = new ArrayList<>();
        String sql = "SELECT pd.PurchaseID, pd.ProductID, p.ProductName, pd.Quantity, pd.CostPrice " +
                "FROM PurchaseDetails pd " +
                "JOIN Products p ON pd.ProductID = p.ProductID " +
                "WHERE pd.PurchaseID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purchaseID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PurchaseDetail detail = new PurchaseDetail(
                    rs.getInt("PurchaseID"),
                    rs.getInt("ProductID"),
                    rs.getString("ProductName"),
                    rs.getInt("Quantity"),
                    rs.getBigDecimal("CostPrice")
                );
                details.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("Error in getPurchaseDetails: " + e.getMessage());
        }
        return details;
    }

    public boolean deletePurchase(int purchaseID) {
        String deleteDetailsSql = "DELETE FROM PurchaseDetails WHERE PurchaseID = ?";
        String deletePurchaseSql = "DELETE FROM Purchases WHERE PurchaseID = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtDetails = connection.prepareStatement(deleteDetailsSql)) {
                stmtDetails.setInt(1, purchaseID);
                stmtDetails.executeUpdate();
            }

            try (PreparedStatement stmtPurchase = connection.prepareStatement(deletePurchaseSql)) {
                stmtPurchase.setInt(1, purchaseID);
                int rowsAffected = stmtPurchase.executeUpdate();
                if (rowsAffected > 0) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in deletePurchase: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public boolean updatePurchase(int purchaseID, Timestamp purchaseDate, BigDecimal totalAmount, String supplierName, int warehouseID) {
        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ? AND IsActive = 1";
        String updateSql = "UPDATE Purchases SET PurchaseDate = ?, TotalAmount = ?, SupplierID = ?, WarehouseID = ? WHERE PurchaseID = ?";

        try {
            connection.setAutoCommit(false);

            int supplierID = -1;
            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
                stmtSupplier.setString(1, supplierName);
                ResultSet rs = stmtSupplier.executeQuery();
                if (rs.next()) {
                    supplierID = rs.getInt("SupplierID");
                } else {
                    connection.rollback();
                    return false;
                }
            }

            try (PreparedStatement stmtUpdate = connection.prepareStatement(updateSql)) {
                stmtUpdate.setTimestamp(1, purchaseDate);
                stmtUpdate.setBigDecimal(2, totalAmount);
                stmtUpdate.setInt(3, supplierID);
                stmtUpdate.setInt(4, warehouseID);
                stmtUpdate.setInt(5, purchaseID);

                int rowsAffected = stmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in updatePurchase: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public boolean addPurchase(String supplierName, int warehouseID, List<Integer> productIDs, List<Integer> quantities, List<BigDecimal> costPrices) {
        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ? AND IsActive = 1";
        String insertPurchaseSql = "INSERT INTO Purchases (PurchaseDate, TotalAmount, SupplierID, WarehouseID) VALUES (?, ?, ?, ?)";
        String insertDetailSql = "INSERT INTO PurchaseDetails (PurchaseID, ProductID, Quantity, CostPrice) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int supplierID = -1;
            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
                stmtSupplier.setString(1, supplierName);
                try (ResultSet rs = stmtSupplier.executeQuery()) {
                    if (rs.next()) {
                        supplierID = rs.getInt("SupplierID");
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
            }

            String checkWarehouseSql = "SELECT WarehouseID FROM Warehouses WHERE WarehouseID = ? AND IsActive = 1";
            try (PreparedStatement stmtWarehouse = connection.prepareStatement(checkWarehouseSql)) {
                stmtWarehouse.setInt(1, warehouseID);
                try (ResultSet rs = stmtWarehouse.executeQuery()) {
                    if (!rs.next()) {
                        connection.rollback();
                        return false;
                    }
                }
            }

            String checkProductSql = "SELECT ProductID FROM Products WHERE ProductID = ? AND IsActive = 1";
            try (PreparedStatement stmtProduct = connection.prepareStatement(checkProductSql)) {
                for (Integer productID : productIDs) {
                    stmtProduct.setInt(1, productID);
                    try (ResultSet rs = stmtProduct.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }
                    }
                }
            }

            if (productIDs.size() != quantities.size() || quantities.size() != costPrices.size()) {
                connection.rollback();
                return false;
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (int i = 0; i < quantities.size(); i++) {
                BigDecimal quantity = new BigDecimal(quantities.get(i));
                BigDecimal costPrice = costPrices.get(i);
                totalAmount = totalAmount.add(quantity.multiply(costPrice));
            }

            int purchaseID;
            try (PreparedStatement stmtInsert = connection.prepareStatement(insertPurchaseSql, Statement.RETURN_GENERATED_KEYS)) {
                stmtInsert.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                stmtInsert.setBigDecimal(2, totalAmount);
                stmtInsert.setInt(3, supplierID);
                stmtInsert.setInt(4, warehouseID);

                int rowsAffected = stmtInsert.executeUpdate();
                if (rowsAffected == 0) {
                    connection.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = stmtInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        purchaseID = generatedKeys.getInt(1);
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement stmtDetail = connection.prepareStatement(insertDetailSql)) {
                for (int i = 0; i < productIDs.size(); i++) {
                    stmtDetail.setInt(1, purchaseID);
                    stmtDetail.setInt(2, productIDs.get(i));
                    stmtDetail.setInt(3, quantities.get(i));
                    stmtDetail.setBigDecimal(4, costPrices.get(i));
                    stmtDetail.addBatch();
                }
                int[] batchResults = stmtDetail.executeBatch();
                for (int result : batchResults) {
                    if (result <= 0) {
                        connection.rollback();
                        return false;
                    }
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("SQL Error in addPurchase: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        String sql = "SELECT WarehouseID, WarehouseName FROM Warehouses WHERE IsActive = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setWarehouseID(rs.getInt("WarehouseID"));
                warehouse.setWarehouseName(rs.getString("WarehouseName"));
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllWarehouses: " + e.getMessage());
        }
        return warehouses;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT ProductID, ProductName FROM Products WHERE IsActive = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setProductName(rs.getString("ProductName"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllProducts: " + e.getMessage());
        }
        return products;
    }
}