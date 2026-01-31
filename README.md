# 社区志愿者服务平台

## 项目简介

社区志愿者服务平台是一个基于Spring Boot 3.4.0的后端项目，提供志愿者活动管理、报名管理、服务记录和活动论坛等功能。

## 功能特性

- **活动管理**: 支持活动的创建、查询、更新和删除，包括活动分类（环保/助老/教育/医疗）和状态管理（招募中/进行中/已完成）
- **报名管理**: 志愿者报名功能，支持自动和人工审核，以及活动开始前1小时的提醒通知
- **服务记录**: 根据签到和签出自动记录服务时长，支持5星评价和内容反馈，可生成服务证明
- **活动论坛**: 支持志愿者进行活动经验分享和活动照片墙功能
- **通知系统**: 支持活动提醒、报名审核结果和系统通知
- **定时任务**: 自动检查即将开始的活动并发送提醒，自动更新活动状态

## 技术栈

- Spring Boot 3.4.0
- MyBatis-Plus
- MySQL
- Redis
- Swagger/OpenAPI 3
- Lombok
- JUnit 5

## 项目结构

```
SpringBoot3-CLI/
├── main/                    # 主模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/rosy/
│   │   │   │   ├── main/   # 主业务逻辑
│   │   │   │   │   ├── controller/  # 控制器层
│   │   │   │   │   ├── domain/      # 实体类
│   │   │   │   │   ├── enums/       # 枚举类
│   │   │   │   │   ├── mapper/      # 数据访问层
│   │   │   │   │   ├── service/     # 服务层
│   │   │   │   │   └── task/        # 定时任务
│   │   │   │   └── web/    # Web控制器
│   │   │   └── resources/
│   │   │       └── application.yml  # 配置文件
│   │   └── test/           # 测试代码
├── web/                     # Web模块
│   └── src/
│       └── main/
│           └── java/com/rosy/web/controller/main/  # Web控制器
├── asset/
│   └── sql/
│       └── volunteer_service.sql  # 数据库脚本
├── API文档.md               # API接口文档
└── README.md               # 项目说明
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库初始化

执行 `asset/sql/volunteer_service.sql` 脚本创建数据库表结构。

### 3. 配置文件

修改 `main/src/main/resources/application.yml` 文件，配置数据库和Redis连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/volunteer_service?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
```

### 4. 运行项目

```bash
# 进入项目目录
cd SpringBoot3-CLI

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run -pl main
```

### 5. 访问API文档

项目启动后，访问 `http://localhost:8080/swagger-ui.html` 查看API文档。

## 主要模块说明

### 1. 活动管理模块

- **ActivityController**: 活动管理接口
- **ActivityService**: 活动业务逻辑
- **ActivityMapper**: 活动数据访问

### 2. 报名管理模块

- **EnrollmentController**: 报名管理接口
- **EnrollmentService**: 报名业务逻辑
- **EnrollmentMapper**: 报名数据访问

### 3. 服务记录模块

- **ServiceRecordController**: 服务记录管理接口
- **ServiceRecordService**: 服务记录业务逻辑
- **ServiceRecordMapper**: 服务记录数据访问

### 4. 活动论坛模块

- **ForumPostController**: 论坛帖子管理接口
- **ForumCommentController**: 论坛评论管理接口
- **ForumPostService**: 论坛帖子业务逻辑
- **ForumCommentService**: 论坛评论业务逻辑

### 5. 通知管理模块

- **NotificationController**: 通知管理接口
- **NotificationService**: 通知业务逻辑
- **NotificationMapper**: 通知数据访问

### 6. 定时任务

- **ActivityReminderTask**: 活动提醒定时任务

## API文档

详细的API文档请参考 [API文档.md](API文档.md)

## 测试

运行测试：

```bash
mvn test
```

## 许可证

MIT License