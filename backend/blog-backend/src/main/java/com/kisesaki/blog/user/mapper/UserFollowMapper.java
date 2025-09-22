package com.kisesaki.blog.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
         * 获取用户关注的人列表
         * 
         * @param followerId 关注者ID
         * @param status     关注状态
         * @return 关注关系列表
         */
        List<UserFollow> findFollowingsByFollower(@Param("followerId") Long followerId,
                        @Param("status") String status);

        /**
         * 获取关注某用户的人列表
         * 
         * @param followingId 被关注者ID
         * @param status      关注状态
         * @return 关注关系列表
         */
        List<UserFollow> findFollowersByFollowing(@Param("followingId") Long followingId,
                        @Param("status") String status);

        /**
         * 统计用户关注的人数
         * 
         * @param followerId 关注者ID
         * @param status     关注状态
         * @return 关注数量
         */
        long countFollowingsByFollower(@Param("followerId") Long followerId,
                        @Param("status") String status);

        /**
         * 统计关注某用户的人数
         * 
         * @param followingId 被关注者ID
         * @param status      关注状态
         * @return 关注者数量
         */
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
        boolean isFollowing(@Param("followerId") Long followerId,
                        @Param("followingId") Long followingId,
                        @Param("status") String status);
}
