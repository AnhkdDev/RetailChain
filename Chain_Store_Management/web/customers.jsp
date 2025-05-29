<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Customers Management</title>
    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="Manage customers for the store, including viewing, adding, editing, and deleting customer information." />
    <meta name="keywords" content="store manager, customers, retail, management" />
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
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap4.min.css"/>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/2.2.2/css/buttons.bootstrap4.min.css"/>
    <!-- Style.css -->
    <link rel="stylesheet" type="text/css" href="assets/css/style.css">
    <style>
        .form-group label {
            margin-bottom: 5px;
            font-weight: 500;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .dt-buttons {
            margin-bottom: 10px;
        }
    </style>
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
                                        <a href="user-profile.jsp"><i class="ti-user"></i>View Profile</a>
                                        <a href="settings.jsp"><i class="ti-settings"></i>Settings</a>
                                        <a href="logout.jsp"><i class="ti-layout-sidebar-left"></i>Logout</a>
                                    </li>
                                </ul>
                            </div>
                            <div class="p-15 p-b-0">
                                <form class="form-material">
                                    <div class="form-group form-primary">
                                        <input type="text" name="search" class="form-control" placeholder="Search Products, Employees...">
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
                                <li class="active">
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
                    <!-- Main Content -->
                    <div class="pcoded-content">
                        <div class="page-header">
                            <div class="page-block">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <div class="page-header-title">
                                            <h5 class="m-b-10">Customers Management</h5>
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
                                            <!-- Search and Filter Form -->
                                            <div class="col-xl-12 col-md-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Search & Filter Customers</h5>
                                                    </div>
                                                    <div class="card-block">
                                                        <form action="customers" method="get" class="form-material">
                                                            <div class="row">
                                                                <div class="col-md-3">
                                                                    <div class="form-group">
                                                                        <label>Search by Name/Phone/Email</label>
                                                                        <input type="text" name="searchQuery" class="form-control" value="${param.searchQuery}" placeholder="Enter name, phone, or email">
                                                                    </div>
                                                                </div>
                                                                <div class="col-md-2">
                                                                    <div class="form-group">
                                                                        <label>Gender</label>
                                                                        <select name="gender" class="form-control">
                                                                            <option value="">All</option>
                                                                            <option value="Male" ${param.gender == 'Male' ? 'selected' : ''}>Male</option>
                                                                            <option value="Female" ${param.gender == 'Female' ? 'selected' : ''}>Female</option>
                                                                            <option value="Other" ${param.gender == 'Other' ? 'selected' : ''}>Other</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="col-md-3">
                                                                    <div class="form-group">
                                                                        <label>Total Spent Range</label>
                                                                        <div class="input-group">
                                                                            <input type="number" name="minSpent" class="form-control" value="${param.minSpent}" placeholder="Min">
                                                                            <input type="number" name="maxSpent" class="form-control" value="${param.maxSpent}" placeholder="Max">
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <button type="submit" class="btn btn-primary mt-4">Apply Filter</button>
                                                                    <a href="customers" class="btn btn-secondary mt-4">Reset</a>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Customers Table -->
                                            <div class="col-xl-12 col-md-12">
                                                <div class="card table-card">
                                                    <div class="card-header">
                                                        <h5>Customer List</h5>
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
                                                                <a href="add-customer.jsp" class="btn btn-primary waves-effect waves-light">
                                                                    <i class="fa fa-plus"></i> Add New Customer
                                                                </a>
                                                            </div>
                                                            <table id="customerTable" class="table table-hover table-bordered">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Customer ID</th>
                                                                        <th>Full Name</th>
                                                                        <th>Phone</th>
                                                                        <th>Email</th>
                                                                        <th>Gender</th>
                                                                        <th>Birth Date</th>
                                                                        <th>Total Spent</th>
                                                                        <th>Created At</th>
                                                                        <th>Actions</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <c:forEach var="customer" items="${customers}">
                                                                        <tr>
                                                                            <td><c:out value="${customer.customerID}"/></td>
                                                                            <td><c:out value="${customer.fullName}"/></td>
                                                                            <td><c:out value="${customer.phone}"/></td>
                                                                            <td><c:out value="${customer.email}"/></td>
                                                                            <td><c:out value="${customer.gender}"/></td>
                                                                            <td><fmt:formatDate value="${customer.birthDate}" pattern="dd-MM-yyyy"/></td>
                                                                            <td><fmt:formatNumber value="${customer.totalSpent}" type="currency"/></td>
                                                                            <td><fmt:formatDate value="${customer.createdAt}" pattern="dd-MM-yyyy HH:mm"/></td>
                                                                            <td>
                                                                                <a href="customer-purchases.jsp?customerID=${customer.customerID}" class="btn btn-sm btn-info waves-effect waves-light" title="View Purchases">
                                                                                    <i class="fa fa-shopping-cart"></i>
                                                                                </a>
                                                                                <a href="edit-customer.jsp?customerID=${customer.customerID}" class="btn btn-sm btn-primary waves-effect waves-light" title="Edit">
                                                                                    <i class="fa fa-edit"></i>
                                                                                </a>
                                                                                <a href="delete-customer?customerID=${customer.customerID}" class="btn btn-sm btn-danger waves-effect waves-light" title="Delete" onclick="return confirm('Are you sure you want to delete this customer?');">
                                                                                    <i class="fa fa-trash"></i>
                                                                                </a>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
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
    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap4.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.2.2/js/dataTables.buttons.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.bootstrap4.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.html5.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/2.2.2/js/buttons.print.min.js"></script>
    <!-- Menu js -->
    <script src="assets/js/pcoded.min.js"></script>
    <script src="assets/js/vertical-layout.min.js"></script>
    <!-- Custom js -->
    <script type="text/javascript" src="assets/js/script.js"></script>
    <!-- DataTables Initialization -->
    <script>
        $(document).ready(function() {
            $('#customerTable').DataTable({
                "paging": true,
                "ordering": true,
                "info": true,
                "searching": false, // Disable DataTables' built-in search since we have a custom search form
                "dom": '<"dt-buttons"Bf>rtip',
                "buttons": [
                    {
                        extend: 'csv',
                        text: 'Export CSV',
                        className: 'btn btn-sm btn-primary'
                    },
                    {
                        extend: 'pdf',
                        text: 'Export PDF',
                        className: 'btn btn-sm btn-primary'
                    }
                ],
                "columnDefs": [
                    { "orderable": false, "targets": 8 } // Disable sorting on Actions column
                ]
            });
        });
    </script>
</body>
</html>