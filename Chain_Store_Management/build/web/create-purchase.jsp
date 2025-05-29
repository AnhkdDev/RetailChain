<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Purchase</title>
    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="Create a new purchase for the store." />
    <meta name="keywords" content="purchase creation, inventory, retail" />
    <meta name="author" content="codedthemes" />
    <!-- Favicon icon -->
    <link rel="icon" href="assets/images/favicon.ico" type="image/x-icon">
    <!-- Google font -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
    <!-- Required Framework -->
    <link rel="stylesheet" type="text/css" href="assets/css/bootstrap/css/bootstrap.min.css">
    <!-- Waves.css -->
    <link rel="stylesheet" href="assets/pages/waves/css/waves.min.css" type="text/css" media="all">
    <!-- Themify icon -->
    <link rel="stylesheet" type="text/css" href="assets/icon/themify-icons/themify-icons.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" type="text/css" href="assets/icon/font-awesome/css/font-awesome.min.css">
    <!-- Scrollbar.css -->
    <link rel="stylesheet" type="text/css" href="assets/css/jquery.mCustomScrollbar.css">
    <!-- Style.css -->
    <link rel="stylesheet" type="text/css" href="assets/css/style.css">
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
                                        <input type="text" class="form-control" placeholder="Search Purchases...">
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
                                    <li><a href="logout.jsp"><i class="ti-log-out"></i> Logout</a></li>
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
                                <img class="img-80 img-radius" src="assets/images/avatar-4.jpg" alt="User-Profile-Image">
                                <div class="user-details">
                                    <span id="more-details"><c:out value="${sessionScope.user.fullName}"/> <i class="fa fa-caret-down"></i></span>
                                </div>
                            </div>
                            <div class="main-menu-content">
                                <ul>
                                    <li class="more-details">
                                        <a href="user-profile.jsp"><i class="ti-user"></i> View Profile</a>
                                        <a href="settings.jsp"><i class="ti-settings"></i> Settings</a>
                                        <a href="logout.jsp"><i class="ti-log-out"></i> Logout</a>
                                    </li>
                                </ul>
                            </div>
<!--                            <div class="p-15 p-b-0">
                                <form class="form-material">
                                    <div class="form-group form-primary">
                                        <input type="text" name="search" class="form-control" placeholder="" required="">
                                        <span class="form-bar"></span>
                                        <label class="float-label"><i class="fa fa-search m-r-5"></i> Search</label>
                                    </div>
                                </form>
                            </div>-->
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
                                        <span class="caret"></span>
                                    </span></a>
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
<li class="pcoded-hasmenu active">
                                    <a href="javascript:void(0)" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-truck"></i></span>
                                        <span class="pcoded-mtext">Purchases</span>
                                        <span class="pcoded-mcaret"></span>
                                    </a>
                                    <ul class="pcoded-submenu">
                                        <li><a href="Purchase">View Purchases</a></li>
                                        <li class="active"><a href="create-purchase.jsp">Create Purchase</a></li>
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
                                    <a href="CustomerListServlet" class="waves-effect waves-dark">
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
                                    <a href="stores" class="waves-effect waves-dark">
                                        <span class="pcoded-micon"><i class="ti-shopping-cart"></i></span>
                                        <span class="pcoded-mtext">Stores</span>
                                    </a>
                                </li>
