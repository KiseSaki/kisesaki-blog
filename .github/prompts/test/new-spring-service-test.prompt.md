---
mode: agent
description: "为Spring Service创建JUnit 5单元测试，并Mock依赖"
---

请为 `{{name}}` 创建一个 JUnit 5 单元测试类 `{{name}}Test.java`。

测试类应：
1.  使用 `@ExtendWith(MockitoExtension.class)`。
2.  使用 `@Mock` 注解 Mock 掉所有依赖的 Repository 和 Mapper。
3.  使用 `@InjectMocks` 将 Mock 注入到 `{{name}}` 实例中。
4.  为每个公开方法编写一个 `@Test` 方法骨架，遵循 `given-when-then` 模式，并留下 `// TODO` 注释。