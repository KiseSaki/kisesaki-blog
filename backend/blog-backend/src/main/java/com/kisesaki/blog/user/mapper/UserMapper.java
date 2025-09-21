package com.kisesaki.blog.user.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.user.entity.User;

/**
 * 用户数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据 OAuth 提供商和 ID 查找用户
     * 
     * @param oauthProvider OAuth 提供商
     * @param oauthId       OAuth ID
     * @return 用户信息
     */
    Optional<User> findByOAuth(@Param("oauthProvider") String oauthProvider, @Param("oauthId") String oauthId);
}
