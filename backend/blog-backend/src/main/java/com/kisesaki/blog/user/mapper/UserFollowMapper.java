package com.kisesaki.blog.user.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.user.entity.UserFollow;

/**
 * 用户关注关系数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {

    /**
     * 查找关注关系
     * 
     * @param followerId  关注者ID
     * @param followingId 被关注者ID
     * @return 关注关系
     */
    @Select("SELECT * FROM user_follows WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    Optional<UserFollow> findByFollowerAndFollowing(@Param("followerId") Long followerId,
            @Param("followingId") Long followingId);

    /**
     * 获取用户关注的人列表
     * 
     * @param followerId 关注者ID
     * @param status     关注状态
     * @return 关注关系列表
     */
    @Select("SELECT * FROM user_follows WHERE follower_id = #{followerId} AND status = #{status}")
    List<UserFollow> findFollowingsByFollower(@Param("followerId") Long followerId,
            @Param("status") String status);

    /**
     * 获取关注某用户的人列表
     * 
     * @param followingId 被关注者ID
     * @param status      关注状态
     * @return 关注关系列表
     */
    @Select("SELECT * FROM user_follows WHERE following_id = #{followingId} AND status = #{status}")
    List<UserFollow> findFollowersByFollowing(@Param("followingId") Long followingId,
            @Param("status") String status);

    /**
     * 统计用户关注的人数
     * 
     * @param followerId 关注者ID
     * @param status     关注状态
     * @return 关注数量
     */
    @Select("SELECT COUNT(*) FROM user_follows WHERE follower_id = #{followerId} AND status = #{status}")
    long countFollowingsByFollower(@Param("followerId") Long followerId,
            @Param("status") String status);

    /**
     * 统计关注某用户的人数
     * 
     * @param followingId 被关注者ID
     * @param status      关注状态
     * @return 关注者数量
     */
    @Select("SELECT COUNT(*) FROM user_follows WHERE following_id = #{followingId} AND status = #{status}")
    long countFollowersByFollowing(@Param("followingId") Long followingId,
            @Param("status") String status);

    /**
     * 检查是否已关注
     * 
     * @param followerId  关注者ID
     * @param followingId 被关注者ID
     * @param status      关注状态
     * @return 是否已关注
     */
    @Select("SELECT COUNT(*) > 0 FROM user_follows WHERE follower_id = #{followerId} AND following_id = #{followingId} AND status = #{status}")
    boolean isFollowing(@Param("followerId") Long followerId,
            @Param("followingId") Long followingId,
            @Param("status") String status);

    /**
     * 删除关注关系
     * 
     * @param followerId  关注者ID
     * @param followingId 被关注者ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM user_follows WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    int deleteByFollowerAndFollowing(@Param("followerId") Long followerId,
            @Param("followingId") Long followingId);
}
