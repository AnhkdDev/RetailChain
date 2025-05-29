<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Settings</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
            padding: 0;
        }
        .header {
            background: linear-gradient(90deg, #007bff, #00c6ff);
            color: #fff;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            border-radius: 0 0 10px 10px;
            position: relative;
            z-index: 1000;
        }
        .header .user-info {
            display: flex;
            align-items: center;
        }
        .header .user-info img {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            margin-right: 15px;
            border: 2px solid #fff;
        }
        .header .user-info .username {
            font-size: 20px;
            font-weight: bold;
        }
        .header .dropdown {
            position: relative;
        }
        .header .dropdown button {
            background: none;
            border: none;
            color: #fff;
            font-size: 16px;
            cursor: pointer;
            padding: 5px;
            display: flex;
            align-items: center;
        }
        .header .dropdown-content {
            display: none;
            position: absolute;
            background-color: #fff;
            min-width: 160px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            border-radius: 5px;
            top: 100%;
            right: 0;
            z-index: 1;
        }
        .header .dropdown:hover .dropdown-content {
            display: block;
        }
        .header .dropdown-content a {
            color: #333;
            padding: 12px 16px;
            text-decoration: none;
            display: block;
        }
        .header .dropdown-content a:hover {
            background-color: #f1f1f1;
        }
        .tabs {
            display: flex;
            gap: 10px;
        }
        .tabs button {
            padding: 10px 20px;
            border: none;
            background-color: #ddd;
            color: #333;
            cursor: pointer;
            border-radius: 5px;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        .tabs button.active {
            background-color: #007bff;
            color: #fff;
        }
        .tabs button:hover {
            background-color: #ccc;
        }
        .tabcontent {
            display: none;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 0 0 5px 5px;
            margin-top: -1px;
            background-color: #fff;
        }
        .tabcontent.active {
            display: block;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .form-group button {
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .form-group button:hover {
            background-color: #0056b3;
        }
        .message {
            color: green;
            margin-bottom: 15px;
        }
        .error {
            color: red;
            margin-bottom: 15px;
        }
        @media (max-width: 600px) {
            .header {
                flex-direction: column;
                padding: 15px;
            }
            .tabs {
                flex-direction: column;
                margin-top: 10px;
            }
            .tabs button {
                width: 100%;
                margin-bottom: 5px;
            }
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="user-info">
            <img src="https://via.placeholder.com/50" alt="Profile Picture">
            <span class="username">admin1</span>
            <div class="dropdown">
                <button><i class="fas fa-caret-down"></i></button>
                <div class="dropdown-content">
                    <a href="user-profile.jsp">Profile</a>
                    <a href="settings.jsp">Settings</a>
                    <a href="homeManager.jsp">Logout</a>
                </div>
            </div>
        </div>
        <div class="tabs">
            <button class="tablinks active" onclick="openTab(event, 'Profile')">Profile</button>
            <button class="tablinks" onclick="openTab(event, 'SystemSettings')">System Settings</button>
            <button class="tablinks" onclick="openTab(event, 'ActivityLog')">Activity Log</button>
        </div>
    </div>

    <div class="container">
        <div id="Profile" class="tabcontent active">
            <h2>Profile Settings</h2>
            <form action="<%= request.getContextPath() %>/SettingsServlet" method="post">
                <input type="hidden" name="action" value="saveProfile">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" value="admin1" readonly>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="admin1@example.com" required>
                </div>
                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="text" name="phoneNumber" value="0987654321" required>
                </div>
                <div class="form-group">
                    <label>Address</label>
                    <input type="text" name="address" value="Hanoi, Vietnam" required>
                </div>
                <div class="form-group">
                    <label>New Password (leave blank to keep current password)</label>
                    <input type="password" name="password" placeholder="Enter new password">
                </div>
                <div class="form-group">
                    <p class="${mess.contains('Error') ? 'error' : 'message'}">${mess}</p>
                    <button type="submit">Save Changes</button>
                </div>
            </form>
        </div>

        <div id="SystemSettings" class="tabcontent">
            <h2>System Settings</h2>
            <form action="<%= request.getContextPath() %>/SettingsServlet" method="post">
                <input type="hidden" name="action" value="saveSystemSettings">
                <div class="form-group">
                    <label>Notifications</label>
                    <input type="checkbox" name="emailNotif" checked> Email
                    <input type="checkbox" name="smsNotif"> SMS
                </div>
                <div class="form-group">
                    <label>Language</label>
                    <select name="language">
                        <option value="en">English</option>
                        <option value="vi" selected>Vietnamese</option>
                    </select>
                </div>
                <div class="form-group">
                    <button type="submit">Save Settings</button>
                </div>
            </form>
        </div>

        <div id="ActivityLog" class="tabcontent">
            <h2>Activity Log</h2>
            <p><%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) %> - Updated profile information</p>
            <p>2025-05-28 10:00 PM - Logged in</p>
        </div>
    </div>

    <script>
        function openTab(evt, tabName) {
            var i, tabcontent, tablinks;
            tabcontent = document.getElementsByClassName("tabcontent");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].classList.remove("active");
            }
            tablinks = document.getElementsByClassName("tablinks");
            for (i = 0; i < tablinks.length; i++) {
                tablinks[i].classList.remove("active");
            }
            document.getElementById(tabName).classList.add("active");
            evt.currentTarget.classList.add("active");
        }
    </script>
</body>
</html>