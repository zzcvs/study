<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>配餐员 - 订单列表</title>
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
                <a href="/distributor/home" class="nav-item"><i class="icon">📦</i><span>配餐工作台</span></a>
                <a href="/distributor/orders" class="nav-item active"><i class="icon">📋</i><span>订单列表</span></a>
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
                <h1>今日订单列表</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <div class="section">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>订单号</th>
                                <th>用户</th>
                                <th>部门</th>
                                <th>餐别</th>
                                <th>金额</th>
                                <th>订单状态</th>
                                <th>配餐状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty orders}">
                                <tr><td colspan="8" class="empty-message">今日暂无订单</td></tr>
                            </c:if>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td>${order.orderNo}</td>
                                    <td>${order.userName}</td>
                                    <td>${order.department}</td>
                                    <td>${order.mealType}</td>
                                    <td>¥${order.totalAmount}</td>
                                    <td><span class="status-badge ${order.orderStatus}">${order.orderStatus}</span></td>
                                    <td>${order.distributeStatus}</td>
                                    <td>
                                        <button class="btn btn-info" onclick="viewOrder(${order.id})">查看详情</button>
                                        <c:if test="${order.orderStatus == 'READY' && (empty order.distributeStatus || order.distributeStatus == 'PENDING')}">
                                            <button class="btn btn-primary" onclick="startDistribute(${order.id})">开始配餐</button>
                                        </c:if>
                                        <c:if test="${order.distributeStatus == 'DISTRIBUTING'}">
                                            <button class="btn btn-success" onclick="completeDistribute(${order.id})">配餐完成</button>
                                        </c:if>
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
    <script src="/js/distributor.js"></script>
</body>
</html>
