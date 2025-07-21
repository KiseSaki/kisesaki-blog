---
mode: agent
description: "创建一个符合项目规范的、可复用的页面组件"
---
请为我创建一个名为 `{{name}}` 的页面级组件。

此组件应具备以下结构：
1.  从 `react-router-dom` 中使用 `useParams` 获取路由参数（例如 `id`）。
2.  定义 `loading`, `error`, `data` 三个状态。
3.  使用 `useEffect` 钩子，在组件加载时调用 API 服务函数（例如 `articleApi.getById(id)`）来获取数据。
4.  根据 `loading` 和 `error` 状态，分别显示加载指示器（Loading...）或错误信息。
5.  成功获取数据后，渲染页面内容。