package com.kisesaki.blog.auth.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kisesaki.blog.auth.entity.UserRole;
import com.kisesaki.blog.auth.mapper.UserRoleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户角色关联服务层
 * 使用MyBatis-Plus LambdaWrapper实现简单CRUD操作
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    private final UserRoleMapper userRoleMapper;

    /**
     * 检查用户是否拥有指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否拥有该角色
     */
    public boolean existsByUserIdAndRoleId(Long userId, Long roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId);
        return userRoleMapper.exists(wrapper);
    }

    /**
     * 根据用户ID查找所有角色关联
     * 
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    public List<UserRole> findByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        return userRoleMapper.selectList(wrapper);
    }

    /**
     * 根据角色ID查找所有用户关联
     * 
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    public List<UserRole> findByRoleId(Long roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        return userRoleMapper.selectList(wrapper);
    }

    /**
     * 为用户分配角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否成功
     */
    public boolean assignRoleToUser(Long userId, Long roleId) {
        // 检查是否已存在
        if (existsByUserIdAndRoleId(userId, roleId)) {
            log.warn("用户{}已拥有角色{}", userId, roleId);
            return false;
        }

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setAssignedAt(OffsetDateTime.now());

        return userRoleMapper.insert(userRole) > 0;
    }

    /**
     * 移除用户的指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 影响的行数
     */
    public int removeRoleFromUser(Long userId, Long roleId) {
        LambdaUpdateWrapper<UserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId);
        return userRoleMapper.delete(wrapper);
    }

    /**
     * 移除用户的所有角色
     * 
     * @param userId 用户ID
     * @return 影响的行数
     */
    public int removeAllRolesFromUser(Long userId) {
        LambdaUpdateWrapper<UserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        return userRoleMapper.delete(wrapper);
    }

    /**
     * 移除指定角色的所有用户关联
     * 
     * @param roleId 角色ID
     * @return 影响的行数
     */
    public int removeAllUsersFromRole(Long roleId) {
        LambdaUpdateWrapper<UserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        return userRoleMapper.delete(wrapper);
    }

    /**
     * 批量为用户分配角色
     * 使用XML实现的复杂操作
     * 
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 影响的行数
     */
    public int batchAssignRolesToUser(Long userId, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return 0;
        }
        return userRoleMapper.batchAssignRolesToUser(userId, roleIds);
    }

    /**
     * 获取拥有指定角色的用户数量
     * 
     * @param roleId 角色ID
     * @return 用户数量
     */
    public long countUsersByRoleId(Long roleId) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        return userRoleMapper.selectCount(wrapper);
    }
}