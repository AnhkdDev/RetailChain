package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FPT University - PRJ30X
 */
public class DBContext {
    protected Connection connection;

    public DBContext() {
        try {
            String user = "sa";
            String pass = "123";
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Shop";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
            if (connection == null) {
                throw new SQLException("Failed to establish database connection.");
            }
            System.out.println("Connected to database successfully!");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Database connection error", ex);
            throw new RuntimeException("Cannot connect to database: " + ex.getMessage(), ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Error closing connection", ex);
        }
    }

    public static void main(String[] args) {
        DBContext dbContext = new DBContext();
        System.out.println("Connection: " + dbContext.getConnection());
        dbContext.closeConnection();
    }
}