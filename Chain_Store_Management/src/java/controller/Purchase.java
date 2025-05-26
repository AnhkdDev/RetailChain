/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
//// */
////package controller;
////
////import dao.PurchaseDAO;
////import java.io.IOException;
////import java.io.PrintWriter;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.annotation.WebServlet;
////import jakarta.servlet.http.HttpServlet;
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////import java.math.BigDecimal;
////import java.util.List;
////
/////**
//// *
//// * @author ASUS
//// */
////@WebServlet(name = "Purchase", urlPatterns = {"/Purchase"})
////public class Purchase extends HttpServlet {
////
////    /**
////     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
////     * methods.
////     *
////     * @param request servlet request
////     * @param response servlet response
////     * @throws ServletException if a servlet-specific error occurs
////     * @throws IOException if an I/O error occurs
////     */
////    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
////            throws ServletException, IOException {
////        response.setContentType("text/html;charset=UTF-8");
////        try (PrintWriter out = response.getWriter()) {
////            /* TODO output your page here. You may use following sample code. */
////            out.println("<!DOCTYPE html>");
////            out.println("<html>");
////            out.println("<head>");
////            out.println("<title>Servlet Purchase</title>");
////            out.println("</head>");
////            out.println("<body>");
////            out.println("<h1>Servlet Purchase at " + request.getContextPath() + "</h1>");
////            out.println("</body>");
////            out.println("</html>");
////        }
////    }
////
////    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
////    /**
////     * Handles the HTTP <code>GET</code> method.
////     *
////     * @param request servlet request
////     * @param response servlet response
////     * @throws ServletException if a servlet-specific error occurs
////     * @throws IOException if an I/O error occurs
////     */
////    @Override
////    protected void doGet(HttpServletRequest request, HttpServletResponse response)
////            throws ServletException, IOException {
////        PurchaseDAO dao = new PurchaseDAO();
////        String action = request.getParameter("action");
////
////        //in ra list
////        try {
////            var listpurchase = dao.getAllPurchases();  // lấy list  từ database 
////            request.setAttribute("listpurchase", listpurchase);
////            request.getRequestDispatcher("purchases.jsp").forward(request, response);
////        } catch (Exception s) {
////            request.getRequestDispatcher("purchases.jsp").forward(request, response);
////        }
////
////       
////
////    }
////
////    /**
////     * Handles the HTTP <code>POST</code> method.
////     *
////     * @param request servlet request
////     * @param response servlet response
////     * @throws ServletException if a servlet-specific error occurs
////     * @throws IOException if an I/O error occurs
////     */
////    @Override
////    protected void doPost(HttpServletRequest request, HttpServletResponse response)
////            throws ServletException, IOException {
////        try {
////            PurchaseDAO dao = new PurchaseDAO();
////            var listpurchase = dao.getAllPurchases();  // lấy list  từ database 
////            request.setAttribute("listpurchase", listpurchase);
////            request.getRequestDispatcher("purchases.jsp").forward(request, response);
////        } catch (Exception s) {
////            request.getRequestDispatcher("purchases.jsp").forward(request, response);
////        }
////
////    }
////
////    /**
////     * Returns a short description of the servlet.
////     *
////     * @return a String containing servlet description
////     */
////    @Override
////    public String getServletInfo() {
////        return "Short description";
////    }// </editor-fold>
////
////}
////
////
//
//
//package controller;
//
//import dao.PurchaseDAO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@WebServlet(name = "Purchase", urlPatterns = {"/Purchase"})
//public class Purchase extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        PurchaseDAO dao = new PurchaseDAO();
//        String action = request.getParameter("action");
//
//        try {
//            if ("delete".equals(action)) {
//                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
//                boolean deleted = dao.deletePurchase(purchaseID);
//                if (deleted) {
//                    request.getSession().setAttribute("successMessage", "Purchase deleted successfully.");
//                } else {
//                    request.setAttribute("errorMessage", "Failed to delete purchase. Purchase may not exist.");
//                }
//                response.sendRedirect("Purchase");
//            } else if ("edit".equals(action)) {
//                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
//                var purchase = dao.getPurchaseById(purchaseID);
//                if (purchase != null) {
//                    request.setAttribute("purchase", purchase);
//                    request.getRequestDispatcher("edit-purchase.jsp").forward(request, response);
//                } else {
//                    request.setAttribute("errorMessage", "Purchase not found.");
//                    request.setAttribute("listpurchase", dao.getAllPurchases());
//                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
//                }
//            } else if ("create".equals(action)) {
//                // Load data for create-purchase.jsp
//                request.setAttribute("warehouses", dao.getAllWarehouses());
//                request.setAttribute("products", dao.getAllProducts());
//                request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
//            } else {
//                request.setAttribute("listpurchase", dao.getAllPurchases());
//                request.getRequestDispatcher("purchases.jsp").forward(request, response);
//            }
//        } catch (NumberFormatException e) {
//            request.setAttribute("errorMessage", "Invalid purchase ID.");
//            request.setAttribute("listpurchase", dao.getAllPurchases());
//            request.getRequestDispatcher("purchases.jsp").forward(request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
//            request.setAttribute("listpurchase", dao.getAllPurchases());
//            request.getRequestDispatcher("purchases.jsp").forward(request, response);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        PurchaseDAO dao = new PurchaseDAO();
//        String action = request.getParameter("action");
//
//        try {
//            if ("create".equals(action)) {
//                System.out.println("Received create purchase request");
//
//                // Lấy thông tin từ form
//                String supplierName = request.getParameter("supplierName");
//                String warehouseIDStr = request.getParameter("warehouseID");
//
//                // Danh sách chi tiết đơn mua hàng
//                List<Integer> productIDs = new ArrayList<>();
//                List<Integer> quantities = new ArrayList<>();
//                List<BigDecimal> costPrices = new ArrayList<>();
//
//                // Lấy chi tiết sản phẩm
//                int index = 1;
//                while (request.getParameter("productID_" + index) != null) {
//                    String productIDStr = request.getParameter("productID_" + index);
//                    String quantityStr = request.getParameter("quantity_" + index);
//                    String costPriceStr = request.getParameter("costPrice_" + index);
//
//                    if (productIDStr == null || productIDStr.trim().isEmpty() ||
//                        quantityStr == null || quantityStr.trim().isEmpty() ||
//                        costPriceStr == null || costPriceStr.trim().isEmpty()) {
//                        System.out.println("Error: Missing product details at index " + index);
//                        request.setAttribute("errorMessage", "Please fill in all product details.");
//                        request.setAttribute("warehouses", dao.getAllWarehouses());
//                        request.setAttribute("products", dao.getAllProducts());
//                        request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
//                        return;
//                    }
//
//                    productIDs.add(Integer.parseInt(productIDStr));
//                    quantities.add(Integer.parseInt(quantityStr));
//                    costPrices.add(new BigDecimal(costPriceStr));
//                    index++;
//                }
//
//                // Kiểm tra dữ liệu đầu vào
//                if (supplierName == null || supplierName.trim().isEmpty() ||
//                    warehouseIDStr == null || warehouseIDStr.trim().isEmpty() ||
//                    productIDs.isEmpty()) {
//                    System.out.println("Error: Missing required fields (supplierName, warehouseID, or product details).");
//                    request.setAttribute("errorMessage", "Please fill in supplier name, warehouse, and at least one product.");
//                    request.setAttribute("warehouses", dao.getAllWarehouses());
//                    request.setAttribute("products", dao.getAllProducts());
//                    request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
//                    return;
//                }
//
//                int warehouseID = Integer.parseInt(warehouseIDStr);
//
//                // Gọi PurchaseDAO
//                boolean created = dao.addPurchase(supplierName.trim(), warehouseID, productIDs, quantities, costPrices);
//                if (created) {
//                    request.getSession().setAttribute("successMessage", "Purchase created successfully.");
//                    response.sendRedirect("Purchase");
//                } else {
//                    request.setAttribute("errorMessage", "Failed to create purchase. Please check supplier name, warehouse, or products.");
//                    request.setAttribute("warehouses", dao.getAllWarehouses());
//                    request.setAttribute("products", dao.getAllProducts());
//                    request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
//                }
//            } else if ("update".equals(action)) {
//                String purchaseIDStr = request.getParameter("purchaseID");
//                String purchaseDateStr = request.getParameter("purchaseDate");
//                String totalAmountStr = request.getParameter("totalAmount");
//                String supplierName = request.getParameter("supplierName");
//                String warehouseIDStr = request.getParameter("warehouseID");
//
//                if (purchaseIDStr == null || purchaseIDStr.trim().isEmpty() ||
//                    purchaseDateStr == null || purchaseDateStr.trim().isEmpty() ||
//                    totalAmountStr == null || totalAmountStr.trim().isEmpty() ||
//                    supplierName == null || supplierName.trim().isEmpty() ||
//                    warehouseIDStr == null || warehouseIDStr.trim().isEmpty()) {
//                    request.setAttribute("errorMessage", "All fields are required.");
//                    request.setAttribute("listpurchase", dao.getAllPurchases());
//                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
//                    return;
//                }
//
//                int purchaseID = Integer.parseInt(purchaseIDStr);
//                int warehouseID = Integer.parseInt(warehouseIDStr);
//                BigDecimal totalAmount = new BigDecimal(totalAmountStr);
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
//                LocalDateTime localDateTime = LocalDateTime.parse(purchaseDateStr, formatter);
//                Timestamp purchaseDate = Timestamp.valueOf(localDateTime);
//
//                boolean updated = dao.updatePurchase(purchaseID, purchaseDate, totalAmount, supplierName.trim(), warehouseID);
//                if (updated) {
//                    request.getSession().setAttribute("successMessage", "Purchase updated successfully.");
//                    response.sendRedirect("Purchase");
//                } else {
//                    request.setAttribute("errorMessage", "Failed to update purchase.");
//                    request.setAttribute("listpurchase", dao.getAllPurchases());
//                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
//                }
//            } else {
//                doGet(request, response);
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("NumberFormatException: " + e.getMessage());
//            request.setAttribute("errorMessage", "Invalid number format for ID, quantity, or price.");
//            request.setAttribute("warehouses", dao.getAllWarehouses());
//            request.setAttribute("products", dao.getAllProducts());
//            request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
//        } catch (Exception e) {
//            System.out.println("Exception: " + e.getMessage());
//            e.printStackTrace();
//            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
//            request.setAttribute("warehouses", dao.getAllWarehouses());
//            request.setAttribute("products", dao.getAllProducts());
//            request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
//        }
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Servlet to manage purchases (display, create, edit, delete)";
//    }
//}

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
                    request.setAttribute("listpurchase", dao.getAllPurchases());
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                }
            } else if ("create".equals(action)) {
                // Load data for create-purchase.jsp
                request.setAttribute("warehouses", dao.getAllWarehouses());
                request.setAttribute("products", dao.getAllProducts());
                request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
            } else if ("viewDetails".equals(action)) {
                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
                var purchase = dao.getPurchaseById(purchaseID);
                if (purchase != null) {
                    List<model.PurchaseDetail> purchaseDetails = dao.getPurchaseDetails(purchaseID);
                    request.setAttribute("purchase", purchase);
                    request.setAttribute("purchaseDetails", purchaseDetails);
                    request.getRequestDispatcher("purchase-details.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Purchase not found.");
                    request.setAttribute("listpurchase", dao.getAllPurchases());
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("listpurchase", dao.getAllPurchases());
                request.getRequestDispatcher("purchases.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid purchase ID.");
            request.setAttribute("listpurchase", dao.getAllPurchases());
            request.getRequestDispatcher("purchases.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.setAttribute("listpurchase", dao.getAllPurchases());
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
                System.out.println("Received create purchase request");

                // Lấy thông tin từ form
                String supplierName = request.getParameter("supplierName");
                String warehouseIDStr = request.getParameter("warehouseID");

                // Danh sách chi tiết đơn mua hàng
                List<Integer> productIDs = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                List<BigDecimal> costPrices = new ArrayList<>();

                // Lấy chi tiết sản phẩm
                int index = 1;
                while (request.getParameter("productID_" + index) != null) {
                    String productIDStr = request.getParameter("productID_" + index);
                    String quantityStr = request.getParameter("quantity_" + index);
                    String costPriceStr = request.getParameter("costPrice_" + index);

                    if (productIDStr == null || productIDStr.trim().isEmpty() ||
                        quantityStr == null || quantityStr.trim().isEmpty() ||
                        costPriceStr == null || costPriceStr.trim().isEmpty()) {
                        System.out.println("Error: Missing product details at index " + index);
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

                // Kiểm tra dữ liệu đầu vào
                if (supplierName == null || supplierName.trim().isEmpty() ||
                    warehouseIDStr == null || warehouseIDStr.trim().isEmpty() ||
                    productIDs.isEmpty()) {
                    System.out.println("Error: Missing required fields (supplierName, warehouseID, or product details).");
                    request.setAttribute("errorMessage", "Please fill in supplier name, warehouse, and at least one product.");
                    request.setAttribute("warehouses", dao.getAllWarehouses());
                    request.setAttribute("products", dao.getAllProducts());
                    request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
                    return;
                }

                int warehouseID = Integer.parseInt(warehouseIDStr);

                // Gọi PurchaseDAO
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
                    request.setAttribute("listpurchase", dao.getAllPurchases());
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
                    request.setAttribute("listpurchase", dao.getAllPurchases());
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                }
            } else {
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            request.setAttribute("errorMessage", "Invalid number format for ID, quantity, or price.");
            request.setAttribute("warehouses", dao.getAllWarehouses());
            request.setAttribute("products", dao.getAllProducts());
            request.getRequestDispatcher("create-purchase.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
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