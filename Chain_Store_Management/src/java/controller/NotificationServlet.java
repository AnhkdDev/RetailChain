package controller;

import DAO.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Notification;
import model.User;

@WebServlet(name = "NotificationServlet", urlPatterns = {"/notifications"})
public class NotificationServlet extends HttpServlet {

    private static final int PAGE_SIZE = 4;
    private static final Logger LOGGER = Logger.getLogger(NotificationServlet.class.getName());

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

        // Loại bỏ chuỗi ký tự lặp (như "ccccccccc")
        if (word.matches("^(.)\\1+$")) {
            return false;
        }

        // Chỉ cho phép chữ cái, số, và ký tự cơ bản
        return word.matches("[a-z0-9]+");
    }

    // Normalize Vietnamese text
    private String normalizeVietnamese(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        LOGGER.log(Level.INFO, "doGet called for /notifications");
        NotificationDAO notificationDAO = new NotificationDAO();
        List<Notification> notifications = null;
        String errorMessage = null;

        try {
            // Lấy UserID từ session
            Integer userID = null;
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                userID = user.getUserID();
            }
            LOGGER.log(Level.INFO, "UserID: {0}", userID);

            // Lấy tham số lọc và trang
            String search = request.getParameter("search");
            String cleanedSearch = null;
            if (search != null) {
                if (search.length() > 100) {
                    throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
                }
                cleanedSearch = cleanSearchKeyword(search);
            }
            String status = request.getParameter("status");
            String pageStr = request.getParameter("page");
            int page = 1;
            try {
                if (pageStr != null) {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) page = 1;
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid page format: {0}", pageStr);
                page = 1;
            }
            LOGGER.log(Level.INFO, "Search: {0}, Cleaned Search: {1}, Status: {2}, Page: {3}",
                    new Object[]{search, cleanedSearch, status, page});

            // Lấy danh sách thông báo
            notifications = notificationDAO.getFilteredNotifications(cleanedSearch, status, page, PAGE_SIZE, userID);
            // Đếm tổng số thông báo
            int totalNotifications = notificationDAO.countFilteredNotifications(cleanedSearch, status, userID);
            int totalPages = (int) Math.ceil((double) totalNotifications / PAGE_SIZE);

            LOGGER.log(Level.INFO, "Số thông báo lấy được: {0}, Tổng số thông báo: {1}, Tổng số trang: {2}",
                    new Object[]{notifications.size(), totalNotifications, totalPages});

            // Set attributes
            request.setAttribute("notifications", notifications != null ? notifications : new ArrayList<>());
            request.setAttribute("search", request.getParameter("search")); // Preserve original input
            request.setAttribute("status", status);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalNotifications", totalNotifications);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("totalPages", totalPages);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid input: {0}", e.getMessage());
            errorMessage = e.getMessage();
            request.setAttribute("notifications", new ArrayList<>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi: {0}", e.getMessage());
            errorMessage = "Lỗi khi lấy danh sách thông báo: " + e.getMessage();
        } finally {
            notificationDAO.closeConnection();
            LOGGER.log(Level.INFO, "Database connection closed");
        }

        if (errorMessage != null) {
            request.setAttribute("message", errorMessage);
            request.setAttribute("messageType", "danger");
        }
        LOGGER.log(Level.INFO, "Forwarding to notifications.jsp");
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        LOGGER.log(Level.INFO, "doPost called");

        NotificationDAO notificationDAO = new NotificationDAO();
        try {
            // Lấy UserID từ session
            Integer userID = null;
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                userID = user.getUserID();
            }
            LOGGER.log(Level.INFO, "UserID: {0}", userID);

            // Lấy tham số lọc
            String search = request.getParameter("search");
            String cleanedSearch = null;
            if (search != null) {
                if (search.length() > 100) {
                    throw new IllegalArgumentException("Search keyword exceeds 100 characters.");
                }
                cleanedSearch = cleanSearchKeyword(search);
            }
            String status = request.getParameter("status");

            String action = request.getParameter("action");
            LOGGER.log(Level.INFO, "Action received: {0}", action);

            if ("delete".equals(action)) {
                String notificationIDStr = request.getParameter("notificationID");
                LOGGER.log(Level.INFO, "Delete request for notificationID: {0}", notificationIDStr);
                if (notificationIDStr == null || notificationIDStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("Notification ID is required.");
                }
                int notificationID = Integer.parseInt(notificationIDStr);
                boolean deleted = notificationDAO.deleteNotification(notificationID);
                if (deleted) {
                    request.setAttribute("message", "Thông báo đã được xóa thành công.");
                    request.setAttribute("messageType", "success");
                } else {
                    request.setAttribute("message", "Không tìm thấy thông báo để xóa.");
                    request.setAttribute("messageType", "danger");
                }
            } else if ("markRead".equals(action)) {
                String notificationIDStr = request.getParameter("notificationID");
                LOGGER.log(Level.INFO, "Mark as read request for notificationID: {0}", notificationIDStr);
                if (notificationIDStr == null || notificationIDStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("Notification ID is required.");
                }
                int notificationID = Integer.parseInt(notificationIDStr);
                boolean marked = notificationDAO.markNotificationAsRead(notificationID);
                if (marked) {
                    request.setAttribute("message", "Thông báo đã được đánh dấu là đã đọc.");
                    request.setAttribute("messageType", "success");
                } else {
                    request.setAttribute("message", "Không tìm thấy thông báo để đánh dấu.");
                    request.setAttribute("messageType", "danger");
                }
            } else {
                LOGGER.log(Level.WARNING, "Unknown action: {0}", action);
                throw new IllegalArgumentException("Hành động không được hỗ trợ.");
            }

            // Lấy lại danh sách thông báo
            List<Notification> notifications = notificationDAO.getFilteredNotifications(cleanedSearch, status, 1, PAGE_SIZE, userID);
            int totalNotifications = notificationDAO.countFilteredNotifications(cleanedSearch, status, userID);
            int totalPages = (int) Math.ceil((double) totalNotifications / PAGE_SIZE);

            // Set attributes
            request.setAttribute("notifications", notifications != null ? notifications : new ArrayList<>());
            request.setAttribute("search", request.getParameter("search"));
            request.setAttribute("status", status);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalNotifications", totalNotifications);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("totalPages", totalPages);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid input: {0}", e.getMessage());
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("notifications", new ArrayList<>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi trong doPost: {0}", e.getMessage());
            request.setAttribute("message", "Lỗi khi xử lý hành động: " + e.getMessage());
            request.setAttribute("messageType", "danger");
        } finally {
            notificationDAO.closeConnection();
            LOGGER.log(Level.INFO, "Database connection closed");
        }

        LOGGER.log(Level.INFO, "Forwarding to notifications.jsp after POST");
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to display and manage notifications in the store dashboard.";
    }
}