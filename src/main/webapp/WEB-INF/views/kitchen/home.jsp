<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>厨房主管 - 首页</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="app-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>厨房主管</h2>
                <p>Kitchen Supervisor</p>
            </div>
            
            <nav class="sidebar-nav">
                <a href="/kitchen/home" class="nav-item active">
                    <i class="icon">🍳</i>
                    <span>工作台</span>
                </a>
                <a href="/kitchen/orders" class="nav-item">
                    <i class="icon">📋</i>
                    <span>订单管理</span>
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
                <h1>厨房工作台</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            
            <div class="content-wrapper">
                <div class="kitchen-dashboard">
                    <div class="order-section">
                        <h2>待处理订单</h2>
                        <div class="order-list">
                            <c:if test="${empty pendingOrders}">
                                <p class="empty-message">暂无待处理订单</p>
                            </c:if>
                            <c:forEach var="order" items="${pendingOrders}">
                                <div class="order-card pending">
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
                                        <button class="btn btn-primary" onclick="startCooking(${order.id})">开始制作</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <div class="order-section">
                        <h2>制作中订单</h2>
                        <div class="order-list">
                            <c:if test="${empty cookingOrders}">
                                <p class="empty-message">暂无制作中订单</p>
                            </c:if>
                            <c:forEach var="order" items="${cookingOrders}">
                                <div class="order-card cooking">
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
                                        <button class="btn btn-success" onclick="completeOrder(${order.id})">制作完成</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <div class="menu-section">
                        <h2>今日菜单</h2>
                        <div class="menu-grid">
                            <c:if test="${empty todayMenu}">
                                <p class="empty-message">今日暂无菜单</p>
                            </c:if>
                            <c:forEach var="menu" items="${todayMenu}">
                                <div class="menu-item">
                                    <h4>${menu.recipe.name}</h4>
                                    <p>分类：${menu.recipe.category}</p>
                                    <p>价格：¥${menu.recipe.price}</p>
                                    <p>库存：${menu.stock}</p>
                                    <p>已售：${menu.soldCount}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <script src="/js/common.js"></script>
    <script src="/js/kitchen.js"></script>
</body>
</html>
