<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>餐厅经理 - 财务管理</title>
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
                <a href="/manager/orders" class="nav-item"><i class="icon">📋</i><span>订单管理</span></a>
                <a href="/manager/finance" class="nav-item active"><i class="icon">💰</i><span>财务管理</span></a>
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
                <h1>财务管理</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <div class="page-toolbar">
                    <h2>今日财务统计</h2>
                    <button class="btn btn-primary" onclick="exportFinanceReport()">导出报表</button>
                </div>
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">📦</div>
                        <div class="stat-info">
                            <h3>今日订单</h3>
                            <p class="stat-number">${dailyStats.totalOrders}</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">💵</div>
                        <div class="stat-info">
                            <h3>今日营收</h3>
                            <p class="stat-number">¥${dailyStats.totalAmount}</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">✅</div>
                        <div class="stat-info">
                            <h3>已支付</h3>
                            <p class="stat-number">¥${dailyStats.paidAmount}</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">⏳</div>
                        <div class="stat-info">
                            <h3>待支付</h3>
                            <p class="stat-number">¥${dailyStats.unpaidAmount}</p>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <script src="/js/common.js"></script>
    <script src="/js/manager.js"></script>
</body>
</html>
