<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>财务管理 - 首页</title>
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
                <a href="/finance/home" class="nav-item active">
                    <i class="icon">📊</i>
                    <span>数据概览</span>
                </a>
                <a href="/finance/daily" class="nav-item">
                    <i class="icon">📅</i>
                    <span>日报表</span>
                </a>
                <a href="/finance/monthly" class="nav-item">
                    <i class="icon">📆</i>
                    <span>月报表</span>
                </a>
                <a href="/finance/records" class="nav-item">
                    <i class="icon">📝</i>
                    <span>财务记录</span>
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
                <h1>财务数据概览</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            
            <div class="content-wrapper">
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">📅</div>
                        <div class="stat-info">
                            <h3>今日订单</h3>
                            <p class="stat-number">${dailyStats.totalOrders}</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon">💰</div>
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
                
                <div class="dashboard-sections">
                    <div class="section">
                        <h2>本月统计</h2>
                        <div class="monthly-stats">
                            <div class="stat-item">
                                <h3>订单总数</h3>
                                <p>${monthlyStats.totalOrders}</p>
                            </div>
                            <div class="stat-item">
                                <h3>总营收</h3>
                                <p>¥${monthlyStats.totalAmount}</p>
                            </div>
                            <div class="stat-item">
                                <h3>已支付</h3>
                                <p>¥${monthlyStats.paidAmount}</p>
                            </div>
                            <div class="stat-item">
                                <h3>待支付</h3>
                                <p>¥${monthlyStats.unpaidAmount}</p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="section">
                        <h2>快捷操作</h2>
                        <div class="quick-actions">
                            <a href="/finance/daily" class="btn btn-primary">查看日报</a>
                            <a href="/finance/monthly" class="btn btn-info">查看月报</a>
                            <a href="/finance/records" class="btn btn-success">查看记录</a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <script src="/js/common.js"></script>
    <script src="/js/finance.js"></script>
</body>
</html>
