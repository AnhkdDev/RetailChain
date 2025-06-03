package controller;

import DAO.ProductDAO;
import model.Product;
import model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;
    private static final int MAX_SEARCH_LENGTH = 100;

    // Hàm tách và lọc từ khóa
    private String cleanSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        // Chuẩn hóa: loại bỏ dấu, chuyển thành chữ thường
        String normalizedKeyword = normalizeVietnamese(keyword);
        // Tách từ dựa trên khoảng trắng
        String[] words = normalizedKeyword.trim().split("\\s+");
        List<String> cleanedWords = new ArrayList<>();

        for (String word : words) {
            // Kiểm tra từ hợp lệ
            if (isValidWord(word)) {
                cleanedWords.add(word);
            }
        }

        // Gộp các từ hợp lệ thành một chuỗi
        return cleanedWords.isEmpty() ? null : String.join(" ", cleanedWords);
    }

    // Kiểm tra từ hợp lệ
    private boolean isValidWord(String word) {
        if (word == null || word.length() < 2) {
            return false;
        }

        // tach ky tu 
        if (word.matches("^(.)\\1+$")) {
            return false;
        }

     
        return word.matches("[a-z0-9]+");
    }

    // Normalize Vietnamese text
    private String normalizeVietnamese(String text) {
        if (text == null) return null;
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8"); // Hỗ trợ nhập liệu tiếng Việt

        ProductDAO productDAO = null;
        List<Product> products = null;
        List<Category> categories = null;
        List<String> errors = new ArrayList<>();
        String search = null;
        String cleanedSearch = null;
        Integer categoryID = null;
        Boolean isActive = null;
        int currentPage = 1;
        int totalProducts = 0;
        int totalPages = 1;

        try {
            productDAO = new ProductDAO();

            // Validation cho tham số
            // Search
            String searchParam = request.getParameter("search");
            if (searchParam != null) {
                search = searchParam.trim();
                if (search.isEmpty()) {
                    search = null;
                } else if (search.length() > MAX_SEARCH_LENGTH) {
                    errors.add("Từ khóa tìm kiếm phải dưới " + MAX_SEARCH_LENGTH + " ký tự.");
                    search = null;
                } else {
                    search = search.replaceAll("[<>\"&'%]", ""); // Loại bỏ ký tự nguy hiểm
                    if (search.isEmpty()) {
                        search = null;
                    } else {
                        cleanedSearch = cleanSearchKeyword(search);
                        if (cleanedSearch == null || cleanedSearch.isEmpty()) {
                            cleanedSearch = null;
                            search = null; // Không có từ khóa hợp lệ
                        }
                    }
                }
            }

            // CategoryID
            String categoryIDParam = request.getParameter("categoryID");
            if (categoryIDParam != null && !categoryIDParam.isEmpty()) {
                try {
                    categoryID = Integer.parseInt(categoryIDParam);
                    if (categoryID <= 0) {
                        errors.add("Danh mục không hợp lệ.");
                        categoryID = null;
                    }
                } catch (NumberFormatException e) {
                    errors.add("Định dạng ID danh mục không hợp lệ.");
                    categoryID = null;
                }
            }

            // isActive
            String isActiveParam = request.getParameter("isActive");
            if (isActiveParam != null && !isActiveParam.isEmpty()) {
                if ("true".equals(isActiveParam)) {
                    isActive = true;
                } else if ("false".equals(isActiveParam)) {
                    isActive = false;
                } else {
                    errors.add("Trạng thái không hợp lệ.");
                }
            }

            // Page
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    errors.add("Số trang không hợp lệ.");
                    currentPage = 1;
                }
            }

            // Lấy danh mục để lọc
            categories = productDAO.getCategories();

            // Lấy danh sách sản phẩm
            products = productDAO.getProductsByPage(cleanedSearch, categoryID, isActive, currentPage, PAGE_SIZE);
            totalProducts = productDAO.getTotalProductCount(cleanedSearch, categoryID, isActive);
            totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

            // Điều chỉnh nếu trang vượt quá giới hạn
            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
                products = productDAO.getProductsByPage(cleanedSearch, categoryID, isActive, currentPage, PAGE_SIZE);
            }

            // Đặt attributes cho JSP
            request.setAttribute("products", products != null ? products : new ArrayList<>());
            request.setAttribute("categories", categories);
            request.setAttribute("search", searchParam); // Giữ nguyên input gốc
            request.setAttribute("categoryID", categoryID);
            request.setAttribute("isActive", isActive);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("message", "Vui lòng sửa các lỗi sau.");
                request.setAttribute("messageType", "danger");
            }
        } catch (Exception e) {
            System.err.println("ProductServlet: Lỗi không mong muốn: " + e.getMessage());
            errors.add("Đã xảy ra lỗi: " + e.getMessage());
            request.setAttribute("message", "Đã xảy ra lỗi. Vui lòng thử lại.");
            request.setAttribute("messageType", "danger");
            request.setAttribute("errors", errors);
        } finally {
            if (productDAO != null) {
                productDAO.closeConnection();
            }
        }

        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý danh sách sản phẩm.";
    }
}