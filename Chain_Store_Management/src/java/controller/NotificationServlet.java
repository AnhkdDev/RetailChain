package controller;

import DAO.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Notification;
import model.User;

@WebServlet(name = "NotificationServlet", urlPatterns = {"/notifications"})
public class NotificationServlet extends HttpServlet {

    private static final int PAGE_SIZE = 4;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("NotificationServlet: doGet called for /notifications");
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
            System.out.println("NotificationServlet: UserID: " + userID);

            // Lấy tham số lọc và trang
            String search = request.getParameter("search");
            String status = request.getParameter("status");
            String pageStr = request.getParameter("page");
            int page = 1;
            try {
                if (pageStr != null) {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) page = 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("NotificationServlet: Invalid page format: " + pageStr);
            }
            System.out.println("NotificationServlet: Search: " + search + ", Status: " + status + ", Page: " + page);

            // Lấy danh sách thông báo
            notifications = notificationDAO.getFilteredNotifications(search, status, page, PAGE_SIZE, userID);
            // Đếm tổng số thông báo
            int totalNotifications = notificationDAO.countFilteredNotifications(search, status, userID);
            int totalPages = (int) Math.ceil((double) totalNotifications / PAGE_SIZE);

            System.out.println("NotificationServlet: Số thông báo lấy được: " + (notifications != null ? notifications.size() : "null"));
            System.out.println("NotificationServlet: Tổng số thông báo: " + totalNotifications + ", Tổng số trang: " + totalPages);

            // Set attributes
            request.setAttribute("notifications", notifications != null ? notifications : new ArrayList<>());
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalNotifications", totalNotifications);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("totalPages", totalPages);
        } catch (Exception e) {
            System.err.println("NotificationServlet: Lỗi: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "Lỗi khi lấy danh sách thông báo: " + e.getMessage();
        } finally {
            notificationDAO.closeConnection();
            System.out.println("NotificationServlet: Database connection closed");
        }

        if (errorMessage != null) {
            request.setAttribute("message", errorMessage);
            request.setAttribute("messageType", "danger");
        }
        System.out.println("NotificationServlet: Set attributes");
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
        System.out.println("NotificationServlet: Forwarded to notifications.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("NotificationServlet: doPost called");

        NotificationDAO notificationDAO = new NotificationDAO();
        try {
            // Lấy UserID từ session
            Integer userID = null;
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                userID = user.getUserID();
            }
            System.out.println("NotificationServlet: UserID: " + userID);

            // Lấy tham số lọc
            String search = request.getParameter("search");
            String status = request.getParameter("status");

            String action = request.getParameter("action");
            System.out.println("NotificationServlet: Action received: " + action);

            if ("delete".equals(action)) {
                String notificationIDStr = request.getParameter("notificationID");
                System.out.println("NotificationServlet: Delete request for notificationID: " + notificationIDStr);
                try {
                    int notificationID = Integer.parseInt(notificationIDStr);
                    boolean deleted = notificationDAO.deleteNotification(notificationID);
                    if (deleted) {
                        request.setAttribute("message", "Thông báo đã được xóa thành công.");
                        request.setAttribute("messageType", "success");
                    } else {
                        request.setAttribute("message", "Không tìm thấy thông báo để xóa.");
                        request.setAttribute("messageType", "danger");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("NotificationServlet: Invalid notificationID format: " + notificationIDStr);
                    request.setAttribute("message", "ID thông báo không hợp lệ.");
                    request.setAttribute("messageType", "danger");
                }
            } else if ("markRead".equals(action)) {
                String notificationIDStr = request.getParameter("notificationID");
                System.out.println("NotificationServlet: Mark as read request for notificationID: " + notificationIDStr);
                try {
                    int notificationID = Integer.parseInt(notificationIDStr);
                    boolean marked = notificationDAO.markNotificationAsRead(notificationID);
                    if (marked) {
                        request.setAttribute("message", "Thông báo đã được đánh dấu là đã đọc.");
                        request.setAttribute("messageType", "success");
                    } else {
                        request.setAttribute("message", "Không tìm thấy thông báo để đánh dấu.");
                        request.setAttribute("messageType", "danger");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("NotificationServlet: Invalid notificationID format: " + notificationIDStr);
                    request.setAttribute("message", "ID thông báo không hợp lệ.");
                    request.setAttribute("messageType", "danger");
                }
            } else {
                System.out.println("NotificationServlet: Unknown action: " + action);
                request.setAttribute("message", "Hành động không được hỗ trợ.");
                request.setAttribute("messageType", "danger");
            }

            // Lấy lại danh sách thông báo với bộ lọc hiện tại
            List<Notification> notifications = notificationDAO.getFilteredNotifications(search, status, 1, PAGE_SIZE, userID);
            System.out.println("NotificationServlet: Số thông báo lấy được sau hành động: " + (notifications != null ? notifications.size() : "null"));
            request.setAttribute("notifications", notifications != null ? notifications : new ArrayList<>());

            // Set các attributes khác
            int totalNotifications = notificationDAO.countFilteredNotifications(search, status, userID);
            int totalPages = (int) Math.ceil((double) totalNotifications / PAGE_SIZE);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalNotifications", totalNotifications);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("totalPages", totalPages);
        } catch (Exception e) {
            System.err.println("NotificationServlet: Lỗi trong doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Lỗi khi xử lý hành động: " + e.getMessage());
            request.setAttribute("messageType", "danger");
        } finally {
            notificationDAO.closeConnection();
            System.out.println("NotificationServlet: Database connection closed");
        }

        System.out.println("NotificationServlet: Forwarding to notifications.jsp after POST");
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to display and manage notifications in the store dashboard.";
    }
}