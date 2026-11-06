<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>企业员工 - 我的订单</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>企业员工</h2>
                <p>Employee Portal</p>
            </div>
            <nav class="sidebar-nav">
                <a href="/employee/home" class="nav-item"><i class="icon">🏠</i><span>首页</span></a>
                <a href="/employee/menu" class="nav-item"><i class="icon">🍽️</i><span>点餐</span></a>
                <a href="/employee/orders" class="nav-item active"><i class="icon">📋</i><span>我的订单</span></a>
            </nav>
            <div class="sidebar-footer">
                <div class="user-info">
                    <p>${user.realName}</p>
                    <small>${user.department}</small>
                </div>
                <a href="/logout" class="btn-logout">退出登录</a>
            </div>
        </aside>
        <main class="main-content">
            <header class="top-bar">
                <h1>我的订单</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <div class="order-list">
                    <c:if test="${empty myOrders}">
                        <p class="empty-message">暂无订单记录</p>
                    </c:if>
                    <c:forEach var="order" items="${myOrders}">
                        <div class="order-card">
                            <div class="order-header">
                                <span class="order-no">${order.orderNo}</span>
                                <span class="order-time">${order.createTime}</span>
                            </div>
                            <div class="order-info">
                                <p><strong>餐别：</strong>${order.mealType}</p>
                                <p><strong>金额：</strong>¥${order.totalAmount}</p>
                                <p><strong>状态：</strong><span class="status-badge ${order.orderStatus}">${order.orderStatus}</span></p>
                            </div>
                            <div class="order-actions">
                                <a href="/employee/order/detail?orderId=${order.id}" class="btn btn-info">查看详情</a>
                                <c:if test="${order.orderStatus == 'PENDING'}">
                                    <button class="btn btn-danger" onclick="cancelOrder(${order.id})">取消订单</button>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </main>
    </div>
    <script src="/js/common.js"></script>
    <script src="/js/employee.js"></script>
</body>
</html>
