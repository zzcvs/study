# 企业餐厅网络点餐系统

## 项目简介

企业餐厅网络点餐系统是一个基于B/S架构的Web应用系统，采用Spring Boot + JSP + MySQL技术栈开发。系统实现了完整的餐厅点餐管理功能，支持多角色权限控制，包括餐厅经理、厨房主管、配餐员、财务管理和企业员工等不同角色的差异化功能。

## 技术栈

### 后端技术
- **框架**: Spring Boot 3.2.0
- **ORM框架**: MyBatis-Plus 3.5.5
- **数据库**: MySQL 8.0+
- **连接池**: Druid 1.2.20
- **JDK版本**: JDK 17

### 前端技术
- **视图技术**: JSP
- **样式**: CSS3
- **脚本**: JavaScript (ES6+)
- **响应式设计**: 媒体查询

### 核心技术点
- Session与Cookie实现登录状态管理
- 文件上传与下载功能
- 基于角色的权限控制（RBAC）
- 拦截器实现权限验证
- RESTful API设计

## 系统架构

### 分层架构
```
├── controller/      # 控制层 - 处理HTTP请求
├── service/         # 业务逻辑层 - 处理业务逻辑
├── mapper/          # 数据访问层 - 数据库操作
├── entity/          # 实体类 - 数据模型
├── config/          # 配置类 - 系统配置
├── interceptor/     # 拦截器 - 权限验证
├── common/          # 公共类 - 通用工具
└── views/           # 视图层 - JSP页面
```

### 数据库设计
- **user**: 用户表
- **recipe**: 食谱表
- **menu**: 菜单表
- **order_info**: 订单表
- **order_detail**: 订单详情表
- **finance_record**: 财务记录表

## 功能模块

### 1. 用户登录与权限控制
- 登录页面（用户名、密码、记住我）
- Session存储用户登录状态
- Cookie辅助实现自动登录
- 基于角色的权限控制
- 登录拦截器防止未登录访问

### 2. 角色差异化界面
- **餐厅经理**: 系统管理、用户管理、食谱管理、订单管理、财务管理
- **厨房主管**: 订单生成与管理、制作状态更新
- **配餐员**: 订单打印与配餐管理
- **财务管理**: 统计报表与财务数据查看
- **企业员工**: 个人订餐、订单查询

### 3. 核心业务功能
- 菜品管理（增删改查、图片上传）
- 菜单管理（每日菜单设置）
- 订单管理（创建、查询、状态更新）
- 财务统计（日报、月报）

## 环境要求

### 开发环境
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- IDE: IntelliJ IDEA 或 Eclipse

### 运行环境
- JDK 17+
- MySQL 8.0+
- Tomcat 9.0+（Spring Boot内置）

## 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd canteen-ordering-system
```

### 2. 数据库配置

#### 2.1 创建数据库
```sql
CREATE DATABASE canteen_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 2.2 执行建表脚本
```bash
mysql -u root -p canteen_system < src/main/resources/sql/init.sql
```

或在MySQL客户端中执行：
```sql
USE canteen_system;
SOURCE src/main/resources/sql/init.sql;
```

#### 2.3 修改数据库连接配置
编辑 `src/main/resources/application.properties`：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/canteen_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 编译项目
```bash
mvn clean compile
```

### 4. 运行项目

#### 方式一：使用Maven运行
```bash
mvn spring-boot:run
```

#### 方式二：使用IDE运行
在IDE中找到 `CanteenOrderingSystemApplication.java` 类，运行main方法。

#### 方式三：打包运行
```bash
mvn clean package
java -jar target/canteen-ordering-system-1.0.0.jar
```

### 5. 访问系统
打开浏览器访问：`http://localhost:8080`

## 测试账号

系统已预置以下测试账号（密码均为：123456）：

| 角色 | 用户名 | 密码 | 功能权限 |
|------|--------|------|----------|
| 餐厅经理 | admin | 123456 | 系统全部管理权限 |
| 厨房主管 | kitchen | 123456 | 订单制作管理 |
| 配餐员 | distributor | 123456 | 订单配餐管理 |
| 财务管理 | finance | 123456 | 财务报表查看 |
| 企业员工 | employee1 | 123456 | 个人订餐 |
| 企业员工 | employee2 | 123456 | 个人订餐 |

## 项目结构

