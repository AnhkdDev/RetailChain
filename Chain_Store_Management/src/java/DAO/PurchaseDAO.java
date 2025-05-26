package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Purchase;
import dal.*;
//import java.beans.Statement;
import java.math.BigDecimal;
//import jdk.jfr.Timestamp;
import java.sql.Timestamp;
import java.sql.Statement;


public class PurchaseDAO extends DBContext {

//        "WHERE p.IsActive = 1 AND s.IsActive = 1 AND w.IsActive = 1";
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, "
                + "s.SupplierName, w.WarehouseName "
                + "FROM Purchases p "
                + "JOIN Suppliers s ON p.SupplierID = s.SupplierID "
                + "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID ";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                purchases.add(new Purchase(rs.getInt("PurchaseID"), rs.getTimestamp("PurchaseDate"),
                        rs.getBigDecimal("TotalAmount"), rs.getString("SupplierName"), rs.getString("WarehouseName")));
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllPurchases: " + e.getMessage());
        }
        System.out.println("Number of purchases retrieved: " + purchases.size());
        return purchases;
    }

    public Purchase getPurchaseById(int purchaseID) {
        Purchase purchase = null;
        String sql = "SELECT p.PurchaseID, p.PurchaseDate, p.TotalAmount, s.SupplierName, w.WarehouseName "
                + "FROM Purchases p "
                + "JOIN Suppliers s ON p.SupplierID = s.SupplierID "
                + "JOIN Warehouses w ON p.WarehouseID = w.WarehouseID "
                + "WHERE p.PurchaseID = ?";

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

    public boolean deletePurchase(int purchaseID) {
        // Giả sử có bảng PurchaseDetails chứa chi tiết giao dịch, cần xóa trước
        String deleteDetailsSql = "DELETE FROM PurchaseDetails WHERE PurchaseID = ?";
        String deletePurchaseSql = "DELETE FROM Purchases WHERE PurchaseID = ?";

        try {
            // Bắt đầu giao dịch để đảm bảo tính toàn vẹn dữ liệu
            connection.setAutoCommit(false);

            // Xóa các chi tiết giao dịch liên quan (nếu có)
            try (PreparedStatement stmtDetails = connection.prepareStatement(deleteDetailsSql)) {
                stmtDetails.setInt(1, purchaseID);
                stmtDetails.executeUpdate();
            }

            // Xóa giao dịch chính
            try (PreparedStatement stmtPurchase = connection.prepareStatement(deletePurchaseSql)) {
                stmtPurchase.setInt(1, purchaseID);
                int rowsAffected = stmtPurchase.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Purchase ID " + purchaseID + " deleted successfully.");
                    connection.commit();
                    return true;
                } else {
                    System.out.println("No purchase found with ID " + purchaseID + ".");
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

    public boolean updatePurchase(int purchaseID, Timestamp purchaseDate, BigDecimal totalAmount, String supplierName, String warehouseName) {
        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ?";
        String getWarehouseSql = "SELECT WarehouseID FROM Warehouses WHERE WarehouseName = ?";
        String updateSql = "UPDATE Purchases SET PurchaseDate = ?, TotalAmount = ?, SupplierID = ?, WarehouseID = ? WHERE PurchaseID = ?";

        try {
            connection.setAutoCommit(false);

            // Lấy SupplierID
            int supplierID = -1;
            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
                stmtSupplier.setString(1, supplierName);
                ResultSet rs = stmtSupplier.executeQuery();
                if (rs.next()) {
                    supplierID = rs.getInt("SupplierID");
                } else {
                    System.out.println("Error: Supplier not found - " + supplierName);
                    connection.rollback();
                    return false;
                }
            }

            // Lấy WarehouseID
            int warehouseID = -1;
            try (PreparedStatement stmtWarehouse = connection.prepareStatement(getWarehouseSql)) {
                stmtWarehouse.setString(1, warehouseName);
                ResultSet rs = stmtWarehouse.executeQuery();
                if (rs.next()) {
                    warehouseID = rs.getInt("WarehouseID");
                } else {
                    System.out.println("Error: Warehouse not found - " + warehouseName);
                    connection.rollback();
                    return false;
                }
            }

            // Cập nhật Purchases
            try (PreparedStatement stmtUpdate = connection.prepareStatement(updateSql)) {
                stmtUpdate.setTimestamp(1, purchaseDate); // Sử dụng java.sql.Timestamp
                stmtUpdate.setBigDecimal(2, totalAmount);
                stmtUpdate.setInt(3, supplierID);
                stmtUpdate.setInt(4, warehouseID);
                stmtUpdate.setInt(5, purchaseID);

                int rowsAffected = stmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Success: Purchase ID " + purchaseID + " updated successfully.");
                    connection.commit();
                    return true;
                } else {
                    System.out.println("Error: No purchase found with ID " + purchaseID + ".");
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
        String getSupplierSql = "SELECT SupplierID FROM Suppliers WHERE SupplierName = ?";
        String insertPurchaseSql = "INSERT INTO Purchases (PurchaseDate, TotalAmount, SupplierID, WarehouseID) VALUES (?, ?, ?, ?)";
        String insertDetailSql = "INSERT INTO PurchaseDetails (PurchaseID, ProductID, Quantity, CostPrice) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Lấy SupplierID
            int supplierID = -1;
            try (PreparedStatement stmtSupplier = connection.prepareStatement(getSupplierSql)) {
                stmtSupplier.setString(1, supplierName);
                try (ResultSet rs = stmtSupplier.executeQuery()) {
                    if (rs.next()) {
                        supplierID = rs.getInt("SupplierID");
                    } else {
                        System.out.println("Error: Supplier '" + supplierName + "' not found in database.");
                        connection.rollback();
                        return false;
                    }
                }
            }

            // Tính TotalAmount
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (int i = 0; i < productIDs.size(); i++) {
                BigDecimal quantity = new BigDecimal(quantities.get(i));
                BigDecimal costPrice = costPrices.get(i);
                totalAmount = totalAmount.add(quantity.multiply(costPrice));
            }

            // Thêm Purchase
            int purchaseID;
            try (PreparedStatement stmtInsert = connection.prepareStatement(insertPurchaseSql, Statement.RETURN_GENERATED_KEYS)) {
                stmtInsert.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Sử dụng thời gian hiện tại
                stmtInsert.setBigDecimal(2, totalAmount);
                stmtInsert.setInt(3, supplierID);
                stmtInsert.setInt(4, warehouseID);

                int rowsAffected = stmtInsert.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Error: Failed to add new purchase.");
                    connection.rollback();
                    return false;
                }

                // Lấy PurchaseID vừa thêm
                try (ResultSet generatedKeys = stmtInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        purchaseID = generatedKeys.getInt(1);
                    } else {
                        System.out.println("Error: Failed to retrieve generated PurchaseID.");
                        connection.rollback();
                        return false;
                    }
                }
            }

            // Thêm PurchaseDetails
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
                        System.out.println("Error: Failed to add some purchase details.");
                        connection.rollback();
                        return false;
                    }
                }
            }

            System.out.println("Success: New Purchase (ID: " + purchaseID + ") added successfully with " + productIDs.size() + " details.");
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

    public List<String> getAllSuppliers() {
        List<String> suppliers = new ArrayList<>();
        String sql = "SELECT SupplierName FROM Suppliers";
        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(rs.getString("SupplierName"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllSuppliers: " + e.getMessage());
        }
        return suppliers;
    }

    public List<String> getAllWarehouses() {
        List<String> warehouses = new ArrayList<>();
        String sql = "SELECT WarehouseName FROM Warehouses";
        try (PreparedStatement stmt = connection.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                warehouses.add(rs.getString("WarehouseName"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllWarehouses: " + e.getMessage());
        }
        return warehouses;
    }

    public static void main(String[] args) {
        PurchaseDAO o = new PurchaseDAO();
        var s = o.getAllPurchases();

        for (Purchase purchase : s) {
            System.out.println(purchase);
        }

    }
}
