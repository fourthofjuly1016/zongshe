# 运输车辆调度后端接口设计与实现报告

## 1. 设计要求核对

| 编号 | 设计要求 | 接口 | 当前实现 |
|---|---|---|---|
| 1 | 增加需求，基于工厂生成 | `POST /api/demands` | 已完成，生成 `FACTORY-...` 任务编号，写入 `transport_task` |
| 2 | 需求失效，软删除 | `PATCH /api/demands/{id}/invalidate` | 已完成，只更新 `deleted=1`、`task_status=INVALID` |
| 3 | 增加车辆 | `POST /api/vehicles` 或 `POST /api/cars` | 已完成，写入车牌、车型、容量和初始状态 |
| 4 | 车辆失效，软删除 | `PATCH /api/vehicles/{id}/invalidate` | 已完成，只更新 `deleted=1`、`car_status=INVALID` |
| 5 | 生成并指派任务 | `POST /api/dispatch/assignments` | 已完成，建立需求与车辆、司机关系并更新双方状态 |
| 6 | 设置/更新车辆位置 | `POST /api/vehicles/{id}/position` | 已完成，每次写入一个轨迹点，保留历史位置 |
| 7 | 设置/更新车辆其他状态 | `PATCH /api/vehicles/{id}/status` | 已完成，同时写入 `vehicle_status_log` |
| 8 | 计算运输收益 | `GET /api/dispatch/assignments/{id}/revenue` | 已完成基础估算，可由前端直接调用 |
| 9 | 计算需求与车辆匹配度 | `POST /api/matching/score` | 已完成规则版核心函数，后续可替换为优化算法 |

## 2. 接口示例

### 2.1 工厂生成需求

```http
POST /api/demands
Content-Type: application/json

{
  "goodsId": 1,
  "pickupPoiId": 1,
  "deliveryPoiId": 2,
  "quantity": 2,
  "priority": 3,
  "plannedPickupTime": "2026-09-22T09:00:00",
  "plannedDeliveryTime": "2026-09-22T14:00:00"
}
```

服务层自动生成需求编号，设置 `taskStatus=PENDING`、`source=FACTORY`，并校验货物和两个 POI 必须存在且起终点不能相同。

### 2.2 生成调度指派

```http
POST /api/dispatch/assignments
Content-Type: application/json

{
  "taskId": 1,
  "carId": 1,
  "driverId": 1
}
```

后端先调用匹配度函数。如果车辆不满足载重、容积、危险品或有效状态要求，则返回 400；通过后创建 `dispatch_assignment`，将需求和车辆状态更新为 `ASSIGNED`。

### 2.3 更新车辆位置

```http
POST /api/vehicles/1/position
Content-Type: application/json

{
  "latitude": 30.5728000,
  "longitude": 104.0668000,
  "speed": 42.5,
  "heading": 90,
  "gpsValid": true,
  "source": "MODBUS"
}
```

位置表是轨迹表，因此“更新当前位置”采用新增轨迹点的方式实现；最新点通过 `GET /api/vehicles/{id}/position/latest` 查询。

### 2.4 更新车辆状态

```http
PATCH /api/vehicles/1/status
Content-Type: application/json

{
  "status": "TRANSPORTING",
  "taskId": 1,
  "remark": "已离开装货点"
}
```

允许状态包括 `IDLE`、`TO_PICKUP`、`LOADING`、`TRANSPORTING`、`UNLOADING`、`EXCEPTION`、`COMPLETED` 和 `ASSIGNED`。

### 2.5 匹配度计算

```http
POST /api/matching/score
Content-Type: application/json

{
  "taskId": 1,
  "carId": 1
}
```

当前规则总分为 100 分：空闲状态 25 分、载重 25 分、容积 20 分、危险品适配 15 分、车型适配 10 分、需求优先级 0 至 5 分。载重不足、容积不足、危险品车型不符、车辆失效或车辆不在 `IDLE` 状态时，`eligible=false`。

## 3. 分层设计原理

```text
Controller
    接收 HTTP 请求、校验 DTO、返回统一 ApiResponse
Service
    编排事务、执行业务规则、转换 Entity 与 Response
Repository
    使用 Spring Data JPA 访问数据库
Entity
    映射物理表和外键关系
Database
    保存基础数据、需求、指派、轨迹和状态历史
```

