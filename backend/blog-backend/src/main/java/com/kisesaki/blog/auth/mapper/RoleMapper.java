package com.kisesaki.blog.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.auth.entity.Permission;
import com.kisesaki.blog.auth.entity.Role;

/**
 * 角色数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户ID查询用户的所有角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> findRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户的所有权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> findPermissionsByUserId(@Param("userId") Long userId);
}
