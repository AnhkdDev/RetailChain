package dal;

import models.Inventories;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoriesDAO extends DBContext {

    public List<Inventories> getFilteredInventory(Integer warehouseID, Integer categoryID, String productName) {
        List<Inventories> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT i.ProductID, i.WarehouseID, p.ProductName, c.CategoryName, p.Size, p.Color, w.WarehouseName, s.StoreName, i.Quantity "
                + "FROM Inventory i "
                + "JOIN Products p ON i.ProductID = p.ProductID "
                + "JOIN Warehouses w ON i.WarehouseID = w.WarehouseID "
                + "JOIN Stores s ON w.StoreID = s.StoreID "
                + "JOIN Categories c ON p.CategoryID = c.CategoryID "
                + "WHERE i.Quantity >= 0"
        );

        List<Object> parameters = new ArrayList<>();
        if (warehouseID != null) {
            sql.append(" AND i.WarehouseID = ?");
            parameters.add(warehouseID);
        }
        if (categoryID != null) {
            sql.append(" AND p.CategoryID = ?");
            parameters.add(categoryID);
        }
        if (productName != null && !productName.trim().isEmpty()) {
            sql.append(" AND p.ProductName LIKE ?");
            parameters.add("%" + productName + "%");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Inventories inv = new Inventories(
                        rs.getInt("ProductID"),
                        rs.getInt("WarehouseID"),
                        rs.getString("ProductName"),
                        rs.getString("CategoryName"),
                        rs.getString("Size"),
                        rs.getString("Color"),
                        rs.getString("WarehouseName"),
                        rs.getString("StoreName"),
                        rs.getInt("Quantity") // Changed to getInt
                );
                list.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateInventoryQuantity(int productID, int warehouseID, int quantity) {
        String sql = "UPDATE Inventory SET Quantity = ? WHERE ProductID = ? AND WarehouseID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productID);
            ps.setInt(3, warehouseID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}





//package dal;
//
//import models.Inventories;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class InventoriesDAO extends DBContext {
//
//    // New method to get total number of records for pagination
//    public int getTotalInventoryRecords(Integer warehouseID, Integer categoryID, String productName) {
//        StringBuilder sql = new StringBuilder(
//                "SELECT COUNT(*) "
//                + "FROM Inventory i "
//                + "JOIN Products p ON i.ProductID = p.ProductID "
//                + "JOIN Warehouses w ON i.WarehouseID = w.WarehouseID "
//                + "JOIN Stores s ON w.StoreID = s.StoreID "
//                + "JOIN Categories c ON p.CategoryID = c.CategoryID "
//                + "WHERE i.Quantity >= 0"
//        );
//
//        List<Object> parameters = new ArrayList<>();
//        if (warehouseID != null) {
//            sql.append(" AND i.WarehouseID = ?");
//            parameters.add(warehouseID);
//        }
//        if (categoryID != null) {
//            sql.append(" AND p.CategoryID = ?");
//            parameters.add(categoryID);
//        }
//        if (productName != null && !productName.trim().isEmpty()) {
//            sql.append(" AND p.ProductName LIKE ?");
//            parameters.add("%" + productName + "%");
//        }
//
//        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
//            for (int i = 0; i < parameters.size(); i++) {
//                ps.setObject(i + 1, parameters.get(i));
//            }
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    // Modified getFilteredInventory to support pagination
//    public List<Inventories> getFilteredInventory(Integer warehouseID, Integer categoryID, String productName, int offset, int limit) {
//        List<Inventories> list = new ArrayList<>();
//        StringBuilder sql = new StringBuilder(
//                "SELECT i.ProductID, i.WarehouseID, p.ProductName, c.CategoryName, p.Size, p.Color, w.WarehouseName, s.StoreName, i.Quantity "
//                + "FROM Inventory i "
//                + "JOIN Products p ON i.ProductID = p.ProductID "
//                + "JOIN Warehouses w ON i.WarehouseID = w.WarehouseID "
//                + "JOIN Stores s ON w.StoreID = s.StoreID "
//                + "JOIN Categories c ON p.CategoryID = c.CategoryID "
//                + "WHERE i.Quantity >= 0"
//        );
//
//        List<Object> parameters = new ArrayList<>();
//        if (warehouseID != null) {
//            sql.append(" AND i.WarehouseID = ?");
//            parameters.add(warehouseID);
//        }
//        if (categoryID != null) {
//            sql.append(" AND p.CategoryID = ?");
//            parameters.add(categoryID);
//        }
//        if (productName != null && !productName.trim().isEmpty()) {
//            sql.append(" AND p.ProductName LIKE ?");
//            parameters.add("%" + productName + "%");
//        }
//        sql.append(" ORDER BY i.ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
//
//        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
//            int index = 1;
//            for (Object param : parameters) {
//                ps.setObject(index++, param);
//            }
//            ps.setInt(index++, offset);
//            ps.setInt(index, limit);
//
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Inventories inv = new Inventories(
//                        rs.getInt("ProductID"),
//                        rs.getInt("WarehouseID"),
//                        rs.getString("ProductName"),
//                        rs.getString("CategoryName"),
//                        rs.getString("Size"),
//                        rs.getString("Color"),
//                        rs.getString("WarehouseName"),
//                        rs.getString("StoreName"),
//                        rs.getInt("Quantity")
//                );
//                list.add(inv);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    // Existing updateInventoryQuantity method remains unchanged
//    public boolean updateInventoryQuantity(int productID, int warehouseID, int quantity) {
//        String sql = "UPDATE Inventory SET Quantity = ? WHERE ProductID = ? AND WarehouseID = ?";
//
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setInt(1, quantity);
//            ps.setInt(2, productID);
//            ps.setInt(3, warehouseID);
//
//            int rowsAffected = ps.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}