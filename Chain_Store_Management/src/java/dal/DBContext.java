package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



/**
 *
 * @author ADMIN
 */
public class DBContext{
        public Connection connection;
  

  

    public DBContext() {
        try {
            String user = "sa";
            String pass = "12345";
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Shop;encrypt=true;trustServerCertificate=true";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  

   
    
    
    public static void main(String[] args) {
        DBContext db = new DBContext();
        
    }
}