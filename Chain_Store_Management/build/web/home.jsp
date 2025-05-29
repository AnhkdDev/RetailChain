<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop Management System</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f4f8;
            color: #333;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .header {
            background: linear-gradient(90deg, #1e3c72, #2a5298);
            padding: 10px 30px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
            border-bottom-left-radius: 20px;
            border-bottom-right-radius: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: sticky;
            top: 0;
            z-index: 100;
        }
        .logo {
            font-size: 22px;
            font-weight: bold;
            color: #fff;
            display: flex;
            align-items: center;
            text-transform: uppercase;
        }
        .logo img {
            width: 24px;
            height: 24px;
            margin-right: 8px;
        }
        .nav {
            display: flex;
            align-items: center;
            flex-wrap: nowrap;
        }
        .nav a {
            margin-left: 8px;
            text-decoration: none;
            color: #fff;
            font-weight: 500;
            padding: 4px 8px;
            border-radius: 12px;
            transition: background-color 0.3s ease, transform 0.2s ease;
            display: flex;
            align-items: center;
            font-size: 12px;
            white-space: nowrap;
        }
        .nav a img {
            width: 14px;
            height: 14px;
            margin-right: 5px;
        }
        .nav a:hover {
            background-color: rgba(255, 255, 255, 0.15);
            transform: translateY(-2px);
        }
        .login-btn {
            background-color: transparent;
            color: #fff;
            padding: 4px 8px;
            border: 1px solid #fff;
            border-radius: 12px;
            cursor: pointer;
            margin-left: 8px;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
            white-space: nowrap;
        }
        .login-btn:hover {
            background-color: rgba(255, 255, 255, 0.15);
        }
        .register-btn {
            background-color: transparent;
            color: #fff;
            padding: 4px 8px;
            border: 1px solid #fff;
            border-radius: 12px;
            cursor: pointer;
            margin-left: 8px;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
            white-space: nowrap;
        }
        .register-btn:hover {
            background-color: rgba(255, 255, 255, 0.15);
        }
        .main-content {
            max-width: 1200px;
            margin: 40px auto;
            text-align: center;
            position: relative;
            flex: 1;
        }
        .title {
            font-size: 36px;
            color: #1e3c72;
            margin-bottom: 20px;
        }
        .buttons {
            margin: 20px 0;
        }
        .btn {
            background-color: #1e3c72;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 5px;
            margin: 0 10px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn:hover {
            background-color: #2a5298;
        }
        .stats {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-top: 30px;
        }
        .stat {
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            width: 200px;
        }
        .stat span {
            font-size: 24px;
            color: #1e3c72;
            font-weight: bold;
        }
        .image {
            position: absolute;
            right: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 300px;
            opacity: 0.8;
        }
        .overview {
            margin-top: 20px;
        }
        .overview div {
            margin: 10px 0;
            font-size: 18px;
        }
        .footer {
            background-color: #2a5298;
            padding: 20px;
            text-align: center;
            box-shadow: 0 -2px 5px rgba(0,0,0,0.1);
            color: #FFFFFF;
            width: 100%;
        }
        @media (max-width: 768px) {
            .header {
                flex-direction: row; /* Giữ hàng ngang trên màn hình vừa */
                padding: 8px 20px;
                flex-wrap: wrap;
            }
            .logo {
                flex-shrink: 0;
            }
            .nav {
                flex-wrap: wrap;
                justify-content: flex-end;
                flex: 1;
            }
            .nav a {
                margin: 4px 6px;
                padding: 4px 8px;
                font-size: 11px;
            }
            .nav a img {
                width: 13px;
                height: 13px;
            }
            .login-btn, .register-btn {
                margin: 4px 6px;
                padding: 4px 8px;
                font-size: 11px;
            }
            .image {
                display: none;
            }
            .stats {
                flex-direction: column;
                align-items: center;
            }
            .stat {
                width: 80%;
            }
        }
        @media (max-width: 480px) {
            .header {
                flex-direction: column; /* Chuyển sang cột trên màn hình nhỏ */
                padding: 6px 15px;
            }
            .nav {
                justify-content: center;
                margin-top: 6px;
            }
            .logo {
                font-size: 20px;
            }
            .logo img {
                width: 20px;
                height: 20px;
            }
            .nav a, .login-btn, .register-btn {
                font-size: 10px;
                padding: 3px 6px;
            }
            .nav a img {
                width: 12px;
                height: 12px;
            }
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="logo">
            <img src="https://img.icons8.com/ios-filled/50/ffffff/shop.png" alt="Shop Icon">
            ShopViet
        </div>
      <div class="nav">
    <a href="#"><img src="https://img.icons8.com/ios-filled/50/ffffff/shopping-bag.png" alt="Products Icon">Products</a>
    <a href="solutions.jsp"><img src="https://img.icons8.com/ios-filled/50/ffffff/folder.png" alt="Solutions Icon">Solutions</a>
    <a href="customers.jsp"><img src="https://img.icons8.com/ios-filled/50/ffffff/users.png" alt="Customers Icon">Customers</a>
    <a href="#"><img src="https://img.icons8.com/ios-filled/50/ffffff/price-tag.png" alt="Service Fees Icon">Fees</a>
    <a href="#"><img src="https://img.icons8.com/ios-filled/50/ffffff/headset.png" alt="Support Icon">Support</a>
    <a href="login.jsp" class="login-btn">Login</a>
    <a href="register.jsp" class="register-btn">Register</a>
</div>
    </div>
    <div class="main-content">
        <h1 class="title">The Most Popular Sales Management Software</h1>
        <div class="buttons">
            <button class="btn" onclick="tryFree()">Try for Free</button>
            <button class="btn" onclick="explore()">Explore</button>
        </div>
        <div class="stats">
            <div class="stat">300,000+ <br> businesses using</div>
            <div class="stat">10,000+ <br> businesses monthly</div>
        </div>
        <img src="https://via.placeholder.com/300" alt="Staff Image" class="image">
<!--        <div class="overview">
            <div>Store Overview</div>
            <div>Today's Revenue: 5,000,000 VND</div>
            <div>New Orders: 15</div>
            <div>Customers: 120</div>
        </div>-->
    </div>
    <div class="footer">
        <p>Address: 123 Main Street, Ho Chi Minh City, Vietnam</p>
        <p>Contact: Phone: +84 901 234 561 | Email: support@shopviet.com</p>
    </div>

    <script>
        function tryFree() {
            alert('Start your free trial!');
        }

        function explore() {
            alert('Explore our features!');
        }
    </script>
</body>
</html>