接口不直接接收或返回 JPA Entity，避免数据库字段和懒加载关系泄漏到前端。所有接口返回统一结构：`success`、`message`、`data`、`timestamp`。

## 4. 业务流程

```text
工厂生成需求
  -> 需求校验和入库
  -> 查询有效车辆
  -> 计算匹配度
  -> 创建 dispatch_assignment
  -> 更新需求和车辆状态
  -> 接收 Modbus/接口位置点
  -> 记录 vehicle_status_log
  -> 查询收益和执行结果
```

需求和车辆采用软删除，是为了保留历史指派、轨迹和状态日志。车辆位置采用追加轨迹点，是为了支持后续轨迹回放和仿真，而不是覆盖历史数据。

## 5. 后续算法升级点

当前匹配规则是可解释的前置版本，适合先完成接口联调。后续可以在 `MatchingService` 中替换为容量约束、时间窗、道路距离、拥堵和危险等级综合评分，再接入 OR-Tools 或 jsprit。接口结构不需要改变，前端只需要继续读取 `score`、`eligible` 和 `reasons`。

## 6. 数据库升级

- 新数据库：执行 `数据库navicat/my-table.sql`。
- 已执行 `upgrade_v2.sql` 的调度数据库：执行一次 `数据库navicat/upgrade_v3.sql`。
- Spring Boot 的 `ddl-auto` 仍为 `validate`，启动时只检查实体与物理表，不自动删除或重建数据。

## 7. 本次改动文件

- `entity/TransportTask.java`、`entity/DispatchAssignment.java`、`entity/VehicleStatusLog.java`：补齐需求、指派和车辆状态历史的实体映射。
- `repository/TransportTaskRepository.java`、`repository/DispatchAssignmentRepository.java`、`repository/VehicleStatusLogRepository.java`：提供有效需求、重复指派和状态历史查询。
- `service/DemandService.java`：生成工厂需求编号、校验货物和 POI、执行需求软删除。
- `service/DispatchService.java`：在一个事务中完成匹配、重复指派检查、关系写入、状态更新和收益估算。
- `service/MatchingService.java`：提供可替换的规则匹配核心函数。
- `service/VehiclePositionService.java`、`service/VehicleStatusService.java`：分别处理轨迹追加和状态历史记录。
- `controller/DemandController.java`、`controller/VehicleController.java`、`controller/DispatchController.java`、`controller/MatchingController.java`：暴露业务 HTTP 接口。
- `dto/`：所有请求和响应均使用 DTO，参数由 Jakarta Validation 校验。
- `exception/GlobalExceptionHandler.java`：统一处理参数错误、资源不存在、数据冲突和业务规则错误。
- `数据库navicat/my-table.sql`、`数据库navicat/upgrade_v3.sql`：增加 `car.deleted`、`transport_task.source`、`transport_task.deleted` 及对应索引。

## 8. 事务和异常处理过程

1. Controller 使用 `@Valid` 和路径参数约束，拒绝空编号、非法坐标、负数量和非法优先级。
2. Service 只查询 `deleted=false` 的需求和车辆，避免失效数据再次参与调度。
3. 指派接口在同一个事务中依次执行资源查询、匹配度判断、重复指派检查、指派保存和状态更新；任一步失败都会回滚。
4. 位置更新采用追加记录，状态更新采用“当前车辆状态 + 历史日志”双写，满足仿真回放和审计需求。
5. 响应统一包装为 `ApiResponse`。资源不存在返回 404，参数校验和业务规则错误返回 400，唯一键或外键冲突返回 409，未预期异常记录服务端日志并返回 500。

## 9. 验证结果和上线前动作

- `mvn compile`：通过。
- `mvn test-compile`：通过。
- `MatchingServiceTests`：通过，覆盖载重不足时返回不可指派及原因。
- `mvn package -DskipTests`：通过，生成 `target/demo-0.0.1-SNAPSHOT.jar`。
- 完整 Spring 集成测试和接口联调依赖 MySQL 可连接，并且数据库已经执行 `upgrade_v3.sql`。本次检查时本机 MySQL 服务未启动，因此没有伪造“迁移成功”或“完整接口测试通过”的结论。

启动 MySQL 后，按顺序执行数据库升级脚本，再启动 Spring Boot。由于项目使用 `ddl-auto=validate`，如果遗漏新增列，应用会在启动阶段直接报告表结构不一致，便于尽早发现环境问题。
