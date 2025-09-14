package com.kisesaki.blog.auth.service;

import com.kisesaki.blog.auth.mapper.PermissionMapper;
import com.kisesaki.blog.auth.mapper.RoleMapper;
import com.kisesaki.blog.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
}
