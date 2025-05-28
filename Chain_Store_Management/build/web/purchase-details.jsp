<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Purchase Details</title>
    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="View details of a purchase." />
    <meta name="keywords" content="purchase details, inventory, retail" />
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
                <!-- ... (giữ nguyên phần preloader từ create-purchase.jsp) ... -->
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
                        <!-- ... (giữ nguyên phần header từ create-purchase.jsp) ... -->
                    </div>
                </div>
            </nav>
            <!-- Sidebar -->
            <div class="pcoded-main-container">
                <div class="pcoded-wrapper">
                    <nav class="pcoded-navbar">
                        <!-- ... (giữ nguyên sidebar từ create-purchase.jsp) ... -->
                        <ul class="pcoded-item pcoded-left-item">
                            <li class="pcoded-hasmenu active">
                                <a href="javascript:void(0)" class="waves-effect waves-dark">
                                    <span class="pcoded-micon"><i class="ti-truck"></i></span>
                                    <span class="pcoded-mtext">Purchases</span>
                                    <span class="pcoded-mcaret"></span>
                                </a>
                                <ul class="pcoded-submenu">
                                    <li class="active"><a href="Purchase">View Purchases</a></li>
                                    <li><a href="Purchase?action=create">Create Purchase</a></li>
                                </ul>
                            </li>
                            <!-- ... (các mục menu khác) ... -->
                        </ul>
                    </nav>
                    <!-- Main Content -->
                    <div class="pcoded-content">
                        <div class="page-header">
                            <div class="page-block">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <div class="page-header-title">
                                            <h5 class="m-b-10">Purchase Details</h5>
                                            <p class="m-b-0">Details of Purchase ID: ${purchase.purchaseID}</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item">
                                                <a href="index.jsp"><i class="fa fa-home"></i></a>
                                            </li>
                                            <li class="breadcrumb-item"><a href="Purchase">Purchases</a></li>
                                            <li class="breadcrumb-item"><a href="#!">Purchase Details</a></li>
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
                                                        <h5>Purchase Information</h5>
                                                    </div>
                                                    <div class="card-block">
                                                        <c:if test="${empty purchase}">
                                                            <div class="alert alert-danger">Purchase not found.</div>
                                                        </c:if>
                                                        <c:if test="${not empty purchase}">
                                                            <div class="row">
                                                                <div class="col-md-6">
                                                                    <p><strong>Purchase ID:</strong> ${purchase.purchaseID}</p>
                                                                    <p><strong>Purchase Date:</strong> <fmt:formatDate value="${purchase.purchaseDate}" pattern="dd-MM-yyyy HH:mm"/></p>
                                                                    <p><strong>Total Amount:</strong> ${purchase.totalAmount}</p>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <p><strong>Supplier:</strong> ${purchase.supplierName}</p>
                                                                    <p><strong>Warehouse:</strong> ${purchase.warehouseName}</p>
                                                                </div>
                                                            </div>
                                                            <h6 class="mt-4">Purchase Details</h6>
                                                            <div class="table-responsive">
                                                                <table class="table table-bordered">
                                                                    <thead>
                                                                        <tr>
                                                                            <th>Product Name</th>
                                                                            <th>Quantity</th>
                                                                            <th>Cost Price</th>
                                                                            <th>Subtotal</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <c:forEach var="detail" items="${purchaseDetails}">
                                                                            <tr>
                                                                                <td>${detail.productName}</td>
                                                                                <td>${detail.quantity}</td>
                                                                                <td>${detail.costPrice}</td>
                                                                                <td>${detail.quantity * detail.costPrice}</td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                            <a href="Purchase" class="btn btn-primary mt-3">Back to Purchases</a>
                                                        </c:if>
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
</body>
</html>
```