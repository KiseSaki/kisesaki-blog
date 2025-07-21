---
mode: agent
description: "为选中的Service方法添加Redis缓存注解"
---

请分析我选中的 Java Service 方法 `{{selection}}`，并为其添加合适的 Spring Boot Cache 注解。

规则如下：
1.  对于查询方法（如 `getById`, `find...`），使用 `@Cacheable`。`cacheNames` 应根据方法所在类和功能命名，`key` 应使用方法参数（如 `#id`）。
2.  对于更新方法，使用 `@CachePut` 来更新缓存。
3.  对于删除方法，使用 `@CacheEvict` 来清除缓存。

请只返回添加了注解之后的方法代码。