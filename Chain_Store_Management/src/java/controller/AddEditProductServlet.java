package controller;

import DAO.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.nio.file.Paths;
import model.Category;
import model.Color;
import model.Size;
import model.Product;

@WebServlet(name = "AddEditProductServlet", urlPatterns = {"/add-edit-product"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10, // 10MB
    maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class AddEditProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddEditProductServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO(getServletContext());
            String action = request.getParameter("action");
            String productIdParam = request.getParameter("productId");

            // Load dropdown data
            List<Size> sizes = productDAO.getSizes();
            List<Color> colors = productDAO.getColors();
            List<Category> categories = productDAO.getCategories();

            request.setAttribute("sizeSuggestions", sizes);
            request.setAttribute("colorSuggestions", colors);
            request.setAttribute("categories", categories);

            if ("edit".equals(action) && productIdParam != null) {
                try {
                    int productId = Integer.parseInt(productIdParam);
                    Product product = productDAO.getProductById(productId);
                    if (product != null) {
                        request.setAttribute("product", product);
                        request.setAttribute("action", "edit");
                    } else {
                        request.setAttribute("message", "Không tìm thấy sản phẩm");
                        request.setAttribute("messageType", "danger");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("message", "Mã sản phẩm không hợp lệ");
                    request.setAttribute("messageType", "danger");
                }
            } else {
                request.setAttribute("action", "add");
            }

            if (!response.isCommitted()) {
                request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            request.setAttribute("message", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            if (!response.isCommitted()) {
                request.getRequestDispatcher("/products.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products?message=" + URLEncoder.encode("Lỗi cơ sở dữ liệu", "UTF-8") + "&messageType=danger");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            request.setAttribute("message", "Lỗi không xác định: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            if (!response.isCommitted()) {
                request.getRequestDispatcher("/products.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products?message=" + URLEncoder.encode("Lỗi không xác định", "UTF-8") + "&messageType=danger");
            }
        } finally {
            if (productDAO != null) productDAO.closeConnection();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO(getServletContext());
            String action = request.getParameter("action");

            if (action == null || action.isEmpty()) {
                LOGGER.warning("Missing action parameter");
                request.setAttribute("message", "Thiếu tham số action");
                request.setAttribute("messageType", "danger");
                request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
                return;
            }

            // Validate form inputs
            Map<String, String> errors = new HashMap<>();
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            String sellingPriceParam = request.getParameter("sellingPrice");
            String sizeIDParam = request.getParameter("sizeID");
            String colorIDParam = request.getParameter("colorID");
            String unit = request.getParameter("unit");
            String stockQuantityParam = request.getParameter("stockQuantity");
            String categoryIDParam = request.getParameter("categoryID");
            String barcode = request.getParameter("barcode");
            String productCode = request.getParameter("productCode");

            // Log input parameters for debugging
            LOGGER.info("Input parameters: productName=" + productName + ", description=" + description +
                        ", sellingPriceParam=" + sellingPriceParam + ", unit=" + unit +
                        ", stockQuantityParam=" + stockQuantityParam + ", categoryIDParam=" + categoryIDParam +
                        ", barcode=" + barcode + ", productCode=" + productCode + ", sizeIDParam=" + sizeIDParam +
                        ", colorIDParam=" + colorIDParam);

            // Validate required fields
            if (productName == null || productName.trim().isEmpty() || productName.length() > 100) {
                errors.put("productName", "Tên sản phẩm là bắt buộc và phải từ 1-100 ký tự.");
            }
            if (description == null || description.trim().isEmpty()) {
                errors.put("description", "Mô tả là bắt buộc.");
            }
            if (sellingPriceParam == null || sellingPriceParam.trim().isEmpty()) {
                errors.put("sellingPrice", "Giá bán là bắt buộc.");
            }
            if (unit == null || unit.trim().isEmpty()) {
                errors.put("unit", "Đơn vị là bắt buộc.");
            }
            if (stockQuantityParam == null || stockQuantityParam.trim().isEmpty()) {
                errors.put("stockQuantity", "Số lượng tồn là bắt buộc.");
            }
            if (categoryIDParam == null || categoryIDParam.trim().isEmpty()) {
                errors.put("categoryID", "Danh mục là bắt buộc.");
            }

            // Validate numeric fields
            BigDecimal sellingPrice = null;
            int stockQuantity = 0;
            int categoryID = 0;
            Integer sizeID = null;
            Integer colorID = null;
            try {
                if (sellingPriceParam != null && !sellingPriceParam.trim().isEmpty()) {
                    sellingPrice = new BigDecimal(sellingPriceParam);
                    if (sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
                        errors.put("sellingPrice", "Giá bán phải lớn hơn 0.");
                    }
                }
                if (stockQuantityParam != null && !stockQuantityParam.trim().isEmpty()) {
                    stockQuantity = Integer.parseInt(stockQuantityParam);
                    if (stockQuantity < 0) {
                        errors.put("stockQuantity", "Số lượng tồn không được âm.");
                    }
                }
                if (categoryIDParam != null && !categoryIDParam.trim().isEmpty()) {
                    categoryID = Integer.parseInt(categoryIDParam);
                    if (categoryID <= 0) {
                        errors.put("categoryID", "Danh mục không hợp lệ.");
                    }
                }
                if (sizeIDParam != null && !sizeIDParam.isEmpty()) {
                    sizeID = Integer.parseInt(sizeIDParam);
                }
                if (colorIDParam != null && !colorIDParam.isEmpty()) {
                    colorID = Integer.parseInt(colorIDParam);
                }
            } catch (NumberFormatException e) {
                if (sellingPrice == null && sellingPriceParam != null) {
                    errors.put("sellingPrice", "Giá bán không hợp lệ.");
                }
                if (stockQuantity == 0 && stockQuantityParam != null) {
                    errors.put("stockQuantity", "Số lượng tồn không hợp lệ.");
                }
                if (categoryID == 0 && categoryIDParam != null) {
                    errors.put("categoryID", "Danh mục không hợp lệ.");
                }
                if (sizeID == null && sizeIDParam != null && !sizeIDParam.isEmpty()) {
                    errors.put("sizeID", "Kích thước không hợp lệ.");
                }
                if (colorID == null && colorIDParam != null && !colorIDParam.isEmpty()) {
                    errors.put("colorID", "Màu sắc không hợp lệ.");
                }
            }

            // Validate optional fields
            if (barcode != null && barcode.length() > 50) {
                errors.put("barcode", "Mã vạch không được vượt quá 50 ký tự.");
            }
            if (productCode != null && productCode.length() > 50) {
                errors.put("productCode", "Mã sản phẩm không được vượt quá 50 ký tự.");
            }

            // Validate images
            List<Part> images = new ArrayList<>();
            List<String> existingImagePaths = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                Part filePart = request.getPart("image" + i);
                String imagePath = request.getParameter("imagePath" + i);
                if (filePart != null && filePart.getSize() > 0) {
                    // Validate file size
                    if (filePart.getSize() > 2 * 1024 * 1024) {
                        errors.put("image" + i, "Kích thước hình ảnh không được vượt quá 2MB.");
                    } else {
                        // Validate file type
                        String contentType = filePart.getContentType();
                        LOGGER.info("Content type for image" + i + ": " + contentType); // Debug content type
                        if (contentType == null || !List.of("image/jpeg", "image/jpg", "image/png").contains(contentType)) {
                            errors.put("image" + i, "Chỉ chấp nhận định dạng .jpg, .jpeg, và .png.");
                        } else {
                            // Validate file name
                            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                            LOGGER.info("Raw file name for image" + i + ": " + fileName); // Debug raw file name
                            if (fileName == null || fileName.trim().isEmpty()) {
                                errors.put("image" + i, "Tên file không hợp lệ.");
                            } else {
                                // Validate file name length and characters
                                if (fileName.length() > 100) {
                                    errors.put("image" + i, "Tên file quá dài, tối đa 100 ký tự.");
                                } else if (!fileName.matches("^[\\p{L}\\p{N}\\s._-]+$")) {
                                    LOGGER.warning("File name failed regex check: " + fileName); // Debug regex failure
                                    // Bỏ qua lỗi tên file nếu định dạng hợp lệ
                                    images.add(filePart);
                                } else {
                                    images.add(filePart);
                                }
                            }
                        }
                    }
                } else if (imagePath != null && !imagePath.isEmpty()) {
                    existingImagePaths.add(imagePath);
                }
            }

            // If there are errors, return to form
            if (!errors.isEmpty()) {
                LOGGER.warning("Validation errors: " + errors);
                request.setAttribute("errors", errors);
                request.setAttribute("product", createProductObject(productName, description, sellingPrice, categoryID, sizeID, colorID, unit, stockQuantity, barcode, productCode));
                request.setAttribute("action", action);
                request.setAttribute("sizeSuggestions", productDAO.getSizes());
                request.setAttribute("colorSuggestions", productDAO.getColors());
                request.setAttribute("categories", productDAO.getCategories());
                request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
                return;
            }

            // Process add or edit
            if ("add".equals(action)) {
                LOGGER.info("Adding product: productName=" + productName + ", description=" + description +
                            ", sellingPrice=" + sellingPrice + ", categoryID=" + categoryID +
                            ", sizeID=" + sizeID + ", colorID=" + colorID + ", unit=" + unit +
                            ", stockQuantity=" + stockQuantity + ", barcode=" + barcode +
                            ", productCode=" + productCode + ", images.size=" + (images != null ? images.size() : 0));
                int newProductId = productDAO.addProduct(productName, description, sellingPrice, categoryID, sizeID, colorID, unit, stockQuantity, barcode, productCode, images);
                if (newProductId > 0) {
                    LOGGER.info("Product added successfully: ID=" + newProductId);
                    // Force reload by adding a timestamp to avoid cache
                    String message = URLEncoder.encode("Thêm sản phẩm thành công", "UTF-8");
                    long timestamp = System.currentTimeMillis();
                    response.sendRedirect(request.getContextPath() + "/products?message=" + message + "&messageType=success&t=" + timestamp);
                    return;
                } else {
                    LOGGER.warning("Failed to add product");
                    request.setAttribute("message", "Thêm sản phẩm thất bại");
                    request.setAttribute("messageType", "danger");
                    request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
                    return;
                }
            } else if ("save".equals(action)) {
                int productId;
                try {
                    productId = Integer.parseInt(request.getParameter("productId"));
                } catch (NumberFormatException e) {
                    request.setAttribute("message", "Mã sản phẩm không hợp lệ");
                    request.setAttribute("messageType", "danger");
                    request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
                    return;
                }
                boolean success = productDAO.updateProduct(productId, productName, description, sellingPrice, categoryID, sizeID, colorID, unit, stockQuantity, barcode, productCode, images, existingImagePaths);
                if (success) {
                    LOGGER.info("Product updated successfully: ID=" + productId);
                    // Force reload by adding a timestamp to avoid cache
                    String message = URLEncoder.encode("Cập nhật sản phẩm thành công", "UTF-8");
                    long timestamp = System.currentTimeMillis();
                    response.sendRedirect(request.getContextPath() + "/products?message=" + message + "&messageType=success&t=" + timestamp + "&productId=" + productId);
                    return;
                } else {
                    LOGGER.warning("Failed to update product");
                    request.setAttribute("message", "Cập nhật sản phẩm thất bại");
                    request.setAttribute("messageType", "danger");
                    request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
                    return;
                }
            }

            // Reload dropdown data in case of failure
            request.setAttribute("sizeSuggestions", productDAO.getSizes());
            request.setAttribute("colorSuggestions", productDAO.getColors());
            request.setAttribute("categories", productDAO.getCategories());
            request.setAttribute("action", action);
            request.setAttribute("product", createProductObject(productName, description, sellingPrice, categoryID, sizeID, colorID, unit, stockQuantity, barcode, productCode));
            request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            request.setAttribute("message", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            request.setAttribute("message", "Lỗi không xác định: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/addEditProduct.jsp").forward(request, response);
        } finally {
            if (productDAO != null) productDAO.closeConnection();
        }
    }

    private Product createProductObject(String productName, String description, BigDecimal sellingPrice, int categoryID,
                                       Integer sizeID, Integer colorID, String unit, int stockQuantity, String barcode, String productCode) {
        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setSellingPrice(sellingPrice);
        product.setCategoryID(categoryID);
        product.setSizeID(sizeID);
        product.setColorID(colorID);
        product.setUnit(unit);
        product.setStockQuantity(stockQuantity);
        product.setBarcode(barcode);
        product.setProductCode(productCode);
        return product;
    }
}