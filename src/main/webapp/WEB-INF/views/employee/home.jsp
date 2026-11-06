<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>企业员工 - 首页</title>
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
                <a href="/employee/home" class="nav-item active">
                    <i class="icon">🏠</i>
                    <span>首页</span>
                </a>
                <a href="/employee/menu" class="nav-item">
                    <i class="icon">🍽️</i>
                    <span>点餐</span>
                </a>
                <a href="/employee/orders" class="nav-item">
                    <i class="icon">📋</i>
                    <span>我的订单</span>
                </a>
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
                <h1>欢迎，${user.realName}</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            
            <div class="content-wrapper">
                <div class="welcome-section">
                    <h2>今日午餐菜单</h2>
                    <div class="menu-grid">
                        <c:if test="${empty lunchMenu}">
                            <p class="empty-message">今日暂无午餐菜单</p>
                        </c:if>
                        <c:forEach var="menu" items="${lunchMenu}">
                            <div class="menu-card">
                                <div class="menu-image">
                                    <c:if test="${not empty menu.recipe and not empty menu.recipe.imageUrl}">
                                        <img src="${menu.recipe.imageUrl}" alt="${menu.recipe.name}">
                                    </c:if>
                                    <c:if test="${empty menu.recipe or empty menu.recipe.imageUrl}">
                                        <div class="placeholder-image">🍽️</div>
                                    </c:if>
                                </div>
                                <div class="menu-info">
                                    <h3>${menu.recipe.name}</h3>
                                    <p class="category">${menu.recipe.category}</p>
                                    <p class="description">${menu.recipe.description}</p>
                                    <p class="price">¥${menu.recipe.price}</p>
                                    <p class="stock">库存：${menu.stock}</p>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    
                    <div class="action-buttons">
                        <a href="/employee/menu" class="btn btn-primary btn-large">立即点餐</a>
                    </div>
                </div>
                
                <div class="my-orders-section">
                    <h2>我的最近订单</h2>
                    <div class="order-list">
                        <c:if test="${empty myOrders}">
                            <p class="empty-message">暂无订单记录</p>
                        </c:if>
                        <c:forEach var="order" items="${myOrders}" begin="0" end="4">
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
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    
                    <div class="view-all">
                        <a href="/employee/orders" class="btn btn-secondary">查看全部订单</a>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <script src="/js/common.js"></script>
    <script src="/js/employee.js"></script>
</body>
</html>
