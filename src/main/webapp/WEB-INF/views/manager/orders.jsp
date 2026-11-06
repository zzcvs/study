<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>餐厅经理 - 订单管理</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>餐厅经理</h2>
                <p>Manager Dashboard</p>
            </div>
            <nav class="sidebar-nav">
                <a href="/manager/home" class="nav-item"><i class="icon">📊</i><span>首页概览</span></a>
                <a href="/manager/users" class="nav-item"><i class="icon">👥</i><span>用户管理</span></a>
                <a href="/manager/recipes" class="nav-item"><i class="icon">🍽️</i><span>食谱管理</span></a>
                <a href="/manager/orders" class="nav-item active"><i class="icon">📋</i><span>订单管理</span></a>
                <a href="/manager/finance" class="nav-item"><i class="icon">💰</i><span>财务管理</span></a>
            </nav>
            <div class="sidebar-footer">
                <div class="user-info">
                    <p>${user.realName}</p>
                    <small>${user.role}</small>
                </div>
                <a href="/logout" class="btn-logout">退出登录</a>
            </div>
        </aside>
        <main class="main-content">
            <header class="top-bar">
                <h1>订单管理</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <div class="section">
                    <h2>全部订单</h2>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>订单号</th>
                                <th>用户</th>
                                <th>部门</th>
                                <th>餐别</th>
                                <th>金额</th>
                                <th>订单状态</th>
                                <th>支付状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty orders}">
                                <tr><td colspan="8" class="empty-message">暂无订单数据</td></tr>
                            </c:if>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td>${order.orderNo}</td>
                                    <td>${order.userName}</td>
                                    <td>${order.department}</td>
                                    <td>${order.mealType}</td>
                                    <td>¥${order.totalAmount}</td>
                                    <td><span class="status-badge ${order.orderStatus}">${order.orderStatus}</span></td>
                                    <td>${order.paymentStatus}</td>
                                    <td>
                                        <button class="btn btn-danger" onclick="deleteOrder(${order.id})">删除</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
    <script src="/js/common.js"></script>
    <script src="/js/manager.js"></script>
</body>
</html>
