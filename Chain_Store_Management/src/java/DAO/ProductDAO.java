package DAO;

import dal.DBContext;
import model.Product;
import model.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductDAO extends DBContext {

    // Fetch products for a specific page with search and filter
    public List<Product> getProductsByPage(String search, Integer categoryID, Boolean isActive, int page, int pageSize) throws SQLException {
        // Validation
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 5;
        if (search != null) {
            search = search.trim();
            if (search.isEmpty() || search.length() > 100) {
                search = null;
            } else {
                search = search.replaceAll("[<>\"&'%]", ""); // Loại bỏ ký tự nguy hiểm
            }
        }
        if (categoryID != null && categoryID <= 0) {
            categoryID = null;
        }

        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        StringBuilder sql = new StringBuilder(
            "SELECT p.ProductID, p.ProductName, p.CategoryID, c.CategoryName, " +
            "p.Size, p.Color, p.Price, p.CostPrice, p.Unit, p.Description, " +
            "p.Images, p.IsActive, p.ReleaseDate " +
            "FROM Products p " +
            "INNER JOIN Categories c ON p.CategoryID = c.CategoryID " +
            "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        // Thêm điều kiện tìm kiếm (hỗ trợ tiếng Việt)
        if (search != null) {
            sql.append(" AND (p.ProductName LIKE N'%' + ? + '%' OR p.Description LIKE N'%' + ? + '%')");
            params.add(search);
            params.add(search);
        }

        // Thêm điều kiện lọc theo danh mục
        if (categoryID != null) {
            sql.append(" AND p.CategoryID = ?");
            params.add(categoryID);
        }

        // Thêm điều kiện lọc theo trạng thái
        if (isActive != null) {
            sql.append(" AND p.IsActive = ?");
            params.add(isActive ? 1 : 0);
        }

        sql.append(" ORDER BY p.IsActive DESC, p.ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(pageSize);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
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
            System.err.println("ProductDAO: Lỗi khi lấy sản phẩm: " + e.getMessage());
            throw e;
        }
        return products;
    }

    // Get total number of products with filters
    public int getTotalProductCount(String search, Integer categoryID, Boolean isActive) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM Products p " +
            "INNER JOIN Categories c ON p.CategoryID = c.CategoryID " +
            "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        // Validation và tìm kiếm
        if (search != null) {
            search = search.trim();
            if (search.isEmpty() || search.length() > 100) {
                search = null;
            } else {
                search = search.replaceAll("[<>\"&'%]", "");
                if (!search.isEmpty()) {
                    sql.append(" AND (p.ProductName LIKE N'%' + ? + '%' OR p.Description LIKE N'%' + ? + '%')");
                    params.add(search);
                    params.add(search);
                }
            }
        }

        // Thêm điều kiện lọc theo danh mục
        if (categoryID != null && categoryID > 0) {
            sql.append(" AND p.CategoryID = ?");
            params.add(categoryID);
        }

        // Thêm điều kiện lọc theo trạng thái
        if (isActive != null) {
            sql.append(" AND p.IsActive = ?");
            params.add(isActive ? 1 : 0);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("ProductDAO: Lỗi khi đếm sản phẩm: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    // Lấy danh sách danh mục để lọc
    public List<Category> getCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName FROM Categories WHERE IsActive = 1 ORDER BY CategoryName";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("ProductDAO: Lỗi khi lấy danh mục: " + e.getMessage());
            throw e;
        }
        return categories;
    }

    // Đóng kết nối
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("ProductDAO: Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
}