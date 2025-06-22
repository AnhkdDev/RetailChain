<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Details</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="View detailed customer information and their invoices." />
    <meta name="keywords" content="store manager, customer details, retail, management" />
    <meta name="author" content="Admin" />
    <link rel="icon" href="assets/images/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/pages/waves/css/waves.min.css" type="text/css" media="all">
    <link rel="stylesheet" type="text/css" href="assets/icon/themify-icons/themify-icons.css">
    <link rel="stylesheet" type="text/css" href="assets/icon/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/type" href="assets/css/jquery.mCustomScrollbar.css">
    <link rel="stylesheet" type="text/css" href="assets/css/style.css">
    <style>
        .modal-lg {
            max-width: 80%;
        }
        .modal-backdrop {
            opacity: 0.5 !important; /* Làm mờ nền */
        }
    </style>
</head>
<body>
    <!-- Modal for Customer Details -->
    <div class="modal fade" id="customerDetailsModal" tabindex="-1" role="dialog" aria-labelledby="customerDetailsModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="customerDetailsModalLabel">Customer Details</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <c:if test="${not empty message}">
                        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                            <c:out value="${message}"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${not empty customer}">
                        <div class="card">
                            <div class="card-block">
<!--                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>Customer ID:</strong> <c:out value="${customer.customerID}"/></p>
                                        <p><strong>Full Name:</strong> <c:out value="${customer.fullName}"/></p>
                                        <p><strong>Phone:</strong> <c:out value="${customer.phone}"/></p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>Email:</strong> <c:out value="${customer.email}"/></p>
                                        <p><strong>Gender:</strong> <c:out value="${customer.gender}"/></p>
                                        <p><strong>Birth Date:</strong> <fmt:formatDate value="${customer.birthDate}" pattern="dd-MM-yyyy"/></p>
                                        <p><strong>Membership Level:</strong> <c:out value="${customer.membershipLevel}"/></p>
                                        <p><strong>Address:</strong> <c:out value="${customer.address}"/></p>
                                    </div>
                                </div>-->
                            </div>
                        </div>
                        <div class="card">
                            <div class="card-header">
                                <h5>Invoices</h5>
                            </div>
                            <div class="card-block">
                                <div class="dt-responsive table-responsive">
                                    <c:choose>
                                        <c:when test="${empty invoices}">
                                            <p>No invoices found for this customer.</p>
                                        </c:when>
                                        <c:otherwise>
                                            <table class="table table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Invoice ID</th>
                                                        <th>Invoice Date</th>
                                                        <th>Product Name</th>
                                                        <th>Quantity</th>
                                                        <th>Unit Price</th>
                                                        <th>Discount (%)</th>
                                                        <th>Line Total</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="invoice" items="${invoices}">
                                                        <tr>
                                                            <td><c:out value="${invoice.invoiceID}"/></td>
                                                            <td><fmt:formatDate value="${invoice.invoiceDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                                                            <td><c:out value="${invoice.productName}"/></td>
                                                            <td><fmt:formatNumber value="${invoice.quantity}" type="number"/></td>
                                                            <td><fmt:formatNumber value="${invoice.unitPrice}" type="currency" currencyCode="VND"/></td>
                                                            <td><fmt:formatNumber value="${invoice.discountPercent}" type="percent"/></td>
                                                            <td><fmt:formatNumber value="${invoice.lineTotal}" type="currency" currencyCode="VND"/></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript to handle modal -->
    <script type="text/javascript" src="assets/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="assets/js/popper.js/popper.min.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            // Auto-show modal when page loads (for testing purposes)
            $('#customerDetailsModal').modal('show');

            // Redirect to CustomerListServlet when closing the modal
            $('#customerDetailsModal').on('hidden.bs.modal', function () {
                window.location.href = 'SearchCustomerServlet'; // Redirect to CustomerListServlet
            });
        });
    </script>
</body>
</html>