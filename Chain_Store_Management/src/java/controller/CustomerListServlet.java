//package controller;
//
//import DAO.CustomerDAO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.List;
//import model.Customer;
//
//@WebServlet(name = "CustomerListServlet", urlPatterns = {"/CustomerListServlet"})
//public class CustomerListServlet extends HttpServlet {
//
//    private static final int RECORDS_PER_PAGE = 4;
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//
//        CustomerDAO customerDAO = new CustomerDAO();
//        try {
//            String search = request.getParameter("search");
//            String gender = request.getParameter("gender");
//            String membershipLevel = request.getParameter("membershipLevel");
//            String reset = request.getParameter("reset");
//
//            if ("true".equals(reset)) {
//                search = null;
//                gender = null;
//                membershipLevel = null;
//            }
//
//            int currentPage = 1;
//            if (request.getParameter("page") != null) {
//                currentPage = Integer.parseInt(request.getParameter("page"));
//            }
//
//            int offset = (currentPage - 1) * RECORDS_PER_PAGE;
//
//            List<Customer> allCustomers;
//            if (search != null || gender != null || membershipLevel != null) {
//                allCustomers = customerDAO.searchCustomers(search, gender, membershipLevel);
//            } else {
//                allCustomers = customerDAO.getAllCustomers();
//            }
//            int totalRecords = allCustomers.size();
//            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
//
//            int fromIndex = Math.min(offset, totalRecords);
//            int toIndex = Math.min(offset + RECORDS_PER_PAGE, totalRecords);
//            List<Customer> paginatedCustomers = (totalRecords > 0) ? allCustomers.subList(fromIndex, toIndex) : allCustomers;
//
//            // Set attributes for JSP
//            request.setAttribute("customers", paginatedCustomers);
//            request.setAttribute("currentPage", currentPage);
//            request.setAttribute("totalPages", totalPages);
//            request.setAttribute("search", search != null ? search : "");
//            request.setAttribute("gender", gender != null ? gender : "");
//            request.setAttribute("membershipLevel", membershipLevel != null ? membershipLevel : "");
//            request.setAttribute("baseUrl", "CustomerListServlet"); // Add baseUrl for pagination.jsp
//
//            request.getRequestDispatcher("customers.jsp").forward(request, response);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            request.setAttribute("message", "Error loading customers: " + e.getMessage());
//            request.setAttribute("messageType", "danger");
//            request.getRequestDispatcher("customers.jsp").forward(request, response);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            request.setAttribute("message", "Invalid page request: " + e.getMessage());
//            request.setAttribute("messageType", "danger");
//            request.setAttribute("customers", new java.util.ArrayList<>());
//            request.setAttribute("currentPage", 1);
//            request.setAttribute("totalPages", 1);
//            request.getRequestDispatcher("customers.jsp").forward(request, response);
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String action = request.getParameter("action");
//        CustomerDAO customerDAO = new CustomerDAO();
//
//        if ("save".equals(action)) {
//            try {
//                Customer customer = new Customer();
//                String customerId = request.getParameter("customerID");
//                customer.setFullName(request.getParameter("fullName"));
//                customer.setPhone(request.getParameter("phone"));
//                customer.setEmail(request.getParameter("email"));
//                customer.setGender(request.getParameter("gender"));
//                customer.setBirthDate(Date.valueOf(request.getParameter("birthDate")));
//                customer.setAddress(request.getParameter("address"));
//                // TotalSpent is not submitted in the form, so set it to 0 for new customers or retain existing value
//                customer.setTotalSpent(0.0); // Adjust based on your logic
//                customer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//
//                if (customerId != null && !customerId.isEmpty()) {
//                    // Update existing customer
//                    customer.setCustomerID(Integer.parseInt(customerId));
//                    customerDAO.updateCustomer(customer);
//                    request.setAttribute("message", "Customer updated successfully.");
//                    request.setAttribute("messageType", "success");
//                } else {
//                    // Insert new customer
//                    customer.setPassword(request.getParameter("password")); // Use password from form
//                    customerDAO.insertCustomer(customer);
//                    request.setAttribute("message", "Customer added successfully.");
//                    request.setAttribute("messageType", "success");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                request.setAttribute("message", "Error saving customer: " + e.getMessage());
//                request.setAttribute("messageType", "danger");
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                request.setAttribute("message", "Invalid input: " + e.getMessage());
//                request.setAttribute("messageType", "danger");
//            }
//        }
//
//        // Reload the customer list
//        processRequest(request, response);
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Servlet to display customer list with pagination and search in the Shop database.";
//    }
//}