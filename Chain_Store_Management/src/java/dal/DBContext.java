package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {
    // Thay đổi thông tin kết nối CSDL của bạn tại đây
    private static final String DB_URL = "jdbc:sqlserver://LAPTOP-QJKTA12L\\KTEAM:1433;databaseName=ShopMoiNhat"; // Thay đổi nếu tên DB khác
    private static final String USER = "saa"; // Thay đổi user của bạn
    private static final String PASS = "1234"; // Thay đổi password của bạn

    public static Connection getConnection() throws SQLException {
        try {
            // Đảm bảo bạn đã thêm thư viện JDBC Driver cho SQL Server vào project
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("SQL Server Driver not found. Please ensure the JDBC driver JAR is in your classpath.", ex);
        }
    }

    // Phương thức main để kiểm tra kết nối (tùy chọn, có thể xóa sau khi kiểm tra)
    public static void main(String[] args) {
        try (Connection connection = DBContext.getConnection()) {
            if (connection != null) {
                System.out.println("Kết nối CSDL thành công!");
            } else {
                System.out.println("Kết nối CSDL thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
        }
    }
}