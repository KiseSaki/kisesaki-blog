---
mode: agent
description: "创建一个新的Zustand全局状态存储"
---

请为 `{{name}}` 创建一个 Zustand 状态存储模块。

模块代码应包含：
1.  一个 `State` 接口，定义状态的结构。
2.  一个 `Actions` 接口，定义可以对状态进行的操作。
3.  使用 `create` 函数来创建 store，并将 `State` 和 `Actions` 结合起来。
4.  如果适用，请使用 `immer` 中间件来简化不可变更新。
5.  导出一个自定义钩子，例如 `use{{name | pascal}}Store`。

例如，对于 `auth` store，状态应包含 `user`, `token`, `isLoggedIn`，操作应包含 `login`, `logout`。