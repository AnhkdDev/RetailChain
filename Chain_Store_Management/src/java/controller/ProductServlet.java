package controller;

import DAO.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5; // Number of products per page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("ProductServlet: doGet called for /products at " + new java.util.Date());
        ProductDAO productDAO = null;
        List<Product> products = null;
        String errorMessage = null;
        int currentPage = 1;
        int totalProducts = 0;
        int totalPages = 1;

        try {
            System.out.println("ProductServlet: Initializing ProductDAO");
            productDAO = new ProductDAO();
            System.out.println("ProductServlet: ProductDAO initialized");

            // Test database connection
            System.out.println("ProductServlet: Testing DB connection");
            productDAO.getConnection().createStatement().execute("SELECT 1");
            System.out.println("ProductServlet: DB connection successful");

            // Get the current page from the request parameter
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    System.err.println("ProductServlet: Invalid page parameter: " + pageParam);
                    currentPage = 1;
                }
            }

            System.out.println("ProductServlet: Fetching products for page " + currentPage);
            products = productDAO.getProductsByPage(currentPage, PAGE_SIZE);
            System.out.println("ProductServlet: Number of products fetched: " + (products != null ? products.size() : "null"));

            if (products != null && !products.isEmpty()) {
                System.out.println("ProductServlet: Sample product - ID: " + products.get(0).getProductID() +
                                   ", Name: " + products.get(0).getProductName() +
                                   ", Category: " + products.get(0).getCategoryName());
            } else {
                System.out.println("ProductServlet: No products returned for page " + currentPage);
            }

            // Get the total number of products to calculate total pages
            totalProducts = productDAO.getTotalProductCount();
            totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
            System.out.println("ProductServlet: Total products: " + totalProducts + ", Total pages: " + totalPages);

            // Set attributes for JSP
            request.setAttribute("products", products != null ? products : new ArrayList<>());
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);
            System.out.println("ProductServlet: Products attribute set with size: " +
                               (products != null ? products.size() : 0));
        } catch (SQLException e) {
            System.err.println("ProductServlet: Database error: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "Database error fetching products: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("ProductServlet: Unexpected error: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "Unexpected error: " + e.getMessage();
        } finally {
            if (productDAO != null) {
                productDAO.closeConnection();
                System.out.println("ProductServlet: Database connection closed");
            }
        }

        if (errorMessage != null) {
            request.setAttribute("message", errorMessage);
            request.setAttribute("messageType", "danger");
            System.out.println("ProductServlet: Error message set: " + errorMessage);
        }
        System.out.println("ProductServlet: Forwarding to products.jsp");
        request.getRequestDispatcher("/products.jsp").forward(request, response);
        System.out.println("ProductServlet: Forwarded to products.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to display products in the store dashboard.";
    }
}