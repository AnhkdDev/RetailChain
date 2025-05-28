<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Người Dùng - Admin 1</title>
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
            background: linear-gradient(135deg, #007bff, #00aced); /* Gradient background */
            padding: 15px 30px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.15);
            border-bottom-left-radius: 15px;
            border-bottom-right-radius: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: sticky;
            top: 0;
            z-index: 100;
        }
        .logo {
            font-size: 28px;
            font-weight: bold;
            background: linear-gradient(90deg, #ffffff, #e6f0fa);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            display: flex;
            align-items: center;
        }
        .logo img {
            width: 30px;
            height: 30px;
            margin-right: 10px;
        }
        .nav {
            display: flex;
            align-items: center;
        }
        .nav a {
            margin-left: 25px;
            text-decoration: none;
            color: #fff;
            font-weight: 500;
            padding: 8px 15px;
            border-radius: 20px;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }
        .nav a:hover {
            background-color: rgba(255, 255, 255, 0.2);
            transform: translateY(-2px);
        }
        .profile-container {
            max-width: 600px;
            margin: 40px auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .profile-container h1 {
            color: #007bff;
            text-align: center;
            margin-bottom: 20px;
        }
        .profile-info {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 15px;
        }
        .profile-info div {
            padding: 10px;
            border-bottom: 1px solid #eee;
        }
        .profile-info .label {
            font-weight: bold;
            color: #555;
        }
        .profile-info .value {
            color: #333;
        }
        .actions {
            text-align: center;
            margin-top: 20px;
        }
        .btn {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin: 0 10px;
            text-decoration: none;
            display: inline-block;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .footer {
            background-color: #00ACED;
            padding: 20px;
            text-align: center;
            box-shadow: 0 -2px 5px rgba(0,0,0,0.1);
            color: #FFFFFF;
            width: 100%;
            margin-top: auto;
        }
        @media (max-width: 768px) {
            .header {
                flex-direction: column;
                padding: 10px 20px;
            }
            .nav {
                margin-top: 10px;
                flex-wrap: wrap;
                justify-content: center;
            }
            .nav a {
                margin: 5px 10px;
            }
            .profile-info {
                grid-template-columns: 1fr;
            }
            .profile-info div {
                border-bottom: none;
                padding: 5px 0;
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
            <a href="#">Products</a>
            <a href="#">Customers</a>
            <a href="#">Service Fee</a>
            <a href="#">Support</a>
            <a href="#">News</a>
            <a href="#">About ShopViet</a>
            <a href="home.jsp">Logout</a>
        </div>
    </div>
    <div class="profile-container">
        <h1>User Profile</h1>
        <div class="profile-info">
            <div class="label">UserName :</div>
            <div class="value">admin1</div>
            <div class="label">Full Name :</div>
            <div class="value">Nguyen Van An</div>
            <div class="label">Email:</div>
            <div class="value">an.nv@shop.com</div>
            <div class="label">Phone Number :</div>
            <div class="value">0901234567</div>
            <div class="label">Address:</div>
            <div class="value">123 Đường Lê Lợi, Quận 1, TP.HCM</div>
            <div class="label">Last Login:</div>
            <div class="value">2025-05-27 08:00:00</div>
        </div>
        <div class="actions">
            <a href="#" class="btn" onclick="alert('Chức năng chỉnh sửa sẽ được triển khai!')">Edit Profile</a>
            <a href="#" class="btn" onclick="alert('Chức năng đổi mật khẩu sẽ được triển khai!')">Changing Password</a>
        </div>
    </div>
    <div class="footer">
        <p>Địa Chỉ: 123 Đường Chính, Thành Phố Hồ Chí Minh, Việt Nam</p>
        <p>Liên Hệ: Điện Thoại: +84 901 234 561 | Email: support@shopviet.com</p>
    </div>
</body>
</html>