package controller;

import DAO.StoresDAO;
import DAO.WarehousesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import model.Warehouses;

@WebServlet(name = "WarehouseServlet", urlPatterns = {"/warehouses"})
public class WarehousesServlet extends HttpServlet {

    private WarehousesDAO warehousesDAO;
    private StoresDAO storesDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        warehousesDAO = new WarehousesDAO();
        storesDAO = new StoresDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Set data for JSP
            request.setAttribute("warehouses", warehousesDAO.getAllWarehouses());
            request.setAttribute("stores", storesDAO.getAllStores());
            
            // Forward to JSP
            request.getRequestDispatcher("/warehouses.jsp").forward(request, response);
            
        } catch (Exception ex) {
            request.setAttribute("error", "Error processing request: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String message = "";
        boolean isError = false;

        try {
            if ("add".equals(action)) {
                message = handleAddWarehouse(request);
            } else if ("edit".equals(action)) {
                message = handleEditWarehouse(request);
            } else if ("delete".equals(action)) {
                message = handleDeleteWarehouse(request);
            } else {
                message = "Invalid action";
                isError = true;
            }
        } catch (Exception ex) {
            message = "Error: " + ex.getMessage();
            isError = true;
        }

        // Redirect with encoded message
        String redirectUrl = "warehouses";
        if (!message.isEmpty()) {
            redirectUrl += "?message=" + URLEncoder.encode(message, "UTF-8") + "&isError=" + isError;
        }
        response.sendRedirect(redirectUrl);
    }

    private String handleAddWarehouse(HttpServletRequest request) throws Exception {
        String warehouseName = request.getParameter("warehouseName");
        String storeIDParam = request.getParameter("storeID");

        // Validate input
        if (warehouseName == null || warehouseName.trim().isEmpty()) {
            throw new Exception("Warehouse name cannot be empty");
        }
        if (storeIDParam == null || storeIDParam.isEmpty()) {
            throw new Exception("Please select a store");
        }

        int storeID = Integer.parseInt(storeIDParam);
        
        Warehouses warehouse = new Warehouses();
        warehouse.setWarehouseName(warehouseName.trim());
        warehouse.setStoreID(storeID);
        
        boolean success = warehousesDAO.addWarehouse(warehouse);
        return success ? "Warehouse added successfully" : "Failed to add warehouse";
    }

    private String handleEditWarehouse(HttpServletRequest request) throws Exception {
        String warehouseIDParam = request.getParameter("warehouseID");
        String warehouseName = request.getParameter("warehouseName");
        String storeIDParam = request.getParameter("storeID");

        // Validate input
        if (warehouseIDParam == null || warehouseName == null || warehouseName.trim().isEmpty() || storeIDParam == null) {
            throw new Exception("Invalid input data");
        }

        int warehouseID = Integer.parseInt(warehouseIDParam);
        int storeID = Integer.parseInt(storeIDParam);
        
        Warehouses warehouse = new Warehouses();
        warehouse.setWarehouseID(warehouseID);
        warehouse.setWarehouseName(warehouseName.trim());
        warehouse.setStoreID(storeID);
        
        boolean success = warehousesDAO.updateWarehouse(warehouse);
        return success ? "Warehouse updated successfully" : "Failed to update warehouse";
    }

    private String handleDeleteWarehouse(HttpServletRequest request) throws Exception {
        String warehouseIDParam = request.getParameter("warehouseID");

        // Validate input
        if (warehouseIDParam == null) {
            throw new Exception("Invalid warehouse ID");
        }

        int warehouseID = Integer.parseInt(warehouseIDParam);
        
        boolean success = warehousesDAO.deleteWarehouse(warehouseID);
        return success ? "Warehouse deleted successfully" : "Failed to delete warehouse";
    }
}