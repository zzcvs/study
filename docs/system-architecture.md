# 企业餐厅网络点餐系统 - 系统架构说明

## 一、系统概述

企业餐厅网络点餐系统是一个基于B/S架构的Web应用系统，采用Spring Boot + JSP + MySQL技术栈开发。系统实现了完整的餐厅点餐管理功能，支持多角色权限控制，包括餐厅经理、厨房主管、配餐员、财务管理和企业员工等不同角色的差异化功能。

### 1.1 系统目标
- 提供便捷的在线点餐服务
- 实现高效的订单管理流程
- 支持多角色权限控制
- 提供完善的财务管理功能
- 实现美观友好的用户界面

### 1.2 技术特点
- 采用Spring Boot框架，简化开发配置
- 使用MyBatis-Plus简化数据库操作
- 实现Session与Cookie的登录状态管理
- 支持文件上传与下载功能
- 基于拦截器的权限验证机制
- 响应式设计，支持多终端访问

## 二、技术架构

### 2.1 整体架构

```
┌─────────────────────────────────────────────────────────┐
│                     表现层 (View)                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │   JSP    │  │   CSS    │  │  JavaScript│         │
│  └──────────┘  └──────────┘  └──────────┘           │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                   控制层 (Controller)                 │
│  ┌──────────────┐  ┌──────────────┐                 │
│  │LoginController│  │ManagerController│              │
│  └──────────────┘  └──────────────┘                 │
│  ┌──────────────┐  ┌──────────────┐                 │
│  │KitchenController│ │DistributorController│          │
│  └──────────────┘  └──────────────┘                 │
│  ┌──────────────┐  ┌──────────────┐                 │
│  │FinanceController│ │EmployeeController│            │
│  └──────────────┘  └──────────────┘                 │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                 业务逻辑层 (Service)                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │UserService│  │RecipeService│ │MenuService│         │
│  └──────────┘  └──────────┘  └──────────┘           │
│  ┌──────────┐  ┌──────────┐                           │
│  │OrderService│ │FinanceService│                       │
│  └──────────┘  └──────────┘                           │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                 数据访问层 (Mapper)                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │UserMapper│  │RecipeMapper│ │MenuMapper│           │
│  └──────────┘  └──────────┘  └──────────┘           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │OrderMapper│ │OrderDetailMapper│ │FinanceMapper│    │
│  └──────────┘  └──────────┘  └──────────┘           │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                   数据库层 (Database)                   │
│              MySQL 8.0+                                │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │   User   │  │  Recipe  │  │   Menu   │           │
│  └──────────┘  └──────────┘  └──────────┘           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │OrderInfo │  │OrderDetail│ │FinanceRecord│          │
│  └──────────┘  └──────────┘  └──────────┘           │
└─────────────────────────────────────────────────────────┘
```

### 2.2 分层说明

#### 表现层 (View)
- **技术**: JSP + HTML + CSS + JavaScript
- **职责**: 
  - 展示用户界面
  - 接收用户输入
  - 显示系统数据
  - 提供交互体验

#### 控制层 (Controller)
- **技术**: Spring MVC
- **职责**:
  - 处理HTTP请求
  - 参数验证
  - 调用业务逻辑
  - 返回视图或数据

#### 业务逻辑层 (Service)
- **技术**: Spring Service
- **职责**:
  - 实现业务逻辑
  - 事务管理
  - 数据处理
  - 业务规则验证

#### 数据访问层 (Mapper)
- **技术**: MyBatis-Plus
- **职责**:
  - 数据库操作
  - SQL映射
  - 结果映射
  - 缓存管理

#### 数据库层 (Database)
- **技术**: MySQL 8.0+
- **职责**:
  - 数据存储
  - 数据持久化
  - 数据完整性
  - 数据安全

## 三、核心功能模块

### 3.1 用户登录与权限控制模块

#### 功能描述
- 用户登录验证
- Session状态管理
- Cookie自动登录
- 基于角色的权限控制
- 登录拦截器

#### 技术实现
```java
// 登录验证
@PostMapping("/login")
public Result<User> login(@RequestParam String username, 
                          @RequestParam String password,
                          HttpSession session) {
    User user = userService.login(username, password);
    if (user != null) {
        session.setAttribute("user", user);
        return Result.success(user);
    }
    return Result.error("用户名或密码错误");
}

// 权限拦截器
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
```

### 3.2 菜品管理模块

#### 功能描述
- 菜品增删改查
- 菜品分类管理
- 菜品图片上传
- 菜品上下架管理

