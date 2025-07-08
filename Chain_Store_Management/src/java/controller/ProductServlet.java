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
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());
    private static final int PAGE_SIZE = 5;
    private static final int MAX_SEARCH_LENGTH = 100;
    private static final int MAX_REPEATED_CHAR = 4; // Ngưỡng ký tự lặp lại liên tiếp
    private static final Pattern VALID_CHAR_PATTERN = Pattern.compile("^[\\p{L}\\p{N}\\s]*$"); // Chỉ chữ cái, số, khoảng trắng
    private static final Pattern REPEATED_CHAR_PATTERN = Pattern.compile("(.)\\1{" + MAX_REPEATED_CHAR + ",}"); // Ký tự lặp lại từ 5 lần trở lên

    private List<String> cleanSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            LOGGER.info("Search keyword is null or empty");
            return null;
        }
        try {
            // Giải mã URL để xử lý các ký tự như %E1%BA%AFn
            keyword = URLDecoder.decode(keyword, "UTF-8");
            // Loại bỏ khoảng trắng dư thừa
            keyword = keyword.trim().replaceAll("\\s+", " ");
            // Kiểm tra độ dài chuỗi
            if (keyword.length() > MAX_SEARCH_LENGTH) {
                LOGGER.info("Search keyword too long: " + keyword);
                return null;
            }
            // Tách chuỗi thành các từ
            String[] words = keyword.split(" ");
            List<String> validWords = new ArrayList<>();
            for (String word : words) {
                // Kiểm tra độ dài từng từ
                if (word.length() <= 1) {
                    LOGGER.info("Search word too short: " + word);
                    continue; // Bỏ qua từ có 1 ký tự
                }
                // Kiểm tra ký tự lặp lại liên tiếp
                Matcher repeatedMatcher = REPEATED_CHAR_PATTERN.matcher(word);
                if (repeatedMatcher.find()) {
                    LOGGER.info("Search word contains repeated characters: " + word);
                    continue; // Bỏ qua từ có ký tự lặp lại
                }
                // Kiểm tra ký tự hợp lệ
                if (!VALID_CHAR_PATTERN.matcher(word).matches()) {
                    LOGGER.info("Search word contains invalid characters: " + word);
                    continue; // Bỏ qua từ có ký tự đặc biệt
                }
                // Loại bỏ các ký tự nguy hiểm (dự phòng)
                word = word.replaceAll("[<>\"&'%]", "");
                if (!word.isEmpty()) {
                    validWords.add(word);
                }
            }
            // Kiểm tra xem danh sách từ khóa có rỗng không
            if (validWords.isEmpty()) {
                LOGGER.info("No valid words after cleaning: " + keyword);
                return null;
            }
            LOGGER.info("Cleaned search keywords: " + validWords);
            return validWords;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error decoding search keyword: " + keyword, e);
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO(getServletContext());

            // Clear temporary images if clearTemp parameter is present
            String clearTemp = request.getParameter("clearTemp");
            if (clearTemp != null) {
                String sessionKey = "tempImageBytes" + (clearTemp.isEmpty() ? "" : "_" + clearTemp);
                request.getSession().removeAttribute(sessionKey);
                LOGGER.info("Cleared tempImageBytes for key: " + sessionKey);
            }

            // Process search parameters
            List<String> searchKeywords = cleanSearchKeyword(request.getParameter("search"));
            String message = request.getParameter("message");
            String messageType = request.getParameter("messageType");

            // Log search keywords for debugging
            LOGGER.info("Search keywords after cleaning: " + (searchKeywords != null ? searchKeywords : "null"));

            // Process category filter
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

            // Process status filter
            Boolean isActive = null;
            String statusFilter = request.getParameter("statusFilter");
            if (statusFilter != null && !statusFilter.isEmpty()) {
                if ("active".equals(statusFilter)) {
                    isActive = true;
                } else if ("inactive".equals(statusFilter)) {
                    isActive = false;
                }
            }

            // Process pagination
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

            // Fetch all products and total count
            List<Product> allProducts = productDAO.getProductsByPage(searchKeywords, categoryID, isActive);
            int totalProducts = productDAO.getTotalProductCount(searchKeywords, categoryID, isActive);
            int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

            // Perform pagination in servlet
            List<Product> products = new ArrayList<>();
            if (allProducts != null) {
                int start = (currentPage - 1) * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, allProducts.size());
                if (start < allProducts.size()) {
                    products = allProducts.subList(start, end);
                }
            }

            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
                int start = (currentPage - 1) * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, allProducts.size());
                if (start < allProducts.size()) {
                    products = allProducts.subList(start, end);
                }
            }

            // Fetch dropdown data
            List<Size> sizeSuggestions = productDAO.getSizes();
            List<Color> colorSuggestions = productDAO.getColors();
            List<Category> categories = productDAO.getCategories();

            // Set request attributes for JSP
            request.setAttribute("products", products != null ? products : new ArrayList<>());
            request.setAttribute("categories", categories);
            request.setAttribute("sizeSuggestions", sizeSuggestions);
            request.setAttribute("colorSuggestions", colorSuggestions);
            request.setAttribute("search", searchKeywords != null ? String.join(" ", searchKeywords) : "");
            request.setAttribute("categoryID", categoryID != null ? categoryID : "");
            request.setAttribute("statusFilter", statusFilter != null ? statusFilter : "");
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
        request.setCharacterEncoding("UTF-8");

        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO(getServletContext());
            String action = request.getParameter("action");

            if (action == null || action.isEmpty()) {
                LOGGER.warning("Missing action parameter");
                request.setAttribute("message", "Thiếu tham số action");
                request.setAttribute("messageType", "danger");
                if (!response.isCommitted()) {
                    request.getRequestDispatcher("/products.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/products?message=" + URLEncoder.encode("Thiếu tham số action", "UTF-8") + "&messageType=danger");
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
                        response.sendRedirect(request.getContextPath() + "/products?message=" + URLEncoder.encode("Thiếu tham số productId hoặc isActive", "UTF-8") + "&messageType=danger");
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
                            response.sendRedirect(request.getContextPath() + "/products?message=" + URLEncoder.encode("Mã sản phẩm không hợp lệ", "UTF-8") + "&messageType=danger");
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
                        response.sendRedirect(request.getContextPath() + "/products?message=" + URLEncoder.encode("Mã sản phẩm phải là số", "UTF-8") + "&messageType=danger");
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
}