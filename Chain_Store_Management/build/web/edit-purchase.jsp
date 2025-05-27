<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Purchase</title>
    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="description" content="Edit purchase details for the store." />
    <meta name="keywords" content="purchases, inventory, retail" />
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

    <!-- Custom CSS cho trang Edit Purchase -->
    <style>
        body {
            background-color: #f4f7f6;
            font-family: 'Roboto', sans-serif;
        }

        .pcoded-content {
            padding: 20px;
        }

        .page-header {
            background: #fff;
            padding: 15px 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .page-header h5 {
            font-size: 1.5rem;
            color: #333;
            margin: 0;
        }

        .page-body {
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            max-width: 600px;
            margin: 0 auto;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            font-weight: 500;
            color: #555;
            margin-bottom: 8px;
            display: block;
        }

        .form-group input {
            width: 100%;
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 0.95rem;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-group input:focus {
            border-color: #4099ff;
            box-shadow: 0 0 5px rgba(64, 153, 255, 0.3);
            outline: none;
        }

        .btn {
            padding: 10px 20px;
            font-size: 0.95rem;
            border-radius: 5px;
            transition: background-color 0.3s ease, transform 0.1s ease;
        }

        .btn-primary {
            background-color: #4099ff;
            border: none;
        }

        .btn-primary:hover {
            background-color: #2e86de;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background-color: #6c757d;
            border: none;
            color: #fff;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
            transform: translateY(-2px);
        }

        .button-group {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }

        @media (max-width: 576px) {
            .page-body {
                padding: 20px;
            }

            .button-group {
                flex-direction: column;
                gap: 8px;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="pcoded-content">
        <div class="page-header">
            <h5>Edit Purchase</h5>
        </div>
        <div class="page-body">
            <form action="Purchase" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="purchaseID" value="${purchase.purchaseID}">
                <div class="form-group">
                    <label for="purchaseDate">Date</label>
                    <input type="datetime-local" id="purchaseDate" name="purchaseDate" value="<fmt:formatDate value='${purchase.purchaseDate}' pattern='yyyy-MM-dd\'T\'HH:mm' />" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="supplierName">Supplier</label>
                    <input type="text" id="supplierName" name="supplierName" value="${purchase.supplierName}" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="warehouseName">Warehouse</label>
                    <input type="text" id="warehouseName" name="warehouseName" value="${purchase.warehouseName}" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="totalAmount">Total Amount</label>
                    <input type="number" id="totalAmount" name="totalAmount" value="${purchase.totalAmount}" class="form-control" step="0.01" required>
                </div>
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="Purchase" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
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