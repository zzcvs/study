# 企业餐厅网络点餐系统 - 部署指南

## 目录
- [系统要求](#系统要求)
- [环境准备](#环境准备)
- [数据库部署](#数据库部署)
- [应用部署](#应用部署)
- [配置说明](#配置说明)
- [常见问题](#常见问题)
- [性能优化](#性能优化)

## 系统要求

### 硬件要求
- CPU: 2核及以上
- 内存: 4GB及以上
- 硬盘: 20GB及以上可用空间

### 软件要求
- 操作系统: Windows 10/11, Linux (Ubuntu 18.04+, CentOS 7+), macOS 10.15+
- JDK: 17或更高版本
- MySQL: 8.0或更高版本
- Maven: 3.6或更高版本（仅开发环境需要）

## 环境准备

### 1. 安装JDK 17

#### Windows
1. 下载JDK 17：https://www.oracle.com/java/technologies/downloads/#java17
2. 运行安装程序，按提示完成安装
3. 配置环境变量：
   - JAVA_HOME: C:\Program Files\Java\jdk-17
   - Path: 添加 %JAVA_HOME%\bin

#### Linux (Ubuntu)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

#### macOS
```bash
brew install openjdk@17
```

### 2. 安装MySQL 8.0

#### Windows
1. 下载MySQL Installer：https://dev.mysql.com/downloads/installer/
2. 运行安装程序，选择Server only
3. 设置root密码（建议：123456）
4. 完成安装

#### Linux (Ubuntu)
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

#### macOS
```bash
brew install mysql
brew services start mysql
```

### 3. 安装Maven（开发环境）

#### Windows
1. 下载Maven：https://maven.apache.org/download.cgi
2. 解压到指定目录（如：C:\Program Files\Apache\maven）
3. 配置环境变量：
   - MAVEN_HOME: C:\Program Files\Apache\maven
   - Path: 添加 %MAVEN_HOME%\bin

#### Linux/macOS
```bash
brew install maven
mvn -version
```

## 数据库部署

### 1. 创建数据库
```bash
mysql -u root -p
```

在MySQL命令行中执行：
```sql
CREATE DATABASE IF NOT EXISTS canteen_system 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### 2. 执行建表脚本
```bash
mysql -u root -p canteen_system < src/main/resources/sql/init.sql
```

或在MySQL客户端中：
```sql
USE canteen_system;
SOURCE src/main/resources/sql/init.sql;
```

### 3. 验证数据库
```sql
USE canteen_system;
SHOW TABLES;
SELECT COUNT(*) FROM user;
```

### 4. 创建数据库用户（可选）
```sql
CREATE USER 'canteen'@'localhost' IDENTIFIED BY 'canteen123';
GRANT ALL PRIVILEGES ON canteen_system.* TO 'canteen'@'localhost';
FLUSH PRIVILEGES;
```

## 应用部署

### 方式一：开发环境部署

#### 1. 修改配置文件
编辑 `src/main/resources/application.properties`：
```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/canteen_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password

# 服务器端口
server.port=8080

# 文件上传路径
upload.path=uploads
```

#### 2. 编译项目
```bash
mvn clean compile
```

#### 3. 运行项目
```bash
mvn spring-boot:run
```

#### 4. 访问系统
打开浏览器访问：http://localhost:8080

### 方式二：打包部署

#### 1. 打包项目
```bash
mvn clean package -DskipTests
```

打包成功后，在 `target` 目录下生成 `canteen-ordering-system-1.0.0.jar`

#### 2. 运行JAR包
```bash
java -jar target/canteen-ordering-system-1.0.0.jar
```

#### 3. 后台运行（Linux）
```bash
nohup java -jar target/canteen-ordering-system-1.0.0.jar > app.log 2>&1 &
```

#### 4. 指定配置文件运行
```bash
java -jar target/canteen-ordering-system-1.0.0.jar --spring.config.location=application-prod.properties
```

### 方式三：Tomcat部署

#### 1. 打包为WAR
修改pom.xml，将打包方式改为war：
```xml
<packaging>war</packaging>
```

打包：
```bash
mvn clean package -DskipTests
```

#### 2. 部署到Tomcat
将生成的WAR文件复制到Tomcat的webapps目录：
```bash
cp target/canteen-ordering-system-1.0.0.war /path/to/tomcat/webapps/
```

#### 3. 启动Tomcat
```bash
cd /path/to/tomcat/bin
./startup.sh  # Linux/macOS
startup.bat   # Windows
```

### 方式四：Docker部署

#### 1. 创建Dockerfile
在项目根目录创建 `Dockerfile`：
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/canteen-ordering-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 2. 构建镜像
```bash
docker build -t canteen-system .
```

#### 3. 运行容器
```bash
docker run -d -p 8080:8080 --name canteen canteen-system
```

#### 4. 使用Docker Compose
创建 `docker-compose.yml`：
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: canteen_system
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/canteen_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 123456

volumes:
  mysql-data:
```

启动服务：
```bash
docker-compose up -d
```

## 配置说明

### 1. 数据库连接池配置
```properties
# Druid连接池配置
spring.datasource.druid.initial-size=5
spring.datasource.druid.min-idle=5
spring.datasource.druid.max-active=20
spring.datasource.druid.max-wait=60000
```

### 2. MyBatis-Plus配置
```properties
# MyBatis-Plus配置
mybatis-plus.mapper-locations=classpath:mapper/*.xml
mybatis-plus.type-aliases-package=com.restaurant.entity
mybatis-plus.configuration.map-underscore-to-camel-case=true
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

### 3. 文件上传配置
```properties
# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 4. 日志配置
```properties
# 日志配置
logging.level.com.restaurant=debug
logging.level.org.springframework=info
logging.file.name=logs/application.log
```

### 5. 生产环境配置
创建 `application-prod.properties`：
```properties
# 生产环境配置
server.port=80
spring.datasource.url=jdbc:mysql://prod-db:3306/canteen_system?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai
spring.datasource.username=prod_user
spring.datasource.password=prod_password

# 关闭SQL日志
logging.level.com.restaurant=info
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.nologging.NoOpImpl
```

## 常见问题

### 1. 端口被占用
**问题**: `Address already in use: bind`

**解决方案**:
- Windows:
  ```bash
  netstat -ano | findstr :8080
  taskkill /PID <进程ID> /F
  ```
- Linux/macOS:
  ```bash
  lsof -ti:8080 | xargs kill -9
  ```

或修改端口号：
```properties
server.port=8081
```

### 2. 数据库连接失败
**问题**: `Communications link failure`

**解决方案**:
1. 检查MySQL服务是否启动
2. 确认数据库用户名和密码
3. 检查防火墙设置
4. 验证数据库URL配置

### 3. 内存不足
**问题**: `Java heap space`

**解决方案**:
```bash
java -Xms512m -Xmx1024m -jar target/canteen-ordering-system-1.0.0.jar
```

### 4. 文件上传失败
**问题**: `The field file exceeds its maximum permitted size`

**解决方案**:
修改配置：
```properties
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
```

### 5. JSP页面无法显示
**问题**: 访问JSP页面显示404或源码

**解决方案**:
1. 确认pom.xml中包含tomcat-embed-jasper依赖
2. 检查spring.mvc.view配置
3. 确保JSP文件放在正确的目录

## 性能优化

### 1. JVM参数优化
```bash
java -server \
     -Xms512m \
     -Xmx1024m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar target/canteen-ordering-system-1.0.0.jar
```

### 2. 数据库优化
```sql
-- 添加索引
CREATE INDEX idx_user_username ON user(username);
CREATE INDEX idx_order_date ON order_info(order_date);
CREATE INDEX idx_order_status ON order_info(order_status);
```

### 3. 连接池优化
```properties
spring.datasource.druid.initial-size=10
spring.datasource.druid.min-idle=10
spring.datasource.druid.max-active=50
spring.datasource.druid.max-wait=10000
```

### 4. 启用Gzip压缩
```properties
server.compression.enabled=true
server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain
```

### 5. 静态资源缓存
```properties
spring.web.resources.cache.period=3600
```

## 监控与维护

### 1. 应用监控
使用Spring Boot Actuator：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

访问：http://localhost:8080/actuator/health

### 2. 日志管理
```bash
# 查看日志
tail -f logs/application.log

# 搜索错误日志
grep ERROR logs/application.log
```

### 3. 数据库备份
```bash
# 备份数据库
mysqldump -u root -p canteen_system > backup_$(date +%Y%m%d).sql

# 恢复数据库
mysql -u root -p canteen_system < backup_20260105.sql
```

### 4. 定时任务清理
创建定时任务清理过期数据：
```java
@Scheduled(cron = "0 0 2 * * ?")
public void cleanExpiredData() {
    // 清理逻辑
}
```

## 安全建议

1. 修改默认密码
2. 启用HTTPS
3. 配置防火墙
4. 定期备份数据
5. 更新依赖版本
6. 启用SQL注入防护

## 联系支持

如有部署问题，请联系技术支持团队。
