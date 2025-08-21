package com.kisesaki.blog.auth.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.auth.entity.Role;

/**
 * 角色数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色名称查找角色
     * 
     * @param name 角色名称
     * @return 角色信息
     */
    @Select("SELECT * FROM roles WHERE name = #{name}")
    Optional<Role> findByName(@Param("name") String name);

    /**
     * 检查角色名称是否存在
     * 
     * @param name 角色名称
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM roles WHERE name = #{name}")
    boolean existsByName(@Param("name") String name);

    /**
     * 根据用户ID查找用户的所有角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("SELECT r.* FROM roles r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<Role> findByUserId(@Param("userId") Long userId);

    /**
     * 获取所有可用角色
     * 
     * @return 角色列表
     */
    @Select("SELECT * FROM roles ORDER BY id ASC")
    List<Role> findAllRoles();
}
