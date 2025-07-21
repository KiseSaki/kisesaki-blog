---
mode: agent
description: "将选中的React组件中的业务逻辑或状态逻辑提取到一个可复用的自定义Hook中"
---
请将以下选中的 React 组件代码中的业务逻辑（如数据获取、状态管理等）重构并提取到一个新的自定义 Hook 中。

要求：
1. 新的 Hook 应该以 `use` 开头（例如 `useArticleData`）。
2. 返回重构后的组件代码和新的 Hook 代码。
3. Hook 应该返回它所管理的状态和操作函数。

```typescript
{{selection}}
```
