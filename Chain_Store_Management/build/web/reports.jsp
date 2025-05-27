<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Reports - Store Manager</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="icon" href="assets/images/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/pages/waves/css/waves.min.css" type="text/css" media="all">
    <link rel="stylesheet" type="text/css" href="assets/icon/themify-icons/themify-icons.css">
    <link rel="stylesheet" type="text/css" href="assets/icon/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="assets/css/jquery.mCustomScrollbar.css">
    <link rel="stylesheet" type="text/css" href="assets/css/style.css">
</head>
<body>
    <div class="theme-loader">
        <div class="loader-track">
            <div class="preloader-wrapper">
                <div class="spinner-layer spinner-blue"><div class="circle-clipper left"><div class="circle"></div></div><div class="gap-patch"><div class="circle"></div></div><div class="circle-clipper right"><div class="circle"></div></div></div>
                <div class="spinner-layer spinner-red"><div class="circle-clipper left"><div class="circle"></div></div><div class="gap-patch"><div class="circle"></div></div><div class="circle-clipper right"><div class="circle"></div></div></div>
                <div class="spinner-layer spinner-yellow"><div class="circle-clipper left"><div class="circle"></div></div><div class="gap-patch"><div class="circle"></div></div><div class="circle-clipper right"><div class="circle"></div></div></div>
                <div class="spinner-layer spinner-green"><div class="circle-clipper left"><div class="circle"></div></div><div class="gap-patch"><div class="circle"></div></div><div class="circle-clipper right"><div class="circle"></div></div></div>
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
                                    <span class="badge bg-c-red">3</span>
                                </a>
                                <ul class="show-notification">
                                    <li><h6>Notifications</h6><label class="label label-danger">New</label></li>
                                    <li class="waves-effect waves-light">
                                        <div class="media">
                                            <div class="media-body">
                                                <h5 class="notification-user">Low Stock Alert</h5>
                                                <p class="notification-msg">Product XYZ is below minimum stock level.</p>
                                                <span class="notification-time">2025-05-22 09:00</span>
                                            </div>
                                        </div>
                                    </li>
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
                            <div class="p-15 p-b-0">
                                <form class="form-material">
                                    <div class="form-group form-primary">
                                        <input type="text" name="search" class="form-control" placeholder="Search Products, Employees..." required="">
                                        <span class="form-bar"></span>
                                        <label class="float-label"><i class="fa fa-search m-r-10"></i>Search</label>
                                    </div>
                                </form>
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
                                <li>
                                    <a href="customers.jsp" class="waves-effect waves-dark">
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
                                <li class="active">
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
                                            <h5 class="m-b-10">Reports & Analytics</h5>
                                            <p class="m-b-0">View sales, inventory, customer, and cash flow reports.</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                            <li class="breadcrumb-item"><a href="reports.jsp">Reports</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="pcoded-inner-content">
                            <div class="main-body">
                                <div class="page-wrapper">
                                    <div class="page-body">
                                        <!-- Date Range Filter -->
                                        <!-- Date Range Filter -->
