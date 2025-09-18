package com.kisesaki.blog.auth.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.auth.dto.role.RoleCreateRequest;
import com.kisesaki.blog.auth.dto.role.RoleDetailResponse;
import com.kisesaki.blog.auth.dto.role.RoleListParams;
import com.kisesaki.blog.auth.dto.role.RoleListResponse;
import com.kisesaki.blog.auth.dto.role.RoleUpdateRequest;
import com.kisesaki.blog.auth.dto.role.RoleUserListParams;
import com.kisesaki.blog.auth.dto.role.RoleUserListResponse;
import com.kisesaki.blog.auth.entity.Permission;
import com.kisesaki.blog.auth.entity.Role;
import com.kisesaki.blog.auth.entity.RolePermission;
import com.kisesaki.blog.auth.entity.UserRole;
import com.kisesaki.blog.auth.mapper.PermissionMapper;
import com.kisesaki.blog.auth.mapper.RoleMapper;
import com.kisesaki.blog.auth.mapper.RolePermissionMapper;
import com.kisesaki.blog.auth.mapper.UserRoleMapper;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.PageQueryUtils;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final UserRoleMapper userRoleMapper;

    /**
     * 获取角色列表，支持分页和按名称模糊搜索
     * 
     * @param params 查询参数
     * @return 角色列表分页数据
     */
    public PageResponse<RoleListResponse> getRoleList(RoleListParams params) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        // 查询条件
        if (params.getName() != null) {
            queryWrapper.like(Role::getName, params.getName());
        }

        // 应用时间范围条件
        PageQueryUtils.applyTimeRangeConditions(queryWrapper, params.getPageable(), Role::getCreatedAt);

        // 应用排序规则
        PageQueryUtils.createSortBuilder(queryWrapper, params.getPageable())
                .defaultSort(Role::getCreatedAt, true) // 默认按创建时间倒序
                .addSortField("id", Role::getId)
                .addSortField("name", Role::getName)
                .addSortField("createdAt", Role::getCreatedAt)
                .apply();

        // 执行分页查询
        return PageQueryUtils.executePageQuery(
                roleMapper,
                queryWrapper,
                params.getPageable(),
                this::convertToRoleListResponse);
    }

    /**
     * 获取角色详情（包括权限列表）
     *
     * @param roleId 角色 ID
     * @return 角色详情
     */
    public RoleDetailResponse getRoleDetail(Long roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw BusinessException.notFound("角色不存在");
        }

        // 查询角色对应的权限列表
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, role.getId());
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(queryWrapper);
        // 提取权限 ID 列表
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .toList();
        // 查询权限详情
        List<RoleDetailResponse.PermissionDto> permissions = permissionMapper.selectBatchIds(permissionIds).stream()
                .map(permission -> {
                    RoleDetailResponse.PermissionDto dto = new RoleDetailResponse.PermissionDto();
                    dto.setId(permission.getId());
                    dto.setName(permission.getName());
                    dto.setDescription(permission.getDescription());
                    return dto;
                }).toList();

        // 构造响应对象
        RoleDetailResponse response = new RoleDetailResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        if (role.getCreatedAt() != null) {
            response.setCreatedAt(role.getCreatedAt().toString());
        }
        response.setPermissions(permissions);

        return response;
    }

    /**
     * 创建新角色
     *
     * @param request 创建角色请求
     * @return 创建的角色详情
     */
    @Transactional
    public RoleDetailResponse createRole(RoleCreateRequest request) {
        // 检查角色名是否已存在
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>()
                .eq(Role::getName, request.getName());
        if (roleMapper.selectCount(queryWrapper) > 0) {
            throw BusinessException.paramError("角色名称已存在");
        }

        // 验证权限ID是否有效
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            List<Permission> permissions = permissionMapper.selectBatchIds(request.getPermissionIds());
            if (permissions.size() != request.getPermissionIds().size()) {
                throw BusinessException.paramError("存在无效的权限ID");
            }
        }

        // 创建角色
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        roleMapper.insert(role);

        // 分配权限
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            assignPermissionsToRole(role.getId(), request.getPermissionIds());
        }

        // 返回角色详情
        return getRoleDetail(role.getId());
    }

    /**
     * 更新角色信息
     *
     * @param roleId  角色ID
     * @param request 更新请求
     * @return 更新后的角色详情
     */
    @Transactional
    public RoleDetailResponse updateRole(Long roleId, RoleUpdateRequest request) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw BusinessException.notFound("角色不存在");
        }

        // 检查角色名是否已被其他角色使用
        if (request.getName() != null && !request.getName().equals(role.getName())) {
            LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>()
                    .eq(Role::getName, request.getName())
                    .ne(Role::getId, roleId);
            if (roleMapper.selectCount(queryWrapper) > 0) {
                throw BusinessException.paramError("角色名称已存在");
            }
        }

        // 更新角色基本信息
        boolean updated = false;
        if (request.getName() != null) {
            role.setName(request.getName());
            updated = true;
        }
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
            updated = true;
        }

        if (updated) {
            roleMapper.updateById(role);
        }

        // 更新权限
        if (request.getPermissionIds() != null) {
            // 验证权限ID是否有效
            if (!request.getPermissionIds().isEmpty()) {
                List<Permission> permissions = permissionMapper.selectBatchIds(request.getPermissionIds());
                if (permissions.size() != request.getPermissionIds().size()) {
                    throw BusinessException.paramError("存在无效的权限ID");
                }
            }
            updateRolePermissions(roleId, request.getPermissionIds());
        }

        return getRoleDetail(roleId);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw BusinessException.notFound("角色不存在");
        }

        // 检查是否有用户使用该角色
        LambdaQueryWrapper<UserRole> userRoleQuery = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, roleId);
        long userCount = userRoleMapper.selectCount(userRoleQuery);
        if (userCount > 0) {
            throw BusinessException.paramError("无法删除角色，存在用户正在使用该角色");
        }

        // 删除角色权限关联
        LambdaQueryWrapper<RolePermission> rolePermissionQuery = new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(rolePermissionQuery);

        // 删除角色
        roleMapper.deleteById(roleId);
    }

    /**
     * 获取角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<RoleDetailResponse.PermissionDto> getRolePermissions(Long roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw BusinessException.notFound("角色不存在");
        }

        // 查询角色对应的权限列表
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(queryWrapper);

        if (rolePermissions.isEmpty()) {
            return List.of();
        }

        // 提取权限 ID 列表
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .toList();

        // 查询权限详情
        return permissionMapper.selectBatchIds(permissionIds).stream()
                .map(permission -> {
                    RoleDetailResponse.PermissionDto dto = new RoleDetailResponse.PermissionDto();
                    dto.setId(permission.getId());
                    dto.setName(permission.getName());
                    dto.setDescription(permission.getDescription());
                    return dto;
                }).toList();
    }

    /**
     * 更新角色权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    @Transactional
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw BusinessException.notFound("角色不存在");
        }

        // 验证权限ID是否有效
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
            if (permissions.size() != permissionIds.size()) {
                throw BusinessException.paramError("存在无效的权限ID");
            }
        }

        // 删除现有权限关联
        LambdaQueryWrapper<RolePermission> deleteQuery = new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(deleteQuery);

        // 添加新权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            assignPermissionsToRole(roleId, permissionIds);
        }
    }

    /**
     * 获取拥有该角色的用户列表
     *
     * @param roleId 角色ID
     * @param params 查询参数
     * @return 用户列表分页数据
     */
    public PageResponse<RoleUserListResponse> getRoleUsers(Long roleId, RoleUserListParams params) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw BusinessException.notFound("角色不存在");
        }

        // 先查询角色关联的用户ID列表
        LambdaQueryWrapper<UserRole> userRoleQuery = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, roleId);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQuery);

        if (userRoles.isEmpty()) {
            return PageResponse.of(List.of(), 0L, params);
        }

        List<Long> userIds = userRoles.stream()
                .map(UserRole::getUserId)
                .toList();

        // 构建用户查询条件
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<User>()
                .in(User::getId, userIds);

        if (params.getUsername() != null && !params.getUsername().trim().isEmpty()) {
            userQuery.like(User::getUsername, params.getUsername().trim());
        }
        if (params.getEmail() != null && !params.getEmail().trim().isEmpty()) {
            userQuery.like(User::getEmail, params.getEmail().trim());
        }

        // 先获取总数
        long totalCount = userMapper.selectCount(userQuery);

        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params);
        }

        // 构建分页对象
        Page<User> page = new Page<>(params.getCurrentPage(), params.getPageSize());

        // 添加排序
        if (params.getSortField() != null) {
            if (params.isDescending()) {
                userQuery.orderByDesc(User::getCreatedAt); // 默认按创建时间排序
            } else {
                userQuery.orderByAsc(User::getCreatedAt);
            }
        } else {
            userQuery.orderByDesc(User::getCreatedAt);
        }

        // 执行分页查询
        Page<User> result = userMapper.selectPage(page, userQuery);

        // 创建用户角色关联映射，方便查找分配时间
        Map<Long, OffsetDateTime> userRoleMap = userRoles.stream()
                .collect(Collectors.toMap(UserRole::getUserId, UserRole::getAssignedAt));

        // 转换为DTO
        List<RoleUserListResponse> userList = result.getRecords().stream().map(user -> {
            RoleUserListResponse response = new RoleUserListResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setNickname(user.getUsername()); // 使用username作为nickname
            response.setAssignedAt(userRoleMap.get(user.getId()));
            return response;
        }).toList();

        // 构造分页响应
        Page<RoleUserListResponse> dtoPage = new Page<>(result.getCurrent(), result.getSize());
        dtoPage.setTotal(totalCount);
        dtoPage.setRecords(userList);

        return PageResponse.of(dtoPage);
    }

    /**
     * 为角色分配权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    private void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        List<RolePermission> rolePermissions = permissionIds.stream()
                .map(permissionId -> {
                    RolePermission rp = new RolePermission();
                    rp.setRoleId(roleId);
                    rp.setPermissionId(permissionId);
                    return rp;
                }).toList();

        // 批量插入
        rolePermissions.forEach(rolePermissionMapper::insert);
    }

    /**
     * 转换实体为角色列表响应对象
     */
    private RoleListResponse convertToRoleListResponse(Role role) {
        RoleListResponse response = new RoleListResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setCreatedAt(role.getCreatedAt());
        return response;
    }
}
