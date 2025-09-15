package com.kisesaki.blog.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * 批量为用户分配角色
     * 复杂的批量插入操作，使用XML实现
     * 
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 影响的行数
     */
    int batchAssignRolesToUser(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
