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

    public List<Purchase> getAllPurchases(String search, String warehouseName, Integer purchaseID, BigDecimal minTotalAmount, BigDecimal maxTotalAmount, int page, int pageSize) {
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

        // Thêm điều kiện tìm kiếm theo WarehouseName
        if (warehouseName != null && !warehouseName.trim().isEmpty()) {
            sql.append(" AND w.WarehouseName = ?");
            params.add(warehouseName.trim());
        }

        // Thêm điều kiện tìm kiếm theo PurchaseID
        if (purchaseID != null) {
            sql.append(" AND p.PurchaseID = ?");
            params.add(purchaseID);
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

    public int getTotalPurchases(String search, String warehouseName, Integer purchaseID, BigDecimal minTotalAmount, BigDecimal maxTotalAmount) {
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

        if (warehouseName != null && !warehouseName.trim().isEmpty()) {
            sql.append(" AND w.WarehouseName = ?");
            params.add(warehouseName.trim());
        }

        if (purchaseID != null) {
            sql.append(" AND p.PurchaseID = ?");
            params.add(purchaseID);
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