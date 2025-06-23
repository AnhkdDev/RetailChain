package controller;

import DAO.ProductDAO;
import model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Category;
import model.Color;
import model.Size;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());
    private static final int PAGE_SIZE = 5;
    private static final int MAX_SEARCH_LENGTH = 100;

    private String cleanSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return null;
        keyword = keyword.trim();
        if (keyword.length() > MAX_SEARCH_LENGTH) return null;
        return keyword.replaceAll("[<>\"&'%]", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO();

            // Xóa ảnh tạm nếu có tham số clearTemp
            String clearTemp = request.getParameter("clearTemp");
            if (clearTemp != null) {
                int productId = Integer.parseInt(clearTemp);
                request.getSession().removeAttribute("tempImageBytes_" + productId);
                LOGGER.info("Cleared tempImageBytes for productId: " + productId);
            }

            String search = cleanSearchKeyword(request.getParameter("search"));
            String message = request.getParameter("message");
            String messageType = request.getParameter("messageType");

            Integer categoryID = null;
            String categoryIDParam = request.getParameter("categoryID");
            if (categoryIDParam != null && !categoryIDParam.isEmpty()) {
                try {
                    categoryID = Integer.parseInt(categoryIDParam);
                    if (categoryID <= 0) categoryID = null;
                } catch (NumberFormatException e) {
                    categoryID = null;
                }
            }

            Boolean isActive = null;
            String isActiveParam = request.getParameter("isActive");
            if (isActiveParam != null && !isActiveParam.isEmpty()) {
                isActive = Boolean.parseBoolean(isActiveParam);
            }

            int currentPage = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            List<Product> products = productDAO.getProductsByPage(search, categoryID, isActive, currentPage, PAGE_SIZE);
            int totalProducts = productDAO.getTotalProductCount(search, categoryID, isActive);
            int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
                products = productDAO.getProductsByPage(search, categoryID, isActive, currentPage, PAGE_SIZE);
            }

            List<Size> sizeSuggestions = productDAO.getSizes();
            List<Color> colorSuggestions = productDAO.getColors();
            List<Category> categories = productDAO.getCategories();

            request.setAttribute("products", products != null ? products : new ArrayList<>());
            request.setAttribute("categories", categories);
            request.setAttribute("sizeSuggestions", sizeSuggestions);
            request.setAttribute("colorSuggestions", colorSuggestions);
            request.setAttribute("search", search != null ? search : "");
            request.setAttribute("categoryID", categoryID != null ? categoryID : "");
            request.setAttribute("isActive", isActive != null ? isActive : "");
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("baseUrl", "products");

            if (message != null && messageType != null) {
                request.setAttribute("message", message);
                request.setAttribute("messageType", messageType);
            }

            if (!response.isCommitted()) {
                request.getRequestDispatcher("/products.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: {0}", e.getMessage());
            request.setAttribute("message", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            if (!response.isCommitted()) {
                request.getRequestDispatcher("/products.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products?message=Lỗi cơ sở dữ liệu&messageType=danger");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: {0}", e.getMessage());
            request.setAttribute("message", "Lỗi không xác định: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            if (!response.isCommitted()) {
                request.getRequestDispatcher("/products.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products?message=Lỗi không xác định&messageType=danger");
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
            productDAO = new ProductDAO();
            String action = request.getParameter("action");

            if (action == null || action.isEmpty()) {
                LOGGER.warning("Missing action parameter");
                request.setAttribute("message", "Thiếu tham số action");
                request.setAttribute("messageType", "danger");
                if (!response.isCommitted()) {
                    request.getRequestDispatcher("/products.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/products?message=Thiếu tham số action&messageType=danger");
                }
                return;
            }

            if ("toggle".equals(action)) {
                String productIdParam = request.getParameter("productId");
                String isActiveParam = request.getParameter("isActive");

                if (productIdParam == null || isActiveParam == null) {
                    LOGGER.warning("Missing productId or isActive parameter");
                    request.setAttribute("message", "Thiếu tham số productId hoặc isActive");
                    request.setAttribute("messageType", "danger");
                    if (!response.isCommitted()) {
                        request.getRequestDispatcher("/products.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/products?message=Thiếu tham số productId hoặc isActive&messageType=danger");
                    }
                    return;
                }

                int productId;
                try {
                    productId = Integer.parseInt(productIdParam);
                    if (productId <= 0) {
                        LOGGER.warning("Invalid productId: " + productIdParam);
                        request.setAttribute("message", "Mã sản phẩm không hợp lệ");
                        request.setAttribute("messageType", "danger");
                        if (!response.isCommitted()) {
                            request.getRequestDispatcher("/products.jsp").forward(request, response);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/products?message=Mã sản phẩm không hợp lệ&messageType=danger");
                        }
                        return;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid productId format: " + productIdParam);
                    request.setAttribute("message", "Mã sản phẩm phải là số");
                    request.setAttribute("messageType", "danger");
                    if (!response.isCommitted()) {
                        request.getRequestDispatcher("/products.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/products?message=Mã sản phẩm phải là số&messageType=danger");
                    }
                    return;
                }

                boolean isActive = Boolean.parseBoolean(isActiveParam);
                LOGGER.info("Toggling product status: productId=" + productId + ", isActive=" + isActive);
                boolean success = productDAO.toggleProductStatus(productId, isActive);

                if (success) {
                    request.setAttribute("message", "Cập nhật trạng thái thành công");
                    request.setAttribute("messageType", "success");
                } else {
                    LOGGER.warning("Failed to toggle product status: productId=" + productId);
                    request.setAttribute("message", "Không tìm thấy sản phẩm hoặc cập nhật thất bại");
                    request.setAttribute("messageType", "danger");
                }
                if (!response.isCommitted()) {
                    request.getRequestDispatcher("/products.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/products");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: {0}", e.getMessage());
            request.setAttribute("message", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            if (!response.isCommitted()) {
                request.getRequestDispatcher("/products.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products?message=Lỗi cơ sở dữ liệu&messageType=danger");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: {0}", e.getMessage());
            request.setAttribute("message", "Lỗi không xác định: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            if (!response.isCommitted()) {
                request.getRequestDispatcher("/products.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/products?message=Lỗi không xác định&messageType=danger");
            }
        } finally {
            if (productDAO != null) productDAO.closeConnection();
        }
    }
}