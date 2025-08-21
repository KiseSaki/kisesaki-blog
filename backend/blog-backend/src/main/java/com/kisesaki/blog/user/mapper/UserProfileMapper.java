package com.kisesaki.blog.user.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.user.entity.UserProfile;

/**
 * 用户扩展信息数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    /**
     * 根据用户ID查找用户扩展信息
     * 
     * @param userId 用户ID
     * @return 用户扩展信息
     */
    @Select("SELECT * FROM user_profiles WHERE user_id = #{userId}")
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);

    /**
     * 检查用户是否已有扩展信息
     * 
     * @param userId 用户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM user_profiles WHERE user_id = #{userId}")
    boolean existsByUserId(@Param("userId") Long userId);
}
