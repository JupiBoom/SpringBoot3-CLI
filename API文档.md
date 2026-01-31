# 社区志愿者服务平台 API 文档

## 项目概述

社区志愿者服务平台是一个基于Spring Boot 3.4.0的后端项目，提供志愿者活动管理、报名管理、服务记录和活动论坛等功能。

## 技术栈

- Spring Boot 3.4.0
- MyBatis-Plus
- MySQL
- Redis
- Swagger/OpenAPI 3

## API 接口文档

### 1. 活动管理模块

#### 1.1 创建活动
- **接口地址**: `POST /activity`
- **请求参数**: 
  ```json
  {
    "title": "活动标题",
    "description": "活动描述",
    "startTime": "2024-01-01T09:00:00",
    "endTime": "2024-01-01T12:00:00",
    "location": "活动地点",
    "requiredPeople": 20,
    "category": 1,
    "organizerId": 1
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": 1
  }
  ```

#### 1.2 根据ID获取活动详情
- **接口地址**: `GET /activity/{id}`
- **路径参数**: 
  - `id`: 活动ID
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {
      "id": 1,
      "title": "活动标题",
      "description": "活动描述",
      "startTime": "2024-01-01T09:00:00",
      "endTime": "2024-01-01T12:00:00",
      "location": "活动地点",
      "requiredPeople": 20,
      "enrolledPeople": 5,
      "category": 1,
      "status": 1,
      "organizerId": 1,
      "createTime": "2024-01-01T08:00:00",
      "updateTime": "2024-01-01T08:00:00"
    }
  }
  ```

#### 1.3 分页查询活动
- **接口地址**: `GET /activity/page`
- **查询参数**:
  - `current`: 当前页码
  - `size`: 每页大小
  - `title`: 活动标题（可选）
  - `category`: 活动分类（可选）
  - `status`: 活动状态（可选）
  - `organizerId`: 组织者ID（可选）
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {
      "records": [...],
      "total": 100,
      "size": 10,
      "current": 1,
      "pages": 10
    }
  }
  ```

#### 1.4 更新活动
- **接口地址**: `PUT /activity/{id}`
- **路径参数**: 
  - `id`: 活动ID
- **请求参数**: 
  ```json
  {
    "title": "更新后的活动标题",
    "description": "更新后的活动描述",
    "startTime": "2024-01-01T09:00:00",
    "endTime": "2024-01-01T12:00:00",
    "location": "更新后的活动地点",
    "requiredPeople": 25,
    "category": 1
  }
  ```

#### 1.5 删除活动
- **接口地址**: `DELETE /activity/{id}`
- **路径参数**: 
  - `id`: 活动ID

#### 1.6 批量删除活动
- **接口地址**: `DELETE /activity/batch`
- **请求参数**: 
  ```json
  [1, 2, 3]
  ```

#### 1.7 更新活动状态
- **接口地址**: `PUT /activity/{id}/status`
- **路径参数**: 
  - `id`: 活动ID
- **请求参数**: 
  ```json
  {
    "status": 2
  }
  ```

#### 1.8 获取活动分类列表
- **接口地址**: `GET /activity/categories`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "code": 1,
        "desc": "环保"
      },
      {
        "code": 2,
        "desc": "助老"
      },
      {
        "code": 3,
        "desc": "教育"
      },
      {
        "code": 4,
        "desc": "医疗"
      }
    ]
  }
  ```

### 2. 报名管理模块

#### 2.1 创建报名
- **接口地址**: `POST /enrollment`
- **请求参数**: 
  ```json
  {
    "activityId": 1,
    "userId": 1,
    "remark": "我有相关经验"
  }
  ```

#### 2.2 根据ID获取报名详情
- **接口地址**: `GET /enrollment/{id}`
- **路径参数**: 
  - `id`: 报名ID

#### 2.3 分页查询报名
- **接口地址**: `GET /enrollment/page`
- **查询参数**:
  - `current`: 当前页码
  - `size`: 每页大小
  - `activityId`: 活动ID（可选）
  - `userId`: 用户ID（可选）
  - `status`: 报名状态（可选）

#### 2.4 更新报名
- **接口地址**: `PUT /enrollment/{id}`
- **路径参数**: 
  - `id`: 报名ID
- **请求参数**: 
  ```json
  {
    "remark": "更新后的备注"
  }
  ```

#### 2.5 删除报名
- **接口地址**: `DELETE /enrollment/{id}`
- **路径参数**: 
  - `id`: 报名ID

#### 2.6 批量删除报名
- **接口地址**: `DELETE /enrollment/batch`
- **请求参数**: 
  ```json
  [1, 2, 3]
  ```

#### 2.7 审核报名
- **接口地址**: `PUT /enrollment/{id}/review`
- **路径参数**: 
  - `id`: 报名ID
- **请求参数**: 
  ```json
  {
    "status": 2,
    "reviewRemark": "审核通过"
  }
  ```

#### 2.8 批量审核报名
- **接口地址**: `PUT /enrollment/batch/review`
- **请求参数**: 
  ```json
  {
    "ids": [1, 2, 3],
    "status": 2,
    "reviewRemark": "批量审核通过"
  }
  ```

#### 2.9 获取报名状态列表
- **接口地址**: `GET /enrollment/statuses`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": [
      {
        "code": 1,
        "desc": "待审核"
      },
      {
        "code": 2,
        "desc": "已通过"
      },
      {
        "code": 3,
        "desc": "已拒绝"
      }
    ]
  }
  ```

