package controllerCustomer;

import DAO.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;

@WebServlet(name = "SearchCustomerServlet", urlPatterns = {"/SearchCustomerServlet"})
public class SearchCustomerServlet extends HttpServlet {

    private static final int RECORDS_PER_PAGE = 5;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        CustomerDAO customerDAO = new CustomerDAO();
        try {
            String search = request.getParameter("search");
            String gender = request.getParameter("gender");
            String membershipLevel = request.getParameter("membershipLevel");
            String reset = request.getParameter("reset");

            if ("true".equals(reset)) {
                search = null;
                gender = null;
                membershipLevel = null;
            }

            int currentPage = 1;
            if (request.getParameter("page") != null) {
                try {
                    currentPage = Integer.parseInt(request.getParameter("page"));
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            int offset = (currentPage - 1) * RECORDS_PER_PAGE;

            List<Customer> allCustomers;
            if ((search == null || search.trim().isEmpty()) &&
                (gender == null || gender.trim().isEmpty()) &&
                (membershipLevel == null || membershipLevel.trim().isEmpty())) {
                allCustomers = customerDAO.getAllCustomers();
            } else {
                allCustomers = customerDAO.searchCustomers(search, gender, membershipLevel);
            }

            int totalRecords = allCustomers.size();
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
            int fromIndex = Math.min(offset, totalRecords);
            int toIndex = Math.min(offset + RECORDS_PER_PAGE, totalRecords);
            List<Customer> paginatedCustomers = (totalRecords > 0) ? allCustomers.subList(fromIndex, toIndex) : allCustomers;

            request.setAttribute("customers", paginatedCustomers);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("search", search);
            request.setAttribute("gender", gender);
            request.setAttribute("membershipLevel", membershipLevel);

            request.getRequestDispatcher("customers.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Error loading customers: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("customers.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            request.setAttribute("message", "Invalid page request: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("customers", new ArrayList<>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
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
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            CustomerDAO customerDAO = new CustomerDAO();
            try {
                int customerID = Integer.parseInt(request.getParameter("customerID"));
                customerDAO.deleteCustomer(customerID);
                request.setAttribute("message", "Customer deleted successfully.");
                request.setAttribute("messageType", "success");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("message", "Invalid customer ID.");
                request.setAttribute("messageType", "danger");
            } catch (SQLException ex) {
                Logger.getLogger(SearchCustomerServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        CustomerDAO customerDAO = new CustomerDAO();

        if ("save".equals(action)) {
            try {
                Customer customer = new Customer();
                String customerId = request.getParameter("customerID");
                customer.setFullName(request.getParameter("fullName"));
                customer.setPhone(request.getParameter("phone"));
                customer.setEmail(request.getParameter("email"));
                customer.setGender(request.getParameter("gender"));
                customer.setBirthDate(Date.valueOf(request.getParameter("birthDate")));
                customer.setAddress(request.getParameter("address"));
                customer.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                if (customerId != null && !customerId.isEmpty()) {
                    customer.setCustomerID(Integer.parseInt(customerId));
                    customerDAO.updateCustomer(customer);
                    request.setAttribute("message", "Customer updated successfully.");
                    request.setAttribute("messageType", "success");
                } else {
                    String password = request.getParameter("password");
                    if (password == null || password.trim().isEmpty()) {
                        throw new IllegalArgumentException("Password is required for new customers.");
                    }
                    customer.setPassword(password);
                    customerDAO.insertCustomer(customer);
                    request.setAttribute("message", "Customer added successfully.");
                    request.setAttribute("messageType", "success");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("message", "Error saving customer: " + e.getMessage());
                request.setAttribute("messageType", "danger");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                request.setAttribute("message", "Invalid input: " + e.getMessage());
                request.setAttribute("messageType", "danger");
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

        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to search and manage customers in the Shop database.";
    }
}