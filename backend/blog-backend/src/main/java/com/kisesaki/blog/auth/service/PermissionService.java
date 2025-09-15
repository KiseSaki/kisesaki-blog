package com.kisesaki.blog.auth.service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.auth.dto.permission.PermissionCreateRequest;
import com.kisesaki.blog.auth.dto.permission.PermissionDetailResponse;
import com.kisesaki.blog.auth.dto.permission.PermissionListParams;
import com.kisesaki.blog.auth.dto.permission.PermissionListResponse;
import com.kisesaki.blog.auth.dto.permission.PermissionOptionResponse;
import com.kisesaki.blog.auth.dto.permission.PermissionUpdateRequest;
import com.kisesaki.blog.auth.entity.Permission;
import com.kisesaki.blog.auth.enums.PermissionAction;
import com.kisesaki.blog.auth.enums.PermissionResource;
import com.kisesaki.blog.auth.mapper.PermissionMapper;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final PermissionMapper permissionMapper;

    /**
     * 获取权限列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    public PageResponse<PermissionListResponse> getPermissionsList(PermissionListParams params) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<Permission>();
        if (params.getName() != null) {
            queryWrapper.like(Permission::getName, params.getName());
        }
        if (params.getResource() != null) {
            queryWrapper.eq(Permission::getResource, params.getResource());
        }
        if (params.getAction() != null) {
            queryWrapper.eq(Permission::getAction, params.getAction());
        }

        // 先获取数量
        long totalCount = permissionMapper.selectCount(queryWrapper);

        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        // 构建分页对象
        Page<Permission> page = new Page<>(params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize());
        Page<Permission> result = permissionMapper.selectPage(page, queryWrapper);

        List<PermissionListResponse> responseList = result.getRecords().stream().map(permission -> {
            PermissionListResponse response = new PermissionListResponse();
            response.setId(permission.getId());
            response.setName(permission.getName());
            response.setDescription(permission.getDescription());
            response.setResource(permission.getResource());
            response.setAction(permission.getAction());
            if (permission.getCreatedAt() != null) {
                response.setCreatedAt(permission.getCreatedAt());
            }
            return response;
        }).toList();

        // 构造 DTO 分页对象并返回
        Page<PermissionListResponse> dtoPage = new Page<>(result.getCurrent(), result.getSize());
        dtoPage.setTotal(totalCount);
        dtoPage.setRecords(responseList);

        return PageResponse.of(dtoPage);
    }

    /**
     * 根据ID获取权限详情
     * 
     * @param id 权限ID
     * @return 权限详情，若不存在则返回null
     */
    public PermissionDetailResponse getPermissionById(Long id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            return null;
        }
        PermissionDetailResponse response = new PermissionDetailResponse();
        response.setId(permission.getId());
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        response.setResource(permission.getResource());
        response.setAction(permission.getAction());
        if (permission.getCreatedAt() != null) {
            response.setCreatedAt(permission.getCreatedAt());
        }
        return response;
    }

    /**
     * 创建权限
     * 
     * @param request 创建请求参数
     */
    public void createPermission(PermissionCreateRequest request) {
        Permission existing = permissionMapper.selectOne(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getName, request.getName())
                .eq(Permission::getResource, request.getResource())
                .eq(Permission::getAction, request.getAction()));

        if (existing != null) {
            throw BusinessException.paramError("权限已存在");
        }

        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission.setResource(request.getResource());
        permission.setAction(request.getAction());
        permission.setCreatedAt(OffsetDateTime.now());
        permissionMapper.insert(permission);
    }

    /**
     * 更新权限
     * 
     * @param id      权限ID
     * @param request 更新请求参数
     */
    public void updatePermission(Long id, PermissionUpdateRequest request) {
        Permission existing = permissionMapper.selectById(id);
        if (existing == null) {
            throw BusinessException.notFound("权限不存在");
        }

        // 检查权限名称、资源、操作的组合是否已被其他权限使用
        Permission duplicate = permissionMapper.selectOne(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getName, request.getName())
                .eq(Permission::getResource, request.getResource())
                .eq(Permission::getAction, request.getAction())
                .ne(Permission::getId, id));

        if (duplicate != null) {
            throw BusinessException.paramError("权限组合已存在");
        }

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setResource(request.getResource());
        existing.setAction(request.getAction());

        permissionMapper.updateById(existing);
        log.info("权限已更新: id={}, name={}", id, request.getName());
    }

    /**
     * 删除权限
     * 
     * @param id 权限ID
     */
    public void deletePermission(Long id) {
        Permission existing = permissionMapper.selectById(id);
        if (existing == null) {
            throw BusinessException.notFound("权限不存在");
        }

        // TODO: 检查权限是否被角色引用，如果被引用则不允许删除
        // 这部分需要在实现角色权限关联表后进行

        permissionMapper.deleteById(id);
        log.info("权限已删除: id={}, name={}", id, existing.getName());
    }

    /**
     * 获取权限资源类型列表
     * 
     * @return 资源类型列表
     */
    public List<PermissionOptionResponse> getPermissionResources() {
        return Arrays.stream(PermissionResource.values())
                .map(resource -> new PermissionOptionResponse(resource.getCode(), resource.getDescription()))
                .toList();
    }

    /**
     * 获取权限操作类型列表
     * 
     * @return 操作类型列表
     */
    public List<PermissionOptionResponse> getPermissionActions() {
        return Arrays.stream(PermissionAction.values())
                .map(action -> new PermissionOptionResponse(action.getCode(), action.getDescription()))
                .toList();
    }
}
