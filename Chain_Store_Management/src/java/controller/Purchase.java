
package controller;

import dao.PurchaseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Purchase", urlPatterns = {"/Purchase"})
public class Purchase extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PurchaseDAO dao = new PurchaseDAO();
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
                boolean deleted = dao.deletePurchase(purchaseID);
                if (deleted) {
                    request.getSession().setAttribute("successMessage", "Purchase deleted successfully.");
                } else {
                    request.setAttribute("errorMessage", "Failed to delete purchase. Purchase may not exist.");
                }
                response.sendRedirect("Purchase");
            } else if ("edit".equals(action)) {
                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
                var purchase = dao.getPurchaseById(purchaseID);
                if (purchase != null) {
                    request.setAttribute("purchase", purchase);
                    request.getRequestDispatcher("edit-purchase.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Purchase not found.");
                    request.setAttribute("listpurchase", dao.getAllPurchases(null, null, null, null, null, 1, 5));
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                }
            } else if ("create".equals(action)) {
                request.setAttribute("warehouses", dao.getAllWarehouses());
                request.setAttribute("products", dao.getAllProducts());
                request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
            } else if ("viewDetails".equals(action)) {
                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
                var purchase = dao.getPurchaseById(purchaseID);
                if (purchase != null) {
                    request.setAttribute("purchase", purchase);
                    request.setAttribute("purchaseDetails", dao.getPurchaseDetails(purchaseID));
                    request.getRequestDispatcher("purchase-details.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Purchase not found.");
                    request.setAttribute("listpurchase", dao.getAllPurchases(null, null, null, null, null, 1, 5));
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                }
            } else {
                // Lấy tham số tìm kiếm và lọc
                String search = request.getParameter("search");
                String warehouseName = request.getParameter("warehouseName");
                Integer purchaseID = null;
                BigDecimal minTotalAmount = null;
                BigDecimal maxTotalAmount = null;

                try {
                    String purchaseIDStr = request.getParameter("purchaseID");
                    String minTotalStr = request.getParameter("minTotalAmount");
                    String maxTotalStr = request.getParameter("maxTotalAmount");

                    if (purchaseIDStr != null && !purchaseIDStr.trim().isEmpty()) {
                        purchaseID = Integer.parseInt(purchaseIDStr);
                    }
                    if (minTotalStr != null && !minTotalStr.trim().isEmpty()) {
                        minTotalAmount = new BigDecimal(minTotalStr);
                    }
                    if (maxTotalStr != null && !maxTotalStr.trim().isEmpty()) {
                        maxTotalAmount = new BigDecimal(maxTotalStr);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid Purchase ID or amount format.");
                }

                // Phân trang
                int page = 1;
                int pageSize = 8;
                String pageStr = request.getParameter("page");
                if (pageStr != null && !pageStr.trim().isEmpty()) {
                    try {
                        page = Integer.parseInt(pageStr);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }

                // Lấy danh sách Purchases với tìm kiếm, lọc, và phân trang
                List<model.Purchase> purchases = dao.getAllPurchases(
                    search, warehouseName, purchaseID, minTotalAmount, maxTotalAmount, page, pageSize
                );

                // Tính tổng số trang
                int totalRecords = dao.getTotalPurchases(search, warehouseName, purchaseID, minTotalAmount, maxTotalAmount);
                int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

                // Truyền dữ liệu
                request.setAttribute("listpurchase", purchases);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("search", search);
                request.setAttribute("warehouseName", warehouseName);
                request.setAttribute("purchaseID", purchaseID);
                request.setAttribute("minTotalAmount", minTotalAmount);
                request.setAttribute("maxTotalAmount", maxTotalAmount);

                // Truyền danh sách warehouses cho dropdown
                request.setAttribute("warehouses", dao.getAllWarehouses());

                request.getRequestDispatcher("purchases.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid purchase ID.");
            request.setAttribute("listpurchase", dao.getAllPurchases(null, null, null, null, null, 1, 5));
            request.getRequestDispatcher("purchases.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.setAttribute("listpurchase", dao.getAllPurchases(null, null, null, null, null, 1, 5));
            request.getRequestDispatcher("purchases.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PurchaseDAO dao = new PurchaseDAO();
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                String supplierName = request.getParameter("supplierName");
                String warehouseIDStr = request.getParameter("warehouseID");

                List<Integer> productIDs = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                List<BigDecimal> costPrices = new ArrayList<>();

                int index = 1;
                while (request.getParameter("productID_" + index) != null) {
                    String productIDStr = request.getParameter("productID_" + index);
                    String quantityStr = request.getParameter("quantity_" + index);
                    String costPriceStr = request.getParameter("costPrice_" + index);

                    if (productIDStr == null || productIDStr.trim().isEmpty() ||
                        quantityStr == null || quantityStr.trim().isEmpty() ||
                        costPriceStr == null || costPriceStr.trim().isEmpty()) {
                        request.setAttribute("errorMessage", "Please fill in all product details.");
                        request.setAttribute("warehouses", dao.getAllWarehouses());
                        request.setAttribute("products", dao.getAllProducts());
                        request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
                        return;
                    }

                    productIDs.add(Integer.parseInt(productIDStr));
                    quantities.add(Integer.parseInt(quantityStr));
                    costPrices.add(new BigDecimal(costPriceStr));
                    index++;
                }

                if (supplierName == null || supplierName.trim().isEmpty() ||
                    warehouseIDStr == null || warehouseIDStr.trim().isEmpty() ||
                    productIDs.isEmpty()) {
                    request.setAttribute("errorMessage", "Please fill in supplier name, warehouse, and at least one product.");
                    request.setAttribute("warehouses", dao.getAllWarehouses());
                    request.setAttribute("products", dao.getAllProducts());
                    request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
                    return;
                }

                int warehouseID = Integer.parseInt(warehouseIDStr);
                boolean created = dao.addPurchase(supplierName.trim(), warehouseID, productIDs, quantities, costPrices);
                if (created) {
                    request.getSession().setAttribute("successMessage", "Purchase created successfully.");
                    response.sendRedirect("Purchase");
                } else {
                    request.setAttribute("errorMessage", "Failed to create purchase. Please check supplier name, warehouse, or products.");
                    request.setAttribute("warehouses", dao.getAllWarehouses());
                    request.setAttribute("products", dao.getAllProducts());
                    request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
                }
            } else if ("update".equals(action)) {
                String purchaseIDStr = request.getParameter("purchaseID");
                String purchaseDateStr = request.getParameter("purchaseDate");
                String totalAmountStr = request.getParameter("totalAmount");
                String supplierName = request.getParameter("supplierName");
                String warehouseIDStr = request.getParameter("warehouseID");

                if (purchaseIDStr == null || purchaseIDStr.trim().isEmpty() ||
                    purchaseDateStr == null || purchaseDateStr.trim().isEmpty() ||
                    totalAmountStr == null || totalAmountStr.trim().isEmpty() ||
                    supplierName == null || supplierName.trim().isEmpty() ||
                    warehouseIDStr == null || warehouseIDStr.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "All fields are required.");
                    request.setAttribute("listpurchase", dao.getAllPurchases(null, null, null, null, null, 1, 5));
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                    return;
                }

                int purchaseID = Integer.parseInt(purchaseIDStr);
                int warehouseID = Integer.parseInt(warehouseIDStr);
                BigDecimal totalAmount = new BigDecimal(totalAmountStr);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime localDateTime = LocalDateTime.parse(purchaseDateStr, formatter);
                Timestamp purchaseDate = Timestamp.valueOf(localDateTime);

                boolean updated = dao.updatePurchase(purchaseID, purchaseDate, totalAmount, supplierName.trim(), warehouseID);
                if (updated) {
                    request.getSession().setAttribute("successMessage", "Purchase updated successfully.");
                    response.sendRedirect("Purchase");
                } else {
                    request.setAttribute("errorMessage", "Failed to update purchase.");
                    request.setAttribute("listpurchase", dao.getAllPurchases(null, null, null, null, null, 1, 5));
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                }
            } else {
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for ID, quantity, or price.");
            request.setAttribute("warehouses", dao.getAllWarehouses());
            request.setAttribute("products", dao.getAllProducts());
            request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.setAttribute("warehouses", dao.getAllWarehouses());
            request.setAttribute("products", dao.getAllProducts());
            request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to manage purchases (display, create, edit, delete, view details)";
    }
}