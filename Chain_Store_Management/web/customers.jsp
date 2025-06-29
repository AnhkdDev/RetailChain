<%-- 
    Document   : customers
    Created on : Jun 17, 2025, 1:15:40 PM
    Author     : Admin
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Store Manager - Customers</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="description" content="Manage customers for the store, including viewing, adding, and deleting customer information." />
        <meta name="keywords" content="store manager, customers, retail, management" />
        <meta name="author" content="Admin" />
        <link rel="icon" href="assets/images/favicon.ico" type="image/x-icon">
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
        <link rel="stylesheet" href="assets/css/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/pages/waves/css/waves.min.css" media="all">
        <link rel="stylesheet" href="assets/icon/themify-icons/themify-icons.css">
        <link rel="stylesheet" href="assets/icon/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="assets/css/jquery.mCustomScrollbar.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <style>
            .form-group {
                margin-right: 15px;
            }
            .btn-info {
                background-color: #17a2b8 !important;
                border-color: #17a2b8 !important;
                color: #fff !important;
            }
            .btn-info:hover {
                background-color: #138496 !important;
                border-color: #138496 !important;
            }
            .btn-primary {
                background-color: #007bff !important;
                border-color: #007bff !important;
                color: #fff !important;
            }
            .btn-primary:hover {
                background-color: #0056b3 !important;
                border-color: #0056b3 !important;
            }
            .btn-danger {
                background-color: #dc3545 !important;
                border-color: #dc3545 !important;
                color: #fff !important;
            }
            .btn-danger:hover {
                background-color: #c82333 !important;
                border-color: #c82333 !important;
            }
            .btn-sm {
                padding: 0.25rem 0.5rem;
                font-size: 0.875rem;
                line-height: 1.5;
                border-radius: 0.2rem;
            }
            .pagination {
                margin-top: 20px;
            }
            .pagination .page-link {
                color: #007bff;
            }
            .pagination .page-link:hover {
                background-color: #0056b3;
                color: #fff;
            }
            .pagination .page-item.active .page-link {
                background-color: #007bff;
                border-color: #007bff;
                color: #fff;
            }
            .card {
                overflow: auto;
                max-height: none !important;
            }
            .full-card {
                cursor: pointer;
            }
            .table-card.fullscreen {
                position: fixed !important;
                top: 0 !important;
                left: 0 !important;
                width: 100% !important;
                height: 100% !important;
                z-index: 1050 !important;
                background: #fff !important;
                margin: 0 !important;
                padding: 15px !important;
                overflow: auto !important;
            }
            .table-card.fullscreen .card-block {
                max-height: 90vh !important;
                overflow-y: auto !important;
            }
            .modal-content {
                border-radius: 0.3rem;
            }
            .modal-header {
                background-color: #007bff;
                color: #fff;
            }
            .modal-title {
                font-weight: 500;
            }
            .modal-footer {
                justify-content: flex-end;
            }
            .modal-lg {
                max-width: 80%;
            }
            .modal-backdrop {
                opacity: 0.5 !important;
            }
            .card-img-top {
                max-width: 100px;
                max-height: 100px;
                object-fit: cover;
            }
            .detail-label {
                font-weight: bold;
                margin-bottom: 5px;
            }
            .detail-value {
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body>
        <div class="theme-loader">
            <div class="loader-track">
                <div class="preloader-wrapper">
                    <div class="spinner-layer spinner-blue">
                        <div class="circle-clipper left"><div class="circle"></div></div>
                        <div class="gap-patch"><div class="circle"></div></div>
                        <div class="circle-clipper right"><div class="circle"></div></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="pcoded" class="pcoded">
            <div class="pcoded-overlay-box"></div>
            <div class="pcoded-container navbar-wrapper">
                <nav class="navbar header-navbar pcoded-header">
                    <div class="navbar-wrapper">
                        <div class="navbar-logo">
                            <a href="index.jsp"><img class="img-fluid" src="assets/images/logo.png" alt="Store-Logo" /></a>
                            <a class="mobile-menu waves-effect waves-light" id="mobile-collapse" href="#!"><i class="ti-menu"></i></a>
                        </div>
                        <div class="navbar-container container-fluid">
                            <ul class="nav-left">
                                <li><div class="sidebar_toggle"><a href="javascript:void(0)"><i class="ti-menu"></i></a></div></li>
                                <li class="header-search">
                                    <div class="main-search morphsearch-search">
                                        <div class="input-group">
                                            <span class="input-group-addon search-close"><i class="ti-close"></i></span>
                                            <input type="text" class="form-control" placeholder="Search Products, Employees...">
                                            <span class="input-group-addon search-btn"><i class="ti-search"></i></span>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                            <ul class="nav-right">
                                <li class="header-notification">
                                    <a href="#!" class="waves-effect waves-light">
                                        <i class="ti-bell"></i>
                                        <span class="badge bg-c-red">${unreadNotifications}</span>
                                    </a>
                                    <ul class="show-notification">
                                        <li><h6>Notifications</h6><label class="label label-danger">New</label></li>
                                            <c:forEach var="notification" items="${notifications}">
                                            <li class="waves-effect waves-light">
                                                <div class="media">
                                                    <div class="media-body">
                                                        <h5 class="notification-user"><c:out value="${notification.title}"/></h5>
                                                        <p class="notification-msg"><c:out value="${notification.message}"/></p>
                                                        <span class="notification-time"><fmt:formatDate value="${notification.createdAt}" pattern="dd-MM-yyyy HH:mm"/></span>
                                                    </div>
                                                </div>
                                            </li>
                                        </c:forEach>
                                        <li><a href="notifications.jsp" class="b-b-primary text-primary">View All Notifications</a></li>
                                    </ul>
                                </li>
                                <li class="user-profile header-notification">
                                    <a href="#!" class="waves-effect waves-light">
                                        <img src="assets/images/avatar-4.jpg" class="img-radius" alt="User-Profile-Image">
                                        <span><c:out value="${sessionScope.user.fullName}"/></span>
                                        <i class="ti-angle-down"></i>
                                    </a>
                                    <ul class="show-notification profile-notification">
                                        <li><a href="user-profile.jsp"><i class="ti-user"></i> Profile</a></li>
                                        <li><a href="settings.jsp"><i class="ti-settings"></i> Settings</a></li>
                                        <li><a href="logout.jsp"><i class="ti-layout-sidebar-left"></i> Logout</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div class="pcoded-main-container">
                    <div class="pcoded-wrapper">
                        <nav class="pcoded-navbar">
                            <div class="sidebar_toggle"><a href="#"><i class="icon-close icons"></i></a></div>
                            <div class="pcoded-inner-navbar main-menu">
                                <div class="main-menu-header">
                                    <img class="img-80 img-radius" src="assets/images/avatar-4.jpg" alt="User-Profile-Image">
                                    <div class="user-details">
                                        <span id="more-details"><c:out value="${sessionScope.user.fullName}"/> <i class="fa fa-caret-down"></i></span>
                                    </div>
                                </div>
                                <div class="main-menu-content">
                                    <ul>
                                        <li class="more-details">
                                            <a href="user-profile.jsp"><i class="ti-user"></i>View Profile</a>
                                            <a href="settings.jsp"><i class="ti-settings"></i>Settings</a>
                                            <a href="logout.jsp"><i class="ti-layout-sidebar-left"></i>Logout</a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="pcoded-navigation-label">Store Management</div>
                                <ul class="pcoded-item pcoded-left-item">
                                    <li><a href="index.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-home"></i></span><span class="pcoded-mtext">Dashboard</span></a></li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-package"></i></span><span class="pcoded-mtext">Products</span><span class="pcoded-mcaret"></span></a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="products.jsp">View Products</a></li>
                                            <li><a href="add-product.jsp">Add Product</a></li>
                                            <li><a href="categories.jsp">Manage Categories</a></li>
                                            <li><a href="sizes.jsp">Manage Sizes</a></li>
                                            <li><a href="colors.jsp">Manage Colors</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-archive"></i></span><span class="pcoded-mtext">Inventory</span><span class="pcoded-mcaret"></span></a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="inventory.jsp">View Inventory</a></li>
                                            <li><a href="warehouses.jsp">Manage Warehouses</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-receipt"></i></span><span class="pcoded-mtext">Invoices</span><span class="pcoded-mcaret"></span></a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="invoices.jsp">View Invoices</a></li>
                                            <li><a href="create-invoice.jsp">Create Invoice</a></li>
                                            <li><a href="bank-transactions.jsp">Bank Transactions</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-truck"></i></span><span class="pcoded-mtext">Purchases</span><span class="pcoded-mcaret"></span></a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="purchases.jsp">View Purchases</a></li>
                                            <li><a href="create-purchase.jsp">Create Purchase</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-user"></i></span><span class="pcoded-mtext">Employees</span><span class="pcoded-mcaret"></span></a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="employees.jsp">View Employees</a></li>
                                            <li><a href="add-employee.jsp">Add Employee</a></li>
                                        </ul>
                                    </li>
                                    <li class="active">
                                        <a href="SearchCustomerServlet" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-id-badge"></i></span><span class="pcoded-mtext">Customers</span></a>
                                    </li>
                                    <li><a href="cashflows.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-money"></i></span><span class="pcoded-mtext">Cash Flow</span></a></li>
                                    <li><a href="stores" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-shopping-cart"></i></span><span class="pcoded-mtext">Stores</span></a></li>
                                    <li><a href="notifications" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-bell"></i></span><span class="pcoded-mtext">Notifications</span></a></li>
                                    <li><a href="reports.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-bar-chart"></i></span><span class="pcoded-mtext">Reports</span></a></li>
                                </ul>
                            </div>
                        </nav>
                        <div class="pcoded-content">
                            <div class="page-header">
                                <div class="page-block">
                                    <div class="row align-items-center">
                                        <div class="col-md-8">
                                            <div class="page-header-title">
                                                <h5 class="m-b-10">Store Manager</h5>
                                                <p class="m-b-0">Manage customer information and view their details.</p>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <ul class="breadcrumb-title">
                                                <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                                <li class="breadcrumb-item"><a href="#!">Customers</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="pcoded-inner-content">
                                <div class="main-body">
                                    <div class="page-wrapper">
                                        <div class="page-body">
                                            <div class="row">
                                                <div class="col-xl-12 col-md-12">
                                                    <div class="card table-card">
                                                        <div class="card-header">
                                                            <h5>Customer List</h5>
                                                            <div class="card-header-right">
                                                                <ul class="list-unstyled card-option">
                                                                    <li class="full-card"><i class="fa fa-window-maximize"></i></li>
                                                                    <li><i class="fa fa-minus minimize-card"></i></li>
                                                                    <li><i class="fa fa-refresh reload-card"></i></li>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                        <div class="card-block">
                                                            <c:if test="${not empty message}">
                                                                <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                                                    <c:out value="${message}"/>
                                                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                                        <span aria-hidden="true">×</span>
                                                                    </button>
                                                                </div>
                                                            </c:if>
                                                            <div class="m-b-20">
                                                                <form class="form-inline" action="SearchCustomerServlet" method="GET">
                                                                    <div class="form-group">
                                                                        <input type="text" class="form-control" name="search" placeholder="Search by Name, Gmail, or Phone..." value="${search}">
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <select class="form-control" name="gender">
                                                                            <option value="">All Genders</option>
                                                                            <option value="Male" ${gender == 'Male' ? 'selected' : ''}>Male</option>
                                                                            <option value="Female" ${gender == 'Female' ? 'selected' : ''}>Female</option>
                                                                            <option value="Other" ${gender == 'Other' ? 'selected' : ''}>Other</option>
                                                                        </select>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <select class="form-control" name="membershipLevel">
                                                                            <option value="">All Membership Levels</option>
                                                                            <option value="Bronze" ${membershipLevel == 'Bronze' ? 'selected' : ''}>Bronze</option>
                                                                            <option value="Silver" ${membershipLevel == 'Silver' ? 'selected' : ''}>Silver</option>
                                                                            <option value="Gold" ${membershipLevel == 'Gold' ? 'selected' : ''}>Gold</option>
                                                                            <option value="Platinum" ${membershipLevel == 'Platinum' ? 'selected' : ''}>Platinum</option>
                                                                            <option value="Diamond" ${membershipLevel == 'Diamond' ? 'selected' : ''}>Diamond</option>
                                                                        </select>
                                                                    </div>
                                                                    <button type="submit" class="btn btn-primary waves-effect waves-light">
                                                                        <i class="fa fa-filter"></i> Search
                                                                    </button>
                                                                    <a href="SearchCustomerServlet?reset=true" class="btn btn-primary waves-effect waves-light ml-2">
                                                                        <i class="fa fa-list"></i> All
                                                                    </a>
                                                                </form>
                                                                <!-- Thêm link đến add-customer.jsp -->
                                                                <a href="add-customer.jsp" class="btn btn-primary waves-effect waves-light mt-2">
                                                                    <i class="fa fa-plus"></i> Add Customer
                                                                </a>
                                                            </div>
                                                            <div class="dt-responsive table-responsive">
                                                                <c:choose>
                                                                    <c:when test="${empty customers}">
                                                                        <p>No customers found.</p>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <table class="table table-hover">
                                                                            <thead>
                                                                                <tr>
                                                                                    <th>Full Name</th>
                                                                                    <th>Phone</th>
                                                                                    <th>Total Spent</th>
                                                                                    <th>Created At</th>
                                                                                    <th>Actions</th>
                                                                                </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                                <c:forEach var="customer" items="${customers}">
                                                                                    <tr>
                                                                                        <td><c:out value="${customer.fullName}"/></td>
                                                                                        <td><c:out value="${customer.phone}"/></td>
                                                                                        <td><c:out value="${customer.address}"/></td>
                                                                                        <td><fmt:formatNumber value="${customer.totalSpent}" type="currency" currencyCode="VND"/></td>
                                                                                        <td><fmt:formatDate value="${customer.createdAt}" pattern="dd-MM-yyyy HH:mm"/></td>
                                                                                        <td>
                                                                                            <a href="CustomerDetailsServlet?customerID=<c:out value="${customer.customerID}"/>" class="btn btn-sm btn-info waves-effect waves-light" title="View Details">
                                                                                                <i class="fa fa-eye"></i>
                                                                                            </a>
                                                                                            <a href="SearchCustomerServlet?action=editCustomer&customerID=<c:out value="${customer.customerID}"/>" class="btn btn-sm btn-primary waves-effect waves-light" title="Edit Customer">
                                                                                                <i class="fa fa-edit"></i>
                                                                                            </a>
                                                                                            <a href="SearchCustomerServlet?action=delete&customerID=<c:out value="${customer.customerID}"/>" class="btn btn-sm btn-danger waves-effect waves-light" onclick="return confirm('Are you sure you want to delete this customer?');" title="Delete Customer">
                                                                                                <i class="fa fa-trash"></i>
                                                                                            </a>
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:forEach>
                                                                            </tbody>
                                                                        </table>
                                                                        <jsp:include page="pagination.jsp" />
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Add/Edit Customer Modal -->
                <div class="modal fade" id="customerModal" tabindex="-1" role="dialog" aria-labelledby="customerModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="customerModalLabel">${editCustomer != null ? 'Edit Customer' : 'Add Customer'}</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="window.location.href = 'SearchCustomerServlet'">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <form action="SearchCustomerServlet" method="POST" id="customerForm">
                                <div class="modal-body">
                                    <input type="hidden" name="action" value="save">
                                    <input type="hidden" name="customerID" id="customerId" value="${editCustomer != null ? editCustomer.customerID : ''}">
                                    <div class="form-group row">
                                        <label for="fullName" class="col-sm-3 col-form-label">Full Name</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" id="fullName" name="fullName" value="${editCustomer != null ? editCustomer.fullName : ''}" required maxlength="100">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="phone" class="col-sm-3 col-form-label">Phone</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" id="phone" name="phone" value="${editCustomer != null ? editCustomer.phone : ''}" required pattern="\d{10}">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="gmail" class="col-sm-3 col-form-label">Gmail</label>
                                        <div class="col-sm-9">
                                            <input type="email" class="form-control" id="gmail" name="gmail" value="${editCustomer != null ? editCustomer.gmail : ''}" required>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="gender" class="col-sm-3 col-form-label">Gender</label>
                                        <div class="col-sm-9">
                                            <select class="form-control" id="gender" name="gender" required>
                                                <option value="Male" ${editCustomer != null && editCustomer.gender == 'Male' ? 'selected' : ''}>Male</option>
                                                <option value="Female" ${editCustomer != null && editCustomer.gender == 'Female' ? 'selected' : ''}>Female</option>
                                                <option value="Other" ${editCustomer != null && editCustomer.gender == 'Other' ? 'selected' : ''}>Other</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="birthDate" class="col-sm-3 col-form-label">Birth Date</label>
                                        <div class="col-sm-9">
                                            <input type="date" class="form-control" id="birthDate" name="birthDate" value="${editCustomer != null ? editCustomer.birthDate : ''}" required>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="address" class="col-sm-3 col-form-label">Address</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" id="address" name="address" value="${editCustomer != null ? editCustomer.address : ''}" required maxlength="255">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label for="img" class="col-sm-3 col-form-label">Image URL</label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" id="img" name="img" value="${editCustomer != null ? editCustomer.img : ''}">
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="window.location.href = 'SearchCustomerServlet'">Close</button>
                                    <button type="submit" class="btn btn-primary">Save</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <!-- Customer Details Modal -->
                <jsp:include page="customer-details.jsp" />
            </div>
        </div>

        <script src="assets/js/jquery/jquery.min.js"></script>
        <script src="assets/js/jquery-ui/jquery-ui.min.js"></script>
        <script src="assets/js/popper.js/popper.min.js"></script>
        <script src="assets/js/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/pages/waves/js/waves.min.js"></script>
        <script src="assets/js/jquery-slimscroll/jquery.slimscroll.js"></script>
        <script src="assets/js/modernizr/modernizr.js"></script>
        <script src="assets/js/SmoothScroll.js"></script>
        <script src="assets/js/jquery.mCustomScrollbar.concat.min.js"></script>
        <script src="assets/js/pcoded.min.js"></script>
        <script src="assets/js/vertical-layout.min.js"></script>
        <script src="assets/js/script.js"></script>
        <script>
                                        // Toggle fullscreen for table card
                                        document.querySelectorAll('.full-card').forEach(function (element) {
                                            element.addEventListener('click', function () {
                                                var card = this.closest('.table-card');
                                                card.classList.toggle('fullscreen');
                                            });
                                        });

                                        // Auto-open edit modal if flag is set
            <c:if test="${showEditModal}">
                                        $(document).ready(function () {
                                            $('#customerModal').modal('show');
                                        });
            </c:if>

                                        // Handle Edit button click to open modal
                                        document.querySelectorAll('.edit-customer').forEach(function (element) {
                                            element.addEventListener('click', function (e) {
                                                e.preventDefault();
                                                var customerId = this.getAttribute('data-customer-id');
                                                if (!customerId) {
                                                    alert('Customer ID is missing!');
                                                    return;
                                                }
                                                window.location.href = 'SearchCustomerServlet?action=editCustomer&customerID=' + customerId;
                                            });
                                        });

                                        // Auto-open customer details modal if flag is set
            <c:if test="${showCustomerDetailsModal}">
                                        $(document).ready(function () {
                                            $('#customerDetailsModal').modal('show');
                                        });
            </c:if>
        </script>
        <c:if test="${empty param.customerID and not empty customer}">
            <c:redirect url="SearchCustomerServlet"/>
        </c:if>
    </body>
</html>