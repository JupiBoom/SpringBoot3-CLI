# 电商订单状态通知服务

## 项目概述

该项目是一个基于Spring Boot 3的电商订单状态通知服务，用于监听订单状态变更事件，并通过多种渠道（短信、微信、邮件）向用户发送通知。

## 功能特性

1. **订单状态变更监听**：实时监听订单状态变更事件（支付成功/发货/签收）
2. **多渠道通知**：支持短信、微信、邮件三种通知渠道
3. **模板化通知**：不同订单状态对应不同的通知模板，支持变量替换
4. **用户通知偏好管理**：用户可以设置自己的通知偏好
5. **通知记录管理**：记录所有通知的发送状态和结果
6. **失败重试机制**：通知发送失败时自动重试（指数退避策略）

## 技术栈

- **Spring Boot 3**：项目基础框架
- **Spring Cloud**：微服务架构支持
- **Spring Cloud Alibaba Nacos**：服务发现和配置中心
- **Spring Cloud Stream**：消息驱动（RabbitMQ）
- **MyBatis Plus**：ORM框架
- **MySQL**：数据库
- **Redis**：缓存
- **Lombok**：简化代码
- **Hutool**：工具类库

## 项目结构

```
notification/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── rosy/
│   │   │           └── notification/
│   │   │               ├── config/          # 配置类
│   │   │               ├── controller/      # 控制器
│   │   │               ├── domain/          # 领域模型
│   │   │               │   ├── entity/      # 实体类
│   │   │               │   └── enums/       # 枚举类
│   │   │               ├── impl/            # 服务实现
│   │   │               ├── listener/        # 事件监听器
│   │   │               ├── mapper/          # Mapper接口
│   │   │               ├── service/         # 服务接口
│   │   │               └── NotificationApplication.java  # 启动类
│   │   └── resources/
│   │       ├── mapper/      # MyBatis映射文件
│   │       ├── sql/         # 数据库脚本
│   │       └── application.yml  # 配置文件
│   └── test/
│       └── java/
│           └── com/
│               └── rosy/
│                   └── notification/
│                       └── NotificationServiceTest.java  # 测试类
└── pom.xml  # Maven配置
```

## 数据库结构

### 主要表结构

1. **notification_record**：通知记录表
   - 记录所有通知的发送状态和结果

2. **user_notification_preference**：用户通知偏好设置表
   - 记录用户的通知偏好设置

3. **notification_template**：通知模板表
   - 存储不同通知类型和订单状态的模板内容

## 配置说明

### 应用配置（application.yml）

```yaml
server:
  port: 8083

spring:
  application:
    name: notification-service
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
        file-extension: yaml
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ecommerce?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: 123456
  redis:
    host: localhost
    port: 6379
    password: 123456
    database: 0
```

## 运行方式

### 1. 启动依赖服务

- **Nacos**：服务发现和配置中心
- **RabbitMQ**：消息队列
- **MySQL**：数据库
- **Redis**：缓存

### 2. 初始化数据库

执行SQL脚本：`src/main/resources/sql/notification.sql`

### 3. 启动服务

```bash
mvn spring-boot:run
```

## 接口说明

### 1. 处理订单状态变更事件

**接口地址**：`POST /api/notification/order/status/change`

**请求体**：
```json
{
  "eventId": "event_123",
  "orderId": "order_123456",
  "productName": "测试商品",
  "orderAmount": 99.99,
  "orderStatus": "pay_success",
  "userId": "user_1",
  "userPhone": "13800138000",
  "userEmail": "user1@example.com",
  "userWechatOpenid": "openid_123",
  "eventTime": "2025-12-24T10:00:00"
}
```

### 2. 发送通知

**接口地址**：`POST /api/notification/send`

**请求体**：
```json
{
  "notificationId": "notification_123",
  "orderId": "order_123456",
  "userId": "user_1",
  "notificationType": "sms",
  "templateId": "sms_pay_success",
  "targetAddress": "13800138000",
  "sendStatus": "pending",
  "sendCount": 0,
  "firstSendTime": "2025-12-24T10:00:00"
}
```

### 3. 获取用户通知偏好设置

**接口地址**：`GET /api/notification/user/preferences/{userId}`

### 4. 获取通知模板

**接口地址**：`GET /api/notification/template/{notificationType}/{orderStatus}`

### 5. 重试发送通知

**接口地址**：`POST /api/notification/retry/{notificationId}`

## 扩展说明

### 添加新的通知渠道

1. 实现`INotificationChannelService`接口
2. 在实现类上添加`@Service`注解，指定bean名称为通知类型
3. 配置通知模板

### 添加新的订单状态

1. 在`OrderStatusEnum`中添加新的枚举值
2. 配置对应的通知模板
3. 处理订单状态变更事件

## 测试

运行测试类：`NotificationServiceTest`

## 注意事项

1. 确保所有依赖服务（Nacos、RabbitMQ、MySQL、Redis）都已启动
2. 配置文件中的数据库连接信息需要根据实际情况修改
3. 短信、微信、邮件的实际发送逻辑需要根据具体的API进行实现
4. 通知重试机制使用指数退避策略，避免对服务造成过大压力