package DAO;

import dal.DBContext;
import model.Store;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class StoreDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(StoreDAO.class.getName());
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s\\(\\)-]{7,15}$");
    private static final Pattern VALID_WORD_PATTERN = Pattern.compile("[a-zA-Z0-9]{2,}");

    public static class StorePage {
        private List<Store> stores;
        private int totalStores;

        public StorePage(List<Store> stores, int totalStores) {
            this.stores = stores;
            this.totalStores = totalStores;
        }

        public List<Store> getStores() {
            return stores;
        }

        public int getTotalStores() {
            return totalStores;
        }
    }

    private void ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = new DBContext().getConnection();
            LOGGER.log(Level.INFO, "Established database connection");
        }
    }

    private String normalizeVietnamese(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }

    private int countDigits(String text) {
        if (text == null) return 0;
        return text.replaceAll("[^0-9]", "").length();
    }

    public StorePage getStores(String search, String status, int page, int pageSize) {
        if (page < 1) {
            page = 1;
            LOGGER.log(Level.WARNING, "Page number less than 1, setting to 1");
        }
        if (pageSize < 1) {
            pageSize = 4;
            LOGGER.log(Level.WARNING, "Page size less than 1, setting to 4");
        }
        if (status != null && !status.equals("active") && !status.equals("inactive")) {
            status = null;
            LOGGER.log(Level.WARNING, "Invalid status value, setting to null");
        }
        String cleanedSearch = null;
        if (search != null) {
            search = search.trim();
            if (search.length() > 100 || search.isEmpty()) {
                search = null;
                LOGGER.log(Level.WARNING, "Search keyword invalid (empty or exceeds 100 chars), setting to null");
            } else {
                cleanedSearch = normalizeVietnamese(search);
                if (cleanedSearch.isEmpty()) {
                    cleanedSearch = null;
                    search = null;
                    LOGGER.log(Level.WARNING, "Cleaned search keyword is empty, setting to null");
                }
            }
        }

        List<Store> stores = new ArrayList<>();
        int totalStores = 0;

        StringBuilder countSql = new StringBuilder(
                "SELECT COUNT(*) FROM Stores WHERE 1=1");
        List<Object> countParams = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM Stores WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Apply keyword filter
        if (cleanedSearch != null) {
            String[] keywords = cleanedSearch.trim().split("\\s+");
            List<String> validKeywords = new ArrayList<>();
            for (String keyword : keywords) {
                if (VALID_WORD_PATTERN.matcher(keyword).matches()) {
                    validKeywords.add(keyword);
                } else {
                    LOGGER.log(Level.WARNING, "Filtered out invalid keyword: {0}", keyword);
                }
            }
            if (!validKeywords.isEmpty()) {
                sql.append(" AND (");
                countSql.append(" AND (");
                for (int i = 0; i < validKeywords.size(); i++) {
                    if (i > 0) sql.append(" OR ");
                    if (i > 0) countSql.append(" OR ");
                    sql.append("(LOWER(CONVERT(NVARCHAR(MAX), storeName) COLLATE Latin1_General_CI_AI) LIKE ? OR " +
                               "LOWER(CONVERT(NVARCHAR(MAX), address) COLLATE Latin1_General_CI_AI) LIKE ?)");
                    countSql.append("(LOWER(CONVERT(NVARCHAR(MAX), storeName) COLLATE Latin1_General_CI_AI) LIKE ? OR " +
                                    "LOWER(CONVERT(NVARCHAR(MAX), address) COLLATE Latin1_General_CI_AI) LIKE ?)");
                    params.add("%" + validKeywords.get(i) + "%");
                    params.add("%" + validKeywords.get(i) + "%");
                    countParams.add("%" + validKeywords.get(i) + "%");
                    countParams.add("%" + validKeywords.get(i) + "%");
                }
                sql.append(")");
                countSql.append(")");
                LOGGER.log(Level.INFO, "Valid keywords after filtering: {0}", validKeywords);
            } else {
                cleanedSearch = null;
                LOGGER.log(Level.WARNING, "No valid keywords after filtering, setting search to null");
            }
        }

        // Apply status filter
        if (status != null && !status.isEmpty()) {
            countSql.append(" AND isActive = ?");
            sql.append(" AND isActive = ?");
            params.add(status.equals("active") ? 1 : 0);
            countParams.add(status.equals("active") ? 1 : 0);
        }

        sql.append(" ORDER BY isActive DESC, storeID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((page - 1) * pageSize);
        params.add(pageSize);

        try {
            ensureConnection();
            try (PreparedStatement countStmt = connection.prepareStatement(countSql.toString())) {
                for (int i = 0; i < countParams.size(); i++) {
                    countStmt.setObject(i + 1, countParams.get(i));
                }
                LOGGER.log(Level.INFO, "Executing count SQL: {0}", countSql.toString());
                LOGGER.log(Level.INFO, "Count parameters: {0}", countParams.toString());
                try (ResultSet countRs = countStmt.executeQuery()) {
                    if (countRs.next()) {
                        totalStores = countRs.getInt(1);
                        LOGGER.log(Level.INFO, "Total stores: {0}", totalStores);
                    }
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
                LOGGER.log(Level.INFO, "Executing SQL: {0}", sql.toString());
                LOGGER.log(Level.INFO, "Parameters: {0}", params.toString());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Store store = new Store();
                        store.setStoreID(String.valueOf(rs.getInt("storeID")));
                        store.setStoreName(rs.getString("storeName"));
                        store.setAddress(rs.getString("address"));
                        store.setPhoneNumber(rs.getString("phone"));
                        store.setEmail(rs.getString("email"));
                        store.setActive(rs.getBoolean("isActive"));
                        stores.add(store);
                        LOGGER.log(Level.INFO, "Found store: ID={0}, Name={1}, Address={2}, Phone={3}", 
                                   new Object[]{store.getStoreID(), store.getStoreName(), store.getAddress(), store.getPhoneNumber()});
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching stores", e);
            throw new RuntimeException("Lỗi khi lấy danh sách cửa hàng", e);
        }
        LOGGER.log(Level.INFO, "Returning {0} stores for keyword: {1}", new Object[]{stores.size(), cleanedSearch});
        return new StorePage(stores, totalStores);
    }

    public boolean toggleStoreStatus(String storeID) {
        if (storeID == null || storeID.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Missing storeID for toggle status");
            return false;
        }
        try {
            Integer.parseInt(storeID);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid storeID format: {0}", storeID);
            return false;
        }

        String sql = "UPDATE Stores SET isActive = CASE WHEN isActive = 1 THEN 0 ELSE 1 END WHERE storeID = ?";
        try {
            ensureConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(storeID));
                int rowsAffected = stmt.executeUpdate();
                LOGGER.log(Level.INFO, "Toggled store status: ID={0}, Rows affected={1}", 
                           new Object[]{storeID, rowsAffected});
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error toggling store status", e);
            throw new RuntimeException("Lỗi khi chuyển đổi trạng thái cửa hàng", e);
        }
    }

    public boolean saveStore(Store store) {
        if (store == null || store.getStoreName() == null || store.getAddress() == null || store.getPhoneNumber() == null) {
            LOGGER.log(Level.WARNING, "Invalid store data for saving");
            return false;
        }
        String storeName = store.getStoreName().trim();
        String address = store.getAddress().trim();
        String phoneNumber = store.getPhoneNumber().trim();
        String email = store.getEmail() != null ? store.getEmail().trim() : "";

        if (storeName.isEmpty() || storeName.length() > 100) {
            LOGGER.log(Level.WARNING, "Invalid store name: {0}", storeName);
            return false;
        }
        if (address.isEmpty() || address.length() > 200) {
            LOGGER.log(Level.WARNING, "Invalid address: {0}", address);
            return false;
        }
        if (phoneNumber.isEmpty() || !PHONE_PATTERN.matcher(phoneNumber).matches() || countDigits(phoneNumber) < 7) {
            LOGGER.log(Level.WARNING, "Invalid phone number: {0}", phoneNumber);
            return false;
        }
        if (email.length() > 100) {
            LOGGER.log(Level.WARNING, "Invalid email: {0}", email);
            return false;
        }

        String sqlSelect = "SELECT COUNT(*) FROM Stores WHERE storeID = ?";
        String sqlCheckName = "SELECT COUNT(*) FROM Stores WHERE storeName = ? AND (storeID != ? OR ? IS NULL)";
        String sqlCheckAddress = "SELECT COUNT(*) FROM Stores WHERE address = ? AND (storeID != ? OR ? IS NULL)";
        String sqlInsert = "INSERT INTO Stores (storeName, address, phone, email, isActive) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE Stores SET storeName = ?, address = ?, phone = ?, email = ?, isActive = ? WHERE storeID = ?";

        try {
            ensureConnection();
            boolean exists = false;
            int storeId = 0;
            if (store.getStoreID() != null && !store.getStoreID().isEmpty()) {
                try {
                    storeId = Integer.parseInt(store.getStoreID());
                    try (PreparedStatement selectStmt = connection.prepareStatement(sqlSelect)) {
                        selectStmt.setInt(1, storeId);
                        try (ResultSet rs = selectStmt.executeQuery()) {
                            rs.next();
                            exists = rs.getInt(1) > 0;
                        }
                    }
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid storeID format: {0}", store.getStoreID());
                    return false;
                }
            }

            // Check for duplicate store name
            try (PreparedStatement checkNameStmt = connection.prepareStatement(sqlCheckName)) {
                checkNameStmt.setString(1, storeName);
                checkNameStmt.setInt(2, storeId);
                checkNameStmt.setObject(3, store.getStoreID() != null ? storeId : null);
                try (ResultSet rs = checkNameStmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        LOGGER.log(Level.WARNING, "Duplicate store name: {0}", storeName);
                        return false; // Duplicate store name
                    }
                }
            }

            // Check for duplicate address
            try (PreparedStatement checkAddressStmt = connection.prepareStatement(sqlCheckAddress)) {
                checkAddressStmt.setString(1, address);
                checkAddressStmt.setInt(2, storeId);
                checkAddressStmt.setObject(3, store.getStoreID() != null ? storeId : null);
                try (ResultSet rs = checkAddressStmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        LOGGER.log(Level.WARNING, "Duplicate address: {0}", address);
                        return false; // Duplicate address
                    }
                }
            }

            if (exists) {
                try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
                    updateStmt.setString(1, storeName);
                    updateStmt.setString(2, address);
                    updateStmt.setString(3, phoneNumber);
                    updateStmt.setString(4, email);
                    updateStmt.setBoolean(5, store.isActive());
                    updateStmt.setInt(6, storeId);
                    int rowsAffected = updateStmt.executeUpdate();
                    LOGGER.log(Level.INFO, "Updated store: ID={0}, Rows affected={1}", new Object[]{storeId, rowsAffected});
                    return rowsAffected > 0;
                }
            } else {
                try (PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)) {
                    insertStmt.setString(1, storeName);
                    insertStmt.setString(2, address);
                    insertStmt.setString(3, phoneNumber);
                    insertStmt.setString(4, email);
                    insertStmt.setBoolean(5, store.isActive());
                    int rowsAffected = insertStmt.executeUpdate();
                    LOGGER.log(Level.INFO, "Inserted store: Name={0}, Rows affected={1}", new Object[]{storeName, rowsAffected});
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving store", e);
            throw new RuntimeException("Lỗi khi lưu cửa hàng", e);
        }
    }

    public Store getStoreById(String storeID) {
        if (storeID == null || storeID.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Missing storeID for getStoreById");
            return null;
        }
        try {
            Integer.parseInt(storeID);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid storeID format: {0}", storeID);
            return null;
        }

        String sql = "SELECT * FROM Stores WHERE storeID = ?";
        try {
            ensureConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(storeID));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Store store = new Store();
                        store.setStoreID(String.valueOf(rs.getInt("storeID")));
                        store.setStoreName(rs.getString("storeName"));
                        store.setAddress(rs.getString("address"));
                        store.setPhoneNumber(rs.getString("phone"));
                        store.setEmail(rs.getString("email"));
                        store.setActive(rs.getBoolean("isActive"));
                        LOGGER.log(Level.INFO, "Retrieved store: ID={0}, Name={1}", 
                                   new Object[]{store.getStoreID(), store.getStoreName()});
                        return store;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving store by ID", e);
            throw new RuntimeException("Lỗi khi lấy thông tin cửa hàng", e);
        }
        return null;
    }

    public void closeConnection() {
        super.closeConnection();
        LOGGER.log(Level.INFO, "Database connection closed");
    }
}