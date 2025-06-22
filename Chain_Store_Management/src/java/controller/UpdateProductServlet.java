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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Paths;

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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8"); // Ensure UTF-8 encoding for request parameters
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String action = request.getParameter("action");
        List<String> errors = new ArrayList<>();

        if ("save".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                String productName = request.getParameter("productName");
                String description = request.getParameter("description");
                BigDecimal sellingPrice = new BigDecimal(request.getParameter("sellingPrice"));
                int categoryID = Integer.parseInt(request.getParameter("categoryID"));
                Integer sizeID = request.getParameter("sizeID") != null && !request.getParameter("sizeID").isEmpty() ? Integer.parseInt(request.getParameter("sizeID")) : null;
                Integer colorID = request.getParameter("colorID") != null && !request.getParameter("colorID").isEmpty() ? Integer.parseInt(request.getParameter("colorID")) : null;
                String unit = request.getParameter("unit");
                int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
                String barcode = request.getParameter("barcode");
                String productCode = request.getParameter("productCode");

                // Validation
                if (productName == null || productName.trim().isEmpty()) {
                    errors.add("Product name is required.");
                }
                if (unit == null || unit.trim().isEmpty()) {
                    errors.add("Unit is required.");
                }
                if (description == null || description.trim().isEmpty()) {
                    errors.add("Description is required.");
                }
                if (sellingPrice == null || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("Selling price must be greater than zero.");
                }
                if (stockQuantity < 0) {
                    errors.add("Stock quantity cannot be negative.");
                }

                List<Part> images = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    Part filePart = request.getPart("image" + i);
                    if (filePart != null && filePart.getSize() > 0) {
                        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString().toLowerCase();
                        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
                            errors.add("Image " + i + " must be in .jpg or .png format. Uploaded: " + fileName);
                        } else {
                            images.add(filePart);
                        }
                    }
                }

                if (errors.isEmpty()) {
                    // Fetch existing product to preserve images if no new ones are uploaded
                    Product existingProduct = productDAO.getProductById(productId);
                    String existingImages = (existingProduct != null && existingProduct.getImages() != null) ? existingProduct.getImages() : "";
                    if (images.isEmpty()) {
                        images = null; // Use existing images if no new ones
                    } else {
                        StringBuilder imagePaths = new StringBuilder();
                        for (int i = 0; i < images.size(); i++) {
                            Part filePart = images.get(i);
                            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                            String baseName = productName.length() > 10 ? productName.substring(0, 10) : productName;
                            String uploadPathStr = "C:/Uploads/products/" + baseName + "_" + System.currentTimeMillis() + "_" + fileName;
                            filePart.write(uploadPathStr);
                            if (imagePaths.length() > 0) imagePaths.append(";");
                            imagePaths.append(uploadPathStr.substring(0, Math.min(uploadPathStr.length(), 200)));
                        }
                        existingImages = imagePaths.toString();
                    }

                    boolean updated = productDAO.updateProduct(productId, productName, description, sellingPrice, categoryID, sizeID, colorID, unit, stockQuantity, barcode, productCode, images);
                    if (updated) {
                        request.setAttribute("message", "Product updated successfully.");
                        request.setAttribute("messageType", "success");
                    } else {
                        errors.add("Failed to update product. Please check the data or try again.");
                    }
                } else {
                    // Pre-populate form with submitted data for error display
                    request.setAttribute("product", new Product(productName, categoryID, sizeID, colorID, sellingPrice, description, "", true, barcode, productCode, stockQuantity, unit));
                    request.setAttribute("productId", productId);
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Invalid numeric data: {0}", e.getMessage());
                errors.add("Invalid data format. Please ensure all numeric fields are correct.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error updating product: {0}", e.getMessage());
                errors.add("An error occurred: " + e.getMessage());
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("message", "Please correct the errors below.");
                request.setAttribute("messageType", "danger");
                // Forward to products.jsp to display errors in the modal
                request.getRequestDispatcher("/products.jsp").forward(request, response);
                return;
            }
        }

        // Redirect back to products page to reload on success
        String redirectUrl = request.getContextPath() + "/products";
        String search = request.getParameter("search");
        String page = request.getParameter("page");
        if (search != null && !search.isEmpty()) redirectUrl += "?search=" + java.net.URLEncoder.encode(search, "UTF-8");
        if (page != null && !page.isEmpty()) redirectUrl += (search != null ? "&" : "?") + "page=" + page;
        response.sendRedirect(redirectUrl);
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
        return "Servlet for updating product details";
    }
}