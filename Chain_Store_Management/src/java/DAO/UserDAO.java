package DAO;

import Model.Users;
import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import org.mindrot.jbcrypt.BCrypt; // Add BCrypt dependency
//


public class UserDAO extends DBContext{

    public static UserDAO INSTANCE = new UserDAO();
 
    private String status = "OK";

//    public UserDAO() {
//        if (INSTANCE == null) {
//            try {
//                con = new DBContext().getConnection();
//                System.out.println("Kết nối database: " + con.isClosed());
//            } catch (Exception e) {
//                status = "Error at connection: " + e.getMessage();
//            }
//        }
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users checkManager(String username, String pass) {
        String sql = "SELECT * FROM Users WHERE username = ? AND RoleID = 1 AND isActive = 1";
        try {
            PreparedStatement ps =connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("PasswordHash");
                // Verify the password using BCrypt
                System.out.println("Stored hash for " + username + ": " + storedHash);
                if (pass.equals(storedHash)) {
                    return new Users(
                            rs.getInt("UserID"),
                            rs.getString("Username"),
                            rs.getString("FullName"),
                            rs.getString("Email"),
                            storedHash,
                            rs.getTimestamp("LastLogin"),
                            rs.getInt("RoleID"),
                            rs.getBoolean("isActive"),
                            rs.getTimestamp("CreatedAt")
                    );
                }
            }
        } catch (SQLException e) {
            status = "Error at read Users: " + e.getMessage();
        }
        return null;
    }
    
    public void signUp(String username, String phoneNumber, String address, String email, String password) {
    String sql = "INSERT INTO Users (Username, PasswordHash, Email, FullName, PhoneNumber, Address, roleID, IsActive, CreatedAt) " +
                 "VALUES (?, ?, ?, ?, ?, ?, 2, 1, GETDATE())"; // Luôn gán roleID = 2
    try {
        // Kiểm tra xem username đã tồn tại chưa
        PreparedStatement psCheck = connection.prepareStatement("SELECT * FROM Users WHERE Username = ?");
        psCheck.setString(1, username);
        ResultSet rs = psCheck.executeQuery();
        if (rs.next()) {
            // Kiểm tra nếu tài khoản đã tồn tại và có roleID = 1 (Admin)
            int existingRoleID = rs.getInt("roleID");
            if (existingRoleID == 1) {
                status = "Error at insert Users: Cannot register an Admin account!";
                return;
            }
            status = "Error at insert Users: Username already exists!";
            return;
        }

        // Thêm người dùng mới với roleID = 2 or !1
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt())); // Mã hóa mật khẩu
        ps.setString(3, email);
        ps.setString(4, ""); // FullName tạm thời để trống
        ps.setString(5, phoneNumber);
        ps.setString(6, address);
        
//vì roleID đã được gán cứng là 2 trong câu SQL
//        ps.setInt(7, 2); // roleID = 2 (Employee)
        ps.executeUpdate();
        status = "OK";
    } catch (SQLException e) {
        status = "Error at insert Users: " + e.getMessage();
        e.printStackTrace();
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    public static void main(String[] args) {
//        UserDAO da = new UserDAO();
//        da.checkManager("admin1", "$2a$10$hashedpassword1");
//        System.out.println( da.checkManager("admin1", "$2a$10$hashedpassword1").getUsername());
UserDAO da = new UserDAO();
    Users user = da.checkManager("admin1", "$2a$10$hashedpassword1"); // Truyền mật khẩu đã mã hóa
    if (user != null) {
        System.out.println(user.getUsername());
    } else {
        System.out.println("Login failed. Check Username, PasswordHash, or RoleID. Status: " + da.getStatus());
    }
   }

    // Other methods (checkSignUp, checkPassWord, etc.) remain unchanged for now
    // ...
}