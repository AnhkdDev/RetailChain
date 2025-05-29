<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="View and manage notifications for the store management system." />
    <meta name="keywords" content="store manager, notifications, retail, management" />
    <meta name="author" content="codedthemes" />
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
                                </a>
                                <ul class="show-notification">
                                    <li><h6>Notifications</h6></li>
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
                                    <li><a href="notifications" class="b-b-primary text-primary">View All Notifications</a></li>
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
                                <li><a href="CustomerListServlet" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-id-badge"></i></span><span class="pcoded-mtext">Customers</span></a></li>
                                <li><a href="cashflows.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-money"></i></span><span class="pcoded-mtext">Cash Flow</span></a></li>
                                <li><a href="stores.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-shopping-cart"></i></span><span class="pcoded-mtext">Stores</span></a></li>
                                <li class="active"><a href="notifications" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-bell"></i></span><span class="pcoded-mtext">Notifications</span></a></li>
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
                                            <h5 class="m-b-10">Notifications</h5>
                                            <p class="m-b-0">View your notifications.</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                            <li class="breadcrumb-item"><a href="#!">Notifications</a></li>
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
                                            <c:if test="${not empty message}">
                                                <div class="col-12">
                                                    <div class="alert alert-${messageType}">${message}</div>
                                                </div>
                                            </c:if>
                                            <div class="col-xl-12 col-md-12">
                                                <div class="card table-card">
                                                    <div class="card-header">
                                                        <h5>Notification List</h5>
                                                        <div class="card-header-right">
                                                            <ul class="list-unstyled card-option">
                                                                <li><i class="fa fa-window-maximize full-card"></i></li>
                                                                <li><i class="fa fa-minus minimize-card"></i></li>
                                                                <li><i class="fa fa-refresh reload-card"></i></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <div class="card-block">
                                                        <div class="dt-responsive table-responsive">
                                                            <div class="m-b-20">
                                                                <form action="notifications" method="get" class="form-inline">
                                                                    <div class="form-group mr-2">
                                                                        <input type="text" name="search" class="form-control" placeholder="Search..." value="${search}">
                                                                    </div>
                                                                    <div class="form-group mr-2">
                                                                        <select name="status" class="form-control">
                                                                            <option value="" <c:if test="${empty status}">selected</c:if>>All</option>
                                                                            <option value="read" <c:if test="${status == 'read'}">selected</c:if>>Read</option>
                                                                            <option value="unread" <c:if test="${status == 'unread'}">selected</c:if>>Unread</option>
                                                                        </select>
                                                                    </div>
                                                                    <button type="submit" class="btn btn-primary waves-effect waves-light"><i class="fa fa-search"></i> Filter</button>
                                                                </form>
                                                            </div>
                                                            <c:choose>
                                                                <c:when test="${empty notifications}">
                                                                    <div class="alert alert-info">No notifications available.</div>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <table class="table table-hover">
                                                                        <thead>
                                                                            <tr>
                                                                                <th>Title</th>
                                                                                <th>Message</th>
                                                                                <th>Created At</th>
                                                                                <th>Status</th>
                                                                                <th>Actions</th>
                                                                            </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                            <c:forEach var="notification" items="${notifications}">
                                                                                <tr class="${notification.isRead ? '' : 'table-active'}">
                                                                                    <td><c:out value="${notification.title}"/></td>
                                                                                    <td><c:out value="${notification.message}"/></td>
                                                                                    <td><fmt:formatDate value="${notification.createdAt}" pattern="dd-MM-yyyy HH:mm"/></td>
                                                                                    <td>
                                                                                        <c:choose>
                                                                                            <c:when test="${notification.isRead}">
                                                                                                <span class="badge badge-success">Read</span>
                                                                                            </c:when>
                                                                                            <c:otherwise>
                                                                                                <span class="badge badge-warning">Unread</span>
                                                                                            </c:otherwise>
                                                                                        </c:choose>
                                                                                    </td>
                                                                                    <td>
                                                                                        <c:if test="${not notification.isRead}">
                                                                                            <form action="notifications" method="post" style="display:inline;">
                                                                                                <input type="hidden" name="action" value="markRead">
                                                                                                <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                                                                <button type="submit" class="btn btn-sm btn-primary waves-effect waves-light" title="Mark as Read" onclick="return confirm('Are you sure you want to mark this notification as read?');">
                                                                                                    <i class="fa fa-check"></i>
                                                                                                </button>
                                                                                            </form>
                                                                                        </c:if>
                                                                                        <form action="notifications" method="post" style="display:inline;">
                                                                                            <input type="hidden" name="action" value="delete">
                                                                                            <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                                                            <button type="submit" class="btn btn-sm btn-danger waves-effect waves-light" title="Delete" onclick="return confirm('Are you sure you want to delete this notification?');">
                                                                                                <i class="fa fa-trash"></i>
                                                                                            </button>
                                                                                        </form>
                                                                                    </td>
                                                                                </tr>
                                                                            </c:forEach>
                                                                        </tbody>
                                                                    </table>
                                                                    <c:if test="${totalNotifications > pageSize}">
                                                                        <nav aria-label="Page navigation">
                                                                            <ul class="pagination justify-content-center">
                                                                                <c:if test="${currentPage > 1}">
                                                                                    <li class="page-item">
                                                                                        <a class="page-link" href="notifications?page=${currentPage - 1}&search=${search}&status=${status}">Previous</a>
                                                                                    </li>
                                                                                </c:if>
                                                                                <c:forEach begin="1" end="${totalPages}" var="i">
                                                                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                                                        <a class="page-link" href="notifications?page=${i}&search=${search}&status=${status}">${i}</a>
                                                                                    </li>
                                                                                </c:forEach>
                                                                                <c:if test="${currentPage < totalPages}">
                                                                                    <li class="page-item">
                                                                                        <a class="page-link" href="notifications?page=${currentPage + 1}&search=${search}&status=${status}">Next</a>
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
</body>
</html>