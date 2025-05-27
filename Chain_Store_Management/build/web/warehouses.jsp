<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Manage Warehouses - Store Manager</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="Manage warehouses for store inventory." />
    <meta name="keywords" content="warehouse management, inventory, retail" />
    <meta name="author" content="codedthemes" />
    <link rel="icon" href="assets/images/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/pages/waves/css/waves.min.css">
    <link rel="stylesheet" href="assets/icon/themify-icons/themify-icons.css">
    <link rel="stylesheet" href="assets/icon/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/jquery.mCustomScrollbar.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        .pcoded-inner-content {
            margin-left: 5px;
            padding: 5px;
            transition: margin-left 0.3s ease;
            width: calc(100% - 5px);
        }
        .pcoded[vertical-nav-type="collapsed"] .pcoded-inner-content {
            margin-left: 80px;
            width: calc(100% - 80px);
        }
        .table-responsive {
            width: 100%;
            margin: 0 auto;
            overflow-x: auto;
        }
        .table {
            width: 100%;
            margin-bottom: 1rem;
        }
        .table th, .table td {
            vertical-align: middle;
            padding: 15px;
        }
        .modal-dialog {
            max-width: 600px;
            margin: 1.75rem auto;
        }
        .card {
            width: 100%;
        }
        .alert {
            margin-bottom: 20px;
        }
        @media (max-width: 768px) {
            .pcoded-inner-content {
                margin-left: 0;
                width: 100%;
                padding: 10px;
            }
            .pcoded[vertical-nav-type="collapsed"] .pcoded-inner-content {
                margin-left: 0;
                width: 100%;
            }
            .table-responsive {
                font-size: 14px;
            }
            .modal-dialog {
                max-width: 90%;
            }
        }
    </style>
