package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBContext {

    protected Connection connection;

    public DBContext() {
       
        //NOTE:  user = "sa"; pass = "123"; url = "jdbc:sqlserver://  Tên_SQL  :1433;databaseName=  Tên_DB   ";
        
        try {
            String user = "sa";
            String pass = "123";
            String url = "jdbc:sqlserver://QUY:1433;databaseName=Shop";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        if ((new DBContext()).connection != null) {
            System.out.println("Connect success");
        } else {
            System.out.println("Connect fail");
        }
    }

}
