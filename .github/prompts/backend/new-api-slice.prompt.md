---
mode: agent
description: "为新的业务功能创建 Controller, DTO, 和 Service 层骨架"
---
我需要为一个新的业务功能 `{{name}}` 创建后端代码骨架。

请为我生成以下内容：
1.  **DTO**: 在 `dto` 包下创建 `{{name}}Dto.java` 和 `Create{{name}}Request.java`。使用 Lombok 和 Jakarta Validation 注解。
2.  **Controller**: 在 `controller` 包下创建 `{{name}}Controller.java`。遵循统一 API 响应格式，并使用 `@PreAuthorize` 进行权限控制。
3.  **Service**: 在 `service` 包下创建 `{{name}}Service.java` 接口和 `{{name}}ServiceImpl.java` 实现类，并留下业务逻辑的 TODO 注释。
4.  **Mapper**: 在 DTO 和 Entity 之间转换，使用 MapStruct 创建一个 `{{name}}Mapper.java` 接口。

请将以上代码生成在一个代码块中，并用注释清晰地标明每个文件的路径和名称。