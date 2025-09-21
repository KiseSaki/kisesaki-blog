package com.kisesaki.blog.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserListParams;
import com.kisesaki.blog.user.dto.admin.AdminUserListResponse;
import com.kisesaki.blog.user.mapper.UserMapper;
import com.kisesaki.blog.user.mapper.UserProfileMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    /**
     * 获取用户列表
     *
     * @param params 查询参数
     * @return 用户列表
     */
    public PageResponse<AdminUserListResponse> getUserList(AdminUserListParams params) {
        // 先获取总数
        Long totalCount = userMapper.adminGetUserListCount(params);
        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        Page<AdminUserListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(), params.getPageable().getPageSize(),
                false
        );
        Page<AdminUserListResponse> result = userMapper.adminGetUserList(page, params);
        result.setTotal(totalCount);

        return PageResponse.of(result);
    }
}
