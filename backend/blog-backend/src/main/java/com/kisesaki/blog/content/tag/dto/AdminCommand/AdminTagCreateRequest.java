package com.kisesaki.blog.content.tag.dto.AdminCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员创建标签请求")
public class AdminTagCreateRequest {
    @NotBlank(message = "标签名称不能为空")
    @Size(min = 1, max = 50, message = "标签名称长度必须在1-50字符之间")
    @Schema(description = "标签名称", example = "Spring Boot")
    private String name;

    @Size(max = 50, message = "URL别名长度不能超过50字符")
    @Pattern(regexp = "^[a-z0-9\\-]*$", message = "URL别名只能包含小写字母、数字和短横线")
    @Schema(description = "URL友好别名，不传递就后端自动生成", example = "spring-boot")
    private String slug;

    @Size(max = 500, message = "标签描述长度不能超过500字符")
    @Schema(description = "标签描述", example = "关于Spring Boot框架的文章")
    private String description;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "颜色必须是有效的HEX格式")
    @Schema(description = "标签颜色（HEX格式），不传递就后端随机生成", example = "#3498db")
    private String color;

    @Schema(description = "是否立即审核通过", example = "true")
    private Boolean isApproved = true;

    @Size(max = 200, message = "审核备注长度不能超过200字符")
    @Schema(description = "审核备注", example = "管理员直接创建，自动审核通过")
    private String approvalNote;
}