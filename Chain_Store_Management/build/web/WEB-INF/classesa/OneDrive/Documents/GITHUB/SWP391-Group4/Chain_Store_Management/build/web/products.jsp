<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            .btn-info {
                background-color: #17a2b8 !important;
                border-color: #17a2b8 !important;
                color: #fff !important;
            }
            .btn-info:hover {
                background-color: #138496 !important;
                border-color: #138496 !important;
            }
            .btn-sm {
                padding: 0.25rem 0.5rem;
                font-size: 0.875rem;
                line-height: 1.5;
                border-radius: 0.2rem;
            }
            .modal-content {
                border-radius: 0.3rem;
            }
            .modal-header {
                background-color: #007bff;
                color: #fff;
            }
            .modal-title {
                font-weight: 500;
            }
            .modal-footer {
                justify-content: flex-end;
            }
            .detail-label {
                font-weight: bold;
                margin-bottom: 5px;
            }
            .detail-value {
                margin-bottom: 15px;
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
                                            <li class="active"><a href="${pageContext.request.contextPath}/products">View Products</a></li>
                                            <!-- Removed Add Product -->
                                            <!-- Removed Manage Categories -->
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
                        <div class="pcoded-content">
                            <div class="page-header">
                                <div class="page-block">
                                    <div class="row align-items-center">
                                        <div class="col-md-8">
                                            <div class="page-header-title">
                                                <h5 class="m-b-10">View Products</h5>
                                                <p class="m-b-0">Monitor your product catalog</p>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <ul class="breadcrumb-title">
                                                <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                                <li class="breadcrumb-item"><a href="products.jsp">Products</a></li>
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
                                                        </div>
                                                        <div class="card-block">
                                                            <!-- Thêm form tìm kiếm và lọc -->
                                                            <div class="m-b-20">
                                                                <form action="${pageContext.request.contextPath}/products" method="get" class="form-inline">
                                                                    <div class="form-group mr-2">
                                                                        <input type="text" name="search" class="form-control" placeholder="Tìm kiếm..." value="${search}">
                                                                    </div>
                                                                    <div class="form-group mr-2">
                                                                        <select name="isActive" class="form-control">
                                                                            <option value="" <c:if test="${empty isActive}">selected</c:if>>Tất cả trạng thái</option>
                                                                            <option value="true" <c:if test="${isActive == true}">selected</c:if>>Kích hoạt</option>
                                                                            <option value="false" <c:if test="${isActive == false}">selected</c:if>>Không kích hoạt</option>
                                                                            </select>
                                                                        </div>
                                                                        <div class="form-group mr-2">
                                                                            <select name="categoryID" class="form-control">
                                                                                <option value="" <c:if test="${empty categoryId}">selected</c:if>>Tất cả danh mục</option>
                                                                            <c:forEach var="category" items="${categories}">
                                                                                <option value="${category.categoryID}" <c:if test="${categoryId == category.categoryID}">selected</c:if>>
                                                                                    <c:out value="${category.categoryName}"/>
                                                                                </option>
                                                                            </c:forEach>
                                                                        </select>
                                                                    </div>
                                                                    <button type="submit" class="btn btn-primary waves-effect waves-light"><i class="fa fa-search"></i> Lọc</button>
                                                                </form>
                                                            </div>
                                                            <p>Tổng sản phẩm: ${totalProducts}</p>
                                                            <p>Trang ${currentPage} / ${totalPages}</p>
                                                            <c:if test="${not empty message}">
                                                                <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                                                    <c:out value="${message}"/>
                                                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                                        <span aria-hidden="true">×</span>
                                                                    </button>
                                                                </div>
                                                            </c:if>
                                                            <div class="table-responsive">
                                                                <table class="table table-hover">
                                                                    <thead>
                                                                        <tr>
                                                                            <th>ID</th>
                                                                            <th>Tên</th>
                                                                            <th>Danh mục</th>
                                                                            <th>Kích thước</th>
                                                                            <th>Màu sắc</th>
                                                                            <th>Giá</th>
                                                                            <th>Trạng thái</th>
                                                                            <th>Hành động</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <c:forEach var="product" items="${products}">
                                                                            <tr>
                                                                                <td><c:out value="${product.productID}"/></td>
                                                                                <td><c:out value="${product.productName}"/></td>
                                                                                <td><c:out value="${product.categoryName}"/></td>
                                                                                <td><c:out value="${product.size}"/></td>
                                                                                <td><c:out value="${product.color}"/></td>
                                                                                <td><fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/></td>
                                                                                <td>
                                                                                    <label class="label label-<c:out value="${product.isActive ? 'success' : 'danger'}"/>">
                                                                                        <c:out value="${product.isActive ? 'Kích hoạt' : 'Không kích hoạt'}"/>
                                                                                    </label>
                                                                                </td>
                                                                                <td>
                                                                                    <button class="btn btn-sm btn-info waves-effect waves-light" data-toggle="modal" data-target="#productDetailsModal" onclick="populateDetails('<c:out value="${product.productID}"/>', '<c:out value="${product.productName}"/>', '<c:out value="${product.categoryName}"/>', '<c:out value="${product.size}"/>', '<c:out value="${product.color}"/>', ${product.price}, ${product.isActive})" title="Xem chi tiết">
                                                                                        <i class="fa fa-eye"></i>
                                                                                    </button>
                                                                                </td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                    </tbody>
                                                                </table>
                                                                <c:if test="${empty products}">
                                                                    <p class="text-center">Không có sản phẩm nào.</p>
                                                                </c:if>
                                                            </div>
                                                            <!-- Cập nhật phân trang để bao gồm các tham số lọc -->
                                                            <c:if test="${totalPages > 1}">
                                                                <nav aria-label="Page navigation">
                                                                    <ul class="pagination justify-content-center">
                                                                        <c:if test="${currentPage > 1}">
                                                                            <li class="page-item">
                                                                                <a class="page-link" href="${pageContext.request.contextPath}/products?page=${currentPage - 1}&search=${search}&isActive=${isActive}&categoryID=${categoryId}">Trước</a>
                                                                            </li>
                                                                        </c:if>
                                                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                                                <a class="page-link" href="${pageContext.request.contextPath}/products?page=${i}&search=${search}&isActive=${isActive}&categoryID=${categoryId}">${i}</a>
                                                                            </li>
                                                                        </c:forEach>
                                                                        <c:if test="${currentPage < totalPages}">
                                                                            <li class="page-item">
                                                                                <a class="page-link" href="${pageContext.request.contextPath}/products?page=${currentPage + 1}&search=${search}&isActive=${isActive}&categoryID=${categoryId}">Sau</a>
                                                                            </li>
                                                                        </c:if>
                                                                    </ul>
                                                                </nav>
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

            <!-- View Details Product Modal -->
            <div class="modal fade" id="productDetailsModal" tabindex="-1" role="dialog" aria-labelledby="productDetailsModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="productDetailsModalLabel">Product Details</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Product ID</label>
                                <div class="col-sm-9 detail-value" id="detailProductId"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Name</label>
                                <div class="col-sm-9 detail-value" id="detailProductName"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Category</label>
                                <div class="col-sm-9 detail-value" id="detailCategoryName"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Size</label>
                                <div class="col-sm-9 detail-value" id="detailSize"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Color</label>
                                <div class="col-sm-9 detail-value" id="detailColor"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Price</label>
                                <div class="col-sm-9 detail-value" id="detailPrice"></div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-3 detail-label">Status</label>
                                <div class="col-sm-9 detail-value" id="detailStatus"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
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
                                function populateDetails(id, name, category, size, color, price, isActive) {
                                    document.getElementById('detailProductId').textContent = id;
                                    document.getElementById('detailProductName').textContent = name;
                                    document.getElementById('detailCategoryName').textContent = category;
                                    document.getElementById('detailSize').textContent = size;
                                    document.getElementById('detailColor').textContent = color;
                                    document.getElementById('detailPrice').textContent = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(price);
                                    document.getElementById('detailStatus').textContent = isActive ? 'Active' : 'Inactive';
                                }
            </script>
    </body>
</html>