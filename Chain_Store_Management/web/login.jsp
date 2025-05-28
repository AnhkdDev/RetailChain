<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng nhập</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"> <%-- Font Awesome cho icon social --%>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
        <style>@import url('https://fonts.googleapis.com/css?family=Montserrat:400,800');

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
                /* Thêm background chuyển động nhẹ nhàng (tùy chọn) */
                background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab);
                background-size: 400% 400%;
                animation: gradientBG 15s ease infinite;
            }

            @keyframes gradientBG {
                0% {
                    background-position: 0% 50%;
                }
                50% {
                    background-position: 100% 50%;
                }
                100% {
                    background-position: 0% 50%;
                }
            }


            h1 {
                font-weight: bold;
                margin: 0;
                color: #333;
            }

            h2 {
                text-align: center;
                color: #333;
            }

            p {
                font-size: 14px;
                font-weight: 100;
                line-height: 20px;
                letter-spacing: 0.5px;
                margin: 20px 0 30px;
                color: #eee; /* Màu chữ trên overlay */
            }

            span {
                font-size: 12px;
            }

            a {
                color: #333;
                font-size: 14px;
                text-decoration: none;
                margin: 15px 0;
            }

            button {
                border-radius: 20px;
                border: 1px solid #FF4B2B;
                background-color: #FF4B2B;
                color: #FFFFFF;
                font-size: 12px;
                font-weight: bold;
                padding: 12px 45px;
                letter-spacing: 1px;
                text-transform: uppercase;
                transition: transform 80ms ease-in;
                cursor: pointer;
            }

            button:active {
                transform: scale(0.95);
            }

            button:focus {
                outline: none;
            }

            /* Button kiểu ghost cho overlay */
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
                padding: 0 50px;
                height: 100%;
                text-align: center;
            }

            input {
                background-color: #eee;
                border: none;
                padding: 12px 15px;
                margin: 8px 0;
                width: 100%;
                border-radius: 5px;
                outline: none;
            }

            /* --- Container chính --- */
            .container {
                background-color: #fff;
                border-radius: 10px;
                box-shadow: 0 14px 28px rgba(0,0,0,0.25),
                    0 10px 10px rgba(0,0,0,0.22);
                position: relative;
                overflow: hidden; /* Quan trọng để ẩn phần bị trượt ra ngoài */
                width: 768px;
                max-width: 100%;
                min-height: 480px;
            }

            /* --- Form Containers --- */
            .form-container {
                position: absolute;
                top: 0;
                height: 100%;
                transition: all 0.6s ease-in-out; /* Đây là animation chính */
            }

            .sign-in-container {
                left: 0;
                width: 50%;
                z-index: 2; /* Nằm trên Register ban đầu */
            }

            .sign-up-container {
                left: 0;
                width: 50%;
                opacity: 0; /* Ẩn ban đầu */
                z-index: 1;
            }

            /* --- Trạng thái Active (Khi chuyển sang Register) --- */
            .container.right-panel-active .sign-in-container {
                transform: translateX(100%); /* Trượt form Login sang phải */
                opacity: 0; /* Làm mờ đi khi trượt */
                z-index: 1;
            }

            .container.right-panel-active .sign-up-container {
                transform: translateX(100%); /* Đưa form Register vào vị trí */
                opacity: 1; /* Hiển thị rõ */
                z-index: 5; /* Nổi lên trên cùng */
                animation: show 0.6s; /* Animation phụ để đảm bảo hiển thị mượt */
            }

            @keyframes show {
                0%, 49.99% {
                    opacity: 0;
                    z-index: 1;
                }

                50%, 100% {
                    opacity: 1;
                    z-index: 5;
                }
            }

            /* --- Overlay Container và Overlay --- */
            .overlay-container {
                position: absolute;
                top: 0;
                left: 50%; /* Nằm nửa phải ban đầu */
                width: 50%;
                height: 100%;
                overflow: hidden;
                transition: transform 0.6s ease-in-out; /* Animation cho overlay container */
                z-index: 100; /* Luôn nằm trên các form */
            }

            /* Di chuyển overlay container sang trái khi active */
            .container.right-panel-active .overlay-container{
                transform: translateX(-100%);
            }

            .overlay {
                background: #FF416C;
                background: -webkit-linear-gradient(to right, #FF4B2B, #FF416C);
                background: linear-gradient(to right, #FF4B2B, #FF416C);
                background-repeat: no-repeat;
                background-size: cover;
                background-position: 0 0;
                color: #FFFFFF;
                position: relative;
                left: -100%; /* Để lộ phần bên phải */
                height: 100%;
                width: 200%; /* Gấp đôi để có phần ẩn và hiện */
                transform: translateX(0);
                transition: transform 0.6s ease-in-out; /* Animation cho nội dung overlay */
            }

            /* Di chuyển nội dung overlay sang phải tương đối khi active */
            .container.right-panel-active .overlay {
                transform: translateX(50%);
            }

            /* --- Các Panel trong Overlay --- */
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
                width: 50%; /* Chiếm 1 nửa của overlay container */
                transform: translateX(0);
                transition: transform 0.6s ease-in-out;
            }

            .overlay-left {
                /* Đặt panel này ở phần bên trái của overlay (sẽ hiển thị khi register active) */
                transform: translateX(-20%); /* Hơi lệch để tạo hiệu ứng khi di chuyển */
            }

            /* Di chuyển panel trái vào giữa khi active */
            .container.right-panel-active .overlay-left {
                transform: translateX(0);
            }

            .overlay-right {
                /* Đặt panel này ở phần bên phải của overlay (sẽ hiển thị ban đầu) */
                right: 0;
                transform: translateX(0);
            }

            /* Di chuyển panel phải ra ngoài khi active */
            .container.right-panel-active .overlay-right {
                transform: translateX(20%); /* Hơi lệch để tạo hiệu ứng khi di chuyển */
            }

            .social-container {
                margin: 20px 0;
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
            }</style>
    </head>
    <body>
 
  <div class="Home">
    <a href="home.jsp" target="_blank"
      style="background-color: #ffffff;
             color: black;
             padding: 8px 30px;
             border-radius: 5px;
             font-size: 15px;
             font-weight: bold;
             text-align: center;
             cursor: pointer;
             width: fit-content;
             text-decoration: none;
             margin-right:1100px; 
             
             display: inline-block;">
       
      Home
    </a>
  </div>



        <div class="container" id="container"> <%-- KHÔNG có class "right-panel-active" ban đầu --%>
            <%-- Phần Form Đăng ký --%>
            <div class="form-container sign-up-container">
                <form action="<%= request.getContextPath() %>/RegisterServlet" method="post"> <%-- Thay RegisterServlet bằng action xử lý đăng ký của bạn --%>
                    <h1>Creating Account</h1>
                    <div class="social-container">
                        <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
                        <a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
                        <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
                    </div>
                    <span>hoặc sử dụng email của bạn để đăng ký</span>
                    <input type="email" name="email" placeholder="Email" required/>
                    <input type="password" name="password" placeholder="Password" required/>
                    <button type="submit">Đăng ký</button>
                </form>
            </div>

      
<%-- Phần Form Đăng nhập --%>
<!--<div class="form-container sign-in-container">
    <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
        <h1>Login</h1>
        <div class="social-container">
            <a href="#" class="social"><i class="fas fa-user"></i></a>
            <a href="#" class="social"><i class="fas fa-shopping-cart"></i></a>
        </div>

        <span>Or Using Your Account</span>
        <div class="login form"> 
            <form action="CheckLogin" method="post">

                <input type="email" name="email" placeholder="Enter your Email" value="${email}" required/>
                <input type="password" name="pass" placeholder="Enter your Password" value="${pass}" required/>

                <button type="submit">Login</button>
            </form>
      
        </div>
       


        <%-- Phần Forget Password tách riêng --%>
        <div style="margin-top: 10px; text-align: right;">
            <a href="#">Forget Password?</a>
        </div>
    </form>
</div>-->
        
        <div class="form-container sign-in-container">
   <form action="<%= request.getContextPath() %>/checkLogin" method="post">
        <h1>Login</h1>
`
        <div class="social-container">
            <a href="#" class="social"><i class="fas fa-user"></i></a>
            <a href="#" class="social"><i class="fas fa-shopping-cart"></i></a>
        </div>

        <span>Or Using Your Account</span>

        <input type="text" name="user" placeholder="Enter your username" value="${username}" required=""/>
        <input type="password" name="pass" placeholder="Enter your Password" value="${password}" required=""/>
        <span style="color: red">${mess}</span>
        <button type="submit">Login</button>

        <%-- Phần Forget Password --%>
        <div style="margin-top: 10px; text-align: right;">
            <a href="#">Forget Password?</a>
        </div>
    </form>
</div>










            <%-- Phần Overlay --%>
            <div class="overlay-container">
                <div class="overlay">
                    <%-- Overlay Panel bên Trái (Hiển thị khi đang ở form Login) --%>
                    <div class="overlay-panel overlay-left">
                        <h1>Welcome Back!</h1>
                        <p>Để giữ kết nối với chúng tôi, vui lòng đăng nhập bằng thông tin cá nhân của bạn</p>
                        <button class="ghost" id="signIn">Đăng nhập</button> <%-- Nút chuyển về Login --%>
                    </div>
                    <%-- Overlay Panel bên Phải (Hiển thị khi đang ở form Register) --%>
                    <div class="overlay-panel overlay-right">
                        <h1>Register!</h1>
                        <p>Enter your personal information and start your journey with us</p>
                        <button class="ghost" id="signUp"> <a href="register.jsp">Sign up</a> </button> <%-- Nút chuyển sang Register --%>
                    </div>
                </div>
            </div>
        </div>

        <script src="<%= request.getContextPath() %>/js/script.js" defer>
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
        </script> <%-- defer để đảm bảo script chạy sau khi parse HTML --%>

    </body>
</html>