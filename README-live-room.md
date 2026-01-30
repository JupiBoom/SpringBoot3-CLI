# 电商直播间管理系统

## 功能概述

本系统是一个简单的电商直播间管理功能，包含以下核心功能：

1. **直播间管理**：创建直播间、设置直播状态
2. **商品关联**：直播间关联带货商品、切换讲解商品、展示商品卖点
3. **数据记录**：记录和展示直播间数据，包括销售数据、商品排行榜、观众人数
4. **数据分析**：直播转化率统计、观众留存曲线

## 技术栈

- 后端：Spring Boot 3.4.x
- 数据库：MySQL
- ORM：MyBatis-Plus
- 前端：HTML5 + Bootstrap 5 + Chart.js

## 数据库设计

系统包含以下数据表：

1. **live_room**：直播间表
2. **live_room_product**：直播间商品关联表
3. **live_room_stats**：直播间数据统计表
4. **live_room_viewer**：直播间观众记录表
5. **live_room_order**：直播间订单记录表

## API接口

### 直播间管理

- `GET /api/live-room/list` - 获取直播间列表
- `GET /api/live-room/page` - 分页获取直播间列表
- `POST /api/live-room/add` - 创建直播间
- `PUT /api/live-room/update` - 更新直播间
- `DELETE /api/live-room/delete/{id}` - 删除直播间
- `POST /api/live-room/update-status/{id}` - 更新直播间状态

### 商品关联

- `GET /api/live-room-product/list/{liveRoomId}` - 获取直播间商品列表
- `POST /api/live-room-product/add` - 添加商品到直播间
- `PUT /api/live-room-product/update` - 更新商品信息
- `DELETE /api/live-room-product/remove/{id}` - 移除商品
- `POST /api/live-room-product/toggle-current/{id}` - 切换当前讲解商品
- `GET /api/live-room-product/sales-ranking/{liveRoomId}` - 获取商品销售排行榜

### 数据统计

- `GET /api/live-room-stats/list/{liveRoomId}` - 获取直播间统计数据列表
- `GET /api/live-room-stats/page/{liveRoomId}` - 分页获取直播间统计数据
- `POST /api/live-room-stats/generate-daily` - 生成每日统计数据
- `POST /api/live-room-stats/update-viewer-stats` - 更新观众统计数据
- `POST /api/live-room-stats/update-sales-stats` - 更新销售统计数据

### 数据分析

- `GET /api/live-room-analytics/conversion-rate/{liveRoomId}` - 获取直播间转化率统计
- `GET /api/live-room-analytics/viewer-retention/{liveRoomId}` - 获取观众留存曲线
- `POST /api/live-room-analytics/conversion-rate-comparison` - 获取多个直播间的转化率对比
- `POST /api/live-room-analytics/viewer-retention-comparison` - 获取观众留存曲线对比

## 使用方法

### 1. 启动应用

```bash
# 进入项目目录
cd SpringBoot3-CLI

# 启动应用
mvn spring-boot:run
```

### 2. 访问管理页面

打开浏览器访问：`http://localhost:8080/live-room-management`

### 3. 基本操作流程

1. **创建直播间**：点击"创建直播间"按钮，填写基本信息
2. **添加商品**：选择直播间，点击"添加商品"，关联带货商品
3. **开始直播**：设置直播间状态为"直播中"
4. **查看数据**：在"数据统计"页面查看实时数据
5. **分析效果**：在"数据分析"页面查看转化率和留存曲线

## 测试

运行测试类 `LiveRoomManagementTest` 来验证功能：

```bash
mvn test -Dtest=LiveRoomManagementTest
```

## 扩展功能

可以进一步扩展以下功能：

1. **实时弹幕系统**：集成WebSocket实现实时弹幕
2. **商品推荐算法**：基于用户行为推荐商品
3. **多直播间管理**：支持同时管理多个直播间
4. **用户权限管理**：不同角色拥有不同权限
5. **数据导出**：支持导出各类统计数据

## 注意事项

1. 确保数据库连接配置正确
2. 首次使用需要执行SQL脚本创建数据表
3. 商品图片URL需要确保可访问
4. 直播流地址需要配置正确的推流/拉流地址