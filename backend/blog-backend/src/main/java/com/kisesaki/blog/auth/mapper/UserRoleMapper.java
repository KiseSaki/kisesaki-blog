package com.kisesaki.blog.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.auth.entity.UserRole;

/**
 * 用户角色关联数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 检查用户是否拥有指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否拥有该角色
     */
    @Select("SELECT COUNT(*) > 0 FROM user_roles WHERE user_id = #{userId} AND role_id = #{roleId}")
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 根据用户ID查找所有角色关联
     * 
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    @Select("SELECT * FROM user_roles WHERE user_id = #{userId}")
    List<UserRole> findByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查找所有用户关联
     * 
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    @Select("SELECT * FROM user_roles WHERE role_id = #{roleId}")
    List<UserRole> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 为用户分配角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 影响的行数
     */
    @Insert("INSERT INTO user_roles (user_id, role_id, assigned_at) VALUES (#{userId}, #{roleId}, NOW())")
    int assignRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 移除用户的指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId} AND role_id = #{roleId}")
    int removeRoleFromUser(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 移除用户的所有角色
     * 
     * @param userId 用户ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    int removeAllRolesFromUser(@Param("userId") Long userId);

    /**
     * 移除指定角色的所有用户关联
     * 
     * @param roleId 角色ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM user_roles WHERE role_id = #{roleId}")
    int removeAllUsersFromRole(@Param("roleId") Long roleId);

    /**
     * 批量为用户分配角色
     * 
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 影响的行数
     */
    int batchAssignRolesToUser(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    /**
     * 获取拥有指定角色的用户数量
     * 
     * @param roleId 角色ID
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM user_roles WHERE role_id = #{roleId}")
    int countUsersByRoleId(@Param("roleId") Long roleId);
}
