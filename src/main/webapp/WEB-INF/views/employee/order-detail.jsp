<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>企业员工 - 订单详情</title>
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
                <h1>订单详情</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <div class="detail-card">
                    <h2>${order.orderNo}</h2>
                    <p><strong>下单时间：</strong>${order.createTime}</p>
                    <p><strong>餐别：</strong>${order.mealType}</p>
                    <p><strong>金额：</strong>¥${order.totalAmount}</p>
                    <p><strong>订单状态：</strong><span class="status-badge ${order.orderStatus}">${order.orderStatus}</span></p>
                    <p><strong>支付状态：</strong>${order.paymentStatus}</p>
                    <p><strong>备注：</strong>${order.remark}</p>
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
                    <div class="view-all">
                        <a href="/employee/orders" class="btn btn-secondary">返回订单列表</a>
                        <c:if test="${order.orderStatus == 'PENDING'}">
                            <button class="btn btn-danger" onclick="cancelOrder(${order.id})">取消订单</button>
                        </c:if>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <script src="/js/common.js"></script>
    <script src="/js/employee.js"></script>
</body>
</html>
