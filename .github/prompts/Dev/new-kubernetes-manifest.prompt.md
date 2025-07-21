---
mode: agent
description: "为应用（前端/后端）生成基本的 Kubernetes Deployment 和 Service manifest"
---
请为 `{{name}}` 应用生成 Kubernetes 的 `Deployment` 和 `Service` YAML 清单文件。

要求：
1.  Deployment 应包含3个副本 (`replicas: 3`)。
2.  使用 `app: {{name}}` 作为 label 和 selector。
3.  容器镜像名称遵循 `[your-dockerhub-username]/kisesaki-blog-{{name}}:latest` 格式。
4.  为容器设置合理的资源请求和限制 (requests/limits for cpu/memory)。
5.  Service 应为 `ClusterIP` 类型，暴露容器的端口。

请输入应用名称 (例如 `frontend` 或 `backend`): {{name}}