<div class="row">
    <div class="col-sm-12">
        <div class="card">
            <div class="card-header">
                <h5>Filter Reports</h5>
            </div>
            <div class="card-block">
                <form action="reports" method="get" class="form-material">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group form-primary">
                                <label class="float-label">Start Date</label>
                                <c:set var="startDateValue">
                                    <c:choose>
                                        <c:when test="${not empty param.startDate}">
                                            <fmt:parseDate value="${param.startDate}" pattern="yyyy-MM-dd" var="parsedStartDate"/>
                                            <fmt:formatDate value="${parsedStartDate}" pattern="yyyy-MM-dd"/>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Nếu không có giá trị, để rỗng -->
                                        </c:otherwise>
                                    </c:choose>
                                </c:set>
                                <input type="date" name="startDate" class="form-control" value="${startDateValue}">
                                <span class="form-bar"></span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group form-primary">
                                <label class="float-label">End Date</label>
                                <c:set var="endDateValue">
                                    <c:choose>
                                        <c:when test="${not empty param.endDate}">
                                            <fmt:parseDate value="${param.endDate}" pattern="yyyy-MM-dd" var="parsedEndDate"/>
                                            <fmt:formatDate value="${parsedEndDate}" pattern="yyyy-MM-dd"/>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Nếu không có giá trị, để rỗng -->
                                        </c:otherwise>
                                    </c:choose>
                                </c:set>
                                <input type="date" name="endDate" class="form-control" value="${endDateValue}">
                                <span class="form-bar"></span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <button type="submit" class="btn btn-primary mt-4">Apply Filter</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
                                            <!-- Sales Report -->
                                            <div class="col-xl-8 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Sales Report</h5>
                                                        <div class="card-header-right">
                                                            <ul class="list-unstyled card-option">
                                                                <li><i class="fa fa-window-maximize full-card"></i></li>
                                                                <li><i class="fa fa-minus minimize-card"></i></li>
                                                                <li><i class="fa fa-refresh reload-card"></i></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="card-block">
                                                        <canvas id="sales-report-chart" style="height: 400px;"></canvas>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Sales by Store -->
                                            <div class="col-xl-4 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Sales by Store</h5>
                                                    </div>
                                                    <div class="card-block">
                                                        <div class="table-responsive">
                                                            <table class="table table-hover">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Store</th>
                                                                        <th>Total Sales</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="storeSale" items="${storeSales}">
                                                                        <tr>
                                                                            <td><c:out value="${storeSale.storeName}"/></td>
                                                                            <td><fmt:formatNumber value="${storeSale.totalSales}" type="currency"/></td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                    <c:if test="${empty storeSales}">
                                                                        <tr><td colspan="2" class="text-center">No data available.</td></tr>
                                                                    </c:if>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Inventory Report -->
                                            <div class="col-xl-12 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Inventory Report</h5>
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
                                                                        <th>Status</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="inventory" items="${inventoryReport}">
                                                                        <tr>
                                                                            <td><c:out value="${inventory.productName}"/></td>
                                                                            <td><c:out value="${inventory.warehouseName}"/></td>
                                                                            <td><c:out value="${inventory.quantity}"/></td>
                                                                            <td>
                                                                                <c:choose>
                                                                                    <c:when test="${inventory.quantity == 0}">
                                                                                        <label class="label label-danger">Out of Stock</label>
                                                                                    </c:when>
                                                                                    <c:when test="${inventory.quantity <= 10}">
                                                                                        <label class="label label-warning">Low Stock</label>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <label class="label label-success">In Stock</label>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                    <c:if test="${empty inventoryReport}">
                                                                        <tr><td colspan="4" class="text-center">No data available.</td></tr>
                                                                    </c:if>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Customer Report -->
                                            <div class="col-xl-6 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Customer Report</h5>
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
                                                                        <th>Customer</th>
                                                                        <th>Total Purchases</th>
                                                                        <th>Last Purchase</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="customer" items="${customerReport}">
                                                                        <tr>
                                                                            <td><c:out value="${customer.customerName}"/></td>
                                                                            <td><fmt:formatNumber value="${customer.totalPurchases}" type="currency"/></td>
                                                                            <td><fmt:formatDate value="${customer.lastPurchase}" pattern="yyyy-MM-dd"/></td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                    <c:if test="${empty customerReport}">
                                                                        <tr><td colspan="3" class="text-center">No data available.</td></tr>
                                                                    </c:if>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Cash Flow Report -->
                                            <div class="col-xl-6 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Cash Flow Report</h5>
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
                                                                        <th>Date</th>
                                                                        <th>Type</th>
                                                                        <th>Amount</th>
                                                                        <th>Description</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="cashFlow" items="${cashFlowReport}">
                                                                        <tr>
                                                                            <td><fmt:formatDate value="${cashFlow.transactionDate}" pattern="yyyy-MM-dd"/></td>
                                                                            <td><c:out value="${cashFlow.transactionType}"/></td>
                                                                            <td><fmt:formatNumber value="${cashFlow.amount}" type="currency"/></td>
                                                                            <td><c:out value="${cashFlow.description}"/></td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                    <c:if test="${empty cashFlowReport}">
                                                                        <tr><td colspan="4" class="text-center">No data available.</td></tr>
                                                                    </c:if>
                                                                </tbody>
                                                            </table>
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
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        $(document).ready(function() {
            var ctx = document.getElementById('sales-report-chart').getContext('2d');
            var salesChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: [<c:forEach var="storeSale" items="${storeSales}" varStatus="loop">'${storeSale.storeName}'<c:if test="${!loop.last}">,</c:if></c:forEach>],
                    datasets: [{
                        label: 'Sales by Store ($)',
                        data: [<c:forEach var="storeSale" items="${storeSales}" varStatus="loop">${storeSale.totalSales}<c:if test="${!loop.last}">,</c:if></c:forEach>],
                        backgroundColor: '#7c4dff',
                        borderColor: '#7c4dff',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Sales Amount ($)'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Store'
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

