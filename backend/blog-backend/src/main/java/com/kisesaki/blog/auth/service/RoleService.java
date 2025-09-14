package com.kisesaki.blog.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.auth.dto.role.RoleListParams;
import com.kisesaki.blog.auth.dto.role.RoleListResponse;
import com.kisesaki.blog.auth.entity.Role;
import com.kisesaki.blog.auth.mapper.PermissionMapper;
import com.kisesaki.blog.auth.mapper.RoleMapper;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    public PageResponse<RoleListResponse> getRoleList(RoleListParams params) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (params.getName() != null) {
            queryWrapper.like(Role::getName, params.getName());
        }
        // 先获取总数
        long totalCount = roleMapper.selectCount(queryWrapper);

        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        // 构建分页对象
        Page<Role> page = new Page<>(params.getPageable().getCurrentPage(), params.getPageable().getPageSize());
        // 执行分页查询
        Page<Role> result = roleMapper.selectPage(page, queryWrapper);

        // getRecords 获取当前页数据，List类型
        // stream 转为stream，方便拷贝，且防止影响原实体
        // toList 转回List
        List<RoleListResponse> roleList = result.getRecords().stream().map(role -> {
            RoleListResponse r = new RoleListResponse();
            r.setId(role.getId());
            r.setName(role.getName());
            r.setDescription(role.getDescription());
            r.setCreatedAt(role.getCreatedAt());
            return r;
        }).toList();

        // 构造 DTO 分页对象并返回
        Page<RoleListResponse> dtoPage = new Page<>(result.getCurrent(), result.getSize());
        dtoPage.setTotal(totalCount);
        dtoPage.setRecords(roleList);

        return PageResponse.of(dtoPage);
    }
}
