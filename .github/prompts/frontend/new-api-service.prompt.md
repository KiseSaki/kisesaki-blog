---
mode: agent
description: "为新的业务实体创建一套完整的API请求函数"
---
请在 `src/api/` 目录下，为 `{{name}}` 实体创建一个新的 API 服务文件 `{{name}}Api.ts`。

该文件应包含以下标准的 CRUD 函数：
- `getAll(params)`: 获取列表（支持分页、过滤参数）
- `getById(id)`: 根据 ID 获取单个实体
- `create(data)`: 创建新实体
- `update(id, data)`: 更新指定实体
- `remove(id)`: 删除指定实体

所有函数都应：
1.  从 `src/api/http.ts` 中导入配置好的 axios 实例。
2.  从 `src/types/` 目录导入相关的请求和响应类型。
3.  处理异步逻辑并返回 `Promise`。