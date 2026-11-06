<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>财务管理 - 财务记录</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>财务管理</h2>
                <p>Finance Management</p>
            </div>
            <nav class="sidebar-nav">
                <a href="/finance/home" class="nav-item"><i class="icon">📊</i><span>数据概览</span></a>
                <a href="/finance/daily" class="nav-item"><i class="icon">📅</i><span>日报表</span></a>
                <a href="/finance/monthly" class="nav-item"><i class="icon">📆</i><span>月报表</span></a>
                <a href="/finance/records" class="nav-item active"><i class="icon">📝</i><span>财务记录</span></a>
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
                <h1>财务记录</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <div class="section">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>日期</th>
                                <th>餐别</th>
                                <th>订单数</th>
                                <th>总金额</th>
                                <th>已支付</th>
                                <th>待支付</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty records}">
                                <tr><td colspan="6" class="empty-message">暂无财务记录</td></tr>
                            </c:if>
                            <c:forEach var="record" items="${records}">
                                <tr>
                                    <td>${record.recordDate}</td>
                                    <td>${record.mealType}</td>
                                    <td>${record.orderCount}</td>
                                    <td>¥${record.totalAmount}</td>
                                    <td>¥${record.paidAmount}</td>
                                    <td>¥${record.unpaidAmount}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
    <script src="/js/common.js"></script>
    <script src="/js/finance.js"></script>
</body>
</html>
