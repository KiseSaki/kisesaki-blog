---
mode: agent
description: "创建一个GitHub Actions工作流文件"
---

请为 `{{name}}` 场景创建一个 GitHub Actions 工作流 `.yml` 文件。

- 如果 `{{name}}` 是 `ci` (持续集成)，工作流应在 `push` 到 `main` 分支时触发，并分别执行后端（`mvn clean install`）和前端（`npm install && npm run build && npm run test`）的构建和测试。
- 如果 `{{name}}` 是 `cd` (持续部署)，工作流应在 `ci` 成功后触发，负责构建 Docker 镜像，推送到 Docker Hub 或其他镜像仓库。