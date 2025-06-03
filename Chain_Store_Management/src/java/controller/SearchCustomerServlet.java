package controller;

import DAO.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;

@WebServlet(name = "SearchCustomerServlet", urlPatterns = {"/SearchCustomerServlet"})
public class SearchCustomerServlet extends HttpServlet {

    private static final int RECORDS_PER_PAGE = 5;
    private static final Logger LOGGER = Logger.getLogger(SearchCustomerServlet.class.getName());

    // Hàm tách và lọc từ khóa
    private String cleanSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        // Chuẩn hóa: loại bỏ dấu, chuyển thành chữ thường
        String normalizedKeyword = normalizeVietnamese(keyword);
        // Tách từ dựa trên khoảng trắng
        String[] words = normalizedKeyword.trim().split("\\s+");
        List<String> cleanedWords = new ArrayList<>();

        for (String word : words) {
            // Kiểm tra từ hợp lệ
            if (isValidWord(word)) {
                cleanedWords.add(word);
            }
        }

        // Gộp các từ hợp lệ thành một chuỗi
        return cleanedWords.isEmpty() ? null : String.join(" ", cleanedWords);
    }

    // Kiểm tra từ hợp lệ
    private boolean isValidWord(String word) {
        if (word == null || word.length() < 2) {
            return false;
        }

        // Loại bỏ chuỗi ký tự lặp (như "ccccccccc")
        if (word.matches("^(.)\\1+$")) {
            return false;
        }

        // Chỉ cho phép chữ cái, số, và ký tự email cơ bản
        return word.matches("[a-z0-9@.]+");
    }

    // Normalize Vietnamese text
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
            // Validate and sanitize search input
            String search = request.getParameter("search");
            String cleanedSearch = null;
            if (search != null) {
                if (search.length() > 100) {
                    throw new IllegalArgumentException("Search term exceeds 100 characters.");
                }
                // Làm sạch từ khóa
                cleanedSearch = cleanSearchKeyword(search);
            }
            String gender = request.getParameter("gender");
            String membershipLevel = request.getParameter("membershipLevel");
            String reset = request.getParameter("reset");

            // Reset filters if requested
            if ("true".equals(reset)) {
                search = null;
                cleanedSearch = null;
                gender = null;
                membershipLevel = null;
            }

            // Validate page parameter
            int currentPage = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) {
                        currentPage = 1;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid page parameter: {0}", pageParam);
                    currentPage = 1;
                }
            }

            int offset = (currentPage - 1) * RECORDS_PER_PAGE;

            // Fetch customers
            List<Customer> allCustomers;
            if ((cleanedSearch == null || cleanedSearch.trim().isEmpty()) &&
                (gender == null || gender.trim().isEmpty()) &&
                (membershipLevel == null || membershipLevel.trim().isEmpty())) {
                allCustomers = customerDAO.getAllCustomers();
            } else {
                allCustomers = customerDAO.searchCustomers(cleanedSearch, gender, membershipLevel);
            }

            // Handle pagination
            int totalRecords = allCustomers.size();
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);
            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
                offset = (currentPage - 1) * RECORDS_PER_PAGE;
            }
            int fromIndex = Math.min(offset, totalRecords);
            int toIndex = Math.min(offset + RECORDS_PER_PAGE, totalRecords);
            List<Customer> paginatedCustomers = (totalRecords > 0) ? allCustomers.subList(fromIndex, toIndex) : new ArrayList<>();

            // Set request attributes
            request.setAttribute("customers", paginatedCustomers);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("search", request.getParameter("search")); // Preserve original input
            request.setAttribute("gender", gender);
            request.setAttribute("membershipLevel", membershipLevel);

            request.getRequestDispatcher("customers.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while loading customers", e);
            request.setAttribute("message", "Error loading customers: " + e.getMessage());
            request.setAttribute("messageType", "danger");
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
                String email = request.getParameter("email");
                String gender = request.getParameter("gender");
                String birthDate = request.getParameter("birthDate");
                String address = request.getParameter("address");

                // Input validation
                if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
                    throw new IllegalArgumentException("Full name is required and must be 1-100 characters.");
                }
                if (phone == null || !phone.matches("\\d{10}")) {
                    throw new IllegalArgumentException("Phone must be a 10-digit number.");
                }
                if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    throw new IllegalArgumentException("Invalid email format.");
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

                customer.setFullName(normalizeVietnamese(fullName));
                customer.setPhone(phone);
                customer.setEmail(email.toLowerCase());
                customer.setGender(gender);
                customer.setBirthDate(Date.valueOf(birthDate));
                customer.setAddress(normalizeVietnamese(address));
                customer.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                if (customerId != null && !customerId.trim().isEmpty()) {
                    customer.setCustomerID(Integer.parseInt(customerId));
                    customerDAO.updateCustomer(customer);
                    request.setAttribute("message", "Customer updated successfully.");
                    request.setAttribute("messageType", "success");
                } else {
                    String password = request.getParameter("password");
                    if (password == null || password.trim().isEmpty() || password.length() < 6) {
                        throw new IllegalArgumentException("Password is required and must be at least 6 characters.");
                    }
                    customer.setPassword(password);
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
        return "Servlet to search and manage customers in the Shop database.";
    }
}