```
canteen-ordering-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── restaurant/
│   │   │           ├── CanteenOrderingSystemApplication.java
│   │   │           ├── common/
│   │   │           │   └── Result.java
│   │   │           ├── config/
│   │   │           │   ├── MybatisPlusConfig.java
│   │   │           │   ├── MyMetaObjectHandler.java
│   │   │           │   └── WebMvcConfig.java
│   │   │           ├── controller/
│   │   │           │   ├── LoginController.java
│   │   │           │   ├── ManagerController.java
│   │   │           │   ├── KitchenController.java
│   │   │           │   ├── DistributorController.java
│   │   │           │   ├── FinanceController.java
│   │   │           │   └── EmployeeController.java
│   │   │           ├── entity/
│   │   │           │   ├── User.java
│   │   │           │   ├── Recipe.java
│   │   │           │   ├── Menu.java
│   │   │           │   ├── OrderInfo.java
│   │   │           │   ├── OrderDetail.java
│   │   │           │   └── FinanceRecord.java
│   │   │           ├── interceptor/
│   │   │           │   └── LoginInterceptor.java
│   │   │           ├── mapper/
│   │   │           │   ├── UserMapper.java
│   │   │           │   ├── RecipeMapper.java
│   │   │           │   ├── MenuMapper.java
│   │   │           │   ├── OrderMapper.java
│   │   │           │   ├── OrderDetailMapper.java
│   │   │           │   └── FinanceRecordMapper.java
│   │   │           └── service/
│   │   │               ├── impl/
│   │   │               │   ├── UserServiceImpl.java
│   │   │               │   ├── RecipeServiceImpl.java
│   │   │               │   ├── MenuServiceImpl.java
│   │   │               │   ├── OrderServiceImpl.java
│   │   │               │   └── FinanceServiceImpl.java
│   │   │               ├── UserService.java
│   │   │               ├── RecipeService.java
│   │   │               ├── MenuService.java
│   │   │               ├── OrderService.java
│   │   │               └── FinanceService.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── mapper/
│   │   │   │   └── MenuMapper.xml
│   │   │   └── sql/
│   │   │       └── init.sql
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── views/
│   │       │       ├── login.jsp
│   │       │       ├── manager/
│   │       │       │   └── home.jsp
│   │       │       ├── kitchen/
│   │       │       │   └── home.jsp
│   │       │       ├── distributor/
│   │       │       │   └── home.jsp
│   │       │       ├── finance/
│   │       │       │   └── home.jsp
│   │       │       └── employee/
│   │       │           └── home.jsp
│   │       ├── css/
│   │       │   └── style.css
│   │       └── js/
│   │           ├── common.js
│   │           ├── login.js
│   │           ├── manager.js
│   │           ├── kitchen.js
│   │           ├── distributor.js
│   │           ├── finance.js
│   │           └── employee.js
│   └── test/
│       └── java/
├── docs/
│   └── database-er-design.md
├── pom.xml
└── README.md
```

## 核心功能说明

### 1. 登录功能
- 使用Session存储用户信息
- 支持记住我功能（Cookie）
- 登录成功后根据角色跳转到不同页面
- 拦截器验证登录状态

### 2. 权限控制
- 基于角色的访问控制（RBAC）
- 不同角色拥有不同的菜单和功能
- 拦截器防止越权访问

### 3. 订单流程
```
员工下单 → 待处理(PENDING) → 制作中(COOKING) → 待取餐(READY) → 已完成(COMPLETED)
                    ↓
                  已取消(CANCELLED)
```

### 4. 菜单管理
- 餐厅经理可以设置每日菜单
- 支持按餐别（早餐、午餐、晚餐）分类
- 实时库存管理

### 5. 财务统计
- 日报表：每日订单数、总金额、支付情况
- 月报表：月度汇总统计
- 财务记录：历史数据查询

## 界面设计

### 设计特点
- 现代简洁的设计风格
- 渐变色主题（紫色系）
- 响应式布局，支持移动端
- 流畅的动画效果
- 统一的页面布局

### 页面布局
- 侧边栏导航
- 顶部状态栏
- 主内容区域
- 统一的页脚

## 常见问题

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 确认数据库用户名和密码是否正确
- 检查application.properties中的配置

### 2. 端口被占用
修改application.properties中的端口号：
```properties
server.port=8081
```

### 3. JSP页面无法显示
- 确认已添加tomcat-embed-jasper依赖
- 检查spring.mvc.view配置

### 4. 文件上传失败
- 确保uploads目录存在且有写权限
- 检查文件大小限制配置

## 开发说明

### 添加新功能
1. 创建实体类（entity）
2. 创建Mapper接口
3. 创建Service接口和实现类
4. 创建Controller
5. 创建JSP页面
6. 添加JavaScript交互

### 代码规范
- 遵循阿里巴巴Java开发规范
- 使用Lombok简化代码
- 统一的异常处理
- 完善的注释说明

## 部署说明

### 生产环境部署
1. 修改数据库配置为生产环境配置
2. 修改文件上传路径
3. 配置日志级别
4. 打包为WAR或JAR文件
5. 部署到Tomcat或直接运行JAR

### Docker部署
```bash
# 构建镜像
docker build -t canteen-system .

# 运行容器
docker run -d -p 8080:8080 --name canteen canteen-system
```

## 技术支持

如有问题，请联系开发团队或提交Issue。

## 许可证

本项目仅供学习和教学使用。

## 更新日志

### v1.0.0 (2026-01-05)
- 初始版本发布
- 实现核心功能模块
- 完成多角色权限控制
- 实现响应式界面设计

## 致谢

感谢所有参与本项目开发的团队成员！
