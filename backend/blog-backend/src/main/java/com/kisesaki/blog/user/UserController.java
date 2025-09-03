package com.kisesaki.blog.user;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * 
 * @author KiseSaki
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @PostMapping("getUserInfoByToken")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfoByToken(Authentication authentication) {
        String username = authentication.getName();
        UserInfoDto userInfo = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @PostMapping("/getUserInfoById")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfoById(Long id) {
        UserInfoDto userInfo = userService.getUserInfoById(id);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @PostMapping("/getUserInfoByUsername")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfoByUsername(String username) {
        UserInfoDto userInfo = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

}
