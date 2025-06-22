
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Add Product</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="icon" href="assets/images/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="assets/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/pages/waves/css/waves.min.css" type="text/css" media="all">
    <link rel="stylesheet" type="text/type" href="assets/icon/themify-icons/themify-icons.css">
    <link rel="stylesheet" type="text/css" href="assets/icon/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="assets/css/jquery.mCustomScrollbar.css">
    <link rel="stylesheet" type="text/css" href="assets/css/style.css">
    <style>
        .detail-label { font-weight: bold; margin-bottom: 5px; }
        .form-control-file { margin-top: 10px; }
    </style>
    <!--<!-- comment  <meta http-equiv="Content-Security-Policy" content="default-src 'self'; style-src 'self' https://fonts.googleapis.com https://fonts.gstatic.com; img-src 'self' data:; script-src 'none';">--> 
</head>
<body>
    <div id="pcoded" class="pcoded">
        <div class="pcoded-overlay-box"></div>
        <div class="pcoded-container navbar-wrapper">
            <div class="pcoded-main-container">
                <div class="pcoded-wrapper">
                    <nav class="pcoded-navbar">
                        <div class="sidebar_toggle"><a href="#"><i class="icon-close icons"></i></a></div>
                        <div class="pcoded-inner-navbar main-menu">
                            <div class="main-menu-header">
                                <img class="img-80 img-radius" src="assets/images/avatar-4.jpg" alt="User-Profile-Image">
                                <div class="user-details">
                                    <span><c:out value="${sessionScope.user != null ? sessionScope.user.fullName : 'Guest'}"/></span>
                                </div>
                            </div>
                            <div class="main-menu-content">
                                <ul>
                                    <li>
                                        <a href="user-profile.jsp">View Profile</a>
                                        <a href="settings.jsp">Settings</a>
                                        <a href="logout.jsp">Logout</a>
                                    </li>
                                </ul>
                            </div>
                            <div class="p-15 p-b-0"></div>
                            <div class="pcoded-navigation-label">Store Management</div>
                            <ul class="pcoded-item pcoded-left-item">
                                <li><a href="index.jsp"><span class="pcoded-micon"><i class="ti-home"></i></span><span class="pcoded-mtext">Dashboard</span></a></li>
                                <li class="pcoded-hasmenu"><a href="#"><span class="pcoded-micon"><i class="ti-package"></i></span><span class="pcoded-mtext">Products</span></a>
                                    <ul class="pcoded-submenu"><li><a href="${pageContext.request.contextPath}/products">View Products</a></li></ul>
                                </li>
                                <li class="pcoded-hasmenu"><a href="#"><span class="pcoded-micon"><i class="ti-archive"></i></span><span class="pcoded-mtext">Inventory</span></a>
                                    <ul class="pcoded-submenu"><li><a href="inventory.jsp">View Inventory</a></li><li><a href="warehouses.jsp">Manage Warehouses</a></li></ul>
                                </li>
                                <li class="pcoded-hasmenu"><a href="#"><span class="pcoded-micon"><i class="ti-receipt"></i></span><span class="pcoded-mtext">Invoices</span></a>
                                    <ul class="pcoded-submenu"><li><a href="invoices.jsp">View Invoices</a></li><li><a href="create-invoice.jsp">Create Invoice</a></li><li><a href="bank-transactions.jsp">Bank Transactions</a></li></ul>
                                </li>
                                <li class="pcoded-hasmenu"><a href="#"><span class="pcoded-micon"><i class="ti-truck"></i></span><span class="pcoded-mtext">Purchases</span></a>
                                    <ul class="pcoded-submenu"><li><a href="purchases.jsp">View Purchases</a></li><li><a href="create-purchase.jsp">Create Purchase</a></li></ul>
                                </li>
                                <li class="pcoded-hasmenu"><a href="#"><span class="pcoded-micon"><i class="ti-user"></i></span><span class="pcoded-mtext">Employees</span></a>
                                    <ul class="pcoded-submenu"><li><a href="employees.jsp">View Employees</a></li><li><a href="add-employee.jsp">Add Employee</a></li></ul>
                                </li>
                                <li><a href="SearchCustomerServlet"><span class="pcoded-micon"><i class="ti-id-badge"></i></span><span class="pcoded-mtext">Customers</span></a></li>
                                <li><a href="cashflows.jsp"><span class="pcoded-micon"><i class="ti-money"></i></span><span class="pcoded-mtext">Cash Flow</span></a></li>
                                <li><a href="stores"><span class="pcoded-micon"><i class="ti-shopping-cart"></i></span><span class="pcoded-mtext">Stores</span></a></li>
                                <li><a href="notifications"><span class="pcoded-micon"><i class="ti-bell"></i></span><span class="pcoded-mtext">Notifications</span></a></li>
                                <li><a href="reports.jsp"><span class="pcoded-micon"><i class="ti-bar-chart"></i></span><span class="pcoded-mtext">Reports</span></a></li>
                            </ul>
                        </div>
                    </nav>
                    <div class="pcoded-content">
                        <div class="page-header">
                            <div class="page-block">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <div class="page-header-title">
                                            <h5 class="m-b-10">Add Product</h5>
                                            <p class="m-b-0">Add a new product to your catalog</p>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <ul class="breadcrumb-title">
                                            <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
                                            <li class="breadcrumb-item"><a href="#">Add Product</a></li>
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
                                            <div class="col-sm-12">
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h5>Add New Product</h5>
                                                    </div>
                                                    <div class="card-block">
                                                        <c:if test="${not empty message}">
                                                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                                                <c:out value="${message}"/>
                                                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                                    <span aria-hidden="true">�</span>
                                                                </button>
                                                            </div>
                                                        </c:if>
                                                        <form action="${pageContext.request.contextPath}/add-product" method="post" enctype="multipart/form-data">
                                                            <input type="hidden" name="action" value="add">
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">T�n h�ng</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <input type="text" class="form-control" name="productName" required>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">Danh m?c</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <select class="form-control" name="categoryID" required>
                                                                        <option value="">Ch?n danh m?c</option>
                                                                        <c:forEach var="category" items="${categories}">
                                                                            <option value="${category.categoryID}">${category.categoryName}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">M� v?ch</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <input type="text" class="form-control" name="barcode">
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">M� s?n ph?m</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <input type="text" class="form-control" name="productCode">
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">K�ch th??c</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <select class="form-control" name="sizeID" required>
                                                                        <option value="">Ch?n k�ch th??c</option>
                                                                        <c:forEach var="size" items="${sizeSuggestions}">
                                                                            <option value="${size.sizeID}">${size.sizeValue}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">M�u s?c</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <select class="form-control" name="colorID" required>
                                                                        <option value="">Ch?n m�u s?c</option>
                                                                        <c:forEach var="color" items="${colorSuggestions}">
                                                                            <option value="${color.colorID}">${color.colorValue}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">??n v?</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <input type="text" class="form-control" name="unit" required>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">Gi� b�n</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <input type="number" step="0.01" class="form-control" name="sellingPrice" required>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">S? l??ng t?n</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <input type="number" class="form-control" name="stockQuantity" required>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">M� t?</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <textarea class="form-control" name="description" rows="4" required></textarea>
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <label class="col-sm-3 detail-label">H�nh ?nh</label>
                                                                <div class="col-sm-9 detail-value">
                                                                    <input type="file" class="form-control-file" name="image1" accept="image/*">
                                                                    <input type="file" class="form-control-file" name="image2" accept="image/*">
                                                                    <input type="file" class="form-control-file" name="image3" accept="image/*">
                                                                    <input type="file" class="form-control-file" name="image4" accept="image/*">
                                                                    <input type="file" class="form-control-file" name="image5" accept="image/*">
                                                                </div>
                                                            </div>
                                                            <div class="form-group row">
                                                                <div class="col-sm-9 offset-sm-3">
                                                                    <a href="${pageContext.request.contextPath}/products" class="btn btn-secondary">H?y</a>
                                                                    <button type="submit" class="btn btn-success">Th�m</button>
                                                                </div>
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
             <!--   <meta http-equiv="Content-Security-Policy" content="default-src 'self'; style-src 'self' https://fonts.googleapis.com https://fonts.gstatic.com; img-src 'self' data:; script-src 'none';">  -->
            </body>
</html>
