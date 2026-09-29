# 运输车辆调度后端

这是运输车辆调度与仿真项目的 Spring Boot 后端，采用 Spring MVC、Spring Data JPA、Bean Validation 和 MySQL/MariaDB。

## 当前结构

- `config`：跨域配置，允许 Vue 开发服务器访问后端。
- `controller`：车辆、货物、POI、道路的 REST CRUD 接口。
- `service`：业务服务和 DTO 映射。
- `repository`：Spring Data JPA 数据访问层。
- `entity`：数据库实体，不直接作为 API 请求和响应对象。
- `dto`：接口请求、响应对象及参数校验规则。
- `common`、`exception`：统一响应格式和全局异常处理。
- `数据库navicat/my-table.sql`：数据库及物理表结构脚本。
- `docs/backend-api-design.md`：9 项后端接口要求、请求示例和业务流程说明。

## 接口

- `GET /api/hello`：基础连通性测试。
- `GET /api/health`：服务健康状态。
- `/api/cars`：车辆 CRUD。
- `/api/goods`：货物 CRUD。
- `/api/pois`：POI CRUD。
- `/api/roads`：道路 CRUD。
- `/api/demands`：工厂需求生成和需求软删除。
- `/api/vehicles`：车辆生成、车辆软删除、位置和状态更新。
- `/api/dispatch/assignments`：需求与车辆/司机指派、收益估算。
- `/api/matching/score`：需求与车辆规则匹配度。

每类资源均支持：

```text
GET    /api/{resources}
GET    /api/{resources}/{id}
POST   /api/{resources}
PUT    /api/{resources}/{id}
DELETE /api/{resources}/{id}
```

成功和失败响应统一包含 `success`、`message`、`data`、`timestamp`。参数非法返回 400，资源不存在返回 404，唯一键或外键冲突返回 409。

项目默认连接 `transport_db` 数据库。新数据库执行 `数据库navicat/my-table.sql`；原九表数据库执行一次 `数据库navicat/upgrade_v2.sql`。

后端默认监听 `http://localhost:8080`，可通过环境变量 `SERVER_PORT` 修改。
