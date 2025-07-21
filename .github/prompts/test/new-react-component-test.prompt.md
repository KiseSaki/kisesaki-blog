---
mode: agent
description: "使用React Testing Library为组件创建测试"
---

请为 `{{name}}` 组件创建一个测试文件 `{{name}}.test.tsx`。

测试文件应：
1.  导入 `@testing-library/react` 的 `render` 和 `screen`。
2.  编写一个基础测试用例，`describe` 块描述组件。
3.  `it` 断言组件能够被成功渲染（"should render successfully"）。
4.  Mock 掉组件所需的 props，并传递给 `render` 函数。
5.  使用 `screen.getByText` 或 `screen.getByRole` 来查找元素，并断言其存在。