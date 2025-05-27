package dal;

import models.Warehouses;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehousesDAO extends DBContext {

    public List<Warehouses> getAllWarehouses() {
        List<Warehouses> list = new ArrayList<>();
        String sql = "SELECT w.WarehouseID, w.WarehouseName, w.StoreID, s.StoreName "
                + "FROM Warehouses w "
                + "JOIN Stores s ON w.StoreID = s.StoreID "
                + "WHERE w.IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Warehouses wh = new Warehouses(
                        rs.getInt("WarehouseID"),
                        rs.getString("WarehouseName"),
                        rs.getInt("StoreID"),
                        rs.getString("StoreName")
                );
                list.add(wh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addWarehouse(Warehouses warehouse) {
        String sql = "INSERT INTO Warehouses (WarehouseName, StoreID, IsActive) VALUES (?, ?, 1)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, warehouse.getWarehouseName());
            ps.setInt(2, warehouse.getStoreID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateWarehouse(Warehouses warehouse) {
        String sql = "UPDATE Warehouses SET WarehouseName = ?, StoreID = ? WHERE WarehouseID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, warehouse.getWarehouseName());
            ps.setInt(2, warehouse.getStoreID());
            ps.setInt(3, warehouse.getWarehouseID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteWarehouse(int warehouseID) {
        // Soft delete
        String sql = "UPDATE Warehouses SET IsActive = 0 WHERE WarehouseID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, warehouseID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Warehouses getWarehouseById(int warehouseID) {
        String sql = "SELECT w.WarehouseID, w.WarehouseName, w.StoreID, s.StoreName "
                + "FROM Warehouses w "
                + "JOIN Stores s ON w.StoreID = s.StoreID "
                + "WHERE w.WarehouseID = ? AND w.IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, warehouseID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Warehouses(
                        rs.getInt("WarehouseID"),
                        rs.getString("WarehouseName"),
                        rs.getInt("StoreID"),
                        rs.getString("StoreName")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

