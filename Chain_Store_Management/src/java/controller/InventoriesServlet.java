package controller;

import dal.CategoriesDAO;
import dal.InventoriesDAO;
import dal.WarehousesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "InventoryServlet", urlPatterns = {"/inventory"})
public class InventoriesServlet extends HttpServlet {

    private InventoriesDAO inventoriesDAO;
    private CategoriesDAO categoriesDAO;
    private WarehousesDAO warehousesDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        inventoriesDAO = new InventoriesDAO();
        categoriesDAO = new CategoriesDAO();
        warehousesDAO = new WarehousesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get filter parameters - mặc định là null để lấy tất cả
            String warehouseParam = request.getParameter("warehouse");
            String categoryParam = request.getParameter("category");
            String productName = request.getParameter("productName");

            Integer warehouseID = null;
            Integer categoryID = null;

            if (warehouseParam != null && !warehouseParam.isEmpty()) {
                warehouseID = Integer.parseInt(warehouseParam);
            }
            if (categoryParam != null && !categoryParam.isEmpty()) {
                categoryID = Integer.parseInt(categoryParam);
            }

            // Lấy dữ liệu ngay khi load trang
            request.setAttribute("inventoryList", inventoriesDAO.getFilteredInventory(warehouseID, categoryID, productName));
            request.setAttribute("categories", categoriesDAO.getAllCategories());
            request.setAttribute("warehouses", warehousesDAO.getAllWarehouses());

            // Forward to JSP
            request.getRequestDispatcher("/inventory.jsp").forward(request, response);

        } catch (Exception ex) {
            // Thay vì forward tới error.jsp, hiển thị lỗi ngay trên inventory.jsp
            request.setAttribute("error", "Error processing request: " + ex.getMessage());
            // Load lại dữ liệu mặc định
            request.setAttribute("inventoryList", inventoriesDAO.getFilteredInventory(null, null, null));
            request.setAttribute("categories", categoriesDAO.getAllCategories());
            request.setAttribute("warehouses", warehousesDAO.getAllWarehouses());
            request.getRequestDispatcher("/inventory.jsp").forward(request, response);
        }
    }
}
    








