---
mode: agent
description: "为前端或后端生成一个多阶段构建的Dockerfile"
---

请为 `{{name}}` 应用创建一个优化的、多阶段构建的 Dockerfile。

- 如果 `{{name}}` 是 `backend` (Spring Boot)，请使用 `maven` 作为构建阶段，`eclipse-temurin` 作为运行阶段。
- 如果 `{{name}}` 是 `frontend` (React)，请使用 `node` 作为构建阶段，`nginx` 作为运行阶段，并配置 Nginx 托管静态文件和反向代理 API 请求。