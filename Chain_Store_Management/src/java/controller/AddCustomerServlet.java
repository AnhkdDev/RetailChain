package controller;

import DAO.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Customer;

/**
 * Servlet to handle adding a new customer with password encryption.
 * @author Admin
 */
@WebServlet(name = "AddCustomerServlet", urlPatterns = {"/add-customer"})
public class AddCustomerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CustomerDAO customerDAO = new CustomerDAO();
        try {
            // Lấy dữ liệu từ form
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            String birthDateStr = request.getParameter("birthDate");
            String address = request.getParameter("address");
            String password = request.getParameter("password");

            // Validate dữ liệu
            if (fullName == null || fullName.trim().isEmpty()) {
                request.getSession().setAttribute("message", "Full name is required.");
                request.getSession().setAttribute("messageType", "danger");
                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
                return;
            }
            if (phone == null || !phone.matches("[0-9]{10}")) {
                request.getSession().setAttribute("message", "Phone number must be 10 digits.");
                request.getSession().setAttribute("messageType", "danger");
                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
                return;
            }
            if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                request.getSession().setAttribute("message", "Invalid email format.");
                request.getSession().setAttribute("messageType", "danger");
                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
                return;
            }
            if (gender == null || !gender.matches("Male|Female|Other")) {
                request.getSession().setAttribute("message", "Invalid gender selection.");
                request.getSession().setAttribute("messageType", "danger");
                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
                return;
            }
            if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
                request.getSession().setAttribute("message", "Password must be at least 8 characters, including one uppercase letter, one lowercase letter, and one number.");
                request.getSession().setAttribute("messageType", "danger");
                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
                return;
            }

            // Chuyển đổi birthDate từ String sang Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate;
            try {
                birthDate = sdf.parse(birthDateStr);
            } catch (java.text.ParseException e) {
                request.getSession().setAttribute("message", "Invalid birth date format.");
                request.getSession().setAttribute("messageType", "danger");
                request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
                return;
            }

            // Tạo đối tượng Customer
            Customer customer = new Customer();
            customer.setFullName(fullName.trim());
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setGender(gender);
            customer.setBirthDate(new java.sql.Date(birthDate.getTime()));
            customer.setCreatedAt(new java.sql.Timestamp(new Date().getTime()));
            customer.setAddress(address != null ? address.trim() : null);
            customer.setPassword(password); // Password will be hashed in CustomerDAO

            // Thêm khách hàng vào database với transaction
            customerDAO.getDBContext().getConnection().setAutoCommit(false);
            customerDAO.insertCustomer(customer);
            customerDAO.getDBContext().getConnection().commit();

            // Log thông tin khách hàng vừa thêm (loại bỏ password)
            System.out.println("AddCustomerServlet: Added new customer - Full Name: " + customer.getFullName());
            System.out.println("Phone: " + customer.getPhone());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Gender: " + customer.getGender());
            System.out.println("Birth Date: " + customer.getBirthDate());
            System.out.println("Address: " + customer.getAddress());

            // Set thông báo thành công và chuyển về danh sách
            request.getSession().setAttribute("message", "Customer added successfully.");
            request.getSession().setAttribute("messageType", "success");
            response.sendRedirect("CustomerListServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (customerDAO.getDBContext().getConnection() != null) {
                    customerDAO.getDBContext().getConnection().rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            request.getSession().setAttribute("message", "Error adding customer: " + e.getMessage());
            request.getSession().setAttribute("messageType", "danger");
            request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
        } finally {
            try {
                if (customerDAO.getDBContext().getConnection() != null) {
                    customerDAO.getDBContext().getConnection().setAutoCommit(true);
                    customerDAO.getDBContext().closeConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/add-customer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to add a new customer in the Shop database with password encryption.";
    }
}