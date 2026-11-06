# 企业餐厅网络点餐系统 - 数据库ER设计

## 一、数据库ER图

```
┌─────────────────┐
│     User        │  用户表
├─────────────────┤
│ id (PK)         │  用户ID
│ username        │  用户名
│ password        │  密码
│ real_name       │  真实姓名
│ role            │  角色
│ phone           │  联系电话
│ email           │  邮箱
│ department      │  部门
│ status          │  状态
│ create_time     │  创建时间
│ update_time     │  更新时间
└────────┬────────┘
         │ 1
         │
         │ N
┌────────▼────────┐
│   Order_Info    │  订单表
├─────────────────┤
│ id (PK)         │  订单ID
│ order_no        │  订单号
│ user_id (FK)    │  用户ID
│ user_name       │  用户姓名
│ department      │  部门
│ order_date      │  订餐日期
│ meal_type       │  餐别
│ total_amount    │  订单总金额
│ order_status    │  订单状态
│ payment_status  │  支付状态
│ distribute_status│ 配餐状态
│ remark          │  备注
│ create_time     │  创建时间
│ update_time     │  更新时间
└────────┬────────┘
         │ 1
         │
         │ N
┌────────▼────────┐
│  Order_Detail   │  订单详情表
├─────────────────┤
│ id (PK)         │  订单详情ID
│ order_id (FK)   │  订单ID
│ recipe_id (FK)  │  食谱ID
│ recipe_name     │  菜品名称
│ price           │  单价
│ quantity        │  数量
│ subtotal        │  小计
│ create_time     │  创建时间
└─────────────────┘

┌─────────────────┐
│     Recipe      │  食谱表
├─────────────────┤
│ id (PK)         │  食谱ID
│ name            │  菜品名称
│ category        │  菜品分类
│ description     │  菜品描述
│ ingredients     │  主要食材
│ image_url       │  菜品图片URL
│ price           │  单价
│ status          │  状态
│ create_time     │  创建时间
│ update_time     │  更新时间
└────────┬────────┘
         │ 1
         │
         │ N
┌────────▼────────┐
│      Menu       │  菜单表
├─────────────────┤
│ id (PK)         │  菜单ID
│ menu_date       │  菜单日期
│ meal_type       │  餐别
│ recipe_id (FK)  │  食谱ID
│ stock           │  库存数量
│ sold_count      │  已售数量
│ status          │  状态
│ create_time     │  创建时间
│ update_time     │  更新时间
└─────────────────┘

┌─────────────────┐
│ Finance_Record  │  财务记录表
├─────────────────┤
│ id (PK)         │  财务记录ID
│ record_date     │  记录日期
│ order_count     │  订单数量
│ total_amount    │  总金额
│ paid_amount     │  已支付金额
│ unpaid_amount   │  未支付金额
│ meal_type       │  餐别
│ create_time     │  创建时间
└─────────────────┘
```

## 二、实体关系说明

### 1. 用户表 (User)
- 存储系统所有用户信息
- 角色包括：餐厅经理(MANAGER)、厨房主管(KITCHEN)、配餐员(DISTRIBUTOR)、财务管理(FINANCE)、企业员工(EMPLOYEE)
- 与订单表是一对多关系，一个用户可以创建多个订单

### 2. 食谱表 (Recipe)
- 存储所有菜品的基础信息
- 菜品分类：主食、汤品、凉菜、热菜、甜点、饮料
- 与菜单表是一对多关系，一个食谱可以出现在多个菜单中

### 3. 菜单表 (Menu)
- 存储每日菜单信息
- 包含日期、餐别（早餐、午餐、晚餐）、库存等信息
- 与食谱表是多对一关系，一个菜单项对应一个食谱

### 4. 订单表 (Order_Info)
- 存储订单主信息
- 包含订单号、用户、日期、金额、状态等信息
- 与用户表是多对一关系，与订单详情表是一对多关系

### 5. 订单详情表 (Order_Detail)
- 存储订单的具体菜品信息
- 与订单表是多对一关系，与食谱表是多对一关系
- 记录每个订单包含的菜品、数量、价格等

### 6. 财务记录表 (Finance_Record)
- 存储每日财务统计数据
- 按日期和餐别统计订单数量、金额等信息

## 三、数据字典

### 1. 用户角色 (role)
- MANAGER: 餐厅经理 - 拥有系统所有管理权限
- KITCHEN: 厨房主管 - 管理订单制作
- DISTRIBUTOR: 配餐员 - 管理订单配餐
- FINANCE: 财务管理 - 查看财务报表
- EMPLOYEE: 企业员工 - 个人订餐

### 2. 订单状态 (order_status)
- PENDING: 待处理
- COOKING: 制作中
- READY: 待取餐
- COMPLETED: 已完成
- CANCELLED: 已取消

### 3. 支付状态 (payment_status)
- UNPAID: 未支付
- PAID: 已支付

### 4. 配餐状态 (distribute_status)
- PENDING: 待配餐
- DISTRIBUTING: 配餐中
- COMPLETED: 已完成

### 5. 餐别 (meal_type)
- 早餐
- 午餐
- 晚餐

### 6. 菜品分类 (category)
- 主食
- 汤品
- 凉菜
- 热菜
- 甜点
- 饮料

## 四、索引设计

1. **用户表索引**
   - idx_username: 用户名索引，用于快速登录查询
   - idx_role: 角色索引，用于按角色查询用户

2. **食谱表索引**
   - idx_category: 分类索引，用于按分类查询菜品
   - idx_status: 状态索引，用于查询上架/下架菜品

3. **菜单表索引**
   - idx_menu_date: 日期索引，用于查询指定日期菜单
   - uk_date_meal_recipe: 唯一索引，防止同一日期同一餐别重复添加同一菜品

4. **订单表索引**
   - idx_user_id: 用户ID索引，用于查询用户订单
   - idx_order_date: 订餐日期索引，用于按日期查询订单
   - idx_order_status: 订单状态索引，用于按状态查询订单
   - idx_order_no: 订单号索引，用于快速查询订单

5. **财务记录表索引**
   - idx_record_date: 日期索引，用于按日期查询财务记录
   - idx_meal_type: 餐别索引，用于按餐别查询财务记录

## 五、外键约束

1. **menu.recipe_id** → **recipe.id**
   - 级联删除：删除食谱时，自动删除相关菜单项

2. **order_info.user_id** → **user.id**
   - 级联删除：删除用户时，自动删除相关订单

3. **order_detail.order_id** → **order_info.id**
   - 级联删除：删除订单时，自动删除订单详情

4. **order_detail.recipe_id** → **recipe.id**
   - 级联删除：删除食谱时，自动删除相关订单详情
