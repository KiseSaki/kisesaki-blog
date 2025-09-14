package com.kisesaki.blog.auth.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgotPasswordRequestDto {

    @NotBlank(message = "电子邮箱不能为空")
    @Email(message = "电子邮箱格式不正确")
    private String email;
}
