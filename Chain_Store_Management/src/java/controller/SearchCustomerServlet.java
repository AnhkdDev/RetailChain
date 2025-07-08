    package controller;

    import DAO.CustomerDAO;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.MultipartConfig;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.*;
    import java.io.IOException;
    import java.nio.file.Paths;
    import java.sql.Date;
    import java.sql.SQLException;
    import java.sql.Timestamp;
    import java.text.Normalizer;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.logging.Level;
    import java.util.logging.Logger;
    import model.Customer;

    @WebServlet(name = "SearchCustomerServlet", urlPatterns = {"/SearchCustomerServlet"})
    @MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
    )
    public class SearchCustomerServlet extends HttpServlet {

        private static final int RECORDS_PER_PAGE = 10;
        private static final Logger LOGGER = Logger.getLogger(SearchCustomerServlet.class.getName());
        private static final String UPLOAD_DIR = "images";

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
                String pageParam = request.getParameter("page");

                LOGGER.log(Level.INFO, "Processing request with action: {0}, search: {1}, cleanedSearch: {2}, gender: {3}, membershipLevel: {4}, reset: {5}, page: {6}",
                           new Object[]{action, search, cleanedSearch, gender, membershipLevel, reset, pageParam});

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

                if ("add".equals(action)) {
                    request.setAttribute("action", "add");
                    request.setAttribute("showCustomerFormModal", true);
                    loadCustomerList(request, customerDAO, cleanedSearch, gender, membershipLevel);
                    request.getRequestDispatcher("customers.jsp").forward(request, response);
                    return;
                }

                if ("editCustomer".equals(action)) {
                    String customerIdParam = request.getParameter("customerID");
                    if (customerIdParam != null && !customerIdParam.trim().isEmpty()) {
                        try {
                            int customerID = Integer.parseInt(customerIdParam);
                            Customer customer = customerDAO.getCustomerById(customerID);
                            if (customer != null) {
                                request.setAttribute("editCustomer", customer);
                                request.setAttribute("action", "editCustomer");
                                request.setAttribute("showCustomerFormModal", true);
                                loadCustomerList(request, customerDAO, cleanedSearch, gender, membershipLevel);
                                request.getRequestDispatcher("customers.jsp").forward(request, response);
                                return;
                            } else {
                                request.setAttribute("message", "Không tìm thấy khách hàng.");
                                request.setAttribute("messageType", "danger");
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.log(Level.WARNING, "ID khách hàng không hợp lệ: {0}", customerIdParam);
                            request.setAttribute("message", "ID khách hàng không hợp lệ.");
                            request.setAttribute("messageType", "danger");
                        }
                    } else {
                        request.setAttribute("message", "Yêu cầu ID khách hàng.");
                        request.setAttribute("messageType", "danger");
                    }
                }

                if ("delete".equals(action)) {
                    String customerIdParam = request.getParameter("customerID");
                    if (customerIdParam != null && !customerIdParam.trim().isEmpty()) {
                        try {
                            int customerID = Integer.parseInt(customerIdParam);
                            customerDAO.deleteCustomer(customerID);
                            request.setAttribute("message", "Xóa khách hàng thành công.");
                            request.setAttribute("messageType", "success");
                        } catch (NumberFormatException e) {
                            LOGGER.log(Level.WARNING, "ID khách hàng không hợp lệ: {0}", customerIdParam);
                            request.setAttribute("message", "ID khách hàng không hợp lệ.");
                            request.setAttribute("messageType", "danger");
                        } catch (SQLException e) {
                            LOGGER.log(Level.SEVERE, "Lỗi cơ sở dữ liệu khi xóa khách hàng", e);
                            request.setAttribute("message", "Lỗi khi xóa khách hàng: " + e.getMessage());
                            request.setAttribute("messageType", "danger");
                        }
                    } else {
                        request.setAttribute("message", "Yêu cầu ID khách hàng.");
                        request.setAttribute("messageType", "danger");
                    }
                }

                loadCustomerList(request, customerDAO, cleanedSearch, gender, membershipLevel);
                request.getRequestDispatcher("customers.jsp").forward(request, response);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Lỗi cơ sở dữ liệu khi tải danh sách khách hàng", e);
                request.setAttribute("message", "Lỗi khi tải danh sách khách hàng: " + e.getMessage());
                request.setAttribute("messageType", "danger");
                request.setAttribute("customers", new ArrayList<>());
                request.setAttribute("currentPage", 1);
                request.setAttribute("totalPages", 1);
                request.getRequestDispatcher("customers.jsp").forward(request, response);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Dữ liệu không hợp lệ: {0}", e.getMessage());
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

        private void loadCustomerList(HttpServletRequest request, CustomerDAO customerDAO, String cleanedSearch, String gender, String membershipLevel) throws SQLException {
            int currentPage = getCurrentPage(request);
            int offset = (currentPage - 1) * RECORDS_PER_PAGE;
            LOGGER.log(Level.INFO, "Đang tải danh sách khách hàng, Trang hiện tại: {0}, Offset: {1}", new Object[]{currentPage, offset});

            List<Customer> customers = customerDAO.searchCustomers(cleanedSearch, gender, membershipLevel, offset, RECORDS_PER_PAGE);
            LOGGER.log(Level.INFO, "Trả về {0} khách hàng cho trang {1}", new Object[]{customers.size(), currentPage});
            int totalRecords = customerDAO.getTotalCustomerCount(cleanedSearch, gender, membershipLevel);
            int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

            setRequestAttributes(request, customers, currentPage, totalPages, request.getParameter("search"), gender, membershipLevel);
        }

        private int getCurrentPage(HttpServletRequest request) {
            int currentPage = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) {
                        currentPage = 1;
                        LOGGER.log(Level.INFO, "Tham số trang âm hoặc bằng 0 ({0}), đặt lại thành 1", pageParam);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Tham số trang không hợp lệ: {0}, đặt lại thành 1", pageParam);
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
            processRequest(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            String action = request.getParameter("action");
            CustomerDAO customerDAO = new CustomerDAO();

            if ("save".equals(action)) {
                Map<String, String> errors = new HashMap<>();
                Customer customer = new Customer();
                String customerId = request.getParameter("customerID");
                String fullName = request.getParameter("fullName");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String gender = request.getParameter("gender");
                String birthDate = request.getParameter("birthDate");
                String gmail = request.getParameter("gmail");
                Part imagePart = request.getPart("imageUpload");
                String existingImagePath = request.getParameter("img");

                // Validate inputs
                if (fullName == null || fullName.trim().isEmpty() || fullName.length() > 100) {
                    errors.put("fullName", "Tên đầy đủ là bắt buộc và phải từ 1-100 ký tự.");
                }
                if (phone == null || !phone.matches("^(\\+84|0)\\d{9}$")) {
                    errors.put("phone", "Số điện thoại phải bắt đầu bằng +84 hoặc 0 và có đúng 10 chữ số.");
                }
                if (address == null || address.trim().isEmpty() || address.length() > 255) {
                    errors.put("address", "Địa chỉ là bắt buộc và phải từ 1-255 ký tự.");
                }
                if (gender == null || !List.of("Male", "Female", "Other").contains(gender)) {
                    errors.put("gender", "Giới tính không hợp lệ.");
                }
                if (birthDate == null || birthDate.trim().isEmpty()) {
                    errors.put("birthDate", "Ngày sinh là bắt buộc.");
                }
                if (gmail == null || !gmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    errors.put("gmail", "Định dạng Gmail không hợp lệ.");
                }

                // Validate and process image
                String imagePath = existingImagePath;
                if (imagePart != null && imagePart.getSize() > 0) {
                    // Validate file size
                    if (imagePart.getSize() > 2 * 1024 * 1024) {
                        errors.put("imageUpload", "Kích thước hình ảnh không được vượt quá 2MB.");
                    } else {
                        // Validate file type
                        String contentType = imagePart.getContentType();
                        if (contentType == null || !contentType.matches("image/(jpeg|jpg|png)")) {
                            errors.put("imageUpload", "Chỉ chấp nhận định dạng .jpg, .jpeg, hoặc .png.");
                        } else {
                            // Get original file name
                            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                            // Create simple path: images/<timestamp>_<original file name>
                            String filePath = UPLOAD_DIR + "/" + System.currentTimeMillis() + "_" + fileName;
                            imagePath = filePath;
                        }
                    }
                }

                // Populate customer object if no validation errors
                if (errors.isEmpty()) {
                    customer.setFullName(fullName);
                    customer.setPhone(phone);
                    customer.setAddress(address);
                    customer.setGender(gender);
                    try {
                        customer.setBirthDate(Date.valueOf(birthDate));
                    } catch (IllegalArgumentException e) {
                        errors.put("birthDate", "Định dạng ngày không hợp lệ.");
                    }
                    customer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                    customer.setActive(true);
                    customer.setUserId(0); // Placeholder, will be set by DAO
                    customer.setGmail(gmail.toLowerCase());
                    customer.setImg(imagePath);
                    if (customerId != null && !customerId.trim().isEmpty()) {
                        try {
                            customer.setCustomerID(Integer.parseInt(customerId));
                        } catch (NumberFormatException e) {
                            errors.put("customerID", "ID khách hàng không hợp lệ.");
                        }
                    }
                }

                // Process save if no errors
                if (errors.isEmpty()) {
                    try {
                        // Create images directory if it doesn't exist
                        String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);
                        java.io.File uploadDir = new java.io.File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        // Delete old image if a new one is uploaded
                        if (existingImagePath != null && !existingImagePath.equals(imagePath) && imagePart != null && imagePart.getSize() > 0) {
                            String oldFilePath = getServletContext().getRealPath(existingImagePath);
                            java.io.File oldFile = new java.io.File(oldFilePath);
                            if (oldFile.exists()) {
                                oldFile.delete();
                                LOGGER.info("Deleted old image: " + oldFilePath);
                            }
                        }

                        // Save new image to disk
                        if (imagePart != null && imagePart.getSize() > 0) {
                            String fullPath = getServletContext().getRealPath(imagePath);
                            imagePart.write(fullPath);
                            LOGGER.info("Saved image to: " + fullPath);
                        }

                        if (customer.getCustomerID() > 0) {
                            customerDAO.updateCustomer(customer);
                            redirectWithSuccess(request, response, "Cập nhật khách hàng thành công.");
                            return;
                        } else {
                            customerDAO.insertCustomer(customer);
                            redirectWithSuccess(request, response, "Thêm khách hàng thành công.");
                            return;
                        }
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Lỗi cơ sở dữ liệu khi lưu khách hàng", e);
                        String errorMsg = e.getMessage();
                        if (errorMsg.contains("Email already exists")) {
                            errors.put("gmail", "Email đã được sử dụng.");
                        } else if (errorMsg.contains("Phone number already exists")) {
                            errors.put("phone", "Số điện thoại đã được sử dụng.");
                        } else if (errorMsg.contains("UserID")) {
                            errors.put("userId", "UserID hiện tại đã được sử dụng bởi một khách hàng active khác hoặc không tìm thấy ID khả dụng.");
                        } else {
                            request.setAttribute("message", "Lỗi khi lưu khách hàng: " + errorMsg);
                            request.setAttribute("messageType", "danger");
                        }
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Lỗi khi lưu file ảnh", e);
                        errors.put("imageUpload", "Lỗi khi lưu file ảnh: " + e.getMessage());
                    }
                }

                // If errors, return to form with error messages
                request.setAttribute("errors", errors);
                request.setAttribute("editCustomer", customer);
                request.setAttribute("action", customer.getCustomerID() > 0 ? "editCustomer" : "add");
                request.setAttribute("showCustomerFormModal", true);
                try {
                    loadCustomerList(request, customerDAO, request.getParameter("search"), request.getParameter("gender"), request.getParameter("membershipLevel"));
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Lỗi cơ sở dữ liệu khi tải danh sách khách hàng", ex);
                }
                request.getRequestDispatcher("customers.jsp").forward(request, response);
                return;
            }

            processRequest(request, response);
        }

        private void redirectWithSuccess(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
            String redirectUrl = "SearchCustomerServlet" +
                    "?search=" + (request.getParameter("search") != null ? java.net.URLEncoder.encode(request.getParameter("search"), "UTF-8") : "") +
                    "&gender=" + "" + // Reset gender to empty (All) after successful save
                    "&membershipLevel=" + (request.getParameter("membershipLevel") != null ? java.net.URLEncoder.encode(request.getParameter("membershipLevel"), "UTF-8") : "") +
                    "&page=" + (request.getParameter("page") != null ? request.getParameter("page") : "1") +
                    "&message=" + java.net.URLEncoder.encode(message, "UTF-8") +
                    "&messageType=success";
            response.sendRedirect(redirectUrl);
        }

        @Override
        public String getServletInfo() {
            return "Servlet to manage customers with search, pagination, and CRUD operations in the Shop database.";
        }
    }