### 3. 服务记录模块

#### 3.1 创建服务记录
- **接口地址**: `POST /service-record`
- **请求参数**: 
  ```json
  {
    "activityId": 1,
    "userId": 1,
    "checkInTime": "2024-01-01T09:00:00",
    "checkOutTime": "2024-01-01T12:00:00"
  }
  ```

#### 3.2 根据ID获取服务记录详情
- **接口地址**: `GET /service-record/{id}`
- **路径参数**: 
  - `id`: 服务记录ID

#### 3.3 分页查询服务记录
- **接口地址**: `GET /service-record/page`
- **查询参数**:
  - `current`: 当前页码
  - `size`: 每页大小
  - `activityId`: 活动ID（可选）
  - `userId`: 用户ID（可选）

#### 3.4 更新服务记录
- **接口地址**: `PUT /service-record/{id}`
- **路径参数**: 
  - `id`: 服务记录ID
- **请求参数**: 
  ```json
  {
    "checkInTime": "2024-01-01T09:00:00",
    "checkOutTime": "2024-01-01T12:00:00"
  }
  ```

#### 3.5 删除服务记录
- **接口地址**: `DELETE /service-record/{id}`
- **路径参数**: 
  - `id`: 服务记录ID

#### 3.6 批量删除服务记录
- **接口地址**: `DELETE /service-record/batch`
- **请求参数**: 
  ```json
  [1, 2, 3]
  ```

#### 3.7 签到
- **接口地址**: `POST /service-record/check-in`
- **请求参数**: 
  ```json
  {
    "activityId": 1,
    "userId": 1
  }
  ```

#### 3.8 签出
- **接口地址**: `POST /service-record/check-out`
- **请求参数**: 
  ```json
  {
    "activityId": 1,
    "userId": 1
  }
  ```

#### 3.9 评价服务
- **接口地址**: `PUT /service-record/{id}/evaluate`
- **路径参数**: 
  - `id`: 服务记录ID
- **请求参数**: 
  ```json
  {
    "rating": 5,
    "comment": "非常棒的活动体验"
  }
  ```

#### 3.10 生成服务证明
- **接口地址**: `POST /service-record/{id}/certificate`
- **路径参数**: 
  - `id`: 服务记录ID

#### 3.11 获取用户服务时长统计
- **接口地址**: `GET /service-record/user/{userId}/total-hours`
- **路径参数**: 
  - `userId`: 用户ID

### 4. 活动论坛模块

#### 4.1 创建论坛帖子
- **接口地址**: `POST /forum-post`
- **请求参数**: 
  ```json
  {
    "activityId": 1,
    "userId": 1,
    "title": "活动经验分享",
    "content": "这次活动非常有意义...",
    "images": ["image1.jpg", "image2.jpg"]
  }
  ```

#### 4.2 根据ID获取帖子详情
- **接口地址**: `GET /forum-post/{id}`
- **路径参数**: 
  - `id`: 帖子ID

#### 4.3 分页查询论坛帖子
- **接口地址**: `GET /forum-post/page`
- **查询参数**:
  - `current`: 当前页码
  - `size`: 每页大小
  - `activityId`: 关联活动ID（可选）
  - `userId`: 用户ID（可选）
  - `title`: 帖子标题（可选）

#### 4.4 更新论坛帖子
- **接口地址**: `PUT /forum-post/{id}`
- **路径参数**: 
  - `id`: 帖子ID
- **请求参数**: 
  ```json
  {
    "title": "更新后的帖子标题",
    "content": "更新后的帖子内容",
    "images": ["image1.jpg", "image2.jpg"]
  }
  ```

#### 4.5 删除论坛帖子
- **接口地址**: `DELETE /forum-post/{id}`
- **路径参数**: 
  - `id`: 帖子ID

#### 4.6 批量删除论坛帖子
- **接口地址**: `DELETE /forum-post/batch`
- **请求参数**: 
  ```json
  [1, 2, 3]
  ```

#### 4.7 点赞帖子
- **接口地址**: `POST /forum-post/{id}/like`
- **路径参数**: 
  - `id`: 帖子ID

