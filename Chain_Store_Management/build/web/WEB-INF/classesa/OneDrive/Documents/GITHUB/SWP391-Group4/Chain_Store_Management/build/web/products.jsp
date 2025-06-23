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
            .invalid-feedback {
                display: none;
            }
            .form-group.invalid .invalid-feedback {
                display: block;
                color: #dc3545;
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
                                            <input type="text" name="search" class="form-control" placeholder="Search by name..." value="${param.search}" required>
                                            <span class="form-bar"></span>
                                            <button type="submit" class="btn btn-secondary" style="position: absolute; right: 0; top: 0;"><i class="fa fa-search"></i></button>
                                        </div>
                                    </form>
                                </div>
                                <div class="pcoded-navigation-label">Store Management</div>
                                <ul class="pcoded-item pcoded-left-item">
                                    <li><a href="index.jsp" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-home"></i></span><span class="pcoded-mtext">Dashboard</span></a></li>
                                    <li class="pcoded-hasmenu active"><a href="javascript:void(0)" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-package"></i></span><span class="pcoded-mtext">Products</span><span class="pcoded-mcaret"></span></a>
                                        <ul class="pcoded-submenu"><li class="active"><a href="${pageContext.request.contextPath}/products">View Products</a></li><li><a href="add-product.jsp">Add Product</a></li><li><a href="categories.jsp">Manage Categories</a></li></ul>
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
                                    <li><a href="CustomerListServlet" class="waves-effect waves-dark"><span class="pcoded-micon"><i class="ti-id-badge"></i></span><span class="pcoded-mtext">Customers</span></a></li>
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
                                                                <a href="${pageContext.request.contextPath}/add-product?action=loadDropdowns" class="btn btn-primary btn-sm">Add New Product</a>
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
                                                                    <button type="submit" class="btn btn-secondary"><i class="fa fa-search"></i> Search</button>
                                                                </form>
                                                            </div>
                                                            <p>Total Products: <c:out value="${totalProducts}"/></p>
                                                            <div class="table-responsive">
                                                                <table class="table table-hover">
                                                                    <thead>
                                                                        <tr>
                                                                            <th>ID</th>
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
                                                                                <td><c:out value="${product.productID}"/></td>
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
                                                                                        <button type="submit" class="btn btn-sm toggle-btn btn-${product.isActive ? 'warning' : 'success'}"
                                                                                                title="Toggle Status">
                                                                                            <i class="fa fa-toggle-${product.isActive ? 'off' : 'on'}"></i>
                                                                                        </button>
                                                                                    </form>
                                                                                    <button type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#editModal${product.productID}">
                                                                                        <i class="fa fa-edit"></i>
                                                                                    </button>
                                                                                    <button type="button" class="btn btn-sm btn-info" data-toggle="modal" data-target="#viewModal${product.productID}">
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
                                                                                                    <img src="${image}" alt="Product Image" style="max-width: 200px; margin: 5px;" onload="this.style.display='block';">
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

                                                                            <!-- Edit Modal -->
                                                                            <div class="modal fade" id="editModal${product.productID}" tabindex="-1" role="dialog" aria-labelledby="editModalLabel${product.productID}" aria-hidden="true">
                                                                                <div class="modal-dialog" role="document">
                                                                                    <div class="modal-content">
                                                                                        <div class="modal-header">
                                                                                            <h5 class="modal-title" id="editModalLabel${product.productID}">Edit Product</h5>
                                                                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="clearTempImages(${product.productID})">
                                                                                                <span aria-hidden="true">×</span>
                                                                                            </button>
                                                                                        </div>
                                                                                        <div class="modal-body">
                                                                                            <form action="${pageContext.request.contextPath}/update-product" method="post" enctype="multipart/form-data">
                                                                                                <input type="hidden" name="productId" value="${product.productID}">
                                                                                                <input type="hidden" name="action" value="save">
                                                                                                <input type="hidden" name="search" value="${param.search}">
                                                                                                <input type="hidden" name="page" value="${currentPage}">
                                                                                                <div class="form-group row ${empty product.productName || (not empty errors and errors.contains('Product name is required.')) ? 'invalid' : ''}">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Tên hàng</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <input type="text" class="form-control" name="productName" value="${not empty product ? product.productName : ''}" required>
                                                                                                        <div class="invalid-feedback">Tên hàng không được để trống.</div>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Danh mục</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <select class="form-control" name="categoryID" required>
                                                                                                            <option value="">Chọn danh mục</option>
                                                                                                            <c:forEach var="category" items="${categories}">
                                                                                                                <option value="${category.categoryID}" ${not empty product and category.categoryID == product.categoryID ? 'selected' : ''}>${category.categoryName}</option>
                                                                                                            </c:forEach>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Mã vạch</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <input type="text" class="form-control" name="barcode" value="${not empty product ? product.barcode : ''}">
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Mã sản phẩm</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <input type="text" class="form-control" name="productCode" value="${not empty product ? product.productCode : ''}">
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Kích thước</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <select class="form-control" name="sizeID" required>
                                                                                                            <option value="">Chọn kích thước</option>
                                                                                                            <c:forEach var="size" items="${sizeSuggestions}">
                                                                                                                <option value="${size.sizeID}" ${not empty product and size.sizeID == product.sizeID ? 'selected' : ''}>${size.sizeValue}</option>
                                                                                                            </c:forEach>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Màu sắc</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <select class="form-control" name="colorID" required>
                                                                                                            <option value="">Chọn màu sắc</option>
                                                                                                            <c:forEach var="color" items="${colorSuggestions}">
                                                                                                                <option value="${color.colorID}" ${not empty product and color.colorID == product.colorID ? 'selected' : ''}>${color.colorValue}</option>
                                                                                                            </c:forEach>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row ${empty product.unit || (not empty errors and errors.contains('Unit is required.')) ? 'invalid' : ''}">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Đơn vị</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <select class="form-control" name="unit" required>
                                                                                                            <option value="">Chọn đơn vị</option>
                                                                                                            <option value="cái" ${product.unit == 'cái' ? 'selected' : ''}>Cái</option>
                                                                                                            <option value="chiếc" ${product.unit == 'chiếc' ? 'selected' : ''}>Chiếc</option>
                                                                                                            <option value="bộ" ${product.unit == 'bộ' ? 'selected' : ''}>Bộ</option>
                                                                                                            <option value="thùng" ${product.unit == 'thùng' ? 'selected' : ''}>Thùng</option>
                                                                                                        </select>
                                                                                                        <div class="invalid-feedback">Đơn vị không được để trống.</div>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row ${empty product.sellingPrice || (not empty errors and errors.contains('Price is required.')) ? 'invalid' : ''}">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Giá bán</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <input type="number" step="0.01" class="form-control" name="sellingPrice" value="${not empty product ? product.sellingPrice : ''}" required>
                                                                                                        <div class="invalid-feedback">Giá bán không được để trống.</div>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row ${empty product.stockQuantity || (not empty errors and errors.contains('Stock quantity is required.')) ? 'invalid' : ''}">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Số lượng tồn</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <input type="number" class="form-control" name="stockQuantity" value="${not empty product ? product.stockQuantity : ''}" required>
                                                                                                        <div class="invalid-feedback">Số lượng tồn không được để trống.</div>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row ${empty product.description || (not empty errors and errors.contains('Description is required.')) ? 'invalid' : ''}">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Mô tả</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <textarea class="form-control" name="description" rows="4" required>${not empty product ? product.description : ''}</textarea>
                                                                                                        <div class="invalid-feedback">Mô tả không được để trống.</div>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row">
                                                                                                    <label class="col-sm-3 col-form-label detail-label">Hình ảnh</label>
                                                                                                    <div class="col-sm-9">
                                                                                                        <!-- Hiển thị ảnh hiện tại từ database -->
                                                                                                        <c:if test="${not empty product.images}">
                                                                                                            <c:forTokens var="image" items="${product.images}" delims=";">
                                                                                                                <img src="${image}" alt="Current Image" style="max-width: 200px; margin: 5px;">
                                                                                                            </c:forTokens>
                                                                                                        </c:if>
                                                                                                        <!-- Hiển thị ảnh tạm thời từ session (byte[] to base64) -->
                                                                                                        <c:if test="${not empty tempImageBytes and not empty product}">
                                                                                                            <% 
                                                                                                                java.util.List<byte[]> tempImageBytes = (java.util.List<byte[]>) request.getAttribute("tempImageBytes");
                                                                                                                if (tempImageBytes != null) {
                                                                                                                    for (byte[] imageBytes : tempImageBytes) {
                                                                                                                        String base64Image = "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(imageBytes);
                                                                                                            %>
                                                                                                                <img src="<%= base64Image %>" alt="Temp Image" style="max-width: 200px; margin: 5px;">
                                                                                                            <%
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    out.println("No temp images found for productId: " + request.getAttribute("productId"));
                                                                                                                }
                                                                                                            %>
                                                                                                        </c:if>
                                                                                                        <input type="file" class="form-control-file" name="image1" accept="image/*">
                                                                                                        <input type="file" class="form-control-file" name="image2" accept="image/*">
                                                                                                        <input type="file" class="form-control-file" name="image3" accept="image/*">
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group row">
                                                                                                    <div class="col-sm-9 offset-sm-3">
                                                                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="clearTempImages(${product.productID})">Cancel</button>
                                                                                                        <button type="submit" class="btn btn-success">Save Changes</button>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </form>
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
                                                                <jsp:param name="status" value="${isActive}"/>
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