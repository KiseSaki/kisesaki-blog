package com.kisesaki.blog.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.auth.entity.Permission;

/**
 * 权限数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
