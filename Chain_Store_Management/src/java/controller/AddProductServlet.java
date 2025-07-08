//    package controller;
//
//    import DAO.ProductDAO;
//    import jakarta.servlet.ServletException;
//    import jakarta.servlet.annotation.MultipartConfig;
//    import jakarta.servlet.annotation.WebServlet;
//    import jakarta.servlet.http.*;
//    import java.io.IOException;
//    import java.sql.SQLException;
//    import java.util.ArrayList;
//    import java.util.List;
//    import java.math.BigDecimal;
//    import java.util.logging.Level;
//    import java.util.logging.Logger;
//    import model.Category;
//    import model.Color;
//    import model.Size;
//
//    @WebServlet(name = "AddProductServlet", urlPatterns = {"/add-product"})
//    @MultipartConfig(
//        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
//        maxFileSize = 1024 * 1024 * 10, // 10MB
//        maxRequestSize = 1024 * 1024 * 50 // 50MB
//    )
//    public class AddProductServlet extends HttpServlet {
//
//        private static final Logger LOGGER = Logger.getLogger(AddProductServlet.class.getName());
//
//        @Override
//        protected void doGet(HttpServletRequest request, HttpServletResponse response)
//                throws ServletException, IOException {
//            response.setContentType("text/html;charset=UTF-8");
//            request.setCharacterEncoding("UTF-8");
//
//            ProductDAO productDAO = null;
//            try {
//                productDAO = new ProductDAO();
//                String action = request.getParameter("action");
//
//                if ("loadDropdowns".equals(action)) {
//                    LOGGER.info("Loading dropdown data for add product...");
//                    List<Size> sizes = productDAO.getSizes();
//                    List<Color> colors = productDAO.getColors();
//                    List<Category> categories = productDAO.getCategories();
//
//                    request.setAttribute("sizeSuggestions", sizes);
//                    request.setAttribute("colorSuggestions", colors);
//                    request.setAttribute("categories", categories);
//                    LOGGER.info("Dropdown data loaded: sizeSuggestions=" + (sizes != null ? sizes.size() : 0) +
//                            ", colorSuggestions=" + (colors != null ? colors.size() : 0) +
//                            ", categories=" + (categories != null ? categories.size() : 0));
//
//                    request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                    return;
//                }
//
//                LOGGER.info("No action, forwarding to /products.jsp");
//                if (!response.isCommitted()) {
//                    request.getRequestDispatcher("/products.jsp").forward(request, response);
//                } else {
//                    response.sendRedirect(request.getContextPath() + "/products");
//                }
//            } catch (SQLException e) {
//                LOGGER.log(Level.SEVERE, "Database error: {0}", e.getMessage());
//                request.setAttribute("message", "Lỗi cơ sở dữ liệu: " + e.getMessage());
//                request.setAttribute("messageType", "danger");
//                if (!response.isCommitted()) {
//                    request.getRequestDispatcher("/products.jsp").forward(request, response);
//                } else {
//                    response.sendRedirect(request.getContextPath() + "/products?message=Lỗi cơ sở dữ liệu&messageType=danger");
//                }
//            } catch (Exception e) {
//                LOGGER.log(Level.SEVERE, "Unexpected error: {0}", e.getMessage());
//                request.setAttribute("message", "Lỗi không xác định: " + e.getMessage());
//                request.setAttribute("messageType", "danger");
//                if (!response.isCommitted()) {
//                    request.getRequestDispatcher("/products.jsp").forward(request, response);
//                } else {
//                    response.sendRedirect(request.getContextPath() + "/products?message=Lỗi không xác định&messageType=danger");
//                }
//            } finally {
//                if (productDAO != null) productDAO.closeConnection();
//            }
//        }
//
//        @Override
//        protected void doPost(HttpServletRequest request, HttpServletResponse response)
//                throws ServletException, IOException {
//            response.setContentType("text/html;charset=UTF-8");
//            request.setCharacterEncoding("UTF-8");
//
//            ProductDAO productDAO = null;
//            try {
//                productDAO = new ProductDAO();
//                String action = request.getParameter("action");
//
//                if (action == null || action.isEmpty()) {
//                    LOGGER.warning("Missing action parameter");
//                    request.setAttribute("message", "Thiếu tham số action");
//                    request.setAttribute("messageType", "danger");
//                    if (!response.isCommitted()) {
//                        request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                    } else {
//                        response.sendRedirect(request.getContextPath() + "/add-product?action=loadDropdowns&message=Thiếu tham số action&messageType=danger");
//                    }
//                    return;
//                }
//
//                if ("add".equals(action)) {
//                    String productName = request.getParameter("productName");
//                    String description = request.getParameter("description");
//                    String sellingPriceParam = request.getParameter("sellingPrice");
//                    String sizeIDParam = request.getParameter("sizeID");
//                    String colorIDParam = request.getParameter("colorID");
//                    String unit = request.getParameter("unit");
//                    String stockQuantityParam = request.getParameter("stockQuantity");
//                    String categoryIDParam = request.getParameter("categoryID");
//                    String barcode = request.getParameter("barcode");
//                    String productCode = request.getParameter("productCode");
//
//                    LOGGER.info("Received form data: productName=" + productName + ", sizeID=" + sizeIDParam + ", colorID=" + colorIDParam +
//                            ", unit=" + unit + ", stockQuantity=" + stockQuantityParam + ", categoryID=" + categoryIDParam);
//
//                    if (productName == null || description == null || sellingPriceParam == null || unit == null ||
//                        stockQuantityParam == null || categoryIDParam == null) {
//                        LOGGER.warning("Missing required fields");
//                        request.setAttribute("message", "Thiếu thông tin sản phẩm");
//                        request.setAttribute("messageType", "danger");
//                        if (!response.isCommitted()) {
//                            request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                        } else {
//                            response.sendRedirect(request.getContextPath() + "/add-product?action=loadDropdowns&message=Thiếu thông tin sản phẩm&messageType=danger");
//                        }
//                        return;
//                    }
//
//                    BigDecimal sellingPrice;
//                    int stockQuantity;
//                    int categoryID;
//                    Integer sizeID = null;
//                    Integer colorID = null;
//                    try {
//                        sellingPrice = new BigDecimal(sellingPriceParam);
//                        stockQuantity = Integer.parseInt(stockQuantityParam);
//                        categoryID = Integer.parseInt(categoryIDParam);
//                        if (sizeIDParam != null && !sizeIDParam.isEmpty()) sizeID = Integer.parseInt(sizeIDParam);
//                        if (colorIDParam != null && !colorIDParam.isEmpty()) colorID = Integer.parseInt(colorIDParam);
//                    } catch (NumberFormatException e) {
//                        LOGGER.warning("Invalid numeric data: sellingPrice=" + sellingPriceParam + ", stockQuantity=" + stockQuantityParam +
//                                ", categoryID=" + categoryIDParam + ", sizeID=" + sizeIDParam + ", colorID=" + colorIDParam);
//                        request.setAttribute("message", "Dữ liệu không hợp lệ");
//                        request.setAttribute("messageType", "danger");
//                        if (!response.isCommitted()) {
//                            request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                        } else {
//                            response.sendRedirect(request.getContextPath() + "/add-product?action=loadDropdowns&message=Dữ liệu không hợp lệ&messageType=danger");
//                        }
//                        return;
//                    }
//
//                    List<Part> images = new ArrayList<>();
//                    for (int i = 1; i <= 5; i++) {
//                        Part filePart = request.getPart("image" + i);
//                        if (filePart != null && filePart.getSize() > 0) images.add(filePart);
//                    }
//
//                    try {
//                        int newProductId = productDAO.addProduct(productName, description, sellingPrice, categoryID, sizeID, colorID, unit, stockQuantity, barcode, productCode, images);
//                        if (newProductId > 0) {
//                            LOGGER.info("Product added successfully with ID: " + newProductId);
//                            response.sendRedirect(request.getContextPath() + "/products?message=Thêm sản phẩm thành công&messageType=success");
//                            return; // Đảm bảo không gọi forward sau redirect
//                        } else {
//                            LOGGER.warning("Failed to add product");
//                            request.setAttribute("message", "Thêm sản phẩm thất bại");
//                            request.setAttribute("messageType", "danger");
//                            if (!response.isCommitted()) {
//                                request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                            } else {
//                                response.sendRedirect(request.getContextPath() + "/add-product?action=loadDropdowns&message=Thêm sản phẩm thất bại&messageType=danger");
//                            }
//                        }
//                    } catch (IOException e) {
//                        LOGGER.log(Level.SEVERE, "File upload error: {0}", e.getMessage());
//                        request.setAttribute("message", "Lỗi khi upload file: " + e.getMessage());
//                        request.setAttribute("messageType", "danger");
//                        if (!response.isCommitted()) {
//                            request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                        } else {
//                            response.sendRedirect(request.getContextPath() + "/add-product?action=loadDropdowns&message=Lỗi khi upload file&messageType=danger");
//                        }
//                    }
//                } else {
//                    LOGGER.warning("Invalid action: " + action);
//                    request.setAttribute("message", "Hành động không hợp lệ");
//                    request.setAttribute("messageType", "danger");
//                    if (!response.isCommitted()) {
//                        request.getRequestDispatcher("/products.jsp").forward(request, response);
//                    } else {
//                        response.sendRedirect(request.getContextPath() + "/products?message=Hành động không hợp lệ&messageType=danger");
//                    }
//                }
//            } catch (SQLException e) {
//                LOGGER.log(Level.SEVERE, "Database error: {0}", e.getMessage());
//                request.setAttribute("message", "Lỗi cơ sở dữ liệu: " + e.getMessage());
//                request.setAttribute("messageType", "danger");
//                if (!response.isCommitted()) {
//                    request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                } else {
//                    response.sendRedirect(request.getContextPath() + "/add-product?action=loadDropdowns&message=Lỗi cơ sở dữ liệu&messageType=danger");
//                }
//            } catch (Exception e) {
//                LOGGER.log(Level.SEVERE, "Unexpected error: {0}", e.getMessage());
//                request.setAttribute("message", "Lỗi không xác định: " + e.getMessage());
//                request.setAttribute("messageType", "danger");
//                if (!response.isCommitted()) {
//                    request.getRequestDispatcher("/addProduct.jsp").forward(request, response);
//                } else {
//                    response.sendRedirect(request.getContextPath() + "/add-product?action=loadDropdowns&message=Lỗi không xác định&messageType=danger");
//                }
//            } finally {
//                if (productDAO != null) productDAO.closeConnection();
//            }
//        }
//    }