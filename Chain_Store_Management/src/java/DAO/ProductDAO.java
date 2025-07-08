package DAO;

import dal.DBContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;
import model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.stream.Collectors;

public class ProductDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final String UPLOAD_DIR = "images";
    private ServletContext servletContext;

    public ProductDAO() {
        super();
    }

    public ProductDAO(ServletContext context) {
        super();
        this.servletContext = context;
    }

    public List<Product> getProductsByPage(List<String> searchKeywords, Integer categoryID, Boolean isActive) throws SQLException {
        List<String> normalizedKeywords = null;
        if (searchKeywords != null && !searchKeywords.isEmpty()) {
            normalizedKeywords = searchKeywords.stream()
                    .map(keyword -> removeAccent(keyword).toLowerCase())
                    .filter(keyword -> !keyword.isEmpty())
                    .collect(Collectors.toList());
        }

        if (categoryID != null && categoryID <= 0) categoryID = null;

        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.ProductID, p.ProductName, p.CategoryID, c.CategoryName, p.SizeID, p.ColorID, p.SellingPrice, p.CostPrice, p.Description, " +
                "p.Images, p.IsActive, p.ReleaseDate, p.Barcode, p.ProductCode, p.StockQuantity, p.Unit " +
                "FROM Products p " +
                "INNER JOIN Categories c ON p.CategoryID = c.CategoryID " +
                "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (categoryID != null) {
            sql.append(" AND p.CategoryID = ?");
            params.add(categoryID);
        }
        if (isActive != null) {
            sql.append(" AND p.IsActive = ?");
            params.add(isActive ? 1 : 0);
        }
        sql.append(" ORDER BY p.IsActive DESC, p.ProductID");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) stmt.setObject(i + 1, params.get(i));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("ProductID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setCategoryID(rs.getInt("CategoryID"));
                    product.setCategoryName(rs.getString("CategoryName"));
                    product.setSizeID(rs.getObject("SizeID") != null ? rs.getInt("SizeID") : null);
                    product.setColorID(rs.getObject("ColorID") != null ? rs.getInt("ColorID") : null);
                    product.setSellingPrice(rs.getBigDecimal("SellingPrice"));
                    product.setCostPrice(rs.getBigDecimal("CostPrice"));
                    product.setDescription(rs.getString("Description"));
                    product.setImages(rs.getString("Images"));
                    product.setIsActive(rs.getBoolean("IsActive"));
                    product.setReleaseDate(rs.getDate("ReleaseDate"));
                    product.setBarcode(rs.getString("Barcode"));
                    product.setProductCode(rs.getString("ProductCode"));
                    product.setStockQuantity(rs.getInt("StockQuantity"));
                    product.setUnit(rs.getString("Unit"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching products: " + e.getMessage(), e);
            throw e;
        }

        if (normalizedKeywords != null && !normalizedKeywords.isEmpty()) {
            final List<String> finalKeywords = normalizedKeywords;
            products = products.stream()
                    .filter(p -> {
                        String name = p.getProductName() != null ? removeAccent(p.getProductName()).toLowerCase() : "";
                        String desc = p.getDescription() != null ? removeAccent(p.getDescription()).toLowerCase() : "";
                        return finalKeywords.stream().anyMatch(keyword ->
                                name.contains(keyword) || desc.contains(keyword)
                        );
                    })
                    .collect(Collectors.toList());
        }
        LOGGER.info("Fetched " + products.size() + " products for keywords: " + (normalizedKeywords != null ? normalizedKeywords : "none"));
        return products;
    }

    public int getTotalProductCount(List<String> searchKeywords, Integer categoryID, Boolean isActive) throws SQLException {
        List<String> normalizedKeywords = null;
        if (searchKeywords != null && !searchKeywords.isEmpty()) {
            normalizedKeywords = searchKeywords.stream()
                    .map(keyword -> removeAccent(keyword).toLowerCase())
                    .filter(keyword -> !keyword.isEmpty())
                    .collect(Collectors.toList());
        }
        if (categoryID != null && categoryID <= 0) categoryID = null;

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Products p " +
                "INNER JOIN Categories c ON p.CategoryID = c.CategoryID " +
                "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (categoryID != null) {
            sql.append(" AND p.CategoryID = ?");
            params.add(categoryID);
        }
        if (isActive != null) {
            sql.append(" AND p.IsActive = ?");
            params.add(isActive ? 1 : 0);
        }

        int totalCount;
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) stmt.setObject(i + 1, params.get(i));
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                totalCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting products: " + e.getMessage(), e);
            throw e;
        }

        // Lọc theo từ khóa trong Java để hỗ trợ tiếng Việt
        if (normalizedKeywords != null && !normalizedKeywords.isEmpty()) {
            List<Product> products = getProductsByPage(searchKeywords, categoryID, isActive);
            totalCount = products.size();
        }

        LOGGER.info("Total product count: " + totalCount + " for keywords: " + (normalizedKeywords != null ? normalizedKeywords : "none"));
        return totalCount;
    }

    public List<Category> getCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName, TypeID FROM Categories WHERE IsActive = 1 ORDER BY CategoryName";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                category.setTypeID(rs.getInt("TypeID"));
                categories.add(category);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories: " + e.getMessage(), e);
            throw e;
        }
        return categories;
    }

    public List<Size> getSizes() throws SQLException {
        List<Size> sizes = new ArrayList<>();
        String sql = "SELECT SizeID, SizeValue, IsActive FROM Sizes WHERE IsActive = 1 ORDER BY SizeValue";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Size size = new Size();
                size.setSizeID(rs.getInt("SizeID"));
                size.setSizeValue(rs.getString("SizeValue"));
                size.setIsActive(rs.getBoolean("IsActive"));
                sizes.add(size);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching sizes: " + e.getMessage(), e);
            throw e;
        }
        LOGGER.info("Fetched " + sizes.size() + " sizes");
        return sizes;
    }

    public List<Color> getColors() throws SQLException {
        List<Color> colors = new ArrayList<>();
        String sql = "SELECT ColorID, ColorValue, IsActive FROM Colors WHERE IsActive = 1 ORDER BY ColorValue";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Color color = new Color();
                color.setColorID(rs.getInt("ColorID"));
                color.setColorValue(rs.getString("ColorValue"));
                color.setIsActive(rs.getBoolean("IsActive"));
                colors.add(color);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching colors: " + e.getMessage(), e);
            throw e;
        }
        LOGGER.info("Fetched " + colors.size() + " colors");
        return colors;
    }

    public int addProduct(String productName, String description, BigDecimal sellingPrice,
                          int categoryID, Integer sizeID, Integer colorID, String unit, int stockQuantity,
                          String barcode, String productCode, List<Part> images) throws SQLException, IOException {
        if (productName == null || productName.trim().isEmpty() || productName.length() > 100) {
            throw new IllegalArgumentException("Tên sản phẩm là bắt buộc và phải từ 1-100 ký tự.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả là bắt buộc.");
        }
        if (sellingPrice == null || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá bán phải lớn hơn 0.");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Đơn vị là bắt buộc.");
        }
        if (categoryID <= 0) {
            throw new IllegalArgumentException("Danh mục không hợp lệ.");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Số lượng tồn không được âm.");
        }
        if (barcode != null && barcode.length() > 50) {
            throw new IllegalArgumentException("Mã vạch không được vượt quá 50 ký tự.");
        }
        if (productCode != null && productCode.length() > 50) {
            throw new IllegalArgumentException("Mã sản phẩm không được vượt quá 50 ký tự.");
        }

        if (servletContext == null) {
            throw new IllegalStateException("ServletContext is not initialized");
        }

        String uploadPathStr = servletContext.getRealPath(UPLOAD_DIR);
        java.nio.file.Path uploadPath = Paths.get(uploadPathStr);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            LOGGER.info("Created upload directory: " + uploadPathStr);
        }

        String sql = "INSERT INTO Products (ProductName, CategoryID, SizeID, ColorID, SellingPrice, Description, Images, IsActive, ReleaseDate, Barcode, ProductCode, StockQuantity, Unit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 1, GETDATE(), ?, ?, ?, ?)";
        StringBuilder imagePaths = new StringBuilder();

        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                Part filePart = images.get(i);
                if (filePart != null && filePart.getSize() > 0) {
                    String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String baseName = productName != null && productName.length() > 10 ? productName.substring(0, 10) : productName;
                    // Chuẩn hóa tên file: loại bỏ ký tự đặc biệt và giữ phần mở rộng
                    String fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                    String fileExt = originalFileName.substring(originalFileName.lastIndexOf('.')).toLowerCase();
                    String normalizedFileName = removeAccent(fileNameWithoutExt).replaceAll("[^a-zA-Z0-9._-]", "_") + fileExt;
                    String filePath = UPLOAD_DIR + "/" + baseName + "_" + System.currentTimeMillis() + "_" + normalizedFileName;
                    String fullPath = servletContext.getRealPath(filePath);
                    LOGGER.info("Original file name: " + originalFileName);
                    LOGGER.info("Normalized file name: " + normalizedFileName);
                    LOGGER.info("Generated file path: " + filePath);
                    LOGGER.info("Full path: " + fullPath);
                    try {
                        Files.createDirectories(Paths.get(fullPath).getParent()); // Đảm bảo thư mục cha tồn tại
                        filePart.write(fullPath);
                        if (imagePaths.length() > 0) imagePaths.append(";");
                        imagePaths.append(filePath);
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error saving file: " + fullPath + ", Error: " + e.getMessage(), e);
                        throw e;
                    }
                }
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, productName);
            stmt.setInt(2, categoryID);
            stmt.setObject(3, sizeID);
            stmt.setObject(4, colorID);
            stmt.setBigDecimal(5, sellingPrice);
            stmt.setString(6, description);
            stmt.setString(7, imagePaths.length() > 0 ? imagePaths.toString() : null);
            stmt.setString(8, barcode != null ? barcode : "");
            stmt.setString(9, productCode != null ? productCode : "");
            stmt.setInt(10, stockQuantity);
            stmt.setString(11, unit);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        LOGGER.info("Added new product: ID=" + generatedKeys.getInt(1));
                        return generatedKeys.getInt(1);
                    }
                }
            }
            LOGGER.warning("Failed to add product: No rows affected");
            return -1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding product: " + e.getMessage(), e);
            throw e;
        }
    }

    public boolean updateProduct(int productId, String productName, String description, BigDecimal sellingPrice,
                                int categoryID, Integer sizeID, Integer colorID, String unit, int stockQuantity,
                                String barcode, String productCode, List<Part> images, List<String> existingImagePaths) throws SQLException, IOException {
        if (servletContext == null) {
            throw new IllegalStateException("ServletContext is not initialized");
        }
        String uploadPathStr = servletContext.getRealPath(UPLOAD_DIR);
        java.nio.file.Path uploadPath = Paths.get(uploadPathStr);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            LOGGER.info("Created upload directory: " + uploadPathStr);
        }

        StringBuilder imagePaths = new StringBuilder();
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                Part filePart = images.get(i);
                if (filePart != null && filePart.getSize() > 0) {
                    String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String baseName = productName != null && productName.length() > 10 ? productName.substring(0, 10) : productName;
                    // Chuẩn hóa tên file: loại bỏ ký tự đặc biệt và giữ phần mở rộng
                    String fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                    String fileExt = originalFileName.substring(originalFileName.lastIndexOf('.')).toLowerCase();
                    String normalizedFileName = removeAccent(fileNameWithoutExt).replaceAll("[^a-zA-Z0-9._-]", "_") + fileExt;
                    String filePath = UPLOAD_DIR + "/" + baseName + "_" + System.currentTimeMillis() + "_" + normalizedFileName;
                    String fullPath = servletContext.getRealPath(filePath);
                    LOGGER.info("Original file name: " + originalFileName);
                    LOGGER.info("Normalized file name: " + normalizedFileName);
                    LOGGER.info("Generated file path: " + filePath);
                    LOGGER.info("Full path: " + fullPath);
                    try {
                        Files.createDirectories(Paths.get(fullPath).getParent()); // Đảm bảo thư mục cha tồn tại
                        filePart.write(fullPath);
                        if (imagePaths.length() > 0) imagePaths.append(";");
                        imagePaths.append(filePath);
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error saving file: " + fullPath + ", Error: " + e.getMessage(), e);
                        throw e;
                    }
                }
            }
        }

        if (imagePaths.length() == 0 && existingImagePaths != null && !existingImagePaths.isEmpty()) {
            imagePaths.append(String.join(";", existingImagePaths));
        }

        String sql = "UPDATE Products SET ProductName = ?, CategoryID = ?, SizeID = ?, ColorID = ?, SellingPrice = ?, Description = ?, " +
                "Images = ?, Barcode = ?, ProductCode = ?, StockQuantity = ?, Unit = ? WHERE ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productName);
            stmt.setInt(2, categoryID);
            stmt.setObject(3, sizeID);
            stmt.setObject(4, colorID);
            stmt.setBigDecimal(5, sellingPrice);
            stmt.setString(6, description);
            stmt.setString(7, imagePaths.length() > 0 ? imagePaths.toString() : null);
            stmt.setString(8, barcode);
            stmt.setString(9, productCode);
            stmt.setInt(10, stockQuantity);
            stmt.setString(11, unit);
            stmt.setInt(12, productId);

            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Updated product: ID=" + productId + ", RowsAffected=" + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product: " + e.getMessage(), e);
            throw e;
        }
    }

    public Product getProductById(int productId) throws SQLException {
        String sql = "SELECT p.ProductID, p.ProductName, p.CategoryID, c.CategoryName, p.SizeID, p.ColorID, p.SellingPrice, p.CostPrice, p.Description, " +
                "p.Images, p.IsActive, p.ReleaseDate, p.Barcode, p.ProductCode, p.StockQuantity, p.Unit " +
                "FROM Products p INNER JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.ProductID = ?";
        Product product = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setProductID(rs.getInt("ProductID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setCategoryID(rs.getInt("CategoryID"));
                    product.setCategoryName(rs.getString("CategoryName"));
                    product.setSizeID(rs.getObject("SizeID") != null ? rs.getInt("SizeID") : null);
                    product.setColorID(rs.getObject("ColorID") != null ? rs.getInt("ColorID") : null);
                    product.setSellingPrice(rs.getBigDecimal("SellingPrice"));
                    product.setCostPrice(rs.getBigDecimal("CostPrice"));
                    product.setDescription(rs.getString("Description"));
                    product.setImages(rs.getString("Images"));
                    product.setIsActive(rs.getBoolean("IsActive"));
                    product.setReleaseDate(rs.getDate("ReleaseDate"));
                    product.setBarcode(rs.getString("Barcode"));
                    product.setProductCode(rs.getString("ProductCode"));
                    product.setStockQuantity(rs.getInt("StockQuantity"));
                    product.setUnit(rs.getString("Unit"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching product by ID: " + e.getMessage(), e);
            throw e;
        }
        return product;
    }

    public boolean toggleProductStatus(int productID, boolean isActive) throws SQLException {
        if (productID <= 0) {
            LOGGER.warning("Invalid productID: " + productID);
            return false;
        }

        String sqlCheck = "SELECT COUNT(*) FROM Products WHERE ProductID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(sqlCheck)) {
            checkStmt.setInt(1, productID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.warning("Product not found: " + productID);
                    return false;
                }
            }
        }

        String sql = "UPDATE Products SET IsActive = ? WHERE ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isActive);
            stmt.setInt(2, productID);
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Toggled product status: ProductID=" + productID + ", IsActive=" + isActive + ", RowsAffected=" + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error toggling product status: " + e.getMessage(), e);
            throw e;
        }
    }

    private String selectImagesById(int productId) throws SQLException {
        String sql = "SELECT Images FROM Products WHERE ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Images");
                }
            }
        }
        return null;
    }

    public static String removeAccent(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D");
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing connection: " + e.getMessage(), e);
        }
    }
}