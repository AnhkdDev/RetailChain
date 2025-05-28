<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        /* CSS như đã sửa ở trên */
        @import url('https://fonts.googleapis.com/css?family=Montserrat:400,800');
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            background: #f6f5f7;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            font-family: 'Montserrat', sans-serif;
            height: 100vh;
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab);
            background-size: 400% 400%;
            animation: gradientBG 15s ease infinite;
        }

        @keyframes gradientBG {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }    

        h2 {
            text-align: center;
            color: #333;
        }

        h1 {
            font-weight: bold;
            color: #333;
            font-size: 36px;
            margin-bottom: 10px;
        }

        p {
            font-size: 14px;
            font-weight: 100;
            line-height: 20px;
            letter-spacing: 0.5px;
            margin: 20px 0 30px;
            color: #eee;
        }

        span {
            font-size: 12px;
            display: block;
            margin-bottom: 10px;
        }

        a {
            color: #333;
            font-size: 14px;
            text-decoration: none;
            margin: 10px 0;
        }

        button {
            border-radius: 20px;
            border: 1px solid #FF4B2B;
            background-color: #FF4B2B;
            color: #FFFFFF;
            font-size: 12px;
            font-weight: bold;
            padding: 10px 40px;
            letter-spacing: 1px;
            text-transform: uppercase;
            transition: transform 80ms ease-in;
            cursor: pointer;
            margin-top: 10px;
        }

        button:active {
            transform: scale(0.95);
        }

        button:focus {
            outline: none;
        }

        button.ghost {
            background-color: transparent;
            border-color: #FFFFFF;
        }

        form {
            background-color: #FFFFFF;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            padding: 0 30px;
            height: 100%;
            text-align: center;
            overflow-y: auto;
        }

        input {
            background-color: #eee;
            border: none;
            padding: 10px 12px;
            margin: 5px 0;
            width: 100%;
            border-radius: 5px;
            outline: none;
            font-size: 13px;
            box-sizing: border-box;
        }

        .container {
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 14px 28px rgba(0,0,0,0.25),
                0 10px 10px rgba(0,0,0,0.22);
            position: relative;
            overflow: hidden;
            width: 768px;
            max-width: 100%;
            min-height: 600px;
        }

        .form-container {
            position: absolute;
            top: 0;
            height: 100%;
            transition: all 0.6s ease-in-out;
        }

        .sign-in-container {
            left: 0;
            width: 50%;
            z-index: 2;
        }

        .sign-up-container {
            left: 0;
            width: 50%;
            opacity: 0;
            z-index: 1;
        }

        .container.right-panel-active .sign-in-container {
            transform: translateX(100%);
            opacity: 0;
            z-index: 1;
        }

        .container.right-panel-active .sign-up-container {
            transform: translateX(100%);
            opacity: 1;
            z-index: 5;
            animation: show 0.6s;
        }

        @keyframes show {
            0%, 49.99% { opacity: 0; z-index: 1; }
            50%, 100% { opacity: 1; z-index: 5; }
        }

        .overlay-container {
            position: absolute;
            top: 0;
            left: 50%;
            width: 50%;
            height: 100%;
            overflow: hidden;
            transition: transform 0.6s ease-in-out;
            z-index: 100;
        }

        .container.right-panel-active .overlay-container {
            transform: translateX(-100%);
        }

        .overlay {
            background: #FF416C;
            background: linear-gradient(to right, #FF4B2B, #FF416C);
            background-repeat: no-repeat;
            background-size: cover;
            background-position: 0 0;
            color: #FFFFFF;
            position: relative;
            left: -100%;
            height: 100%;
            width: 200%;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
        }

        .container.right-panel-active .overlay {
            transform: translateX(50%);
        }

        .overlay-panel {
            position: absolute;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            padding: 0 40px;
            text-align: center;
            top: 0;
            height: 100%;
            width: 50%;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
        }

        .overlay-left {
            transform: translateX(-20%);
        }

        .container.right-panel-active .overlay-left {
            transform: translateX(0);
        }

        .overlay-right {
            right: 0;
            transform: translateX(0);
        }

        .container.right-panel-active .overlay-right {
            transform: translateX(20%);
        }

        .social-container {
            margin: 10px 0;
        }

        .social-container a {
            border: 1px solid #DDDDDD;
            border-radius: 50%;
            display: inline-flex;
            justify-content: center;
            align-items: center;
            margin: 0 5px;
            height: 40px;
            width: 40px;
            color: #333;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        .social-container a:hover {
            background-color: #eee;
        }
    </style>
</head>
<body>
    <div class="container right-panel-active" id="container">
        <div class="form-container sign-up-container">
            <form action="<%= request.getContextPath() %>/registerServlet" method="post">
                <h1>Register</h1>
                <div class="social-container">
                    <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
                    <a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
                    <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
                </div>
                <span>hoặc sử dụng email của bạn để đăng ký</span>
                <input type="text" name="username" placeholder="Username" required />
                <input type="text" name="phoneNumber" placeholder="Phone Number" required/>
                <input type="text" name="address" placeholder="Address" required/>
                <input type="email" name="email" placeholder="Email" required/>
                <input type="password" name="password" placeholder="Password" required/>
<!--                <select name="roleID" required>
                    <option value="2">Employee</option>
                    <option value="3">Manager</option>
                </select>-->
                <p style="color:red">${mess}</p>
                <button type="submit">Đăng ký</button>
            </form>
        </div>

        <div class="form-container sign-in-container">
            <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
                <h1>Login</h1>
                <div class="social-container">
                    <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
                    <a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
                    <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
                </div>
                <span>hoặc sử dụng tài khoản của bạn</span>
                <input type="email" name="email" placeholder="Email hoặc Tên đăng nhập" required/>
                <input type="password" name="password" placeholder="Mật khẩu" required/>
                <a href="#">Quên mật khẩu?</a>
                <button type="submit">Đăng nhập</button>
            </form>
        </div>

        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h1>Welcome Back!</h1>
                    <p>Để giữ kết nối với chúng tôi, vui lòng đăng nhập bằng thông tin cá nhân của bạn</p>
                    <button class="ghost" id="signIn"><a href="login.jsp">Login</a></button>
                </div>
                <div class="overlay-panel overlay-right">
                    <h1>Welcome!</h1>
                    <p>Nhập thông tin cá nhân của bạn và bắt đầu hành trình với chúng tôi</p>
                    <button class="ghost" id="signUp">Register Account</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const signUpButton = document.getElementById('signUp');
            const signInButton = document.getElementById('signIn');
            const container = document.getElementById('container');

            if (signUpButton && container) {
                signUpButton.addEventListener('click', () => {
                    container.classList.add("right-panel-active");
                });
            }

            if (signInButton && container) {
                signInButton.addEventListener('click', () => {
                    container.classList.remove("right-panel-active");
                });
            }
        });
    </script>
</body>
</html>