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
}
