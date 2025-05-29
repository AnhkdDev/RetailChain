package Controller;

import DAO.UserDAO;
import Model.Users;
import dal.DBContext;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/checkLogin"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;
    
   

    @Override
    public void init() throws ServletException {
        userDAO = UserDAO.INSTANCE; // Use the singleton instance
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters from the form
        String username = request.getParameter("user");
        String password = request.getParameter("pass");

        try {
            // Validate user credentials
            Users user = userDAO.checkManager(username, password);

            if (user != null) {
                // Update last_login timestamp
                updateLastLogin(user.getUserId());

                // Successful login: Store user in session and redirect to home
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/homeManager.jsp");
            } else {
                // Failed login: Set error message and forward back to login page
                request.setAttribute("mess", "Invalid username or password, or you are not a manager.");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mess", "An error occurred. Please try again later.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void updateLastLogin(int userId) {
        String sql = "UPDATE Users SET last_login = ? WHERE user_id = ?";
        try {
            java.sql.PreparedStatement ps = userDAO.getConnection().prepareStatement(sql)  ;
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
