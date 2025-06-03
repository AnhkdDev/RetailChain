package DAO;

import dal.DBContext;
import model.Notification;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DBContext {

    // Lấy thông báo với bộ lọc và phân trang
    public List<Notification> getFilteredNotifications(String keyword, String status, int page, int pageSize, Integer userID) {
        List<Notification> notifications = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT NotificationID, Title, Message, IsRead, CreatedAt, UserID " +
                "FROM Notifications WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        // Thêm điều kiện trạng thái
        if (status != null && !status.isEmpty()) {
            if ("read".equals(status)) {
                sql.append(" AND IsRead = 1");
            } else if ("unread".equals(status)) {
                sql.append(" AND IsRead = 0");
            } else {
                throw new IllegalArgumentException("Invalid status value: " + status);
            }
        }

        // Thêm điều kiện UserID
        if (userID != null) {
            sql.append(" AND (UserID = ? OR UserID IS NULL)");
            params.add(userID);
        }

        // Sắp xếp: chưa đọc lên đầu, đã đọc xuống cuối, mỗi nhóm theo CreatedAt DESC
        sql.append(" ORDER BY IsRead ASC, CreatedAt DESC");
        // Thêm phân trang
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((page - 1) * pageSize);
        params.add(pageSize);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            // Set tham số
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationID(rs.getInt("NotificationID"));
                    notification.setTitle(rs.getString("Title"));
                    notification.setMessage(rs.getString("Message"));
                    notification.setIsRead(rs.getBoolean("IsRead"));
                    notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    int userIdValue = rs.getInt("UserID");
                    notification.setUserID(rs.wasNull() ? null : userIdValue);

                    // Apply keyword filter in Java if keyword is provided
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        if (keyword.length() > 100) {
                            throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
                        }
                        String normalizedTitle = normalizeVietnamese(notification.getTitle());
                        String normalizedMessage = normalizeVietnamese(notification.getMessage());
                        if (normalizedTitle.contains(keyword) || normalizedMessage.contains(keyword)) {
                            notifications.add(notification);
                        }
                    } else {
                        notifications.add(notification);
                    }
                }
            }
            System.out.println("NotificationDAO: Số thông báo lấy được (page " + page + "): " + notifications.size());
        } catch (SQLException e) {
            System.err.println("NotificationDAO: Lỗi khi lấy danh sách thông báo: " + e.getMessage());
            throw new RuntimeException("Lỗi khi lấy danh sách thông báo: " + e.getMessage(), e);
        }
        return notifications;
    }

    // Đếm tổng số thông báo theo bộ lọc
    public int countFilteredNotifications(String keyword, String status, Integer userID) {
        List<Notification> notifications = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT NotificationID, Title, Message, IsRead, CreatedAt, UserID FROM Notifications WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Thêm điều kiện trạng thái
        if (status != null && !status.isEmpty()) {
            if ("read".equals(status)) {
                sql.append(" AND IsRead = 1");
            } else if ("unread".equals(status)) {
                sql.append(" AND IsRead = 0");
            } else {
                throw new IllegalArgumentException("Invalid status value: " + status);
            }
        }

        // Thêm điều kiện UserID
        if (userID != null) {
            sql.append(" AND (UserID = ? OR UserID IS NULL)");
            params.add(userID);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            // Set tham số
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationID(rs.getInt("NotificationID"));
                    notification.setTitle(rs.getString("Title"));
                    notification.setMessage(rs.getString("Message"));
                    notification.setIsRead(rs.getBoolean("IsRead"));
                    notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    int userIdValue = rs.getInt("UserID");
                    notification.setUserID(rs.wasNull() ? null : userIdValue);

                    // Apply keyword filter in Java if keyword is provided
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        if (keyword.length() > 100) {
                            throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
                        }
                        String normalizedTitle = normalizeVietnamese(notification.getTitle());
                        String normalizedMessage = normalizeVietnamese(notification.getMessage());
                        if (normalizedTitle.contains(keyword) || normalizedMessage.contains(keyword)) {
                            notifications.add(notification);
                        }
                    } else {
                        notifications.add(notification);
                    }
                }
            }
            int count = notifications.size();
            System.out.println("NotificationDAO: Tổng số thông báo: " + count);
            return count;
        } catch (SQLException e) {
            System.err.println("NotificationDAO: Lỗi khi đếm thông báo: " + e.getMessage());
            throw new RuntimeException("Lỗi khi đếm thông báo: " + e.getMessage(), e);
        }
    }

    // Xóa thông báo theo notificationID
    public boolean deleteNotification(int notificationID) {
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notificationID);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("NotificationDAO: Đã xóa thông báo ID " + notificationID + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("NotificationDAO: Lỗi khi xóa thông báo ID " + notificationID + ": " + e.getMessage());
            throw new RuntimeException("Lỗi khi xóa thông báo: " + e.getMessage(), e);
        }
    }

    // Đánh dấu thông báo là đã đọc
    public boolean markNotificationAsRead(int notificationID) {
        String sql = "UPDATE Notifications SET IsRead = 1 WHERE NotificationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notificationID);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("NotificationDAO: Đã đánh dấu thông báo ID " + notificationID + " là đã đọc, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("NotificationDAO: Lỗi khi đánh dấu thông báo ID " + notificationID + ": " + e.getMessage());
            throw new RuntimeException("Lỗi khi đánh dấu thông báo: " + e.getMessage(), e);
        }
    }

    // Normalize Vietnamese text by removing diacritics and converting to lowercase
    private String normalizeVietnamese(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }
}