<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>企业员工 - 点餐</title>
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
                <a href="/employee/menu" class="nav-item active"><i class="icon">🍽️</i><span>点餐</span></a>
                <a href="/employee/orders" class="nav-item"><i class="icon">📋</i><span>我的订单</span></a>
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
                <h1>在线点餐</h1>
                <div class="current-time" id="currentTime"></div>
            </header>
            <div class="content-wrapper">
                <form class="filter-form" method="get" action="/employee/menu">
                    <div class="form-group">
                        <label for="date">日期</label>
                        <input type="date" id="date" name="date" value="${queryDate}">
                    </div>
                    <div class="form-group">
                        <label for="mealTypeFilter">餐别</label>
                        <select id="mealTypeFilter" name="mealType">
                            <option value="早餐" ${queryMealType == '早餐' ? 'selected' : ''}>早餐</option>
                            <option value="午餐" ${queryMealType == '午餐' ? 'selected' : ''}>午餐</option>
                            <option value="晚餐" ${queryMealType == '晚餐' ? 'selected' : ''}>晚餐</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">查询菜单</button>
                </form>

                <div class="order-form">
                    <h2>${queryDate} ${queryMealType}菜单</h2>
                    <input type="hidden" id="orderDate" value="${queryDate}">
                    <input type="hidden" id="orderMealType" value="${queryMealType}">
                    <div class="form-group">
                        <label for="remark">备注</label>
                        <textarea id="remark" name="remark" rows="2" placeholder="口味、忌口等说明"></textarea>
                    </div>
                    <div class="menu-grid">
                        <c:if test="${empty menuList}">
                            <p class="empty-message">当前日期暂无菜单，请更换日期或餐别后再试</p>
                        </c:if>
                        <c:forEach var="menu" items="${menuList}">
                            <div class="menu-item">
                                <h4>${menu.recipe.name}</h4>
                                <p class="category">${menu.recipe.category}</p>
                                <p class="description">${menu.recipe.description}</p>
                                <p class="price">¥${menu.recipe.price}</p>
                                <p class="stock">库存：${menu.stock}</p>
                                <div class="item-actions">
                                    <label>
                                        <input type="checkbox" value="${menu.recipeId}">
                                        选择
                                    </label>
                                    <input type="number" min="1" max="${menu.stock}" value="1">
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="order-summary">
                        <div class="total-amount">合计：<span id="totalAmount">¥0.00</span></div>
                        <button type="button" class="btn btn-primary btn-large" onclick="createOrder()">提交订单</button>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <script src="/js/common.js"></script>
    <script src="/js/employee.js"></script>
</body>
</html>