#### 4.8 取消点赞帖子
- **接口地址**: `DELETE /forum-post/{id}/like`
- **路径参数**: 
  - `id`: 帖子ID

#### 4.9 创建论坛评论
- **接口地址**: `POST /forum-comment`
- **请求参数**: 
  ```json
  {
    "postId": 1,
    "userId": 1,
    "content": "很有意义的分享",
    "parentId": null
  }
  ```

#### 4.10 根据ID获取评论详情
- **接口地址**: `GET /forum-comment/{id}`
- **路径参数**: 
  - `id`: 评论ID

#### 4.11 分页查询论坛评论
- **接口地址**: `GET /forum-comment/page`
- **查询参数**:
  - `current`: 当前页码
  - `size`: 每页大小
  - `postId`: 帖子ID（可选）
  - `userId`: 用户ID（可选）
  - `parentId`: 父评论ID（可选）

#### 4.12 获取帖子的所有评论
- **接口地址**: `GET /forum-comment/post/{postId}`
- **路径参数**: 
  - `postId`: 帖子ID

#### 4.13 更新论坛评论
- **接口地址**: `PUT /forum-comment/{id}`
- **路径参数**: 
  - `id`: 评论ID
- **请求参数**: 
  ```json
  {
    "content": "更新后的评论内容"
  }
  ```

#### 4.14 删除论坛评论
- **接口地址**: `DELETE /forum-comment/{id}`
- **路径参数**: 
  - `id`: 评论ID

#### 4.15 批量删除论坛评论
- **接口地址**: `DELETE /forum-comment/batch`
- **请求参数**: 
  ```json
  [1, 2, 3]
  ```

#### 4.16 点赞评论
- **接口地址**: `POST /forum-comment/{id}/like`
- **路径参数**: 
  - `id`: 评论ID

#### 4.17 取消点赞评论
- **接口地址**: `DELETE /forum-comment/{id}/like`
- **路径参数**: 
  - `id`: 评论ID

### 5. 通知管理模块

#### 5.1 发送通知
- **接口地址**: `POST /notification`
- **请求参数**:
  - `userId`: 接收用户ID
  - `title`: 通知标题
  - `content`: 通知内容
  - `type`: 通知类型
  - `relatedId`: 关联ID（可选）

#### 5.2 批量发送通知
- **接口地址**: `POST /notification/batch`
- **请求参数**:
  - `userIds`: 接收用户ID列表
  - `title`: 通知标题
  - `content`: 通知内容
  - `type`: 通知类型
  - `relatedId`: 关联ID（可选）

#### 5.3 根据ID获取通知详情
- **接口地址**: `GET /notification/{id}`
- **路径参数**: 
  - `id`: 通知ID

#### 5.4 分页查询通知
- **接口地址**: `GET /notification/page`
- **查询参数**:
  - `current`: 当前页码
  - `size`: 每页大小
  - `userId`: 用户ID（可选）
  - `type`: 通知类型（可选）
  - `isRead`: 是否已读（可选）

#### 5.5 标记通知为已读
- **接口地址**: `PUT /notification/{id}/read`
- **路径参数**: 
  - `id`: 通知ID

#### 5.6 批量标记通知为已读
- **接口地址**: `PUT /notification/batch/read`
- **请求参数**: 
  ```json
  [1, 2, 3]
  ```

#### 5.7 标记用户所有通知为已读
- **接口地址**: `PUT /notification/user/{userId}/read-all`
- **路径参数**: 
  - `userId`: 用户ID

#### 5.8 获取用户未读通知数量
- **接口地址**: `GET /notification/unread-count`
- **查询参数**:
  - `userId`: 用户ID

#### 5.9 获取通知类型列表
- **接口地址**: `GET /notification/types`

## 数据模型

### 1. 活动分类 (ActivityCategoryEnum)
- `1`: 环保
- `2`: 助老
- `3`: 教育
- `4`: 医疗

### 2. 活动状态 (ActivityStatusEnum)
- `1`: 招募中
- `2`: 进行中
- `3`: 已完成
- `4`: 已取消

### 3. 报名状态 (EnrollmentStatusEnum)
- `1`: 待审核
- `2`: 已通过
- `3`: 已拒绝

### 4. 通知类型 (NotificationTypeEnum)
- `1`: 活动提醒
- `2`: 报名审核结果
- `3`: 系统通知

## 定时任务

### 1. 活动提醒任务
- **执行时间**: 每小时执行一次
- **功能**: 检查1小时内即将开始的活动，向已报名并通过审核的用户发送提醒通知

### 2. 活动状态更新任务
- **执行时间**: 每天凌晨2点
- **功能**: 检查并更新活动状态，将已结束的活动状态更新为"已完成"，将已开始但未结束的活动状态更新为"进行中"

## 错误码说明

- `200`: 操作成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 禁止访问
- `404`: 资源不存在
- `500`: 服务器内部错误