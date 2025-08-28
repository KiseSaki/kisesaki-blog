package com.kisesaki.blog.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenRequestDto {
    @NotBlank(message = "Refresh Token 不能为空")
    private String refreshToken;
}
