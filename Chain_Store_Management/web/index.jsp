<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Store</title>
    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="Store Manager Dashboard for managing products, inventory, invoices, and more." />
    <meta name="keywords" content="store manager, retail, dashboard, inventory, sales" />
    <meta name="author" content="codedthemes" />
    <!-- Favicon icon -->
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/favicon.ico" type="image/x-icon">
    <!-- Google font -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
    <!-- Required Framework -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/bootstrap/css/bootstrap.min.css">
    <!-- Waves.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/pages/waves/css/waves.min.css" type="text/css" media="all">
    <!-- Themify icon -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/icon/themify-icons/themify-icons.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/icon/font-awesome/css/font-awesome.min.css">
    <!-- Scrollbar.css -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/jquery.mCustomScrollbar.css">
    <!-- Amchart export.css -->
    <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
    <!-- Style.css -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <!-- Pre-loader start -->
    <div class="theme-loader">
        <div class="loader-track">
            <div class="preloader-wrapper">
                <div class="spinner-layer spinner-blue">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div>
                    <div class="gap-patch">
                        <div class="circle"></div>
                    </div>
                    <div class="circle-clipper right">
                        <div class="circle"></div>
                    </div>
                </div>
                <div class="spinner-layer spinner-red">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div>
                    <div class="gap-patch">
                        <div class="circle"></div>
                    </div>
                    <div class="circle-clipper right">
                        <div class="circle"></div>
                    </div>
                </div>
                <div class="spinner-layer spinner-yellow">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div>
                    <div class="gap-patch">
                        <div class="circle"></div>
                    </div>
                    <div class="circle-clipper right">
                        <div class="circle"></div>
                    </div>
                </div>
                <div class="spinner-layer spinner-green">
                    <div class="circle-clipper left">
                        <div class="circle"></div>
                    </div>
                    <div class="gap-patch">
                        <div class="circle"></div>
                    </div>
                    <div class="circle-clipper right">
                        <div class="circle"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Pre-loader end -->
    <div id="pcoded" class="pcoded">
        <div class="pcoded-overlay-box"></div>
        <div class="pcoded-container navbar-wrapper">
            <!-- Header -->
            <nav class="navbar header-navbar pcoded-header">
                <div class="navbar-wrapper">
                    <div class="navbar-logo">
                        <a href="${pageContext.request.contextPath}/index.jsp">
                            <img class="img-fluid" src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Store-Logo" />
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
                                        <input type="text" class="form-control" placeholder="Search...">
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
                                        <a href="${pageContext.request.contextPath}/notifications.jsp" class="b-b-primary text-primary">View All Notifications</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="user-profile header-notification">
                                <a href="#!" class="waves-effect waves-light">
                                    <img src="${pageContext.request.contextPath}/assets/images/avatar-4.jpg" class="img-radius" alt="User-Profile-Image">
                                    <span><c:out value="${sessionScope.user.fullName}"/></span>
                                    <i class="ti-angle-down"></i>
                                </a>
                                <ul class="show-notification profile-notification">
                                    <li><a href="${pageContext.request.contextPath}/user-profile.jsp"><i class="ti-user"></i> Profile</a></li>
                                    <li><a href="${pageContext.request.contextPath}/settings.jsp"><i class="ti-settings"></i> Settings</a></li>
                                    <li><a href="${pageContext.request.contextPath}/logout.jsp"><i class="ti-layout-sidebar-left"></i> Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <!-- Sidebar -->
            <div class="pcoded-main-container">
                <div class="pcoded-wrapper">
                    <nav class="pcoded-navbar">
                        <div class="sidebar_toggle"><a href="#"><i class="icon-close icons"></i></a></div>
                        <div class="pcoded-inner-navbar main-menu">
                            <div class="main-menu-header">
                                <img class="img-80 img-radius" src="${pageContext.request.contextPath}/assets/images/avatar-4.jpg" alt="User-Profile-Image">
                                <div class="user-details">
                                    <span id="more-details"><c:out value="${sessionScope.user.fullName}"/> <i class="fa fa-caret-down"></i></span>
                                </div>

                            </div>
                            <div class="main-menu-content">
                                <ul>
                                    <li class="more-details">
                                        <a href="${pageContext.request.contextPath}/user-profile.jsp"><i class="ti-user"></i>View Profile</a>
                                        <a href="${pageContext.request.contextPath}/settings.jsp"><i class="ti-settings"></i>Settings</a>
                                        <a href="${pageContext.request.contextPath}/logout.jsp"><i class="ti-layout-sidebar-left"></i>Logout</a>

                                    </li>
                                </ul>
                            </div>
                            <div class="p-15 p-b-0">
                                <form class="form-material">
                                    <div class="form-group form-primary">
                                        <input type="text" name="search" class="form-control" placeholder="Search...">
                                        <span class="form-bar"></span>
                                        <label class="float-label"><i class="fa fa-search m-r-10"></i>Search</label>
                                    </div>
                                </form>
                            </div>
                            <div class="pcoded-navigation-label">Store Management</div>
                            <ul class="pcoded-item pcoded-left-item">
                                <li class="active">
                                    <a href="${pageContext.request.contextPath}/index.jsp" class="waves-effect waves-dark">
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
                                        <li><a href="${pageContext.request.contextPath}/products.jsp">View Products</a></li>
                                        <li><a href="${pageContext.request.contextPath}/add-product.jsp">Add Product</a></li>
                                        <li><a href="${pageContext.request.contextPath}/categories.jsp">Manage Categories</a></li>
                                    </ul>
                                </li>
                                <li class="pcoded-hasmenu">
                                    <a href="javascript:void(0)" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-archive"></i></span>
                                        <span class="pcoded-mtext">Inventory</span>
                                        <span class="pcoded-mcaret"></span>
                                    </a>
                                    <ul class="pcoded-submenu">
                                        <li><a href="${pageContext.request.contextPath}/inventory.jsp">View Inventory</a></li>
                                        <li><a href="${pageContext.request.contextPath}/warehouses.jsp">Manage Warehouses</a></li>
                                    </ul>
                                </li>
                                <li class="pcoded-hasmenu">
                                    <a href="javascript:void(0)" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-receipt"></i></span>
                                        <span class="pcoded-mtext">Invoices</span>
                                        <span class="pcoded-mcaret"></span>
                                    </a>
                                    <ul class="pcoded-submenu">
                                        <li><a href="${pageContext.request.contextPath}/invoices.jsp">View Invoices</a></li>
                                        <li><a href="${pageContext.request.contextPath}/create-invoice.jsp">Create Invoice</a></li>
                                        <li><a href="${pageContext.request.contextPath}/bank-transactions.jsp">Bank Transactions</a></li>
                                    </ul>
                                </li>
                                <li class="pcoded-hasmenu">
                                    <a href="javascript:void(0)" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-truck"></i></span>
                                        <span class="pcoded-mtext">Purchases</span>
                                        <span class="pcoded-mcaret"></span>
                                    </a>
                                    <ul class="pcoded-submenu">
                                        <li><a href="${pageContext.request.contextPath}/Purchase">View Purchases</a></li>
                                        <li><a href="${pageContext.request.contextPath}/Purchase?action=create">Create Purchase</a></li>
                                    </ul>
                                </li>
                                <li class="pcoded-hasmenu">
                                    <a href="javascript:void(0)" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-user"></i></span>
                                        <span class="pcoded-mtext">Employees</span>
                                        <span class="pcoded-mcaret"></span>
                                    </a>
                                    <ul class="pcoded-submenu">
                                        <li><a href="${pageContext.request.contextPath}/employees.jsp">View Employees</a></li>
                                        <li><a href="${pageContext.request.contextPath}/add-employee.jsp">Add Employee</a></li>
                                    </ul>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/CustomerListServlet" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-id-badge"></i></span>
                                        <span class="pcoded-mtext">Customers</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/cashflows.jsp" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-money"></i></span>
                                        <span class="pcoded-mtext">Cash Flow</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/stores.jsp" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-shopping-cart"></i></span>
                                        <span class="pcoded-mtext">Stores</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/notifications.jsp" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-bell"></i></span>
                                        <span class="pcoded-mtext">Notifications</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/reports.jsp" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-bar-chart"></i></span>
                                        <span class="pcoded-mtext">Reports</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </nav>
                    <!-- Main Content -->
                    <div class="pcoded-content">
                        <div class="page-header">
                            <div class="page-block">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <div class="page-header-title">
                                            <h5 class="m-b-10">Store Manager Dashboard</h5>
                                            <p class="m-b-0">Welcome, <c:out value="${sessionScope.user.fullName}"/>!</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item">
                                                <a href="${pageContext.request.contextPath}/index.jsp"><i class="fa fa-home"></i></a>
                                            </li>
                                            <li class="breadcrumb-item"><a href="#!">Dashboard</a></li>
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
                                            <!-- Key Metrics -->
                                            <div class="col-xl-3 col-md-6">
                                                <div class="card">
                                                    <div class="card-block">
                                                        <div class="row align-items-center">
                                                            <div class="col-8">
                                                                <h4 class="text-c-purple"><fmt:formatNumber value="${totalSales}" type="currency"/></h4>
                                                                <h6 class="text-muted m-b-0">Total Sales (Today)</h6>
                                                            </div>
                                                            <div class="col-4 text-right">
                                                                <i class="fa fa-money f-28"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer bg-c-purple">
                                                        <div class="row align-items-center">
                                                            <div class="col-9">
                                                                <p class="text-white m-b-0"><a href="${pageContext.request.contextPath}/reports.jsp" class="text-white">View Report</a></p>
                                                            </div>
                                                            <div class="col-3 text-right">
                                                                <i class="fa fa-arrow-right text-white f-16"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xl-3 col-md-6">
                                                <div class="card">
                                                    <div class="card-block">
                                                        <div class="row align-items-center">
                                                            <div class="col-8">
                                                                <h4 class="text-c-green">${lowInventoryCount}</h4>
                                                                <h6 class="text-muted m-b-0">Low Inventory Items</h6>
                                                            </div>
                                                            <div class="col-4 text-right">
                                                                <i class="fa fa-exclamation-triangle f-28"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer bg-c-green">
                                                        <div class="row align-items-center">
                                                            <div class="col-9">
                                                                <p class="text-white m-b-0"><a href="${pageContext.request.contextPath}/inventory.jsp" class="text-white">View Inventory</a></p>
                                                            </div>
                                                            <div class="col-3 text-right">
                                                                <i class="fa fa-arrow-right text-white f-16"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xl-3 col-md-6">
                                                <div class="card">
                                                    <div class="card-block">
                                                        <div class="row align-items-center">
                                                            <div class="col-8">
                                                                <h4 class="text-c-blue">${pendingInvoices}</h4>
                                                                <h6 class="text-muted m-b-0">Pending Invoices</h6>
                                                            </div>
                                                            <div class="col-4 text-right">
                                                                <i class="fa fa-receipt f-28"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer bg-c-blue">
                                                        <div class="row align-items-center">
                                                            <div class="col-9">
                                                                <p class="text-white m-b-0"><a href="${pageContext.request.contextPath}/invoices.jsp" class="text-white">View Invoices</a></p>
                                                            </div>
                                                            <div class="col-3 text-right">
                                                                <i class="fa fa-arrow-right text-white f-16"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xl-3 col-md-6">
                                                <div class="card">
                                                    <div class="card-block">
                                                        <div class="row align-items-center">
                                                            <div class="col-8">
                                                                <h4 class="text-c-red">${activeEmployees}</h4>
                                                                <h6 class="text-muted m-b-0">Active Employees</h6>
                                                            </div>
                                                            <div class="col-4 text-right">
                                                                <i class="fa fa-users f-28"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer bg-c-red">
                                                        <div class="row align-items-center">
                                                            <div class="col-9">
                                                                <p class="text-white m-b-0"><a href="${pageContext.request.contextPath}/employees.jsp" class="text-white">View Employees</a></p>
                                                            </div>
                                                            <div class="col-3 text-right">
                                                                <i class="fa fa-arrow-right text-white f-16"></i>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Sales Analytics Chart -->
                                            <div class="col-xl-8 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Sales Analytics</h5>
                                                        <div class="card-header-right">
                                                            <ul class="list-unstyled card-option">
                                                                <li><i class="fa fa-window-maximize full-card"></i></li>
                                                                <li><i class="fa fa-minus minimize-card"></i></li>
                                                                <li><i class="fa fa-refresh reload-card"></i></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="card-block">
                                                        <div id="sales-analytics" style="height: 400px;"></div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Low Inventory Alerts -->
                                            <div class="col-xl-4 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Low Inventory Alerts</h5>
                                                        <div class="card-header-right">
                                                            <ul class="list-unstyled card-option">
                                                                <li><i class="fa fa-window-maximize full-card"></i></li>
                                                                <li><i class="fa fa-minus minimize-card"></i></li>
                                                                <li><i class="fa fa-refresh reload-card"></i></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="card-block">
                                                        <div class="table-responsive">
                                                            <table class="table table-hover">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Product</th>
                                                                        <th>Warehouse</th>
                                                                        <th>Quantity</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="item" items="${lowInventoryItems}">
                                                                        <tr>
                                                                            <td><c:out value="${item.productName}"/></td>
                                                                            <td><c:out value="${item.warehouseName}"/></td>
                                                                            <td><c:out value="${item.quantity}"/></td>
                                                                        </tr>

                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                            <div class="text-right m-r-20">
                                                                <a href="${pageContext.request.contextPath}/inventory.jsp" class="b-b-primary text-primary">View All Inventory</a>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Recent Invoices -->
                                            <div class="col-xl-12 col-md-12">
                                                <div class="card table-card">
                                                    <div class="card-header">
                                                        <h5>Recent Invoices</h5>
                                                        <div class="card-header-right">
                                                            <ul class="list-unstyled card-option">
                                                                <li><i class="fa fa-window-maximize full-card"></i></li>
                                                                <li><i class="fa fa-minus minimize-card"></i></li>
                                                                <li><i class="fa fa-refresh reload-card"></i></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="card-block">
                                                        <div class="table-responsive">
                                                            <table class="table table-hover">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Invoice ID</th>
                                                                        <th>Date</th>
                                                                        <th>Customer</th>
                                                                        <th>Total Amount</th>
                                                                        <th>Status</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="invoice" items="${recentInvoices}">
                                                                        <tr>
                                                                            <td><c:out value="${invoice.invoiceID}"/></td>
                                                                            <td><fmt:formatDate value="${invoice.invoiceDate}" pattern="dd-MM-yyyy"/></td>
                                                                            <td><c:out value="${invoice.customerName}"/></td>
                                                                            <td><fmt:formatNumber value="${invoice.totalAmount}" type="currency"/></td>
                                                                            <td><label class="label label-<c:out value="${invoice.status == 'Pending' ? 'warning' : 'success'}"/>"><c:out value="${invoice.status}"/></label></td>
                                                                        </tr>

                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                            <div class="text-right m-r-20">
                                                                <a href="${pageContext.request.contextPath}/invoices.jsp" class="b-b-primary text-primary">View All Invoices</a>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Recent Purchases -->
                                            <div class="col-xl-12 col-md-12">
                                                <div class="card table-card">
                                                    <div class="card-header">
                                                        <h5>Recent Purchases</h5>
                                                        <div class="card-header-right">
                                                            <ul class="list-unstyled card-option">
                                                                <li><i class="fa fa-window-maximize full-card"></i></li>
                                                                <li><i class="fa fa-minus minimize-card"></i></li>
                                                                <li><i class="fa fa-refresh reload-card"></i></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="card-block">
                                                        <div class="table-responsive">
                                                            <table class="table table-hover">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Purchase ID</th>
                                                                        <th>Date</th>
                                                                        <th>Supplier</th>
                                                                        <th>Total Amount</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="purchase" items="${recentPurchases}">
                                                                        <tr>
                                                                            <td><c:out value="${purchase.purchaseID}"/></td>
                                                                            <td><fmt:formatDate value="${purchase.purchaseDate}" pattern="dd-MM-yyyy"/></td>
                                                                            <td><c:out value="${purchase.supplierName}"/></td>
                                                                            <td><fmt:formatNumber value="${purchase.totalAmount}" type="currency"/></td>
                                                                        </tr>

                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                            <div class="text-right m-r-20">
                                                                <a href="${pageContext.request.contextPath}/purchases.jsp" class="b-b-primary text-primary">View All Purchases</a>

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
        </div>
        <!-- Required Jquery -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-ui/jquery-ui.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/popper.js/popper.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/bootstrap/js/bootstrap.min.js"></script>
        <!-- Waves js -->
        <script src="${pageContext.request.contextPath}/assets/pages/waves/js/waves.min.js"></script>
        <!-- JQuery slimscroll js -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-slimscroll/jquery.slimscroll.js"></script>
        <!-- Modernizr js -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/modernizr/modernizr.js"></script>
        <!-- Slimscroll js -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/SmoothScroll.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/jquery.mCustomScrollbar.concat.min.js"></script>
        <!-- Chart js -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/chart.js/Chart.js"></script>
        <!-- Amchart js -->
        <script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
        <script src="${pageContext.request.contextPath}/assets/pages/widget/amchart/gauge.js"></script>
        <script src="${pageContext.request.contextPath}/assets/pages/widget/amchart/serial.js"></script>
        <script src="${pageContext.request.contextPath}/assets/pages/widget/amchart/light.js"></script>
        <script src="${pageContext.request.contextPath}/assets/pages/widget/amchart/pie.min.js"></script>
        <script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
        <!-- Menu js -->
        <script src="${pageContext.request.contextPath}/assets/js/pcoded.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/vertical-layout.min.js"></script>
        <!-- Custom js -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/pages/dashboard/custom-dashboard.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/script.js"></script>
        <!-- Chart.js for Sales Analytics -->
        <script>
            $(document).ready(function () {
                var ctx = document.getElementById('sales-analytics').getContext('2d');
                
                // Lấy dữ liệu từ server bằng JSTL
                var labels = [];
                var salesData = [];
                
                <c:if test="${empty dailySales}">
                    // Dữ liệu mặc định nếu không có doanh thu
                    labels = ['2025-05-22', '2025-05-23', '2025-05-24', '2025-05-25', '2025-05-26', '2025-05-27', '2025-05-28'];
                    salesData = [0, 0, 0, 0, 0, 0, 0];
                </c:if>
                <c:forEach var="sale" items="${dailySales}">
                    labels.push("${sale.date}");
                    salesData.push(${sale.amount});
                </c:forEach>

                var salesChart = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: 'Daily Sales',

                            data: [12000, 15000, 10000, 18000, 20000, 17000, 22000],

                            borderColor: '#7c4dff',
                            backgroundColor: 'rgba(124, 77, 255, 0.2)',
                            fill: true,
                            tension: 0.4
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true,
                                title: {
                                    display: true,
                                    text: 'Sales Amount (₫)'
                                }
                            },
                            x: {
                                title: {
                                    display: true,
                                    text: 'Date'
                                }
                            }
                        },
                        plugins: {
                            legend: {
                                display: true
                            }
                        }
                    }
                });
            });
        </script>
    </body>
</html>