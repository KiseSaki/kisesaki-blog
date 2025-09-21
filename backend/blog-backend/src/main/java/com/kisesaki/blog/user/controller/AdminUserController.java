package com.kisesaki.blog.user.controller;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.user.dto.admin.AdminUserListParams;
import com.kisesaki.blog.user.dto.admin.AdminUserListResponse;
import com.kisesaki.blog.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "管理员用户管理", description = "管理员用户管理相关接口")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping()
    public ApiResponse<PageResponse<AdminUserListResponse>> getUserList(@Valid AdminUserListParams params){
        return ResultUtils.success(adminUserService.getUserList(params));
    }
}
