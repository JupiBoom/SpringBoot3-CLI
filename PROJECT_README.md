# 社区志愿者服务平台 - 后端项目

## 项目概述

这是一个基于 Spring Boot 3.4.x 构建的社区志愿者服务平台后端项目，提供活动管理、报名管理、服务记录、论坛交流等核心功能。

## 技术栈

- **框架**: Spring Boot 3.4.0
- **JDK**: Java 21
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **ORM**: MyBatis-Plus 3.5.9
- **构建工具**: Maven 3.9.6
- **API文档**: SpringDoc OpenAPI (Swagger)

## 项目结构

```
volunteer-service/
├── common/                 # 公共模块
│   ├── constant/          # 常量
│   ├── domain/            # 通用实体
│   ├── enums/             # 枚举
│   ├── exception/         # 异常
│   └── utils/             # 工具类
├── framework/             # 框架模块
│   ├── aspect/            # AOP切面
│   ├── config/            # 配置类
│   └── handler/           # 处理器
├── main/                  # 业务模块
│   ├── domain/            # 业务实体/VO/DTO
│   ├── mapper/            # 数据访问层
│   └── service/           # 业务逻辑层
└── web/                   # Web模块
    ├── controller/        # 控制器
    ├── job/               # 定时任务
    └── resources/         # 配置文件
```

## 核心功能模块

### 1. 活动管理模块

**功能特性:**
- 活动的增删改查
- 活动分类：环保/助老/教育/医疗/其他
- 活动状态：招募中/进行中/已完成/已取消
- 活动地点（支持经纬度）
- 需求人数管理
- 浏览次数统计

**API接口:**
- `POST /activity/create` - 创建活动
- `POST /activity/update` - 更新活动
- `POST /activity/delete` - 删除活动
- `GET /activity/get/{id}` - 获取活动详情
- `POST /activity/list/page` - 分页查询活动
- `POST /activity/list` - 查询活动列表
- `POST /activity/cancel/{id}` - 取消活动

### 2. 报名管理模块

**功能特性:**
- 志愿者报名活动
- 自动和人工报名审核
- 活动开始前1小时自动提醒（定时任务）
- 签到签出功能
- 报名状态跟踪

**API接口:**
- `POST /registration/register` - 报名活动
- `POST /registration/cancel` - 取消报名
- `POST /registration/review` - 审核报名
- `POST /registration/check-in` - 签到
- `POST /registration/check-out` - 签出
- `GET /registration/get/{id}` - 获取报名详情
- `GET /registration/my-list` - 获取我的报名列表
- `GET /registration/activity/{activityId}` - 获取活动报名列表

### 3. 服务记录模块

**功能特性:**
- 根据签到签出自动记录服务时长
- 5星评价系统（组织者评价志愿者）
- 志愿者自我评价
- 服务证明生成（带唯一编号）
- 累计服务时长统计

**API接口:**
- `GET /service-record/get/{id}` - 获取服务记录
- `POST /service-record/list/page` - 分页查询服务记录
- `GET /service-record/my-list` - 获取我的服务记录
- `POST /service-record/review` - 评价服务记录
- `POST /service-record/comment` - 志愿者评价
- `POST /service-record/generate-certificate/{id}` - 生成服务证明
- `GET /service-record/my-total-hours` - 获取累计服务时长
- `GET /service-record/my-service-count` - 获取累计服务次数

### 4. 活动论坛模块

**功能特性:**
- 经验分享帖子发布
- 活动回顾帖子
- 问题咨询
- 帖子置顶/精华功能
- 评论回复功能（支持二级回复）

**API接口:**
- `POST /forum/post/create` - 创建帖子
- `POST /forum/post/delete` - 删除帖子
- `GET /forum/post/get/{id}` - 获取帖子详情
- `POST /forum/post/list/page` - 分页查询帖子
- `GET /forum/post/top` - 获取置顶帖子
- `GET /forum/post/essence` - 获取精华帖子
- `POST /forum/comment/create` - 创建评论
- `GET /forum/comment/list/{postId}` - 获取评论列表

### 5. 照片墙模块

**功能特性:**
- 活动照片上传
- 精选照片展示
- 照片审核功能
- 按活动筛选照片

**API接口:**
- `POST /photo-wall/upload` - 上传照片
- `POST /photo-wall/delete` - 删除照片
- `GET /photo-wall/get/{id}` - 获取照片
- `POST /photo-wall/list/page` - 分页查询照片
- `GET /photo-wall/featured` - 获取精选照片
- `GET /photo-wall/activity/{activityId}` - 获取活动照片

## 数据库表结构

### 核心表

1. **user** - 用户表
2. **activity** - 活动表
3. **registration** - 报名表
4. **service_record** - 服务记录表
5. **forum_post** - 论坛帖子表
6. **forum_comment** - 论坛评论表
7. **photo_wall** - 照片墙表
8. **notification** - 通知消息表
9. **system_config** - 系统配置表

## 环境配置

### 环境要求

- JDK 21
- MySQL 8.0+
- Redis 6.0+
- Maven 3.9.6

### 配置文件

编辑 `web/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/volunteer_service?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
```

## 快速开始

### 1. 创建数据库

```sql
CREATE DATABASE volunteer_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 执行SQL脚本

运行 `asset/sql/volunteer_service.sql` 创建表结构和初始数据。

### 3. 编译项目

```bash
# 使用指定的Maven
D:\work\code\apache-maven-3.9.6-bin\apache-maven-3.9.6\bin\mvn clean install
```

### 4. 启动项目

```bash
# 使用指定的JDK
D:\work\code\jdk-21.0.6\bin\java -jar web/target/web-0.0.1.jar
```

或使用IDE启动 `WebApplication` 类。

### 5. 访问API文档

启动后访问: http://localhost:8080/swagger-ui.html

## 定时任务

### 活动提醒任务

- **执行频率**: 每5分钟
- **功能**: 检查活动开始前1小时的报名记录，发送提醒通知
- **实现类**: `com.rosy.web.job.ActivityReminderJob`

## 开发规范

### 包结构规范

- `domain.entity` - 数据库实体类
- `domain.vo` - 视图对象（返回给前端）
- `domain.dto` - 数据传输对象（接收前端参数）
- `domain.enums` - 枚举类
- `mapper` - MyBatis Mapper接口
- `service` - 业务逻辑接口
- `service.impl` - 业务逻辑实现
- `controller` - 控制器

### API设计规范

- 使用RESTful风格
- 统一返回格式: `ApiResponse<T>`
- 参数校验使用JSR-303注解
- 接口文档使用Swagger注解

## 安全说明

- 当前版本使用简单的UserHolder获取用户ID
- 生产环境应集成Spring Security + JWT
- 敏感操作需要权限校验

## 后续优化建议

1. 集成Spring Security实现完整的认证授权
2. 添加文件上传功能（头像、活动封面、照片墙）
3. 实现短信/邮件通知服务
4. 添加服务证明PDF生成功能
5. 实现数据统计分析功能
6. 添加缓存优化（Redis）
7. 集成消息队列处理通知
