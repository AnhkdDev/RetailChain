<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>${action == 'edit' ? 'Edit Product' : 'Add Product'}</title>
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
        .invalid-feedback { display: none; }
        .form-group.invalid .invalid-feedback { display: block; color: #dc3545; }
        .preview-image { max-width: 200px; max-height: 200px; object-fit: cover; border: 1px solid #ddd; border-radius: 4px; padding: 2px; margin: 5px; }
        .preview-container { position: relative; margin-top: 10px; }
        .remove-image { position: absolute; top: 0; right: 0; background: red; color: white; border: none; border-radius: 50%; width: 20px; height: 20px; line-height: 20px; text-align: center; cursor: pointer; }
        @media (max-width: 768px) {
            .table-responsive { overflow-x: auto; }
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
                                        <h5 class="m-b-10">${action == 'edit' ? 'Edit Product' : 'Add Product'}</h5>
                                        <p class="m-b-0">${action == 'edit' ? 'Update product details' : 'Add a new product to the catalog'}</p>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <ul class="breadcrumb-title">
                                        <li class="breadcrumb-item"><a href="index.jsp"><i class="fa fa-home"></i></a></li>
                                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
                                        <li class="breadcrumb-item">${action == 'edit' ? 'Edit Product' : 'Add Product'}</li>
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
                                                    <h5>${action == 'edit' ? 'Edit Product' : 'Add Product'}</h5>
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
                                                    <form action="${pageContext.request.contextPath}/add-edit-product" method="post" enctype="multipart/form-data">
                                                        <input type="hidden" name="action" value="${action == 'edit' ? 'save' : 'add'}">
                                                        <input type="hidden" name="productId" value="${product != null ? product.productID : ''}">
                                                        <input type="hidden" name="search" value="${param.search}">
                                                        <input type="hidden" name="page" value="${currentPage}">
                                                        <div class="form-group row ${empty product.productName && action == 'edit' || (not empty errors && errors.containsKey('productName')) ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Tên hàng</label>
                                                            <div class="col-sm-9">
                                                                <input type="text" class="form-control" name="productName" value="${product != null ? product.productName : ''}" required>
                                                                <div class="invalid-feedback">${errors.productName}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ${not empty errors && errors.containsKey('categoryID') ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Danh mục</label>
                                                            <div class="col-sm-9">
                                                                <select class="form-control" name="categoryID" required>
                                                                    <option value="">Chọn danh mục</option>
                                                                    <c:forEach var="category" items="${categories}">
                                                                        <option value="${category.categoryID}" ${product != null && category.categoryID == product.categoryID ? 'selected' : ''}>${category.categoryName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <div class="invalid-feedback">${errors.categoryID}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row">
                                                            <label class="col-sm-3 col-form-label">Mã vạch</label>
                                                            <div class="col-sm-9">
                                                                <input type="text" class="form-control" name="barcode" value="${product != null ? product.barcode : ''}">
                                                            </div>
                                                        </div>
                                                        <div class="form-group row">
                                                            <label class="col-sm-3 col-form-label">Mã sản phẩm</label>
                                                            <div class="col-sm-9">
                                                                <input type="text" class="form-control" name="productCode" value="${product != null ? product.productCode : ''}">
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ${not empty errors && errors.containsKey('sizeID') ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Kích thước</label>
                                                            <div class="col-sm-9">
                                                                <select class="form-control" name="sizeID">
                                                                    <option value="">Chọn kích thước</option>
                                                                    <c:forEach var="size" items="${sizeSuggestions}">
                                                                        <option value="${size.sizeID}" ${product != null && size.sizeID == product.sizeID ? 'selected' : ''}>${size.sizeValue}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <div class="invalid-feedback">${errors.sizeID}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ${not empty errors && errors.containsKey('colorID') ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Màu sắc</label>
                                                            <div class="col-sm-9">
                                                                <select class="form-control" name="colorID">
                                                                    <option value="">Chọn màu sắc</option>
                                                                    <c:forEach var="color" items="${colorSuggestions}">
                                                                        <option value="${color.colorID}" ${product != null && color.colorID == product.colorID ? 'selected' : ''}>${color.colorValue}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <div class="invalid-feedback">${errors.colorID}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ${empty product.unit && action == 'edit' || (not empty errors && errors.containsKey('unit')) ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Đơn vị</label>
                                                            <div class="col-sm-9">
                                                                <select class="form-control" name="unit" required>
                                                                    <option value="">Chọn đơn vị</option>
                                                                    <option value="cái" ${product != null && product.unit == 'cái' ? 'selected' : ''}>Cái</option>
                                                                    <option value="chiếc" ${product != null && product.unit == 'chiếc' ? 'selected' : ''}>Chiếc</option>
                                                                    <option value="bộ" ${product != null && product.unit == 'bộ' ? 'selected' : ''}>Bộ</option>
                                                                    <option value="thùng" ${product != null && product.unit == 'thùng' ? 'selected' : ''}>Thùng</option>
                                                                </select>
                                                                <div class="invalid-feedback">${errors.unit}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ${empty product.sellingPrice && action == 'edit' || (not empty errors && errors.containsKey('sellingPrice')) ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Giá bán</label>
                                                            <div class="col-sm-9">
                                                                <input type="number" step="0.01" class="form-control" name="sellingPrice" value="${product != null ? product.sellingPrice : ''}" required>
                                                                <div class="invalid-feedback">${errors.sellingPrice}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ${empty product.stockQuantity && action == 'edit' || (not empty errors && errors.containsKey('stockQuantity')) ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Số lượng tồn</label>
                                                            <div class="col-sm-9">
                                                                <input type="number" class="form-control" name="stockQuantity" value="${product != null ? product.stockQuantity : ''}" required>
                                                                <div class="invalid-feedback">${errors.stockQuantity}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ${empty product.description && action == 'edit' || (not empty errors && errors.containsKey('description')) ? 'invalid' : ''}">
                                                            <label class="col-sm-3 col-form-label">Mô tả</label>
                                                            <div class="col-sm-9">
                                                                <textarea class="form-control" name="description" rows="4" required>${product != null ? product.description : ''}</textarea>
                                                                <div class="invalid-feedback">${errors.description}</div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row">
                                                            <label class="col-sm-3 col-form-label">Hình ảnh</label>
                                                            <div class="col-sm-9">
                                                                <c:if test="${action == 'edit' && not empty product.images}">
                                                                    <p><strong>Ảnh hiện tại:</strong></p>
                                                                    <c:forTokens var="image" items="${product.images}" delims=";" varStatus="status">
                                                                        <div class="preview-container" id="currentImageContainer${status.index + 1}">
                                                                            <c:set var="imageUrl" value="${pageContext.request.contextPath}/${image}?t=${applicationScope.now.time}" />
                                                                            <c:if test="${not empty image}">
                                                                                <img src="${imageUrl}" class="preview-image" id="currentImage${status.index + 1}" alt="Current Image" onerror="this.className+=' error-image'; console.log('Image load failed for: ${imageUrl}')">
                                                                            </c:if>
                                                                            <button type="button" class="remove-image" onclick="removeImage('currentImageContainer${status.index + 1}', 'imagePath${status.index + 1}')">X</button>
                                                                            <input type="hidden" name="imagePath${status.index + 1}" id="imagePath${status.index + 1}" value="${image}">
                                                                        </div>
                                                                    </c:forTokens>
                                                                </c:if>
                                                                <input type="file" class="form-control-file ${errors.image1 != null ? 'is-invalid' : ''}" id="image1" name="image1" accept=".jpg,.jpeg,.png" onchange="previewImage(this, 'previewContainer1', 'previewImage1')">
                                                                <small class="form-text text-muted">Kích thước tối đa: 2MB. Định dạng: .jpg, .jpeg, .png</small>
                                                                <div class="invalid-feedback">${errors.image1}</div>
                                                                <div id="previewContainer1" class="preview-container" style="display: none;">
                                                                    <img id="previewImage1" class="preview-image" alt="Xem trước hình ảnh">
                                                                    <button type="button" class="remove-image" onclick="clearPreview('previewContainer1', 'previewImage1', 'image1')">X</button>
                                                                </div>
                                                                <input type="file" class="form-control-file ${errors.image2 != null ? 'is-invalid' : ''}" id="image2" name="image2" accept=".jpg,.jpeg,.png" onchange="previewImage(this, 'previewContainer2', 'previewImage2')">
                                                                <small class="form-text text-muted">Kích thước tối đa: 2MB. Định dạng: .jpg, .jpeg, .png</small>
                                                                <div class="invalid-feedback">${errors.image2}</div>
                                                                <div id="previewContainer2" class="preview-container" style="display: none;">
                                                                    <img id="previewImage2" class="preview-image" alt="Xem trước hình ảnh">
                                                                    <button type="button" class="remove-image" onclick="clearPreview('previewContainer2', 'previewImage2', 'image2')">X</button>
                                                                </div>
                                                                <input type="file" class="form-control-file ${errors.image3 != null ? 'is-invalid' : ''}" id="image3" name="image3" accept=".jpg,.jpeg,.png" onchange="previewImage(this, 'previewContainer3', 'previewImage3')">
                                                                <small class="form-text text-muted">Kích thước tối đa: 2MB. Định dạng: .jpg, .jpeg, .png</small>
                                                                <div class="invalid-feedback">${errors.image3}</div>
                                                                <div id="previewContainer3" class="preview-container" style="display: none;">
                                                                    <img id="previewImage3" class="preview-image" alt="Xem trước hình ảnh">
                                                                    <button type="button" class="remove-image" onclick="clearPreview('previewContainer3', 'previewImage3', 'image3')">X</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row">
                                                            <div class="col-sm-9 offset-sm-3">
                                                                <a href="${pageContext.request.contextPath}/products?clearTemp=${product != null ? product.productID : ''}" class="btn btn-secondary">Cancel</a>
                                                                <button type="submit" class="btn btn-success">${action == 'edit' ? 'Save Changes' : 'Add Product'}</button>
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
        function previewImage(input, containerId, previewId) {
            var file = input.files[0];
            var previewContainer = document.getElementById(containerId);
            var previewImage = document.getElementById(previewId);
            if (file) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    previewContainer.style.display = 'block';
                    previewImage.src = e.target.result;
                };
                reader.readAsDataURL(file);
            } else {
                previewContainer.style.display = 'none';
                previewImage.src = '';
            }
        }

        function clearPreview(containerId, previewId, inputId) {
            var previewContainer = document.getElementById(containerId);
            var previewImage = document.getElementById(previewId);
            var input = document.getElementById(inputId);
            previewContainer.style.display = 'none';
            previewImage.src = '';
            input.value = '';
        }

        function removeImage(containerId, inputId) {
            var container = document.getElementById(containerId);
            var input = document.getElementById(inputId);
            container.remove();
            input.value = '';
        }
    </script>
</body>
</html>