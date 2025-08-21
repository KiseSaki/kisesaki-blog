package com.kisesaki.blog.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.auth.entity.RolePermission;

/**
 * 角色权限关联数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 检查角色是否拥有指定权限
     * 
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 是否拥有该权限
     */
    @Select("SELECT COUNT(*) > 0 FROM role_permissions WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    boolean existsByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 根据角色ID查找所有权限关联
     * 
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    @Select("SELECT * FROM role_permissions WHERE role_id = #{roleId}")
    List<RolePermission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID查找所有角色关联
     * 
     * @param permissionId 权限ID
     * @return 角色权限关联列表
     */
    @Select("SELECT * FROM role_permissions WHERE permission_id = #{permissionId}")
    List<RolePermission> findByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 为角色分配权限
     * 
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 影响的行数
     */
    @Insert("INSERT INTO role_permissions (role_id, permission_id, granted_at) VALUES (#{roleId}, #{permissionId}, NOW())")
    int grantPermissionToRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 移除角色的指定权限
     * 
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM role_permissions WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    int revokePermissionFromRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 移除角色的所有权限
     * 
     * @param roleId 角色ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM role_permissions WHERE role_id = #{roleId}")
    int removeAllPermissionsFromRole(@Param("roleId") Long roleId);

    /**
     * 移除指定权限的所有角色关联
     * 
     * @param permissionId 权限ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM role_permissions WHERE permission_id = #{permissionId}")
    int removeAllRolesFromPermission(@Param("permissionId") Long permissionId);

    /**
     * 批量为角色分配权限
     * 
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     * @return 影响的行数
     */
    int batchGrantPermissionsToRole(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 获取拥有指定权限的角色数量
     * 
     * @param permissionId 权限ID
     * @return 角色数量
     */
    @Select("SELECT COUNT(*) FROM role_permissions WHERE permission_id = #{permissionId}")
    int countRolesByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 根据角色名称和权限名称检查权限
     * 
     * @param roleName       角色名称
     * @param permissionName 权限名称
     * @return 是否拥有该权限
     */
    @Select("SELECT COUNT(*) > 0 FROM role_permissions rp " +
            "INNER JOIN roles r ON rp.role_id = r.id " +
            "INNER JOIN permissions p ON rp.permission_id = p.id " +
            "WHERE r.name = #{roleName} AND p.name = #{permissionName}")
    boolean hasPermission(@Param("roleName") String roleName, @Param("permissionName") String permissionName);
}