#### 技术实现
```java
// 菜品图片上传
@Override
public boolean uploadRecipeImage(Long recipeId, MultipartFile file) {
    String newFilename = UUID.randomUUID().toString() + extension;
    File destFile = new File(uploadPath, newFilename);
    file.transferTo(destFile);
    
    Recipe recipe = getById(recipeId);
    recipe.setImageUrl("/uploads/" + newFilename);
    return updateById(recipe);
}
```

### 3.3 菜单管理模块

#### 功能描述
- 每日菜单设置
- 按餐别分类（早餐、午餐、晚餐）
- 库存管理
- 菜品关联

#### 技术实现
```java
// 获取今日菜单
@Select("SELECT m.*, r.name as recipe_name, r.price, r.category " +
        "FROM menu m LEFT JOIN recipe r ON m.recipe_id = r.id " +
        "WHERE m.menu_date = #{menuDate} AND m.status = 1")
List<Menu> getTodayMenu(@Param("menuDate") LocalDate menuDate);
```

### 3.4 订单管理模块

#### 功能描述
- 订单创建
- 订单查询
- 订单状态更新
- 订单取消

#### 订单流程
```
员工下单 → 待处理(PENDING) → 制作中(COOKING) → 待取餐(READY) → 已完成(COMPLETED)
                    ↓
                  已取消(CANCELLED)
```

#### 技术实现
```java
// 创建订单
@Transactional
public OrderInfo createOrder(OrderInfo orderInfo, List<OrderDetail> orderDetails) {
    String orderNo = generateOrderNo();
    orderInfo.setOrderNo(orderNo);
    orderInfo.setOrderStatus("PENDING");
    
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (OrderDetail detail : orderDetails) {
        detail.setSubtotal(detail.getPrice().multiply(new BigDecimal(detail.getQuantity())));
        totalAmount = totalAmount.add(detail.getSubtotal());
    }
    orderInfo.setTotalAmount(totalAmount);
    
    save(orderInfo);
    for (OrderDetail detail : orderDetails) {
        detail.setOrderId(orderInfo.getId());
        orderDetailMapper.insert(detail);
    }
    return orderInfo;
}
```

### 3.5 财务管理模块

#### 功能描述
- 日报表统计
- 月报表统计
- 财务记录查询
- 收支分析

#### 技术实现
```java
// 日报表统计
public Map<String, Object> getDailyStatistics(LocalDate date) {
    List<OrderInfo> orders = orderMapper.getOrdersByDate(date);
    
    int totalOrders = orders.size();
    BigDecimal totalAmount = BigDecimal.ZERO;
    BigDecimal paidAmount = BigDecimal.ZERO;
    BigDecimal unpaidAmount = BigDecimal.ZERO;
    
    for (OrderInfo order : orders) {
        totalAmount = totalAmount.add(order.getTotalAmount());
        if ("PAID".equals(order.getPaymentStatus())) {
            paidAmount = paidAmount.add(order.getTotalAmount());
        } else {
            unpaidAmount = unpaidAmount.add(order.getTotalAmount());
        }
    }
    
    Map<String, Object> statistics = new HashMap<>();
    statistics.put("totalOrders", totalOrders);
    statistics.put("totalAmount", totalAmount);
    statistics.put("paidAmount", paidAmount);
    statistics.put("unpaidAmount", unpaidAmount);
    
    return statistics;
}
```

## 四、数据库设计

### 4.1 数据库ER图

详见 [database-er-design.md](database-er-design.md)

### 4.2 核心表结构

#### 用户表 (user)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 用户ID（主键） |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(100) | 密码 |
| real_name | VARCHAR(50) | 真实姓名 |
| role | VARCHAR(20) | 角色 |
| phone | VARCHAR(20) | 联系电话 |
| email | VARCHAR(100) | 邮箱 |
| department | VARCHAR(50) | 部门 |
| status | TINYINT | 状态 |

#### 食谱表 (recipe)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 食谱ID（主键） |
| name | VARCHAR(100) | 菜品名称 |
| category | VARCHAR(50) | 菜品分类 |
| description | TEXT | 菜品描述 |
| ingredients | TEXT | 主要食材 |
| image_url | VARCHAR(255) | 菜品图片URL |
| price | DECIMAL(10,2) | 单价 |
| status | TINYINT | 状态 |

#### 订单表 (order_info)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 订单ID（主键） |
| order_no | VARCHAR(50) | 订单号（唯一） |
| user_id | BIGINT | 用户ID（外键） |
| user_name | VARCHAR(50) | 用户姓名 |
| order_date | DATE | 订餐日期 |
| meal_type | VARCHAR(20) | 餐别 |
| total_amount | DECIMAL(10,2) | 订单总金额 |
| order_status | VARCHAR(20) | 订单状态 |
| payment_status | VARCHAR(20) | 支付状态 |
| distribute_status | VARCHAR(20) | 配餐状态 |

