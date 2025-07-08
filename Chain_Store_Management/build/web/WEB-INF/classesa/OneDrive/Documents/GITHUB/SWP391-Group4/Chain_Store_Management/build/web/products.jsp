
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List, java.util.Base64" %>
<!DOCTYPE html>
<html>
<head>
    <title>View Products</title>
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
    <style>
        .pagination {
            margin-top: 20px;
            justify-content: center;
        }
        .pagination .page-link {
            color: #007bff;
        }
        .pagination .page-link:hover {
            background-color: #0056b3;
            color: #fff;
        }
        .pagination .page-item.active .page-link {
            background-color: #007bff;
            border-color: #007bff;
            color: #fff;
        }
        .search-bar {
            margin-bottom: 20px;
        }
        .toggle-btn {
            padding: 2px 6px;
            font-size: 0.8rem;
        }
        .modal-dialog {
            max-width: 800px;
        }
        .modal-body img {
            max-width: 200px;
            margin: 5px;
        }
        @media (max-width: 768px) {
            .card-header-right {
                margin-top: 10px;
            }
            .table-responsive {
                overflow-x: auto;
            }
        }
        @media print {
            .modal-content {
                border: none;
                box-shadow: none;
            }
            .modal-header, .modal-footer {
                display: none;
            }
            .modal-body {
                padding: 0;
            }
        }
    </style>
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
                                <span id="more-details"><c:out value="${sessionScope.user != null ? sessionScope.user.fullName : 'Guest'}"/> <i class="fa fa-caret-down"></i></span>
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
                            <form class="form-material" action="${pageContext.request.contextPath}/products" method="get">
                                <div class="form-group form-primary">
                                    <input type="text" name="search" class="form-control" placeholder="Search by name..." value="${param.search}">
                                    <span class="form-bar"></span>
                                    <button type="submit" class="btn btn-secondary" style="position: absolute; right: 0; top: 0;"><i class="fa fa-search"></i></button>
                                </div>
                            </form>
                        </div>
                        <div class="pcoded-navigation-label">Store Management</div>
                        <ul class="pcoded-item pcoded-left-item">
                            <li><a href="index.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-home"></i></span><span class="pcoded-mtext">Dashboard</span></a></li>
                            <li class="pcoded-hasmenu active"><a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-package"></i></span><span class="pcoded-mtext">Products</span><span class="pcoded-mcaret"></span></a>
                                <ul class="pcoded-submenu">
                                    <li class="active"><a href="${pageContext.request.contextPath}/products">View Products</a></li>
                                    <li><a href="${pageContext.request.contextPath}/add-edit-product?action=add">Add Product</a></li>
                                    <li><a href="categories.jsp">Manage Categories</a></li>
                                </ul>
                            </li>
                            <li class="pcoded-hasmenu"><a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-archive"></i></span><span class="pcoded-mtext">Inventory</span><span class="pcoded-mcaret"></span></a>
                                <ul class="pcoded-submenu"><li><a href="inventory.jsp">View Inventory</a></li><li><a href="warehouses.jsp">Manage Warehouses</a></li></ul>
                            </li>
                            <li class="pcoded-hasmenu"><a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-receipt"></i></span><span class="pcoded-mtext">Invoices</span><span class="pcoded-mcaret"></span></a>
                                <ul class="pcoded-submenu"><li><a href="invoices.jsp">View Invoices</a></li><li><a href="create-invoice.jsp">Create Invoice</a></li><li><a href="bank-transactions.jsp">Bank Transactions</a></li></ul>
                            </li>
                            <li class="pcoded-hasmenu"><a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-truck"></i></span><span class="pcoded-mtext">Purchases</span><span class="pcoded-mcaret"></span></a>
                                <ul class="pcoded-submenu"><li><a href="purchases.jsp">View Purchases</a></li><li><a href="create-purchase.jsp">Create Purchase</a></li></ul>
                            </li>
                            <li class="pcoded-hasmenu"><a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-user"></i></span><span class="pcoded-mtext">Employees</span><span class="pcoded-mcaret"></span></a>
                                <ul class="pcoded-submenu"><li><a href="employees.jsp">View Employees</a></li><li><a href="add-employee.jsp">Add Employee</a></li></ul>
                            </li>
                            <li><a href="SearchCustomerServlet" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-id-badge"></i></span><span class="pcoded-mtext">Customers</span></a></li>
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
                                        <h5 class="m-b-10">View Products</h5>
                                        <p class="m-b-0">Manage your product catalog</p>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <ul class="breadcrumb-title">
                                        <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
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
                                                    <h5>Product List</h5>
                                                    <div class="card-header-right">
                                                        <a href="${pageContext.request.contextPath}/add-edit-product?action=add" class="btn btn-primary btn-sm">Add New Product</a>
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
                                                    <div class="search-bar">
                                                        <form action="${pageContext.request.contextPath}/products" method="get" class="form-inline">
                                                            <div class="form-group mr-2">
                                                                <input type="text" name="search" class="form-control" placeholder="Search by name..." value="${param.search}" aria-label="Search">
                                                            </div>
                                                            <div class="form-group mr-2">
                                                                <select name="statusFilter" class="form-control">
                                                                    <option value="" ${param.statusFilter == null || param.statusFilter == '' ? 'selected' : ''}>All Status</option>
                                                                    <option value="active" ${param.statusFilter == 'active' ? 'selected' : ''}>Active</option>
                                                                    <option value="inactive" ${param.statusFilter == 'inactive' ? 'selected' : ''}>Inactive</option>
                                                                </select>
                                                            </div>
                                                            <button type="submit" class="btn btn-secondary"><i class="fa fa-search"></i> Search</button>
                                                        </form>
                                                    </div>
                                                    <p>Total Products: <c:out value="${totalProducts}"/></p>
                                                    <div class="table-responsive">
                                                        <table class="table table-hover">
                                                            <thead>
                                                                <tr>
                                                                 
                                                                    <th>Name</th>
                                                                    <th>Category</th>
                                                                    <th>Size</th>
                                                                    <th>Color</th>
                                                                    <th>Price (VND)</th>
                                                                    <th>Stock</th>
                                                                    <th>Status</th>
                                                                    <th>Actions</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach var="product" items="${products}">
                                                                    <tr>
                                                                        
                                                                        <td><c:out value="${product.productName}"/></td>
                                                                        <td><c:out value="${product.categoryName}"/></td>
                                                                        <td>
                                                                            <c:set var="sizeValue" value="N/A"/>
                                                                            <c:forEach var="size" items="${sizeSuggestions}">
                                                                                <c:if test="${size.sizeID == product.sizeID}">
                                                                                    <c:set var="sizeValue" value="${size.sizeValue}"/>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                            <c:out value="${sizeValue}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:set var="colorValue" value="N/A"/>
                                                                            <c:forEach var="color" items="${colorSuggestions}">
                                                                                <c:if test="${color.colorID == product.colorID}">
                                                                                    <c:set var="colorValue" value="${color.colorValue}"/>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                            <c:out value="${colorValue}"/>
                                                                        </td>
                                                                        <td><fmt:formatNumber value="${product.sellingPrice}" type="currency" currencyCode="VND"/></td>
                                                                        <td><c:out value="${product.stockQuantity}"/></td>
                                                                        <td>
                                                                            <span class="badge badge-<c:out value="${product.isActive ? 'success' : 'danger'}"/>">
                                                                                <c:out value="${product.isActive ? 'Active' : 'Inactive'}"/>
                                                                            </span>
                                                                        </td>
                                                                        <td>
                                                                            <form action="${pageContext.request.contextPath}/products" method="post" style="display:inline;">
                                                                                <input type="hidden" name="action" value="toggle">
                                                                                <input type="hidden" name="productId" value="${product.productID}">
                                                                                <input type="hidden" name="isActive" value="${not product.isActive}">
                                                                                <button type="submit" class="btn btn-sm toggle-btn btn-${product.isActive ? 'warning' : 'success'}" title="Toggle Status">
                                                                                    <i class="fa fa-toggle-${product.isActive ? 'off' : 'on'}"></i>
                                                                                </button>
                                                                            </form>
                                                                            <a href="${pageContext.request.contextPath}/add-edit-product?action=edit&productId=${product.productID}" class="btn btn-sm btn-primary" title="Edit Product">
                                                                                <i class="fa fa-edit"></i>
                                                                            </a>
                                                                            <button type="button" class="btn btn-sm btn-info" data-toggle="modal" data-target="#viewModal${product.productID}" title="View Product">
                                                                                <i class="fa fa-eye"></i>
                                                                            </button>
                                                                        </td>
                                                                    </tr>
                                                                    <!-- View Modal -->
                                                                    <div class="modal fade" id="viewModal${product.productID}" tabindex="-1" role="dialog" aria-labelledby="viewModalLabel${product.productID}" aria-hidden="true">
                                                                        <div class="modal-dialog" role="document">
                                                                            <div class="modal-content">
                                                                                <div class="modal-header">
                                                                                    <h5 class="modal-title" id="viewModalLabel${product.productID}">View Product</h5>
                                                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                                        <span aria-hidden="true">×</span>
                                                                                    </button>
                                                                                </div>
                                                                                <div class="modal-body">
                                                                                    <p><strong>ID:</strong> <c:out value="${product.productID}"/></p>
                                                                                    <p><strong>Name:</strong> <c:out value="${product.productName}"/></p>
                                                                                    <p><strong>Category:</strong> <c:out value="${product.categoryName}"/></p>
                                                                                    <p><strong>Size:</strong>
                                                                                        <c:set var="sizeValue" value="N/A"/>
                                                                                        <c:forEach var="size" items="${sizeSuggestions}">
                                                                                            <c:if test="${size.sizeID == product.sizeID}">
                                                                                                <c:set var="sizeValue" value="${size.sizeValue}"/>
                                                                                            </c:if>
                                                                                        </c:forEach>
                                                                                        <c:out value="${sizeValue}"/>
                                                                                    </p>
                                                                                    <p><strong>Color:</strong>
                                                                                        <c:set var="colorValue" value="N/A"/>
                                                                                        <c:forEach var="color" items="${colorSuggestions}">
                                                                                            <c:if test="${color.colorID == product.colorID}">
                                                                                                <c:set var="colorValue" value="${color.colorValue}"/>
                                                                                            </c:if>
                                                                                        </c:forEach>
                                                                                        <c:out value="${colorValue}"/>
                                                                                    </p>
                                                                                    <p><strong>Price:</strong> <fmt:formatNumber value="${product.sellingPrice}" type="currency" currencyCode="VND"/></p>
                                                                                    <p><strong>Stock:</strong> <c:out value="${product.stockQuantity}"/></p>
                                                                                    <p><strong>Unit:</strong>
                                                                                        <c:set var="unitValue" value="${product.unit}"/>
                                                                                        <c:choose>
                                                                                            <c:when test="${unitValue == 'cái'}">Cái</c:when>
                                                                                            <c:when test="${unitValue == 'chiếc'}">Chiếc</c:when>
                                                                                            <c:when test="${unitValue == 'bộ'}">Bộ</c:when>
                                                                                            <c:when test="${unitValue == 'thùng'}">Thùng</c:when>
                                                                                            <c:otherwise><c:out value="${unitValue}"/></c:otherwise>
                                                                                        </c:choose>
                                                                                    </p>
                                                                                    <p><strong>Barcode:</strong> <c:out value="${product.barcode}"/></p>
                                                                                    <p><strong>Product Code:</strong> <c:out value="${product.productCode}"/></p>
                                                                                    <p><strong>Release Date:</strong> <fmt:formatDate value="${product.releaseDate}" pattern="dd/MM/yyyy"/></p>
                                                                                    <p><strong>Status:</strong> <span class="badge badge-${product.isActive ? 'success' : 'danger'}"><c:out value="${product.isActive ? 'Active' : 'Inactive'}"/></span></p>
                                                                                    <p><strong>Description:</strong> <c:out value="${product.description}"/></p>
                                                                                    <c:if test="${not empty product.images}">
                                                                                        <p><strong>Images:</strong></p>
                                                                                        <c:forTokens var="image" items="${product.images}" delims=";">
                                                                                            <img src="${image}" alt="Product Image" style="max-width: 200px; margin: 5px;" onload="this.style.display = 'block';">
                                                                                        </c:forTokens>
                                                                                    </c:if>
                                                                                </div>
                                                                                <div class="modal-footer">
                                                                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                                    <button type="button" class="btn btn-primary" onclick="window.print()">Print</button>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                        <c:if test="${empty products}">
                                                            <p class="text-center">No products available.</p>
                                                        </c:if>
                                                    </div>
                                                    <jsp:include page="pagination.jsp">
                                                        <jsp:param name="baseUrl" value="${pageContext.request.contextPath}/products"/>
                                                        <jsp:param name="search" value="${param.search}"/>
                                                        <jsp:param name="statusFilter" value="${param.statusFilter}"/>
                                                    </jsp:include>
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
        <script>
            function clearTempImages(productId) {
                window.location.href = "${pageContext.request.contextPath}/products?clearTemp=" + productId;
            }
        </script>
    </body>
</html>