</head>
<body>
    <!-- Preloader -->
    <div class="theme-loader">
        <div class="loader-track">
            <div class="preloader-wrapper">
                <div class="spinner-layer spinner-blue">
                    <div class="circle-clipper left"><div class="circle"></div></div>
                    <div class="gap-patch"><div class="circle"></div></div>
                    <div class="circle-clipper right"><div class="circle"></div></div>
                </div>
                <div class="spinner-layer spinner-red">
                    <div class="circle-clipper left"><div class="circle"></div></div>
                    <div class="gap-patch"><div class="circle"></div></div>
                    <div class="circle-clipper right"><div class="circle"></div></div>
                </div>
                <div class="spinner-layer spinner-yellow">
                    <div class="circle-clipper left"><div class="circle"></div></div>
                    <div class="gap-patch"><div class="circle"></div></div>
                    <div class="circle-clipper right"><div class="circle"></div></div>
                </div>
                <div class="spinner-layer spinner-green">
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
            <!-- Navbar -->
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
                                    <span class="badge bg-c-red"><c:out value="${unreadNotifications}" default="0"/></span>
                                </a>
                                <ul class="show-notification">
                                    <li><h6>Notifications</h6><label class="label label-danger">New</label></li>
                                    <c:forEach var="notification" items="${notifications}">
                                        <li class="waves-effect waves-light">
                                            <div class="media">
                                                <div class="media-body">
                                                    <h5 class="notification-user"><c:out value="${notification.title}"/></h5>
                                                    <p class="notification-msg"><c:out value="${notification.message}"/></p>
                                                    <span class="notification-time"><fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
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
                                    <span><c:out value="${sessionScope.user.username}" default="Guest"/></span>
                                    <i class="ti-angle-down"></i>
                                </a>
                                <ul class="show-notification profile-notification">
                                    <li><a href="user-profile.jsp"><i class="ti-user"></i> Profile</a></li>
                                    <li><a href="settings.jsp"><i class="ti-settings"></i> Settings</a></li>
                                    <li><a href="logout"><i class="ti-layout-sidebar-left"></i> Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <div class="pcoded-main-container">
                <div class="pcoded-wrapper">
                    <!-- Sidebar -->
                    <nav class="pcoded-navbar">
                        <div class="sidebar_toggle"><a href="#"><i class="icon-close icons"></i></a></div>
                        <div class="pcoded-inner-navbar main-menu">
                            <div class="main-menu-header">
                                <img class="img-80 img-radius" src="assets/images/avatar-4.jpg" alt="User-Profile-Image">
                                <div class="user-details">
                                    <span id="more-details"><c:out value="${sessionScope.user.username}" default="Guest"/> <i class="fa fa-caret-down"></i></span>
                                </div>
                            </div>
                            <div class="main-menu-content">
                                <ul>
                                    <li class="more-details">
                                        <a href="user-profile.jsp"><i class="ti-user"></i>View Profile</a>
                                        <a href="settings.jsp"><i class="ti-settings"></i>Settings</a>
                                        <a href="logout"><i class="ti-layout-sidebar-left"></i>Logout</a>
                                    </li>
                                </ul>
                            </div>
                            <div class="p-15 p-b-0">
                                <form class="form-material">
                                    <div class="form-group form-primary">
                                        <input type="text" name="search" class="form-control" placeholder="Search">
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
                                    </ul>
                                </li>
                                <li class="pcoded-hasmenu active pcoded-trigger">
                                    <a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-archive"></i></span><span class="pcoded-mtext">Inventory</span><span class="pcoded-mcaret"></span></a>
                                    <ul class="pcoded-submenu">
                                        <li><a href="inventory">View Inventory</a></li>
                                        <li class="active"><a href="warehouses.jsp">Manage Warehouses</a></li>
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
                                <li><a href="customers.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-id-badge"></i></span><span class="pcoded-mtext">Customers</span></a></li>
                                <li><a href="cashflows.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-money"></i></span><span class="pcoded-mtext">Cash Flow</span></a></li>
                                <li><a href="stores.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-shopping-cart"></i></span><span class="pcoded-mtext">Stores</span></a></li>
                                <li><a href="notifications.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-bell"></i></span><span class="pcoded-mtext">Notifications</span></a></li>
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
                                            <h5 class="m-b-10">Manage Warehouses</h5>
                                            <p class="m-b-0">Manage your store warehouses.</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                            <li class="breadcrumb-item"><a href="inventory">Inventory</a></li>
                                            <li class="breadcrumb-item"><a href="warehouses.jsp">Warehouses</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="pcoded-inner-content">
                            <div class="main-body">
                                <div class="page-wrapper">
                                    <div class="page-body">
                                        <!-- Messages -->
                                        <c:if test="${not empty param.message}">
                                            <div class="alert ${param.isError == 'true' ? 'alert-danger' : 'alert-success'} alert-dismissible fade show" role="alert">
                                                <c:out value="${param.message}"/>
                                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty error}">
                                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                                <c:out value="${error}"/>
                                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                        </c:if>
                                        <!-- Warehouse Table -->
                                        <div class="card">
                                            <div class="card-header">
                                                <h5>Warehouse List</h5>
                                                <div class="card-header-right">
                                                    <button type="button" class="btn btn-primary btn-sm" 
                                                            data-toggle="modal" data-target="#warehouseModal"
                                                            onclick="resetWarehouseForm()">
                                                        <i class="fa fa-plus"></i> Add Warehouse
                                                    </button>
                                                </div>
                                            </div>
                                            <div class="card-block">
                                                <div class="table-responsive">
                                                    <table class="table table-hover">
                                                        <thead>
                                                            <tr>
                                                                <th>ID</th>
                                                                <th>Warehouse Name</th>
                                                                <th>Store</th>
                                                                <th>Actions</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:choose>
                                                                <c:when test="${empty warehouses}">
                                                                    <tr>
                                                                        <td colspan="4" class="text-center">No warehouses found</td>
                                                                    </tr>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:forEach var="warehouse" items="${warehouses}">
                                                                        <tr>
                                                                            <td>${warehouse.warehouseID}</td>
                                                                            <td>${warehouse.warehouseName}</td>
                                                                            <td>${warehouse.storeName}</td>
                                                                            <td>
                                                                                <button class="btn btn-sm btn-info mr-1" 
                                                                                        data-toggle="modal" data-target="#warehouseModal"
                                                                                        onclick="editWarehouse('${warehouse.warehouseID}', '${warehouse.warehouseName}', '${warehouse.storeID}')">
                                                                                    <i class="fa fa-edit"></i> Edit
                                                                                </button>
                                                                                <form action="warehouses" method="post" style="display: inline;">
                                                                                    <input type="hidden" name="action" value="delete">
                                                                                    <input type="hidden" name="warehouseID" value="${warehouse.warehouseID}">
                                                                                    <button type="submit" class="btn btn-sm btn-danger" 
                                                                                            onclick="return confirm('Are you sure you want to delete this warehouse?')">
                                                                                        <i class="fa fa-trash"></i> Delete
                                                                                    </button>
                                                                                </form>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Warehouse Modal Form -->
                        <div class="modal fade" id="warehouseModal" tabindex="-1" role="dialog" aria-labelledby="warehouseModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="warehouseModalLabel">Add Warehouse</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <form id="warehouseForm" action="warehouses" method="post">
                                        <div class="modal-body">
                                            <input type="hidden" name="action" id="formAction" value="add">
                                            <input type="hidden" name="warehouseID" id="warehouseID">
                                            <div class="form-group">
                                                <label for="warehouseName">Warehouse Name</label>
                                                <input type="text" class="form-control" id="warehouseName" name="warehouseName" >
                                            </div>
                                            <div class="form-group">
                                                <label for="storeID">Store</label>
                                                <select class="form-control" id="storeID" name="storeID" required>
                                                    <option value="">Select Store</option>
                                                    <c:forEach var="store" items="${stores}">
                                                        <option value="${store.storeID}">${store.storeName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                            <button type="submit" class="btn btn-primary">Save</button>
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
    <!-- Scripts -->
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
        // Sidebar toggle functionality
        $(document).ready(function() {
            $('.sidebar_toggle a, #mobile-collapse').on('click', function() {
                $('#pcoded').toggleClass('pcoded-sidebar-collapsed');
                $(window).trigger('resize');
            });
        });

        function resetWarehouseForm() {
            document.getElementById('warehouseForm').reset();
            document.getElementById('formAction').value = 'add';
            document.getElementById('warehouseID').value = '';
            document.getElementById('warehouseModalLabel').innerText = 'Add Warehouse';
        }

        function editWarehouse(id, name, storeID) {
            document.getElementById('formAction').value = 'edit';
            document.getElementById('warehouseID').value = id;
            document.getElementById('warehouseName').value = name;
            document.getElementById('storeID').value = storeID;
            document.getElementById('warehouseModalLabel').innerText = 'Edit Warehouse';
        }

        // Prevent auto-redirect if message exists
        document.addEventListener('DOMContentLoaded', function () {
            const hasMessage = window.location.search.includes('message=');
            if (!hasMessage && ${empty warehouses}) {
                window.location.href = 'warehouses?action=load';
            }
        });
    </script>
</body>
</html>