package DAO;

import dal.DBContext;
import model.Store;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StoreDAO extends DBContext {

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

    // Kiểm tra định dạng phoneNumber (chỉ chứa số, dấu cách, dấu ngoặc, dấu gạch ngang)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9\\s\\(\\)-]+$");

    public StorePage getStores(String search, String status, int page, int pageSize) {
        // Validation
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = 4; // Giá trị mặc định
        }
        if (status != null && !status.equals("active") && !status.equals("inactive")) {
            status = null; // Bỏ qua status không hợp lệ
        }
        if (search != null) {
            search = search.trim();
            if (search.length() > 100 || search.isEmpty()) {
                search = null; // Giới hạn độ dài và bỏ qua nếu rỗng
            } else {
                // Loại bỏ ký tự nguy hiểm (dù đã dùng PreparedStatement)
                search = search.replaceAll("[^a-zA-Z0-9\\s]", "");
            }
        }

        List<Store> stores = new ArrayList<>();
        int totalStores = 0;

        // Tạo câu lệnh SQL để đếm tổng số cửa hàng
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM Stores WHERE (LOWER(storeName) LIKE LOWER(?) OR LOWER(address) LIKE LOWER(?))");
        List<String> countParams = new ArrayList<>();
        countParams.add(search != null ? "%" + search + "%" : "%");
        countParams.add(search != null ? "%" + search + "%" : "%");

        if (status != null && !status.isEmpty()) {
            countSql.append(" AND isActive = ?");
            countParams.add(status.equals("active") ? "1" : "0");
        }

        // Tạo câu lệnh SQL để lấy dữ liệu cửa hàng với phân trang
        StringBuilder sql = new StringBuilder("SELECT * FROM Stores WHERE (LOWER(storeName) LIKE LOWER(?) OR LOWER(address) LIKE LOWER(?))");
        List<String> stringParams = new ArrayList<>(countParams);
        if (status != null && !status.isEmpty()) {
            sql.append(" AND isActive = ?");
        }
        sql.append(" ORDER BY isActive DESC, storeID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            // Lấy tổng số cửa hàng
            try (PreparedStatement countStmt = connection.prepareStatement(countSql.toString())) {
                for (int i = 0; i < countParams.size(); i++) {
                    countStmt.setString(i + 1, countParams.get(i));
                }
                ResultSet countRs = countStmt.executeQuery();
                if (countRs.next()) {
                    totalStores = countRs.getInt(1);
                }
            }

            // Lấy danh sách cửa hàng theo phân trang
            try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
                int paramIndex = 1;
                for (int i = 0; i < stringParams.size(); i++) {
                    stmt.setString(paramIndex++, stringParams.get(i));
                }
                if (status != null && !status.isEmpty()) {
                    stmt.setString(paramIndex++, status.equals("active") ? "1" : "0");
                }
                stmt.setInt(paramIndex++, (page - 1) * pageSize);
                stmt.setInt(paramIndex, pageSize);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Store store = new Store();
                    store.setStoreID(String.valueOf(rs.getInt("storeID")));
                    store.setStoreName(rs.getString("storeName"));
                    store.setAddress(rs.getString("address"));
                    store.setPhoneNumber(rs.getString("phone"));
                    store.setEmail(rs.getString("email"));
                    store.setActive(rs.getBoolean("isActive"));
                    stores.add(store);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách cửa hàng", e);
        }
        return new StorePage(stores, totalStores);
    }

    public boolean toggleStoreStatus(String storeID) {
        if (storeID == null || storeID.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(storeID); // Kiểm tra storeID là số nguyên
        } catch (NumberFormatException e) {
            return false;
        }

        String sql = "UPDATE Stores SET isActive = CASE WHEN isActive = 1 THEN 0 ELSE 1 END WHERE storeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(storeID));
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi chuyển đổi trạng thái cửa hàng", e);
        }
    }

    public boolean saveStore(Store store) {
        // Validation
        if (store == null || store.getStoreName() == null || store.getAddress() == null || store.getPhoneNumber() == null) {
            return false;
        }
        String storeName = store.getStoreName().trim();
        String address = store.getAddress().trim();
        String phoneNumber = store.getPhoneNumber().trim();
        String email = store.getEmail() != null ? store.getEmail().trim() : "";

        if (storeName.isEmpty() || storeName.length() > 100) {
            return false; // Tên cửa hàng rỗng hoặc quá dài
        }
        if (address.isEmpty() || address.length() > 200) {
            return false; // Địa chỉ rỗng hoặc quá dài
        }
        if (phoneNumber.isEmpty() || phoneNumber.length() > 20 || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return false; // Số điện thoại không hợp lệ
        }
        if (email.length() > 100) {
            return false; // Email quá dài
        }

        String sqlSelect = "SELECT COUNT(*) FROM Stores WHERE storeID = ?";
        String sqlInsert = "INSERT INTO Stores (storeName, address, phone, email, isActive) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE Stores SET storeName = ?, address = ?, phone = ?, email = ?, isActive = ? WHERE storeID = ?";

        try {
            // Kiểm tra xem storeID có tồn tại không
            boolean exists = false;
            if (store.getStoreID() != null && !store.getStoreID().isEmpty()) {
                try {
                    int storeId = Integer.parseInt(store.getStoreID());
                    try (PreparedStatement selectStmt = connection.prepareStatement(sqlSelect)) {
                        selectStmt.setInt(1, storeId);
                        ResultSet rs = selectStmt.executeQuery();
                        rs.next();
                        exists = rs.getInt(1) > 0;
                    }
                } catch (NumberFormatException e) {
                    return false; // storeID không hợp lệ
                }
            }

            if (exists) {
                // Cập nhật cửa hàng
                try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
                    updateStmt.setString(1, storeName);
                    updateStmt.setString(2, address);
                    updateStmt.setString(3, phoneNumber);
                    updateStmt.setString(4, email);
                    updateStmt.setBoolean(5, store.isActive());
                    updateStmt.setInt(6, Integer.parseInt(store.getStoreID()));
                    int rowsAffected = updateStmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                // Thêm cửa hàng mới
                try (PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)) {
                    insertStmt.setString(1, storeName);
                    insertStmt.setString(2, address);
                    insertStmt.setString(3, phoneNumber);
                    insertStmt.setString(4, email);
                    insertStmt.setBoolean(5, store.isActive());
                    int rowsAffected = insertStmt.executeUpdate();
                    return rowsAffected > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lưu cửa hàng", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đóng kết nối", e);
        }
    }

    public static void main(String[] args) {
        StoreDAO storeDAO = new StoreDAO();
        int pageSize = 4;
        try {
            System.out.println("Trang 1 (Tất cả cửa hàng):");
            StorePage page = storeDAO.getStores(null, null, 1, pageSize);
            printStores(page.getStores(), page.getTotalStores(), 1, pageSize);

            System.out.println("\nTrang 2 (Tất cả cửa hàng):");
            page = storeDAO.getStores(null, null, 2, pageSize);
            printStores(page.getStores(), page.getTotalStores(), 2, pageSize);

            System.out.println("\nTrang 1 (Cửa hàng Active):");
            page = storeDAO.getStores(null, "active", 1, pageSize);
            printStores(page.getStores(), page.getTotalStores(), 1, pageSize);

            System.out.println("\nTrang 1 (Tìm kiếm 'north'):");
            page = storeDAO.getStores("north", null, 1, pageSize);
            printStores(page.getStores(), page.getTotalStores(), 1, pageSize);

            System.out.println("\nTrang 1 (Tìm kiếm 'NORTH'):");
            page = storeDAO.getStores("NORTH", null, 1, pageSize);
            printStores(page.getStores(), page.getTotalStores(), 1, pageSize);

            System.out.println("\nKiểm tra chuyển đổi trạng thái cho storeID 1:");
            boolean toggled = storeDAO.toggleStoreStatus("1");
            System.out.println("Đã chuyển đổi: " + toggled);
            page = storeDAO.getStores(null, null, 1, pageSize);
            printStores(page.getStores(), page.getTotalStores(), 1, pageSize);

            System.out.println("\nKiểm tra lưu cửa hàng mới:");
            Store newStore = new Store();
            newStore.setStoreName("Test Branch");
            newStore.setAddress("999 Test Road, City J");
            newStore.setPhoneNumber("(123) 456-7890");
            newStore.setEmail("test@store.com");
            newStore.setActive(true);
            boolean saved = storeDAO.saveStore(newStore);
            System.out.println("Đã lưu: " + saved);
            page = storeDAO.getStores(null, null, 1, pageSize);
            printStores(page.getStores(), page.getTotalStores(), 1, pageSize);
        } catch (Exception e) {
            System.err.println("Lỗi trong main: " + e.getMessage());
            e.printStackTrace();
        } finally {
            storeDAO.closeConnection();
        }
    }

    private static void printStores(List<Store> stores, int totalStores, int page, int pageSize) {
        int startIndex = (page - 1) * pageSize + 1;
        int endIndex = Math.min(startIndex + stores.size() - 1, totalStores);
        System.out.println("Hiển thị từ " + startIndex + " đến " + endIndex + " trong tổng số " + totalStores + " cửa hàng");
        if (stores.isEmpty()) {
            System.out.println("Không tìm thấy cửa hàng nào.");
        } else {
            for (Store store : stores) {
                System.out.println("Mã cửa hàng: " + store.getStoreID());
                System.out.println("Tên: " + store.getStoreName());
                System.out.println("Địa chỉ: " + store.getAddress());
                System.out.println("Số điện thoại: " + store.getPhoneNumber());
                System.out.println("Email: " + store.getEmail());
                System.out.println("Trạng thái: " + (store.isActive() ? "Active" : "Inactive"));
                System.out.println("------------------------");
            }
        }
    }
}