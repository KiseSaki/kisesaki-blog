---
mode: agent
description: "创建一个配置完整的JPA数据库实体类"
---

请为 `{{name}}` 创建一个 JPA 实体类。

该类应包含：
1.  `@Entity` 和 `@Table` 注解。
2.  `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` Lombok 注解。
3.  一个自增的 `id` 主键 (`@Id`, `@GeneratedValue`)。
4.  `createdAt` 和 `updatedAt` 字段，并使用 `@CreationTimestamp` 和 `@UpdateTimestamp` 自动管理。
5.  根据 `{{name}}` 的特性，添加几个示例业务字段（例如 String, Integer, Boolean 类型）。