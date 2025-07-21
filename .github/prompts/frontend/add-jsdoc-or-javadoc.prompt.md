---
mode: agent
description: "为选中的函数或方法添加符合规范的 JSDoc / JavaDoc 注释"
---
请为以下选中的代码添加标准格式的文档注释。

要求：
- 如果是 TypeScript/JavaScript，使用 JSDoc 格式。
- 如果是 Java，使用 JavaDoc 格式。
- 描述函数/方法的功能。
- 解释每个参数 (`@param`) 和返回值 (`@return`)。
- 如果可能抛出异常，请添加 `@throws` 标签。

```
{{selection}}
```
