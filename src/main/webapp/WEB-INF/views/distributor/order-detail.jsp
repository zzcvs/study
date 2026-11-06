<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>配餐员 - 订单详情</title>
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
                <h1>订单详情</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <div class="detail-card">
                    <h2>${order.orderNo}</h2>
                    <p><strong>用户：</strong>${order.userName}</p>
                    <p><strong>部门：</strong>${order.department}</p>
                    <p><strong>餐别：</strong>${order.mealType}</p>
                    <p><strong>金额：</strong>¥${order.totalAmount}</p>
                    <p><strong>订单状态：</strong><span class="status-badge ${order.orderStatus}">${order.orderStatus}</span></p>
                    <p><strong>配餐状态：</strong>${order.distributeStatus}</p>
                    <p><strong>备注：</strong>${order.remark}</p>
                    <div class="order-actions">
                        <c:if test="${order.orderStatus == 'READY' && (empty order.distributeStatus || order.distributeStatus == 'PENDING')}">
                            <button class="btn btn-primary" onclick="startDistribute(${order.id})">开始配餐</button>
                        </c:if>
                        <c:if test="${order.distributeStatus == 'DISTRIBUTING'}">
                            <button class="btn btn-success" onclick="completeDistribute(${order.id})">配餐完成</button>
                        </c:if>
                        <a href="/distributor/orders" class="btn btn-secondary">返回列表</a>
                    </div>
                </div>
                <div class="section">
                    <h2>菜品明细</h2>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>菜品</th>
                                <th>单价</th>
                                <th>数量</th>
                                <th>小计</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="detail" items="${details}">
                                <tr>
                                    <td>${detail.recipeName}</td>
                                    <td>¥${detail.price}</td>
                                    <td>${detail.quantity}</td>
                                    <td>¥${detail.subtotal}</td>
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
