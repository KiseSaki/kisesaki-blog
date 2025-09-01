package com.kisesaki.blog.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyEmailRequestDto {
    @NotBlank(message = "邮箱验证令牌不能为空")
    private String emailToken;
}
