package controllerStore;

import DAO.StoreDAO;
import DAO.StoreDAO.StorePage;
import model.Store;
import java.io.IOException;
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

        if (reset != null && reset.equals("true")) {
            search = null;
            status = null;
        }

        if (action != null && action.equals("toggle")) {
            String storeID = request.getParameter("storeID");
            if (storeID != null) {
                boolean toggled = storeDAO.toggleStoreStatus(storeID);
                String redirectUrl = "stores?page=" + (request.getParameter("page") != null ? request.getParameter("page") : "1");
                if (search != null) redirectUrl += "&search=" + java.net.URLEncoder.encode(search, "UTF-8");
                if (status != null) redirectUrl += "&status=" + status;
                request.setAttribute("message", toggled ? "Store status updated successfully." : "Failed to update store status.");
                request.setAttribute("messageType", toggled ? "success" : "danger");
                response.sendRedirect(redirectUrl);
                return;
            }
        }

        if (action != null && action.equals("save")) {
            Store store = new Store();
            String storeID = request.getParameter("storeID");
            if (storeID != null && !storeID.isEmpty()) {
                store.setStoreID(storeID);
            }
            store.setStoreName(request.getParameter("storeName"));
            store.setAddress(request.getParameter("address"));
            store.setPhoneNumber(request.getParameter("phoneNumber"));
            store.setEmail(""); // Email không có trong form
            store.setActive(Boolean.parseBoolean(request.getParameter("isActive")));
            boolean saved = storeDAO.saveStore(store);
            String redirectUrl = "stores?page=1";
            if (search != null) redirectUrl += "&search=" + java.net.URLEncoder.encode(search, "UTF-8");
            if (status != null) redirectUrl += "&status=" + status;
            request.setAttribute("message", saved ? "Store saved successfully." : "Failed to save store.");
            request.setAttribute("messageType", saved ? "success" : "danger");
            response.sendRedirect(redirectUrl);
            return;
        }

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        StorePage storePage = storeDAO.getStores(search, status, page, PAGE_SIZE);
        List<Store> stores = storePage.getStores();
        int totalStores = storePage.getTotalStores();
        int totalPages = (int) Math.ceil((double) totalStores / PAGE_SIZE);
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
            storePage = storeDAO.getStores(search, status, page, PAGE_SIZE);
            stores = storePage.getStores();
        }

        request.setAttribute("stores", stores);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

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