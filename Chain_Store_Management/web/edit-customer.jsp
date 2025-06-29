<%-- 
    Document   : edit-customer
    Created on : Jun 30, 2025, 12:15 AM
    Author     : Admin
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Edit Customer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <!-- Modal -->
    <div class="modal fade" id="editCustomerModal" tabindex="-1" aria-labelledby="editCustomerModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editCustomerModalLabel">Edit Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="window.location.href='SearchCustomerServlet'"></button>
                </div>
                <div class="modal-body">
                    <c:if test="${not empty message}">
                        <div class="alert alert-${messageType}" role="alert">
                            ${message}
                        </div>
                    </c:if>
                    <form action="SearchCustomerServlet" method="POST">
                        <input type="hidden" name="action" value="save">
                        <input type="hidden" name="customerID" id="customerId" value="${editCustomer.customerID}">
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="fullName" name="fullName" value="${editCustomer.fullName}" required maxlength="100">
                        </div>
                        <div class="mb-3">
                            <label for="phone" class="form-label">Phone</label>
                            <input type="text" class="form-control" id="phone" name="phone" value="${editCustomer.phone}" required pattern="\d{10}">
                        </div>
                        <div class="mb-3">
                            <label for="gmail" class="form-label">Gmail</label>
                            <input type="email" class="form-control" id="gmail" name="gmail" value="${editCustomer.gmail}" required>
                        </div>
                        <div class="mb-3">
                            <label for="gender" class="form-label">Gender</label>
                            <select class="form-select" id="gender" name="gender" required>
                                <option value="Male" ${editCustomer.gender == 'Male' ? 'selected' : ''}>Male</option>
                                <option value="Female" ${editCustomer.gender == 'Female' ? 'selected' : ''}>Female</option>
                                <option value="Other" ${editCustomer.gender == 'Other' ? 'selected' : ''}>Other</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="birthDate" class="form-label">Birth Date</label>
                            <input type="date" class="form-control" id="birthDate" name="birthDate" value="${editCustomer.birthDate != null ? editCustomer.birthDate : ''}" required>
                        </div>
                        <div class="mb-3">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" class="form-control" id="address" name="address" value="${editCustomer.address}" required maxlength="255">
                        </div>
                        <div class="mb-3">
                            <label for="img" class="form-label">Image URL</label>
                            <input type="text" class="form-control" id="img" name="img" value="${editCustomer.img}">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onclick="window.location.href='SearchCustomerServlet'">Close</button>
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Automatically show the modal when the page loads
        document.addEventListener('DOMContentLoaded', function () {
            var editCustomerModal = new bootstrap.Modal(document.getElementById('editCustomerModal'), {
                backdrop: 'static',
                keyboard: false
            });
            editCustomerModal.show();
        });
    </script>
</body>
</html>