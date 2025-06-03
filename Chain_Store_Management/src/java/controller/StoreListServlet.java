package controller;

import DAO.StoreDAO;
import DAO.StoreDAO.StorePage;
import model.Store;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "StoreListServlet", urlPatterns = {"/stores"})
public class StoreListServlet extends HttpServlet {

    private StoreDAO storeDAO;
    private static final int PAGE_SIZE = 4;
    private static final int MAX_SEARCH_LENGTH = 100;
    private static final String PHONE_PATTERN = "^[0-9\\s\\(\\)-]+$";

    @Override
    public void init() throws ServletException {
        storeDAO = new StoreDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String reset = request.getParameter("reset");
        int page = 1;
        List<String> errors = new ArrayList<>();

        // Validation cho search
        if (search != null) {
            search = search.trim();
            if (search.isEmpty()) {
                search = null;
            } else if (search.length() > MAX_SEARCH_LENGTH) {
                errors.add("Search term must not exceed " + MAX_SEARCH_LENGTH + " characters.");
                search = null;
            } else {
                // Loại bỏ ký tự không hợp lệ
                search = search.replaceAll("[^a-zA-Z0-9\\s]", "");
                if (search.isEmpty()) {
                    search = null;
                }
            }
        }

        // Validation cho status
        if (status != null && !status.equals("active") && !status.equals("inactive")) {
            status = null;
        }

        // Xử lý reset
        if (reset != null && reset.equals("true")) {
            search = null;
            status = null;
        }

        // Validation và xử lý toggle
        if (action != null && action.equals("toggle")) {
            String storeID = request.getParameter("storeID");
            if (storeID == null || storeID.trim().isEmpty()) {
                errors.add("Store ID is required for toggling status.");
            } else {
                try {
                    Integer.parseInt(storeID);
                    boolean toggled = storeDAO.toggleStoreStatus(storeID);
                    String redirectUrl = "stores?page=" + (request.getParameter("page") != null ? request.getParameter("page") : "1");
                    if (search != null) redirectUrl += "&search=" + java.net.URLEncoder.encode(search, "UTF-8");
                    if (status != null) redirectUrl += "&status=" + status;
                    request.setAttribute("message", toggled ? "Store status updated successfully." : "Failed to update store status.");
                    request.setAttribute("messageType", toggled ? "success" : "danger");
                    response.sendRedirect(redirectUrl);
                    return;
                } catch (NumberFormatException e) {
                    errors.add("Invalid Store ID format.");
                }
            }
        }

        // Validation và xử lý save
        if (action != null && action.equals("save")) {
            Store store = new Store();
            String storeID = request.getParameter("storeID");
            String storeName = request.getParameter("storeName");
            String address = request.getParameter("address");
            String phoneNumber = request.getParameter("phoneNumber");
            String isActiveStr = request.getParameter("isActive");

            // Validation
            if (storeID != null && !storeID.trim().isEmpty()) {
                try {
                    Integer.parseInt(storeID);
                    store.setStoreID(storeID);
                } catch (NumberFormatException e) {
                    errors.add("Invalid Store ID format.");
                }
            }
            if (storeName == null || storeName.trim().isEmpty()) {
                errors.add("Store name is required.");
            } else if (storeName.length() > 100) {
                errors.add("Store name must not exceed 100 characters.");
            }
            if (address == null || address.trim().isEmpty()) {
                errors.add("Address is required.");
            } else if (address.length() > 200) {
                errors.add("Address must not exceed 200 characters.");
            }
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                errors.add("Phone number is required.");
            } else if (phoneNumber.length() > 20 || !phoneNumber.matches(PHONE_PATTERN)) {
                errors.add("Invalid phone number format.");
            }
            boolean isActive = "true".equalsIgnoreCase(isActiveStr);

            if (errors.isEmpty()) {
                store.setStoreName(storeName);
                store.setAddress(address);
                store.setPhoneNumber(phoneNumber);
                store.setEmail(""); // Email không có trong form
                store.setActive(isActive);
                boolean saved = storeDAO.saveStore(store);
                String redirectUrl = "stores?page=1";
                if (search != null) redirectUrl += "&search=" + java.net.URLEncoder.encode(search, "UTF-8");
                if (status != null) redirectUrl += "&status=" + status;
                request.setAttribute("message", saved ? "Store saved successfully." : "Failed to save store.");
                request.setAttribute("messageType", saved ? "success" : "danger");
                response.sendRedirect(redirectUrl);
                return;
            } else {
                // Lưu giá trị đã nhập để hiển thị lại trong form
                request.setAttribute("store", new Store(storeName, address, phoneNumber, "", isActive));
                request.setAttribute("storeID", storeID);
            }
        }

        // Validation cho page
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            errors.add("Invalid page number.");
            page = 1;
        }

        // Lấy danh sách cửa hàng
        StorePage storePage = storeDAO.getStores(search, status, page, PAGE_SIZE);
        List<Store> stores = storePage.getStores();
        int totalStores = storePage.getTotalStores();
        int totalPages = (int) Math.ceil((double) totalStores / PAGE_SIZE);
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
            storePage = storeDAO.getStores(search, status, page, PAGE_SIZE);
            stores = storePage.getStores();
        }

        // Gửi dữ liệu đến JSP
        request.setAttribute("stores", stores);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("message", "Please correct the errors below.");
            request.setAttribute("messageType", "danger");
        }

        request.getRequestDispatcher("stores.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing store listings";
    }
}