<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>配餐员 - 首页</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>配餐员</h2>
                <p>Distributor</p>
            </div>
            
            <nav class="sidebar-nav">
                <a href="/distributor/home" class="nav-item active">
                    <i class="icon">📦</i>
                    <span>配餐工作台</span>
                </a>
                <a href="/distributor/orders" class="nav-item">
                    <i class="icon">📋</i>
                    <span>订单列表</span>
                </a>
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
                <h1>配餐工作台</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            
            <div class="content-wrapper">
                <div class="distributor-dashboard">
                    <div class="order-section">
                        <h2>待配餐订单</h2>
                        <div class="order-list">
                            <c:if test="${empty readyOrders}">
                                <p class="empty-message">暂无待配餐订单</p>
                            </c:if>
                            <c:forEach var="order" items="${readyOrders}">
                                <div class="order-card ready">
                                    <div class="order-header">
                                        <span class="order-no">${order.orderNo}</span>
                                        <span class="order-time">${order.createTime}</span>
                                    </div>
                                    <div class="order-info">
                                        <p><strong>用户：</strong>${order.userName}</p>
                                        <p><strong>部门：</strong>${order.department}</p>
                                        <p><strong>餐别：</strong>${order.mealType}</p>
                                        <p><strong>金额：</strong>¥${order.totalAmount}</p>
                                    </div>
                                    <div class="order-actions">
                                        <button class="btn btn-info" onclick="viewOrder(${order.id})">查看详情</button>
                                        <button class="btn btn-primary" onclick="startDistribute(${order.id})">开始配餐</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <div class="order-section">
                        <h2>配餐中订单</h2>
                        <div class="order-list">
                            <c:if test="${empty distributingOrders}">
                                <p class="empty-message">暂无配餐中订单</p>
                            </c:if>
                            <c:forEach var="order" items="${distributingOrders}">
                                <div class="order-card distributing">
                                    <div class="order-header">
                                        <span class="order-no">${order.orderNo}</span>
                                        <span class="order-time">${order.createTime}</span>
                                    </div>
                                    <div class="order-info">
                                        <p><strong>用户：</strong>${order.userName}</p>
                                        <p><strong>部门：</strong>${order.department}</p>
                                        <p><strong>餐别：</strong>${order.mealType}</p>
                                        <p><strong>金额：</strong>¥${order.totalAmount}</p>
                                    </div>
                                    <div class="order-actions">
                                        <button class="btn btn-info" onclick="viewOrder(${order.id})">查看详情</button>
                                        <button class="btn btn-success" onclick="completeDistribute(${order.id})">配餐完成</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <script src="/js/common.js"></script>
    <script src="/js/distributor.js"></script>
</body>
</html>
