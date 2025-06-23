package controller;

import DAO.ProductDAO;
import model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UpdateProductServlet", urlPatterns = {"/update-product"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class UpdateProductServlet extends HttpServlet {

    private ProductDAO productDAO;
    private static final Logger LOGGER = Logger.getLogger(UpdateProductServlet.class.getName());

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Processing request for /update-product");
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String action = request.getParameter("action");
        LOGGER.info("Action parameter: " + action);
        List<String> errors = new ArrayList<>();

        if ("save".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId").trim());
                LOGGER.info("Processing productId: " + productId);
                String productName = request.getParameter("productName") != null ? request.getParameter("productName").trim() : "";
                String description = request.getParameter("description") != null ? request.getParameter("description").trim() : "";
                BigDecimal sellingPrice = new BigDecimal(request.getParameter("sellingPrice").trim());
                int categoryID = Integer.parseInt(request.getParameter("categoryID").trim());
                Integer sizeID = request.getParameter("sizeID") != null && !request.getParameter("sizeID").trim().isEmpty() ?
                                Integer.parseInt(request.getParameter("sizeID").trim()) : null;
                Integer colorID = request.getParameter("colorID") != null && !request.getParameter("colorID").trim().isEmpty() ?
                                Integer.parseInt(request.getParameter("colorID").trim()) : null;
                String unit = request.getParameter("unit") != null ? request.getParameter("unit").trim() : "";
                int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity").trim());
                String barcode = request.getParameter("barcode") != null ? request.getParameter("barcode").trim() : "";
                String productCode = request.getParameter("productCode") != null ? request.getParameter("productCode").trim() : "";

                if (productName.isEmpty()) errors.add("Tên sản phẩm không được để trống.");
                if (unit.isEmpty()) errors.add("Đơn vị không được để trống.");
                if (description.isEmpty()) errors.add("Mô tả không được để trống.");
                if (sellingPrice.compareTo(BigDecimal.ZERO) <= 0) errors.add("Giá bán phải lớn hơn 0.");
                if (stockQuantity < 0) errors.add("Số lượng tồn không được âm.");

                // Lấy danh sách Part từ request
                List<Part> images = new ArrayList<>();
                for (int i = 1; i <= 3; i++) {
                    Part filePart = request.getPart("image" + i);
                    if (filePart != null && filePart.getSize() > 0) {
                        LOGGER.info("Found Part for image" + i + ": size=" + filePart.getSize() + ", contentType=" + filePart.getContentType());
                        images.add(filePart);
                    } else {
                        LOGGER.warning("No valid Part found for image" + i + ". Check file input.");
                    }
                }

                if (errors.isEmpty()) {
                    Product existingProduct = productDAO.getProductById(productId);
                    String existingImages = (existingProduct != null && existingProduct.getImages() != null) ?
                                          existingProduct.getImages() : "";
                    LOGGER.info("Existing images for productId " + productId + ": " + existingImages);

                    boolean updated = productDAO.updateProduct(productId, productName, description, sellingPrice,
                            categoryID, sizeID, colorID, unit, stockQuantity, barcode, productCode, images);
                    if (updated) {
                        LOGGER.info("Removed tempImageBytes for productId: " + productId + " after successful update");
                        String encodedMessage = URLEncoder.encode("Cập nhật sản phẩm thành công", StandardCharsets.UTF_8.toString());
                        response.sendRedirect(request.getContextPath() + "/products?message=" + encodedMessage + "&messageType=success" +
                                (request.getParameter("search") != null ? "&search=" + java.net.URLEncoder.encode(request.getParameter("search"), StandardCharsets.UTF_8.toString()) : "") +
                                (request.getParameter("page") != null ? "&page=" + request.getParameter("page") : ""));
                        return;
                    } else {
                        errors.add("Cập nhật sản phẩm thất bại. Vui lòng kiểm tra dữ liệu hoặc thử lại.");
                    }
                } else {
                    Product existingProduct = productDAO.getProductById(productId);
                    String existingImages = (existingProduct != null && existingProduct.getImages() != null) ?
                                          existingProduct.getImages() : "";
                    Product tempProduct = new Product(productName, categoryID, sizeID, colorID, sellingPrice,
                            description, existingImages, true, barcode, productCode, stockQuantity, unit);
                    request.setAttribute("product", tempProduct);
                    request.setAttribute("productId", productId);
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Dữ liệu số không hợp lệ: {0}", e.getMessage());
                errors.add("Định dạng dữ liệu không hợp lệ. Vui lòng kiểm tra các trường số.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật sản phẩm: {0}", e.getMessage());
                errors.add("Đã xảy ra lỗi: " + e.getMessage());
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("message", "Vui lòng sửa các lỗi sau:");
                request.setAttribute("messageType", "danger");
                request.getRequestDispatcher("/products.jsp").forward(request, response);
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/products");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void destroy() {
        if (productDAO != null) {
            productDAO.closeConnection();
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet để cập nhật thông tin sản phẩm";
    }
}