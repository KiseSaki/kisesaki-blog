package com.kisesaki.blog.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.user.entity.UserSettings;

/**
 * 用户设置数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserSettingsMapper extends BaseMapper<UserSettings> {
    // 所有查询都通过Lambda Wrapper在Service层实现
}
