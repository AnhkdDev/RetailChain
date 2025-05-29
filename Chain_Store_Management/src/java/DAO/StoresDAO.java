package DAO;

import dal.DBContext;
import model.Stores;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoresDAO extends DBContext {

    public List<Stores> getAllStores() {
        List<Stores> list = new ArrayList<>();
        String sql = "SELECT StoreID, StoreName FROM Stores WHERE IsActive = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Stores store = new Stores(
                    rs.getInt("StoreID"),
                    rs.getString("StoreName")
                );
                list.add(store);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}