## 五、角色权限设计

### 5.1 角色定义

| 角色 | 代码 | 权限范围 |
|------|------|----------|
| 餐厅经理 | MANAGER | 系统全部管理权限 |
| 厨房主管 | KITCHEN | 订单制作管理 |
| 配餐员 | DISTRIBUTOR | 订单配餐管理 |
| 财务管理 | FINANCE | 财务报表查看 |
| 企业员工 | EMPLOYEE | 个人订餐 |

### 5.2 权限矩阵

| 功能模块 | MANAGER | KITCHEN | DISTRIBUTOR | FINANCE | EMPLOYEE |
|----------|---------|---------|-------------|---------|----------|
| 用户管理 | ✓ | ✗ | ✗ | ✗ | ✗ |
| 食谱管理 | ✓ | ✗ | ✗ | ✗ | ✗ |
| 菜单管理 | ✓ | ✗ | ✗ | ✗ | ✗ |
| 订单查看 | ✓ | ✓ | ✓ | ✗ | ✓(个人) |
| 订单处理 | ✓ | ✓ | ✓ | ✗ | ✗ |
| 财务查看 | ✓ | ✗ | ✗ | ✓ | ✗ |
| 个人订餐 | ✗ | ✗ | ✗ | ✗ | ✓ |

## 六、界面设计

### 6.1 设计原则
- 简洁现代的设计风格
- 渐变色主题（紫色系）
- 响应式布局，支持移动端
- 流畅的动画效果
- 统一的页面布局

### 6.2 页面布局
```
┌─────────────────────────────────────────────────┐
│  侧边栏导航            │
│  - 首页                │  顶部状态栏
│  - 用户管理            │  - 用户信息
│  - 订单管理            │  - 当前时间
│  - 财务管理            │
│                       │
│  退出登录              │
└───────────────────────┴────────────────────────┘
│                                                 │
│              主内容区域                           │
│                                                 │
│  - 数据统计卡片                                  │
│  - 表格展示                                      │
│  - 操作按钮                                      │
│                                                 │
└─────────────────────────────────────────────────┘
```

### 6.3 响应式设计
- 桌面端：完整布局
- 平板端：自适应布局
- 移动端：单列布局

## 七、安全设计

### 7.1 认证机制
- Session认证
- Cookie辅助
- 密码加密存储

### 7.2 授权机制
- 基于角色的访问控制（RBAC）
- 拦截器验证
- URL权限控制

### 7.3 数据安全
- SQL注入防护（MyBatis-Plus）
- XSS防护
- 文件上传安全
- 敏感数据加密

## 八、性能优化

### 8.1 数据库优化
- 合理使用索引
- 查询优化
- 连接池配置
- 读写分离（可选）

### 8.2 缓存策略
- MyBatis二级缓存
- 静态资源缓存
- Session缓存

### 8.3 前端优化
- CSS/JS压缩
- 图片懒加载
- 异步加载

## 九、部署架构

### 9.1 开发环境
```
┌─────────────────┐
│   开发者电脑    │
│  - JDK 17       │
│  - Maven        │
│  - IDE          │
│  - MySQL        │
└─────────────────┘
```

### 9.2 生产环境
```
┌─────────────────┐
│   负载均衡器    │
└────────┬────────┘
         │
    ┌────┴────┐
    │         │
┌───▼───┐ ┌──▼────┐
│ 应用1  │ │ 应用2  │
└───┬───┘ └──┬────┘
    │         │
    └────┬────┘
         │
    ┌────▼────┐
    │ MySQL   │
    │ 主从    │
    └─────────┘
```

## 十、扩展性设计

### 10.1 模块化设计
- 各功能模块独立
- 接口定义清晰
- 低耦合高内聚

### 10.2 可扩展性
- 支持新增角色
- 支持新增功能模块
- 支持数据库扩展
- 支持微服务改造

## 十一、总结

企业餐厅网络点餐系统采用现代化的技术栈和架构设计，实现了完整的餐厅点餐管理功能。系统具有以下特点：

1. **技术先进**: 采用Spring Boot + MyBatis-Plus等主流技术
2. **架构清晰**: 分层架构，职责明确
3. **功能完善**: 覆盖餐厅管理的主要业务场景
4. **界面美观**: 现代化的UI设计，响应式布局
5. **安全可靠**: 完善的权限控制和数据安全机制
6. **易于维护**: 代码规范，文档完善
7. **可扩展性强**: 模块化设计，易于扩展

系统满足高校课程设计的要求，可以直接用于教学演示和实际应用。
