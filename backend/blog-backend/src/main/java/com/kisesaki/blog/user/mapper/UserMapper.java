package com.kisesaki.blog.user.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM \"user\" WHERE username = #{username} AND status != 'deleted'")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM \"user\" WHERE email = #{email} AND status != 'deleted'")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * 根据 OAuth 提供商和 ID 查找用户
     * 
     * @param oauthProvider OAuth 提供商
     * @param oauthId       OAuth ID
     * @return 用户信息
     */
    @Select("SELECT * FROM \"user\" WHERE oauth_provider = #{oauthProvider} AND oauth_id = #{oauthId} AND status != 'deleted'")
    Optional<User> findByOAuth(@Param("oauthProvider") String oauthProvider, @Param("oauthId") String oauthId);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM \"user\" WHERE username = #{username} AND status != 'deleted'")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM \"user\" WHERE email = #{email} AND status != 'deleted'")
    boolean existsByEmail(@Param("email") String email);
}
