package com.kisesaki.blog.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.user.entity.UserProfile;

/**
 * 用户扩展信息数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
    // 所有查询都通过Lambda Wrapper在Service层实现
}