<li>
                                    <a href="notifications" class="waves-effect waves-dark">
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
                    <!-- Main Content -->
                    <div class="pcoded-content">
                        <div class="page-header">
                            <div class="page-block">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <div class="page-header-title">
                                            <h5 class="m-b-10">Create Purchase</h5>
                                            <p class="m-b-0">Fill in the details to create a new purchase</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item">
                                                <a href="index.jsp"><i class="fa fa-home"></i></a>
                                            </li>
                                            <li class="breadcrumb-item"><a href="Purchase">Purchases</a></li>
                                            <li class="breadcrumb-item"><a href="#!">Create Purchase</a></li>
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
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>New Purchase</h5>
                                                    </div>
                                                    <div class="card-block">
                                                        <c:if test="${not empty errorMessage}">
                                                            <div class="alert alert-danger">${errorMessage}</div>
                                                        </c:if>
                                                        <c:if test="${not empty successMessage}">
                                                            <div class="alert alert-success">${successMessage}</div>
                                                        </c:if>
                                                        <form action="Purchase" method="post">
                                                            <input type="hidden" name="action" value="create">
                                                            <div class="form-group">
                                                                <label for="supplierName">Supplier Name</label>
                                                                <input type="text" class="form-control" id="supplierName" name="supplierName" placeholder="Enter supplier name" required>
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="warehouseID">Warehouse</label>
                                                                <select class="form-control" id="warehouseID" name="warehouseID" required>
                                                                    <option value="">Select Warehouse</option>
                                                                    <c:forEach var="warehouse" items="${warehouses}">
                                                                        <option value="${warehouse.warehouseID}"><c:out value="${warehouse.warehouseName}"/></option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <h6>Purchase Details</h6>
                                                            <div id="purchaseDetails">
                                                                <div class="row" id="detail_1">
                                                                    <div class="col-md-4 form-group">
                                                                        <label for="productID_1">Product</label>
                                                                        <select class="form-control" id="productID_1" name="productID_1" required>
                                                                            <option value="">Select Product</option>
                                                                            <c:forEach var="product" items="${products}">
                                                                                <option value="${product.productID}"><c:out value="${product.productName}"/></option>
                                                                            </c:forEach>
                                                                        </select>
                                                                    </div>
                                                                    <div class="col-md-3 form-group">
                                                                        <label for="quantity_1">Quantity</label>
                                                                        <input type="number" class="form-control" id="quantity_1" name="quantity_1" min="1" placeholder="Enter quantity" required>
                                                                    </div>
                                                                    <div class="col-md-3 form-group">
                                                                        <label for="costPrice_1">Cost Price</label>
                                                                        <input type="number" step="0.01" class="form-control" id="costPrice_1" name="costPrice_1" min="0" placeholder="Enter cost price" required>
                                                                    </div>
                                                                    <div class="col-md-2 form-group">
                                                                        <label> </label>
                                                                        <button type="button" class="btn btn-danger btn-block" onclick="removeDetail(1)">Remove</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <button type="button" class="btn btn-secondary mt-2" onclick="addDetail()">Add Another Product</button>
                                                            <button type="submit" class="btn btn-primary mt-2">Create Purchase</button>
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
    <!-- Required Jquery -->
    <script type="text/javascript" src="assets/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="assets/js/jquery-ui/jquery-ui.min.js"></script>
    <script type="text/javascript" src="assets/js/popper.js/popper.min.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap/js/bootstrap.min.js"></script>
    <!-- Waves js -->
    <script src="assets/pages/waves/js/waves.min.js"></script>
    <!-- JQuery slimscroll js -->
    <script type="text/javascript" src="assets/js/jquery-slimscroll/jquery.slimscroll.js"></script>
    <!-- Modernizr js -->
    <script type="text/javascript" src="assets/js/modernizr/modernizr.js"></script>
    <!-- Slimscroll js -->
    <script type="text/javascript" src="assets/js/SmoothScroll.js"></script>
    <script src="assets/js/jquery.mCustomScrollbar.concat.min.js"></script>
    <!-- Menu js -->
    <script src="assets/js/pcoded.min.js"></script>
    <script src="assets/js/vertical-layout.min.js"></script>
    <!-- Custom js -->
    <script type="text/javascript" src="assets/js/script.js"></script>
    <script>
        let detailCount = 1;

        function addDetail() {
            detailCount++;
            const detailsDiv = document.getElementById('purchaseDetails');
            const newDetail = `
                <div class="row mt-2" id="detail_${detailCount}">
                    <div class="col-md-4 form-group">
                        <label for="productID_${detailCount}">Product</label>
                        <select class="form-control" id="productID_${detailCount}" name="productID_${detailCount}" required>
                            <option value="">Select Product</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.productID}"><c:out value="${product.productName}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3 form-group">
                        <label for="quantity_${detailCount}">Quantity</label>
                        <input type="number" class="form-control" id="quantity_${detailCount}" name="quantity_${detailCount}" min="1" placeholder="Enter quantity" required>
                    </div>
                    <div class="col-md-3 form-group">
                        <label for="costPrice_${detailCount}">Cost Price</label>
                        <input type="number" step="0.01" class="form-control" id="costPrice_${detailCount}" name="costPrice_${detailCount}" min="0" placeholder="Enter cost price" required>
                    </div>
                    <div class="col-md-2 form-group">
                        <label> </label>
                        <button type="button" class="btn btn-danger btn-block" onclick="removeDetail(${detailCount})">Remove</button>
                    </div>
                </div>`;
            detailsDiv.insertAdjacentHTML('beforeend', newDetail);
        }

        function removeDetail(index) {
            const detailDiv = document.getElementById(`detail_${index}`);
            if (detailDiv) {
                detailDiv.remove();
            }
        }
    </script>
</body>
</html>