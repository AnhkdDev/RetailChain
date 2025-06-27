package controller;

import DAO.StoreDAO;
import DAO.StoreDAO.StorePage;
import model.Store;
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
import java.util.regex.Pattern;
@WebServlet(name = "StoreListServlet", urlPatterns = {"/stores"})
public class StoreServlet extends HttpServlet {

    private StoreDAO storeDAO;
    private static final int PAGE_SIZE = 4;
    private static final int MAX_SEARCH_LENGTH = 100;
    private static final String PHONE_PATTERN = "^\\+?[0-9\\s\\(\\)-]{7,15}$";
    private static final String NAME_ADDRESS_PATTERN = "^[a-zA-Z0-9\\s\\p{L},.-]+$";
    private static final Logger LOGGER = Logger.getLogger(StoreServlet.class.getName());

    @Override
    public void init() throws ServletException {
        storeDAO = new StoreDAO();
    }

    private String cleanSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            LOGGER.log(Level.INFO, "Search keyword is null or empty");
            return null;
        }
        String normalizedKeyword = normalizeVietnamese(keyword);
        if (normalizedKeyword.length() > MAX_SEARCH_LENGTH) {
            LOGGER.log(Level.WARNING, "Search keyword exceeds {0} characters: {1}", new Object[]{MAX_SEARCH_LENGTH, normalizedKeyword});
            return null;
        }
        String result = normalizedKeyword.trim();
        LOGGER.log(Level.INFO, "Original keyword: {0}, Cleaned: {1}", new Object[]{keyword, result});
        return result;
    }

    private String normalizeVietnamese(String text) {
        if (text == null) return null;
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }

    private int countDigits(String text) {
        if (text == null) return 0;
        return text.replaceAll("[^0-9]", "").length();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String search = request.getParameter("search");
        String cleanedSearch = null;
        String status = request.getParameter("status");
        String reset = request.getParameter("reset");
        int page = 1;
        List<String> errors = new ArrayList<>();

        // Validation tìm kiếm
        if (search != null) {
            search = search.trim();
            if (search.isEmpty()) {
                search = null;
            } else if (search.length() > MAX_SEARCH_LENGTH) {
                errors.add("Tên hoặc địa chỉ tìm kiếm không được vượt quá " + MAX_SEARCH_LENGTH + " ký tự.");
                search = null;
            } else {
                cleanedSearch = cleanSearchKeyword(search);
                if (cleanedSearch == null || cleanedSearch.isEmpty()) {
                    cleanedSearch = null;
                    search = null;
                    errors.add("Từ khóa tìm kiếm không hợp lệ.");
                }
            }
        }

        // Validation trạng thái
        if (status != null && !status.equals("active") && !status.equals("inactive")) {
            status = null;
        }

        // Reset bộ lọc
        if (reset != null && reset.equals("true")) {
            search = null;
            cleanedSearch = null;
            status = null;
        }

        // Validation phân trang
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            errors.add("Số trang không hợp lệ.");
            page = 1;
        }

        // Handle edit action
        if (action != null && action.equals("edit")) {
            String storeID = request.getParameter("storeID");
            Store editStore = storeDAO.getStoreById(storeID);
            if (editStore != null) {
                request.setAttribute("editStore", editStore);
                LOGGER.log(Level.INFO, "Retrieved store for edit: ID={0}, Name={1}", new Object[]{editStore.getStoreID(), editStore.getStoreName()});
            } else {
                errors.add("Cửa hàng không tồn tại.");
                LOGGER.log(Level.WARNING, "Store not found for edit: ID={0}", storeID);
            }
        }

        // Xử lý toggle trạng thái
        if (action != null && action.equals("toggle")) {
            String storeID = request.getParameter("storeID");
            if (storeID == null || storeID.trim().isEmpty()) {
                errors.add("Mã cửa hàng là bắt buộc để thay đổi trạng thái.");
                LOGGER.log(Level.WARNING, "Missing storeID for toggle action");
            } else {
                try {
                    Integer.parseInt(storeID);
                    boolean toggled = storeDAO.toggleStoreStatus(storeID);
                    String redirectUrl = "stores?page=" + page;
                    if (search != null) redirectUrl += "&search=" + java.net.URLEncoder.encode(search, "UTF-8");
                    if (status != null) redirectUrl += "&status=" + status;
                    request.setAttribute("message", toggled ? "Cập nhật trạng thái cửa hàng thành công." : "Cửa hàng không tồn tại hoặc cập nhật thất bại.");
                    request.setAttribute("messageType", toggled ? "success" : "danger");
                    LOGGER.log(Level.INFO, "Toggle store status: ID={0}, Success={1}", new Object[]{storeID, toggled});
                    response.sendRedirect(redirectUrl);
                    return;
                } catch (NumberFormatException e) {
                    errors.add("Định dạng mã cửa hàng không hợp lệ.");
                    LOGGER.log(Level.WARNING, "Invalid storeID format: {0}", storeID);
                }
            }
        }

        // Xử lý lưu cửa hàng
        if (action != null && action.equals("save")) {
            Store store = new Store();
            String storeID = request.getParameter("storeID");
            String storeName = request.getParameter("storeName");
            String address = request.getParameter("address");
            String phoneNumber = request.getParameter("phoneNumber");
            String isActiveStr = request.getParameter("isActive");

            // Validation storeID
            if (storeID != null && !storeID.trim().isEmpty()) {
                try {
                    Integer.parseInt(storeID);
                    store.setStoreID(storeID);
                } catch (NumberFormatException e) {
                    errors.add("Định dạng mã cửa hàng không hợp lệ.");
                    LOGGER.log(Level.WARNING, "Invalid storeID format: {0}", storeID);
                }
            }

            // Validation storeName
            if (storeName == null || storeName.trim().isEmpty()) {
                errors.add("Tên cửa hàng là bắt buộc.");
            } else if (storeName.length() > 100) {
                errors.add("Tên cửa hàng không được vượt quá 100 ký tự.");
            } else if (!storeName.matches(NAME_ADDRESS_PATTERN)) {
                errors.add("Tên cửa hàng chỉ được chứa chữ cái, số, khoảng trắng, dấu phẩy, dấu chấm, dấu gạch ngang và ký tự tiếng Việt.");
            }

            // Validation address
            if (address == null || address.trim().isEmpty()) {
                errors.add("Địa chỉ là bắt buộc.");
            } else if (address.length() > 200) {
                errors.add("Địa chỉ không được vượt quá 200 ký tự.");
            } else if (!address.matches(NAME_ADDRESS_PATTERN)) {
                errors.add("Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng, dấu phẩy, dấu chấm, dấu gạch ngang và ký tự tiếng Việt.");
            }

            // Phone number validation
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                errors.add("Số điện thoại là bắt buộc.");
            } else if (phoneNumber.length() < 7 || phoneNumber.length() > 15) {
                errors.add("Số điện thoại phải chứa từ 7 đến 15 ký tự.");
            } else if (!phoneNumber.matches(PHONE_PATTERN)) {
                errors.add("Số điện thoại chỉ được chứa số, dấu cách, dấu ngoặc, dấu gạch ngang, và có thể bắt đầu bằng dấu +.");
            } else if (countDigits(phoneNumber) < 7) {
                errors.add("Số điện thoại phải chứa ít nhất 7 chữ số.");
            }

            // Validation isActive
            boolean isActive = "true".equalsIgnoreCase(isActiveStr);

            if (errors.isEmpty()) {
                store.setStoreName(storeName);
                store.setAddress(address);
                store.setPhoneNumber(phoneNumber);
                store.setEmail("");
                store.setActive(isActive);
                boolean saved = storeDAO.saveStore(store);
                if (!saved) {
                    errors.add("Tên cửa hàng hoặc địa chỉ đã được sử dụng cho một cửa hàng khác.");
                    request.setAttribute("store", new Store(storeName, address, phoneNumber, "", isActive));
                    request.setAttribute("storeID", storeID);
                    request.setAttribute("errors", errors);
                    request.setAttribute("message", "Vui lòng sửa các lỗi dưới đây.");
                    request.setAttribute("messageType", "danger");
                    LOGGER.log(Level.WARNING, "Failed to save store: Duplicate name or address");
                } else {
                    String redirectUrl = "stores?page=1";
                    if (search != null) redirectUrl += "&search=" + java.net.URLEncoder.encode(search, "UTF-8");
                    if (status != null) redirectUrl += "&status=" + status;
                    request.setAttribute("message", "Lưu cửa hàng thành công.");
                    request.setAttribute("messageType", "success");
                    LOGGER.log(Level.INFO, "Saved store: Name={0}, Address={1}", new Object[]{storeName, address});
                    response.sendRedirect(redirectUrl);
                    return;
                }
            } else {
                request.setAttribute("store", new Store(storeName, address, phoneNumber, "", isActive));
                request.setAttribute("storeID", storeID);
                request.setAttribute("errors", errors);
                request.setAttribute("message", "Vui lòng sửa các lỗi dưới đây.");
                request.setAttribute("messageType", "danger");
                LOGGER.log(Level.WARNING, "Validation errors: {0}", errors);
            }
        }

        try {
            LOGGER.log(Level.INFO, "Fetching stores with search: {0}, status: {1}, page: {2}", new Object[]{cleanedSearch, status, page});
            StorePage storePage = storeDAO.getStores(cleanedSearch, status, page, PAGE_SIZE);
            List<Store> stores = storePage.getStores();
            int totalStores = storePage.getTotalStores();
            int totalPages = (int) Math.ceil((double) totalStores / PAGE_SIZE);
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
                storePage = storeDAO.getStores(cleanedSearch, status, page, PAGE_SIZE);
                stores = storePage.getStores();
            }

            request.setAttribute("stores", stores);
            request.setAttribute("search", search != null ? search : "");
            request.setAttribute("status", status != null ? status : "");
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalStores);
            request.setAttribute("baseUrl", "stores");
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("message", "Vui lòng sửa các lỗi dưới đây.");
                request.setAttribute("messageType", "danger");
            }

            LOGGER.log(Level.INFO, "Found {0} stores for keyword: {1}", new Object[]{stores.size(), cleanedSearch});
            for (Store store : stores) {
                LOGGER.log(Level.INFO, "Store: ID={0}, Name={1}, Address={2}, Phone={3}", 
                           new Object[]{store.getStoreID(), store.getStoreName(), store.getAddress(), store.getPhoneNumber()});
            }

            request.getRequestDispatcher("stores.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing store request", e);
            request.setAttribute("message", "Lỗi khi tải danh sách cửa hàng: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("stores", new ArrayList<>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            request.setAttribute("totalProducts", 0);
            request.getRequestDispatcher("stores.jsp").forward(request, response);
        }
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

    @Override
    public void destroy() {
        if (storeDAO != null) {
            storeDAO.closeConnection();
        }
    }
}