<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Product - Store Manager</title>
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
                                <li class="pcoded-hasmenu active">
                                    <a href="javascript:void(0)" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-package"></i></span>
                                        <span class="pcoded-mtext">Products</span>
                                        <span class="pcoded-mcaret"></span>
                                    </a>
                                    <ul class="pcoded-submenu">
                                        <li><a href="products.jsp">View Products</a></li>
                                        <li class="active"><a href="add-product.jsp">Add Product</a></li>
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
                                            <h5 class="m-b-10">Add New Product</h5>
                                            <p class="m-b-0">Enter details to add a new product to the inventory.</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                            <li class="breadcrumb-item"><a href="products.jsp">Products</a></li>
                                            <li class="breadcrumb-item"><a href="add-product.jsp">Add Product</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="pcoded-inner-content">
                            <div class="main-body">
                                <div class="page-wrapper">
                                    <div class="page-body">
                                        <!-- Add Product Form -->
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Add Product</h5>
                                                    </div>
                                                    <div class="card-block">
                                                        <!-- Success/Error Message -->
                                                        <c:if test="${not empty message}">
                                                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                                                <c:out value="${message}"/>
                                                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                                    <span aria-hidden="true">×</span>
                                                                </button>
                                                            </div>
                                                        </c:if>
                                                        <form action="add-product" method="post">
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Product ID</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" class="form-control" name="productID" placeholder="e.g., P001" required>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Product Name</label>
                                                                <div class="col-sm-10">
                                                                    <input type="text" class="form-control" name="productName" placeholder="e.g., T-Shirt" required>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Description</label>
                                                                <div class="col-sm-10">
                                                                    <textarea class="form-control" name="description" placeholder="Enter product description"></textarea>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Price</label>
                                                                <div class="col-sm-10">
                                                                    <input type="number" step="0.01" class="form-control" name="price" placeholder="e.g., 29.99" required>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Category</label>
                                                                <div class="col-sm-10">
                                                                    <select class="form-control" name="categoryID" required>
                                                                        <option value="">Select Category</option>
                                                                        <c:forEach var="category" items="${categories}">
                                                                            <option value="${category.categoryID}"><c:out value="${category.categoryName}"/></option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Size</label>
                                                                <div class="col-sm-10">
                                                                    <select class="form-control" name="sizeID" required>
                                                                        <option value="">Select Size</option>
                                                                        <c:forEach var="size" items="${sizes}">
                                                                            <option value="${size.sizeID}"><c:out value="${size.sizeName}"/></option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Color</label>
                                                                <div class="col-sm-10">
                                                                    <select class="form-control" name="colorID" required>
                                                                        <option value="">Select Color</option>
                                                                        <c:forEach var="color" items="${colors}">
                                                                            <option value="${color.colorID}"><c:out value="${color.colorName}"/></option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Product Type</label>
                                                                <div class="col-sm-10">
                                                                    <select class="form-control" name="productTypeID" required>
                                                                        <option value="">Select Product Type</option>
                                                                        <c:forEach var="productType" items="${productTypes}">
                                                                            <option value="${productType.productTypeID}"><c:out value="${productType.typeName}"/></option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-2 col-form-label">Stock Quantity</label>
                                                                <div class="col-sm-10">
                                                                    <input type="number" class="form-control" name="stockQuantity" placeholder="e.g., 100" required>
                                                                </div>
                                                            </div>
                                                            <div class="text-right">
                                                                <button type="submit" class="btn btn-primary">Save Product</button>
                                                                <a href="products.jsp" class="btn btn-secondary">Back to Products</a>
                                                            </div>
                                                        </form>
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

