package controllerCustomer;

import DAO.CustomerDAO;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Customer;
import model.Invoice;

/**
 * Servlet to handle viewing customer details and their invoices, and preparing edit page.
 * @author Admin
 */
@WebServlet(name = "CustomerDetailsServlet", urlPatterns = {"/CustomerDetailsServlet"})
public class CustomerDetailsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CustomerDAO customerDAO = new CustomerDAO();
        try {
            String customerIDStr = request.getParameter("customerID");
            System.out.println("CustomerDetailsServlet: Received customerID = " + customerIDStr); // Log
            if (customerIDStr == null || customerIDStr.trim().isEmpty()) {
                request.setAttribute("message", "Customer ID is missing.");
                request.setAttribute("messageType", "danger");
                request.getRequestDispatcher("customers.jsp").forward(request, response);
                return;
            }

            int customerID;
            try {
                customerID = Integer.parseInt(customerIDStr);
            } catch (NumberFormatException e) {
                System.out.println("CustomerDetailsServlet: Invalid customerID format: " + customerIDStr); // Log
                request.setAttribute("message", "Invalid customer ID format: " + customerIDStr);
                request.setAttribute("messageType", "danger");
                request.getRequestDispatcher("customers.jsp").forward(request, response);
                return;
            }

            Customer customer = customerDAO.getCustomerById(customerID);
            if (customer == null) {
                System.out.println("CustomerDetailsServlet: No customer found for ID: " + customerID); // Log
                request.setAttribute("message", "Customer not found with ID: " + customerID);
                request.setAttribute("messageType", "danger");
                request.getRequestDispatcher("customers.jsp").forward(request, response);
                return;
            }

            String action = request.getParameter("action");
            System.out.println("CustomerDetailsServlet: Action = " + action); // Log
            if ("edit".equals(action)) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("edit-customer.jsp").forward(request, response);
            } else {
                List<Invoice> invoices = customerDAO.getCustomerInvoices(customerID);
                request.setAttribute("customer", customer);
                request.setAttribute("invoices", invoices);
                request.getRequestDispatcher("customer-details.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("CustomerDetailsServlet: SQL Error: " + e.getMessage()); // Log
            request.setAttribute("message", "Error retrieving customer details: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("customers.jsp").forward(request, response);
        } finally {
            try {
                if (customerDAO.getDBContext() != null) {
                    customerDAO.getDBContext().closeConnection();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        return "Servlet to display customer details and invoices in the Shop database.";
    }
}