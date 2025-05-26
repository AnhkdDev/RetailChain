package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Purchase;
import dal.*;
import java.math.BigDecimal;

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
    
    public static void main(String[] args) {
        PurchaseDAO o = new PurchaseDAO();
        var s = o.getAllPurchases();
        
        for (Purchase purchase : s) {
            System.out.println(purchase);
        }
        
    }
}
