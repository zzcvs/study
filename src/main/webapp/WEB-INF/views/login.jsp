<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>企业餐厅网络点餐系统 - 登录</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body class="login-page">
    <div class="login-container">
        <div class="login-box">
            <div class="login-header">
                <h1>企业餐厅网络点餐系统</h1>
                <p>Enterprise Canteen Ordering System</p>
            </div>
            
            <form class="login-form" id="loginForm">
                <div class="form-group">
                    <label for="username">用户名</label>
                    <input type="text" id="username" name="username" placeholder="请输入用户名" required>
                </div>
                
                <div class="form-group">
                    <label for="password">密码</label>
                    <input type="password" id="password" name="password" placeholder="请输入密码" required>
                </div>
                
                <div class="form-group checkbox-group">
                    <input type="checkbox" id="remember" name="remember">
                    <label for="remember">记住我</label>
                </div>
                
                <button type="submit" class="btn-login">登录</button>
            </form>
            
            <div class="login-footer">
                <p>测试账号：admin/123456（餐厅经理）</p>
                <p>kitchen/123456（厨房主管）</p>
                <p>distributor/123456（配餐员）</p>
                <p>finance/123456（财务管理）</p>
                <p>employee1/123456（企业员工）</p>
            </div>
        </div>
    </div>
    
    <script src="/js/login.js"></script>
</body>
</html>
