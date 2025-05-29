package DAO;

import dal.DBContext;
import model.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductDAO extends DBContext {

    // Fetch products for a specific page
    public List<Product> getProductsByPage(int page, int pageSize) throws SQLException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * pageSize; // Calculate the starting row for the current page

        String sql = "SELECT p.ProductID, p.ProductName, p.CategoryID, c.CategoryName, " +
                     "p.Size, p.Color, p.Price, p.CostPrice, p.Unit, p.Description, " +
                     "p.Images, p.IsActive, p.ReleaseDate " +
                     "FROM Products p " +
                     "INNER JOIN Categories c ON p.CategoryID = c.CategoryID " +
                     "WHERE p.IsActive = 1 AND c.IsActive = 1 " +
                     "ORDER BY p.ProductID " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"; // Pagination using OFFSET and FETCH

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("ProductID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setCategoryID(rs.getInt("CategoryID"));
                    product.setCategoryName(rs.getString("CategoryName"));
                    product.setSize(rs.getString("Size"));
                    product.setColor(rs.getString("Color"));
                    product.setPrice(rs.getBigDecimal("Price"));
                    product.setCostPrice(rs.getBigDecimal("CostPrice"));
                    product.setUnit(rs.getString("Unit"));
                    product.setDescription(rs.getString("Description"));
                    product.setImages(rs.getString("Images"));
                    product.setIsActive(rs.getBoolean("IsActive"));
                    product.setReleaseDate(rs.getDate("ReleaseDate"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("ProductDAO: Error fetching products by page: " + e.getMessage());
            throw e;
        }
        return products;
    }

    // Get the total number of active products
    public int getTotalProductCount() throws SQLException {
        String sql = "SELECT COUNT(*) " +
                     "FROM Products p " +
                     "INNER JOIN Categories c ON p.CategoryID = c.CategoryID " +
                     "WHERE p.IsActive = 1 AND c.IsActive = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ProductDAO: Error fetching product count: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    // Optional: Keep the original method for other uses
    public List<Product> getAllProducts() throws SQLException {
        return getProductsByPage(1, Integer.MAX_VALUE); // Fetch all products
    }

    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        try {
            int page = 1;
            int pageSize = 5;
            List<Product> products = dao.getProductsByPage(page, pageSize);
            System.out.println("Products on page " + page + " (size " + pageSize + "): " + products.size());
            for (Product p : products) {
                System.out.println("ID: " + p.getProductID());
                System.out.println("Name: " + p.getProductName());
                System.out.println("Category: " + p.getCategoryName());
                System.out.println("Size: " + p.getSize());
                System.out.println("Color: " + p.getColor());
                System.out.println("Price: " + p.getPrice());
                System.out.println("Is Active: " + p.getIsActive());
                System.out.println("==================================");
            }
            int totalProducts = dao.getTotalProductCount();
            System.out.println("Total products: " + totalProducts);
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}