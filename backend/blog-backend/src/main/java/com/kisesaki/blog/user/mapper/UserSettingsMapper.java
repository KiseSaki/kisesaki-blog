package com.kisesaki.blog.user.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.user.entity.UserSettings;

/**
 * 用户设置数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserSettingsMapper extends BaseMapper<UserSettings> {

    /**
     * 根据用户ID和设置键查找设置
     * 
     * @param userId     用户ID
     * @param settingKey 设置键
     * @return 用户设置
     */
    @Select("SELECT * FROM user_settings WHERE user_id = #{userId} AND setting_key = #{settingKey}")
    Optional<UserSettings> findByUserIdAndKey(@Param("userId") Long userId, @Param("settingKey") String settingKey);

    /**
     * 根据用户ID查找所有设置
     * 
     * @param userId 用户ID
     * @return 用户设置列表
     */
    @Select("SELECT * FROM user_settings WHERE user_id = #{userId}")
    List<UserSettings> findAllByUserId(@Param("userId") Long userId);

    /**
     * 删除用户的指定设置
     * 
     * @param userId     用户ID
     * @param settingKey 设置键
     * @return 删除的记录数
     */
    @Delete("DELETE FROM user_settings WHERE user_id = #{userId} AND setting_key = #{settingKey}")
    int deleteByUserIdAndKey(@Param("userId") Long userId, @Param("settingKey") String settingKey);

    /**
     * 删除用户的所有设置
     * 
     * @param userId 用户ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM user_settings WHERE user_id = #{userId}")
    int deleteAllByUserId(@Param("userId") Long userId);

    /**
     * 检查用户设置是否存在
     * 
     * @param userId     用户ID
     * @param settingKey 设置键
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM user_settings WHERE user_id = #{userId} AND setting_key = #{settingKey}")
    boolean existsByUserIdAndKey(@Param("userId") Long userId, @Param("settingKey") String settingKey);
}
