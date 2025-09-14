package com.kisesaki.blog.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色的创建、删除、分配等操作")
public class RoleController {
}
