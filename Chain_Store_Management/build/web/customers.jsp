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
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/pages/waves/css/waves.min.css" type="text/css" media="all">
        <link rel="stylesheet" type="text/css" href="assets/icon/themify-icons/themify-icons.css">
        <link rel="stylesheet" type="text/css" href="assets/icon/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="assets/css/jquery.mCustomScrollbar.css">
        <link rel="stylesheet" type="text/css" href="assets/css/style.css">
        <style>
            .form-group { margin-right: 15px; }
            .btn-info { background-color: #17a2b8 !important; border-color: #17a2b8 !important; color: #fff !important; }
            .btn-info:hover { background-color: #138496 !important; border-color: #138496 !important; }
            .btn-primary { background-color: #007bff !important; border-color: #007bff !important; color: #fff !important; }
            .btn-primary:hover { background-color: #0056b3 !important; border-color: #0056b3 !important; }
            .btn-danger { background-color: #dc3545 !important; border-color: #dc3545 !important; color: #fff !important; }
            .btn-danger:hover { background-color: #c82333 !important; border-color: #c82333 !important; }
            .btn-sm { padding: 0.25rem 0.5rem; font-size: 0.875rem; line-height: 1.5; border-radius: 0.2rem; }
            .pagination { margin-top: 20px; }
            .pagination .page-link { color: #007bff; }
            .pagination .page-link:hover { background-color: #0056b3; color: #fff; }
            .pagination .page-item.active .page-link { background-color: #007bff; border-color: #007bff; color: #fff; }
            .card { overflow: auto; max-height: none !important; }
            .full-card { cursor: pointer; }
            .table-card.fullscreen { position: fixed !important; top: 0 !important; left: 0 !important; width: 100% !important; height: 100% !important; z-index: 1050 !important; background: #fff !important; margin: 0 !important; padding: 15px !important; overflow: auto !important; }
            .table-card.fullscreen .card-block { max-height: 90vh !important; overflow-y: auto !important; }
            .modal-content { border-radius: 0.3rem; }
            .modal-header { background-color: #007bff; color: #fff; }
            .modal-title { font-weight: 500; }
            .modal-footer { justify-content: flex-end; }
            .detail-label { font-weight: bold; margin-bottom: 5px; }
            .detail-value { margin-bottom: 15px; }
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
                            <a href="index.jsp">
                                <img class="img-fluid" src="assets/images/logo.png" alt="Store-Logo" />
                            </a>
                            <a class="mobile-menu waves-effect waves-light" id="mobile-collapse" href="#!">
                                <i class="ti-menu"></i>
                            </a>
                        </div>
                        <div class="navbar-container container-fluid">
                            <ul class="nav-left">
                                <li>
                                    <div class="sidebar_toggle"><a href="javascript:void(0)"><i class="ti-menu"></i></a></div>
                                </li>
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
                                        <li>
                                            <h6>Notifications</h6>
                                            <label class="label label-danger">New</label>
                                        </li>
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
                                        <li>
                                            <a href="notifications.jsp" class="b-b-primary text-primary">View All Notifications</a>
                                        </li>
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
                                    <li>
                                        <a href="index.jsp" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-home"></i></span>
                                            <span class="pcoded-mtext">Dashboard</span>
                                        </a>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-package"></i></span>
                                            <span class="pcoded-mtext">Products</span>
                                            <span class="pcoded-mcaret"></span>
                                        </a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="products.jsp">View Products</a></li>
                                            <li><a href="add-product.jsp">Add Product</a></li>
                                            <li><a href="categories.jsp">Manage Categories</a></li>
                                            <li><a href="sizes.jsp">Manage Sizes</a></li>
                                            <li><a href="colors.jsp">Manage Colors</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-archive"></i></span>
                                            <span class="pcoded-mtext">Inventory</span>
                                            <span class="pcoded-mcaret"></span>
                                        </a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="inventory.jsp">View Inventory</a></li>
                                            <li><a href="warehouses.jsp">Manage Warehouses</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-receipt"></i></span>
                                            <span class="pcoded-mtext">Invoices</span>
                                            <span class="pcoded-mcaret"></span>
                                        </a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="invoices.jsp">View Invoices</a></li>
                                            <li><a href="create-invoice.jsp">Create Invoice</a></li>
                                            <li><a href="bank-transactions.jsp">Bank Transactions</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-truck"></i></span>
                                            <span class="pcoded-mtext">Purchases</span>
                                            <span class="pcoded-mcaret"></span>
                                        </a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="purchases.jsp">View Purchases</a></li>
                                            <li><a href="create-purchase.jsp">Create Purchase</a></li>
                                        </ul>
                                    </li>
                                    <li class="pcoded-hasmenu">
                                        <a href="javascript:void(0)" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-user"></i></span>
                                            <span class="pcoded-mtext">Employees</span>
                                            <span class="pcoded-mcaret"></span>
                                        </a>
                                        <ul class="pcoded-submenu">
                                            <li><a href="employees.jsp">View Employees</a></li>
                                            <li><a href="add-employee.jsp">Add Employee</a></li>
                                        </ul>
                                    </li>
                                    <li class="active">
                                        <a href="SearchCustomerServlet" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-id-badge"></i></span>
                                            <span class="pcoded-mtext">Customers</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="cashflows.jsp" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-money"></i></span>
                                            <span class="pcoded-mtext">Cash Flow</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="stores.jsp" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-shopping-cart"></i></span>
                                            <span class="pcoded-mtext">Stores</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="notifications.jsp" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-bell"></i></span>
                                            <span class="pcoded-mtext">Notifications</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="reports.jsp" class="waves-effect waves-dark">
                                            <span class="pcoded-micon"><i class="ti-bar-chart"></i></span>
                                            <span class="pcoded-mtext">Reports</span>
                                        </a>
                                    </li>
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
                                                <li class="breadcrumb-item">
                                                    <a href="index.jsp"><i class="fa fa-home"></i></a>
                                                </li>
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
                                                                        <input type="text" class="form-control" name="search" placeholder="Search by Name, Email, or Phone..." value="${search}">
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
                                                            </div>
                                                            <div class="dt-responsive table-responsive">
                                                                <div class="m-b-20">
                                                                    <button class="btn btn-primary waves-effect waves-light" data-toggle="modal" data-target="#customerModal" onclick="clearForm()">
                                                                        <i class="fa fa-plus"></i> Add New Customer
                                                                    </button>
                                                                </div>
                                                                <c:choose>
                                                                    <c:when test="${empty customers}">
                                                                        <p>No customers found.</p>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <table class="table table-hover">
                                                                            <thead>
                                                                                <tr>
                                                                                    <th>Customer ID</th>
                                                                                    <th>Full Name</th>
                                                                                    <th>Phone</th>
                                                                                    <th>Email</th>
                                                                                    <th>Gender</th>
                                                                                    <th>Birth Date</th>
                                                                                    <th>Address</th>
                                                                                    <th>Total Spent</th>
                                                                                    <th>Created At</th>
                                                                                    <th>Actions</th>
                                                                                </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                                <c:forEach var="customer" items="${customers}">
                                                                                    <tr>
                                                                                        <td><code><c:out value="${customer.customerID}"/></code></td>
                                                                                        <td><c:out value="${customer.fullName}"/></td>
                                                                                        <td><c:out value="${customer.phone.substring(0, customer.phone.length() - 4)}****"/></td>
                                                                                        <td><c:out value="${customer.email}"/></td>
                                                                                        <td><c:out value="${customer.gender}"/></td>
                                                                                        <td><fmt:formatDate value="${customer.birthDate}" pattern="dd-MM-yyyy"/></td>
                                                                                        <td><c:out value="${customer.address}"/></td>
                                                                                        <td><fmt:formatNumber value="${customer.totalSpent}" type="currency" currencyCode="VND"/></td>
                                                                                        <td><fmt:formatDate value="${customer.createdAt}" pattern="dd-MM-yyyy HH:mm"/></td>
                                                                                        <td>
                                                                                            <button class="btn btn-sm btn-info waves-effect waves-light" data-toggle="modal" data-target="#customerDetailsModal" onclick="populateDetails('<c:out value="${customer.customerID}"/>', '<c:out value="${customer.fullName}"/>', '<c:out value="${customer.phone}"/>', '<c:out value="${customer.email}"/>', '<c:out value="${customer.gender}"/>', '<fmt:formatDate value="${customer.birthDate}" pattern="dd-MM-yyyy"/>', '<c:out value="${customer.address}"/>', ${customer.totalSpent}, '<fmt:formatDate value="${customer.createdAt}" pattern="dd-MM-yyyy HH:mm"/>')" title="View Details">
                                                                                                <i class="fa fa-eye"></i>
                                                                                            </button>
                                                                                            <button class="btn btn-sm btn-info waves-effect waves-light" data-toggle="modal" data-target="#customerModal" onclick="populateForm('<c:out value="${customer.customerID}"/>', '<c:out value="${customer.fullName}"/>', '<c:out value="${customer.phone}"/>', '<c:out value="${customer.email}"/>', '<c:out value="${customer.gender}"/>', '<fmt:formatDate value="${customer.birthDate}" pattern="yyyy-MM-dd"/>', '<c:out value="${customer.address}"/>')" title="Edit">
                                                                                                <i class="fa fa-edit"></i>
                                                                                            </button>
                                                                                            <a href="${pageContext.request.contextPath}/SearchCustomerServlet?action=delete&customerID=${customer.customerID}" class="btn btn-sm btn-danger waves-effect waves-light" title="Delete" onclick="return confirm('Are you sure you want to delete this customer?');">
                                                                                                <i class="fa fa-trash"></i>
                                                                                            </a>
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:forEach>
                                                                            </tbody>
                                                                        </table>
                                                                        <c:if test="${totalPages > 1}">
                                                                            <nav aria-label="Page navigation">
                                                                                <ul class="pagination justify-content-center">
                                                                                    <c:if test="${currentPage > 1}">
                                                                                        <li class="page-item">
                                                                                            <a class="page-link" href="SearchCustomerServlet?page=${currentPage - 1}&search=${search}&gender=${gender}&membershipLevel=${membershipLevel}">Previous</a>
                                                                                        </li>
                                                                                    </c:if>
                                                                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                                                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                                                            <a class="page-link" href="SearchCustomerServlet?page=${i}&search=${search}&gender=${gender}&membershipLevel=${membershipLevel}">${i}</a>
                                                                                        </li>
                                                                                    </c:forEach>
                                                                                    <c:if test="${currentPage < totalPages}">
                                                                                        <li class="page-item">
                                                                                            <a class="page-link" href="SearchCustomerServlet?page=${currentPage + 1}&search=${search}&gender=${gender}&membershipLevel=${membershipLevel}">Next</a>
                                                                                        </li>
                                                                                    </c:if>
                                                                                </ul>
                                                                            </nav>
                                                                        </c:if>
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
            </div>

            <!-- Add/Edit Customer Modal -->
            <div class="modal fade" id="customerModal" tabindex="-1" role="dialog" aria-labelledby="customerModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="customerModalLabel">Add Customer</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="SearchCustomerServlet" method="post" id="customerForm">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" id="customerId" name="customerID">
                                <div class="form-group row">
                                    <label for="fullName" class="col-sm-3 col-form-label">Full Name</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" id="fullName" name="fullName" placeholder="e.g., John Doe" required>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="phone" class="col-sm-3 col-form-label">Phone</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" id="phone" name="phone" placeholder="e.g., (123) 456-7890" required>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="email" class="col-sm-3 col-form-label">Email</label>
                                    <div class="col-sm-9">
                                        <input type="email" class="form-control" id="email" name="email" placeholder="e.g., john.doe@example.com" required>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="gender" class="col-sm-3 col-form-label">Gender</label>
                                    <div class="col-sm-9">
                                        <select class="form-control" id="gender" name="gender" required>
                                            <option value="Male">Male</option>
                                            <option value="Female">Female</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="birthDate" class="col-sm-3 col-form-label">Birth Date</label>
                                    <div class="col-sm-9">
                                        <input type="date" class="form-control" id="birthDate" name="birthDate" required>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="address" class="col-sm-3 col-form-label">Address</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" id="address" name="address" placeholder="e.g., 123 Main St, City" required>
                                    </div>
                                </div>
                                <div class="form-group row" id="passwordGroup">
                                    <label for="password" class="col-sm-3 col-form-label">Password</label>
                                    <div class="col-sm-9">
                                        <input type="password" class="form-control" id="password" name="password" placeholder="Enter password for new customer">
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary" form="customerForm">Save Customer</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- View Details Customer Modal -->
            <div class="modal fade" id="customerDetailsModal" tabindex="-1" role="dialog" aria-labelledby="customerDetailsModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="customerDetailsModalLabel">Customer Details</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Customer ID</label>
                                <div class="col-sm-9 detail-value" id="detailCustomerId"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Full Name</label>
                                <div class="col-sm-9 detail-value" id="detailFullName"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Phone</label>
                                <div class="col-sm-9 detail-value" id="detailPhone"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Email</label>
                                <div class="col-sm-9 detail-value" id="detailEmail"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Gender</label>
                                <div class="col-sm-9 detail-value" id="detailGender"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Birth Date</label>
                                <div class="col-sm-9 detail-value" id="detailBirthDate"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Address</label>
                                <div class="col-sm-9 detail-value" id="detailAddress"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Total Spent</label>
                                <div class="col-sm-9 detail-value" id="detailTotalSpent"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Created At</label>
                                <div class="col-sm-9 detail-value" id="detailCreatedAt"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <script type="text/javascript" src="assets/js/jquery/jquery.min.js"></script>
            <script type="text/javascript" src="assets/js/jquery-ui/jquery-ui.min.js"></script>
            <script type="text/javascript" src="assets/js/popper.js/popper.min.js"></script>
            <script type="text/javascript" src="assets/js/bootstrap/js/bootstrap.min.js"></script>
            <script src="assets/pages/waves/js/waves.min.js"></script>
            <script type="text/javascript" src="assets/js/jquery-slimscroll/jquery.slimscroll.js"></script>
            <script type="text/javascript" src="assets/js/modernizr/modernizr.js"></script>
            <script type="text/javascript" src="assets/js/SmoothScroll.js"></script>
            <script src="assets/js/jquery.mCustomScrollbar.concat.min.js"></script>
            <script src="assets/js/pcoded.min.js"></script>
            <script src="assets/js/vertical-layout.min.js"></script>
            <script type="text/javascript" src="assets/js/script.js"></script>
            <script>
                function populateForm(id, fullName, phone, email, gender, birthDate, address) {
                    document.getElementById('customerId').value = id;
                    document.getElementById('fullName').value = fullName;
                    document.getElementById('phone').value = phone;
                    document.getElementById('email').value = email;
                    document.getElementById('gender').value = gender;
                    document.getElementById('birthDate').value = birthDate;
                    document.getElementById('address').value = address;
                    document.getElementById('customerModalLabel').textContent = 'Edit Customer';
                    document.getElementById('passwordGroup').style.display = 'none';
                }
                function clearForm() {
                    document.getElementById('customerId').value = '';
                    document.getElementById('fullName').value = '';
                    document.getElementById('phone').value = '';
                    document.getElementById('email').value = '';
                    document.getElementById('gender').value = 'Male';
                    document.getElementById('birthDate').value = '';
                    document.getElementById('address').value = '';
                    document.getElementById('password').value = '';
                    document.getElementById('customerModalLabel').textContent = 'Add Customer';
                    document.getElementById('passwordGroup').style.display = 'block';
                }
                function populateDetails(id, fullName, phone, email, gender, birthDate, address, totalSpent, createdAt) {
                    document.getElementById('detailCustomerId').textContent = id;
                    document.getElementById('detailFullName').textContent = fullName;
                    document.getElementById('detailPhone').textContent = phone;
                    document.getElementById('detailEmail').textContent = email;
                    document.getElementById('detailGender').textContent = gender;
                    document.getElementById('detailBirthDate').textContent = birthDate;
                    document.getElementById('detailAddress').textContent = address;
                    document.getElementById('detailTotalSpent').textContent = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(totalSpent);
                    document.getElementById('detailCreatedAt').textContent = createdAt;
                }
                document.querySelectorAll('.full-card').forEach(function (element) {
                    element.addEventListener('click', function () {
                        var card = this.closest('.table-card');
                        if (card.classList.contains('fullscreen')) {
                            card.classList.remove('fullscreen');
                        } else {
                            card.classList.add('fullscreen');
                        }
                    });
                });
            </script>
        </body>
    </html>