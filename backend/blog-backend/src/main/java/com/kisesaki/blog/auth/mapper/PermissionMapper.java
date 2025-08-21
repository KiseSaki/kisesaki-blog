package com.kisesaki.blog.auth.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.auth.entity.Permission;

/**
 * 权限数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据权限名称查找权限
     * 
     * @param name 权限名称
     * @return 权限信息
     */
    @Select("SELECT * FROM permissions WHERE name = #{name}")
    Optional<Permission> findByName(@Param("name") String name);

    /**
     * 检查权限名称是否存在
     * 
     * @param name 权限名称
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM permissions WHERE name = #{name}")
    boolean existsByName(@Param("name") String name);

    /**
     * 根据角色ID查找角色的所有权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Select("SELECT p.* FROM permissions p " +
            "INNER JOIN role_permissions rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查找用户的所有权限（通过角色关联）
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("SELECT DISTINCT p.* FROM permissions p " +
            "INNER JOIN role_permissions rp ON p.id = rp.permission_id " +
            "INNER JOIN user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<Permission> findByUserId(@Param("userId") Long userId);

    /**
     * 根据资源类型查找权限
     * 
     * @param resource 资源类型
     * @return 权限列表
     */
    @Select("SELECT * FROM permissions WHERE resource = #{resource} ORDER BY action ASC")
    List<Permission> findByResource(@Param("resource") String resource);

    /**
     * 获取所有可用权限
     * 
     * @return 权限列表
     */
    @Select("SELECT * FROM permissions ORDER BY resource ASC, action ASC")
    List<Permission> findAllPermissions();

    /**
     * 根据资源和操作查找权限
     * 
     * @param resource 资源类型
     * @param action   操作类型
     * @return 权限信息
     */
    @Select("SELECT * FROM permissions WHERE resource = #{resource} AND action = #{action}")
    Optional<Permission> findByResourceAndAction(@Param("resource") String resource, @Param("action") String action);
}
