///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controller;
//
//import dao.PurchaseDAO;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// *
// * @author ASUS
// */
//@WebServlet(name = "Purchase", urlPatterns = {"/Purchase"})
//public class Purchase extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Purchase</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet Purchase at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        PurchaseDAO dao = new PurchaseDAO();
//        String action = request.getParameter("action");
//
//        //in ra list
//        try {
//            var listpurchase = dao.getAllPurchases();  // lấy list  từ database 
//            request.setAttribute("listpurchase", listpurchase);
//            request.getRequestDispatcher("purchases.jsp").forward(request, response);
//        } catch (Exception s) {
//            request.getRequestDispatcher("purchases.jsp").forward(request, response);
//        }
//
//       
//
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            PurchaseDAO dao = new PurchaseDAO();
//            var listpurchase = dao.getAllPurchases();  // lấy list  từ database 
//            request.setAttribute("listpurchase", listpurchase);
//            request.getRequestDispatcher("purchases.jsp").forward(request, response);
//        } catch (Exception s) {
//            request.getRequestDispatcher("purchases.jsp").forward(request, response);
//        }
//
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
//
//

package controller;

import dao.PurchaseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;

@WebServlet(name = "Purchase", urlPatterns = {"/Purchase"})
public class Purchase extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Purchase</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Purchase at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PurchaseDAO dao = new PurchaseDAO();
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                // Xử lý xóa giao dịch
                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
                boolean deleted = dao.deletePurchase(purchaseID);
                if (deleted) {
                    request.getSession().setAttribute("successMessage", "Purchase deleted successfully.");
                } else {
                    request.setAttribute("errorMessage", "Failed to delete purchase. It may not exist.");
                }
                response.sendRedirect("Purchase");
                return;
            } else if ("edit".equals(action)) {
                // Lấy thông tin giao dịch để chỉnh sửa
                int purchaseID = Integer.parseInt(request.getParameter("purchaseID"));
                var purchase = dao.getPurchaseById(purchaseID);
                if (purchase != null) {
                    request.setAttribute("purchase", purchase);
                    request.getRequestDispatcher("edit-purchase.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Purchase not found.");
                    var listpurchase = dao.getAllPurchases();
                    request.setAttribute("listpurchase", listpurchase);
                    request.getRequestDispatcher("purchases.jsp").forward(request, response);
                }
            } else {
                // Hiển thị danh sách giao dịch mặc định
                var listpurchase = dao.getAllPurchases();
                request.setAttribute("listpurchase", listpurchase);
                request.getRequestDispatcher("purchases.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("purchases.jsp").forward(request, response);
        }
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    PurchaseDAO dao = new PurchaseDAO();
    String action = request.getParameter("action");

    try {
        if ("update".equals(action)) {
            // Log để kiểm tra dữ liệu gửi đến
            System.out.println("Received update request with parameters:");
            String purchaseIDStr = request.getParameter("purchaseID");
            String purchaseDateStr = request.getParameter("purchaseDate");
            String totalAmountStr = request.getParameter("totalAmount");
            String supplierName = request.getParameter("supplierName");
            String warehouseName = request.getParameter("warehouseName");

            System.out.println("purchaseID: " + purchaseIDStr);
            System.out.println("purchaseDate: " + purchaseDateStr);
            System.out.println("totalAmount: " + totalAmountStr);
            System.out.println("supplierName: " + supplierName);
            System.out.println("warehouseName: " + warehouseName);

            // Kiểm tra dữ liệu đầu vào
            if (purchaseIDStr == null || purchaseIDStr.isEmpty() ||
                purchaseDateStr == null || purchaseDateStr.isEmpty() ||
                totalAmountStr == null || totalAmountStr.isEmpty() ||
                supplierName == null || supplierName.isEmpty() ||
                warehouseName == null || warehouseName.isEmpty()) {
                System.out.println("Error: One or more parameters are null or empty.");
                request.setAttribute("errorMessage", "All fields are required. Please fill in all details.");
                var listpurchase = dao.getAllPurchases();
                request.setAttribute("listpurchase", listpurchase);
                request.getRequestDispatcher("purchases.jsp").forward(request, response);
                return;
            }

            // Xử lý cập nhật giao dịch
            int purchaseID = Integer.parseInt(purchaseIDStr);
            BigDecimal totalAmount = new BigDecimal(totalAmountStr);

            // Chuyển đổi purchaseDateStr sang Timestamp sử dụng java.time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(purchaseDateStr, formatter);
            Timestamp purchaseDate = Timestamp.valueOf(localDateTime);

            System.out.println("Parsed purchaseDate: " + purchaseDate);

            // Gọi phương thức updatePurchase
            System.out.println("Calling updatePurchase with: purchaseID=" + purchaseID + ", purchaseDate=" + purchaseDate + ", totalAmount=" + totalAmount + ", supplierName=" + supplierName + ", warehouseName=" + warehouseName);
            boolean updated = dao.updatePurchase(purchaseID, purchaseDate, totalAmount, supplierName, warehouseName);
            if (updated) {
                request.getSession().setAttribute("successMessage", "Purchase updated successfully.");
            } else {
                request.setAttribute("errorMessage", "Failed to update purchase. It may not exist, or the supplier/warehouse names are invalid.");
            }
            response.sendRedirect("Purchase");
        } else {
            doGet(request, response);
        }
    } catch (NumberFormatException e) {
        System.out.println("NumberFormatException: " + e.getMessage());
        request.setAttribute("errorMessage", "Invalid number format for Purchase ID or Total Amount.");
        var listpurchase = dao.getAllPurchases();
        request.setAttribute("listpurchase", listpurchase);
        request.getRequestDispatcher("purchases.jsp").forward(request, response);
    } catch (Exception e) {
        System.out.println("Exception: " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("errorMessage", "An error occurred while updating: " + e.getMessage());
        var listpurchase = dao.getAllPurchases();
        request.setAttribute("listpurchase", listpurchase);
        request.getRequestDispatcher("purchases.jsp").forward(request, response);
    }
}

@Override
public String getServletInfo() {
    return "Servlet to manage purchases (display, edit, delete)";
}
}