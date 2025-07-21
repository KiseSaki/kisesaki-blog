---
mode: agent
description: "创建一个符合项目规范的、可复用的React组件"
---
请为我创建一个名为 `{{name}}` 的新 React 组件。

这个组件应该：
1.  使用 TypeScript (`.tsx`)。
2.  接收 `props`，并为其定义清晰的 TypeScript 接口。
3.  使用 `React.memo` 包裹，以进行性能优化。
4.  包含 TSDoc 风格的注释来解释组件用途和 props。
5.  使用 TailWindCSS 进行基础样式定义。
6.  这是一个纯展示组件，不包含任何业务逻辑。

例如，如果 `name` 是 `ArticleCard`，则 props 可能包含 `title`、`author` 和 `summary`。