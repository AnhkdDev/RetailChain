package controller;

import DAO.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.Customer;

@WebServlet(name = "SearchCustomerServlet", urlPatterns = {"/SearchCustomerServlet"})
public class SearchCustomerServlet extends HttpServlet {

    private static final int RECORDS_PER_PAGE = 3;
    private static final Logger LOGGER = Logger.getLogger(SearchCustomerServlet.class.getName());

    private String cleanSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            LOGGER.log(Level.INFO, "Search keyword is null or empty");
            return null;
        }
        String normalizedKeyword = normalizeVietnamese(keyword).trim();
        if (normalizedKeyword.length() > 100) {
            LOGGER.log(Level.WARNING, "Search keyword exceeds 100 characters, truncating");
            normalizedKeyword = normalizedKeyword.substring(0, 100);
        }
        String[] words = normalizedKeyword.split("\\s+");
        if (words.length == 0) {
            LOGGER.log(Level.WARNING, "No valid words in search keyword: {0}", keyword);
            return null;
        }
        String firstWord = words[0];
        if (firstWord.matches("[a-zA-Z0-9@._-]+")) {
            LOGGER.log(Level.INFO, "Original keyword: {0}, Cleaned: {1}", new Object[]{keyword, firstWord});
            return firstWord;
        } else {
            LOGGER.log(Level.WARNING, "Invalid first word filtered out: {0}", firstWord);
            return null;
        }
    }

    private String normalizeVietnamese(String text) {
        if (text == null) return null;
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        CustomerDAO customerDAO = new CustomerDAO();
        try {
            String action = request.getParameter("action");
            String search = request.getParameter("search");
            String cleanedSearch = cleanSearchKeyword(search);
            String gender = request.getParameter("gender");
            String membershipLevel = request.getParameter("membershipLevel");
            String reset = request.getParameter("reset");

            LOGGER.log(Level.INFO, "Processing request with action: {0}, search: {1}, cleanedSearch: {2}, gender: {3}, membershipLevel: {4}, reset: {5}", 
                       new Object[]{action, search, cleanedSearch, gender, membershipLevel, reset});

            if (search != null && !search.trim().isEmpty() && cleanedSearch == null) {
                request.setAttribute("message", "Từ khóa tìm kiếm không hợp lệ. Chỉ cho phép chữ cái, số, @, . , - và _ cho từ đầu tiên.");
                request.setAttribute("messageType", "warning");
            }

            if ("true".equals(reset)) {
                search = null;
                cleanedSearch = null;
                gender = null;
                membershipLevel = null;
            }

            if ("editCustomer".equals(action)) {
                String customerIdParam = request.getParameter("customerID");
                if (customerIdParam != null && !customerIdParam.trim().isEmpty()) {
                    try {
                        int customerID = Integer.parseInt(customerIdParam);
                        Customer customer = customerDAO.getCustomerById(customerID);
                        if (customer != null) {
                            request.setAttribute("editCustomer", customer);
                            request.setAttribute("showEditModal", true); // Flag để mở modal Edit
                            request.getRequestDispatcher("customers.jsp").forward(request, response);
                            return;
                        } else {
                            request.setAttribute("message", "Customer not found.");
                            request.setAttribute("messageType", "danger");
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Invalid customer ID: {0}", customerIdParam);
                        request.setAttribute("message", "Invalid customer ID.");
                        request.setAttribute("messageType", "danger");
                    }
                } else {
                    request.setAttribute("message", "Customer ID is required.");
                    request.setAttribute("messageType", "danger");
                }
            }

            int currentPage = getCurrentPage(request);
            int offset = (currentPage - 1) * RECORDS_PER_PAGE;
            LOGGER.log(Level.INFO, "Default action, Current page: {0}, Offset: {1}", new Object[]{currentPage, offset});

            List<Customer> customers = customerDAO.searchCustomers(cleanedSearch, gender, membershipLevel, offset, RECORDS_PER_PAGE);
            LOGGER.log(Level.INFO, "Found {0} customers for keyword: {1}", new Object[]{customers.size(), cleanedSearch});
            int totalRecords = customerDAO.getTotalCustomerCount(cleanedSearch, gender, membershipLevel);
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            setRequestAttributes(request, customers, currentPage, totalPages, search, gender, membershipLevel);
            request.getRequestDispatcher("customers.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while loading customers", e);
            request.setAttribute("message", "Error loading customers: " + e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("customers", new ArrayList<>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            request.getRequestDispatcher("customers.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid input: {0}", e.getMessage());
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("customers", new ArrayList<>());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            request.getRequestDispatcher("customers.jsp").forward(request, response);
        } finally {
            if (customerDAO.getDBContext() != null) {
                customerDAO.getDBContext().closeConnection();
            }
        }
    }

    private int getCurrentPage(HttpServletRequest request) {
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) {
                    currentPage = 1;
                    LOGGER.log(Level.INFO, "Page parameter was negative or zero ({0}), defaulting to 1", pageParam);
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid page parameter: {0}, defaulting to 1", pageParam);
                currentPage = 1;
            }
        }
        return currentPage;
    }

    private void setRequestAttributes(HttpServletRequest request, List<Customer> customers, int currentPage, int totalPages, 
                                     String search, String gender, String membershipLevel) {
        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search != null ? search : "");
        request.setAttribute("gender", gender != null ? gender : "");
        request.setAttribute("membershipLevel", membershipLevel != null ? membershipLevel : "");
        request.setAttribute("baseUrl", "SearchCustomerServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            CustomerDAO customerDAO = new CustomerDAO();
            try {
                String customerIdParam = request.getParameter("customerID");
                if (customerIdParam == null || customerIdParam.trim().isEmpty()) {
                    throw new IllegalArgumentException("Customer ID is required.");
                }
                int customerID = Integer.parseInt(customerIdParam);
                customerDAO.deleteCustomer(customerID);
                request.setAttribute("message", "Customer deleted successfully.");
                request.setAttribute("messageType", "success");
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid customer ID: {0}", request.getParameter("customerID"));
                request.setAttribute("message", "Invalid customer ID.");
                request.setAttribute("messageType", "danger");
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Invalid input: {0}", e.getMessage());
                request.setAttribute("message", e.getMessage());
                request.setAttribute("messageType", "danger");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error while deleting customer", e);
                request.setAttribute("message", "Error deleting customer: " + e.getMessage());
                request.setAttribute("messageType", "danger");
            } finally {
                if (customerDAO.getDBContext() != null) {
                    customerDAO.getDBContext().closeConnection();
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
                String fullName = request.getParameter("fullName");
                String phone = request.getParameter("phone");
                String gmail = request.getParameter("gmail");
                String gender = request.getParameter("gender");
                String birthDate = request.getParameter("birthDate");
                String address = request.getParameter("address");
                String img = request.getParameter("img");

                if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
                    throw new IllegalArgumentException("Full name is required and must be 1-100 characters.");
                }
                if (phone == null || !phone.matches("\\d{10}")) {
                    throw new IllegalArgumentException("Phone must be a 10-digit number.");
                }
                if (gmail == null || !gmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    throw new IllegalArgumentException("Invalid Gmail format.");
                }
                if (gender == null || !List.of("Male", "Female", "Other").contains(gender)) {
                    throw new IllegalArgumentException("Invalid gender.");
                }
                if (birthDate == null || birthDate.trim().isEmpty()) {
                    throw new IllegalArgumentException("Birth date is required.");
                }
                if (address == null || address.trim().isEmpty() || address.length() > 255) {
                    throw new IllegalArgumentException("Address is required and must be 1-255 characters.");
                }

                customer.setFullName(fullName);
                customer.setPhone(phone);
                customer.setGmail(gmail.toLowerCase());
                customer.setGender(gender);
                customer.setBirthDate(Date.valueOf(birthDate));
                customer.setAddress(address);
                customer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                customer.setImg(img);

                if (customerId != null && !customerId.trim().isEmpty()) {
                    customer.setCustomerID(Integer.parseInt(customerId));
                    customerDAO.updateCustomer(customer);
                    request.setAttribute("message", "Customer updated successfully.");
                    request.setAttribute("messageType", "success");
                } else {
                    customerDAO.insertCustomer(customer);
                    request.setAttribute("message", "Customer added successfully.");
                    request.setAttribute("messageType", "success");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error while saving customer", e);
                request.setAttribute("message", "Error saving customer: " + e.getMessage());
                request.setAttribute("messageType", "danger");
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Invalid input: {0}", e.getMessage());
                request.setAttribute("message", e.getMessage());
                request.setAttribute("messageType", "danger");
            } finally {
                if (customerDAO.getDBContext() != null) {
                    customerDAO.getDBContext().closeConnection();
                }
            }
        }

        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to manage customers with search, pagination, and CRUD operations in the Shop database.";
    }
}