package com.kisesaki.blog.content.interaction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.interaction.entity.Likes;

/**
 * 点赞反应 Mapper 接口
 */
@Mapper
public interface LikeMapper extends BaseMapper<Likes> {

    /**
     * 统计指定目标的反应数量
     *
     * @param targetType   目标类型
     * @param targetId     目标ID
     * @param reactionType 反应类型
     * @return 反应数量
     */
    @Select("SELECT COUNT(*) FROM likes WHERE target_type = #{targetType}::target_type AND target_id = #{targetId} AND reaction_type = #{reactionType}::reaction_type")
    int countReactionsByTarget(@Param("targetType") Likes.TargetType targetType,
            @Param("targetId") Long targetId,
            @Param("reactionType") Likes.ReactionType reactionType);

    /**
     * 查询用户对指定目标的反应
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param targetId   目标ID
     * @return 反应记录，如果不存在则返回null
     */
    @Select("SELECT * FROM likes WHERE user_id = #{userId} AND target_type = #{targetType}::target_type AND target_id = #{targetId}")
    Likes findUserReaction(@Param("userId") Long userId,
            @Param("targetType") Likes.TargetType targetType,
            @Param("targetId") Long targetId);

    /**
     * 查询用户对多个目标的反应状态
     *
     * @param userId     用户ID
     * @param targetType 目标类型
     * @param targetIds  目标ID列表
     * @return 反应记录列表
     */
    @Select("<script>" +
            "SELECT * FROM likes WHERE user_id = #{userId} AND target_type = #{targetType}::target_type" +
            " AND target_id IN " +
            "<foreach collection='targetIds' item='targetId' open='(' separator=',' close=')'>" +
            "#{targetId}" +
            "</foreach>" +
            "</script>")
    List<Likes> findUserReactionsByTargetIds(@Param("userId") Long userId,
            @Param("targetType") Likes.TargetType targetType,
            @Param("targetIds") List<Long> targetIds);

    /**
     * 统计用户的点赞总数
     *
     * @param userId 用户ID
     * @return 点赞总数
     */
    @Select("SELECT COUNT(*) FROM likes WHERE user_id = #{userId} AND reaction_type = 'LIKE'::reaction_type")
    int countUserLikes(@Param("userId") Long userId);

    /**
     * 统计指定目标的所有反应数量 (包括点赞和踩)
     *
     * @param targetType 目标类型
     * @param targetId   目标ID
     * @return 总反应数量
     */
    @Select("SELECT COUNT(*) FROM likes WHERE target_type = #{targetType}::target_type AND target_id = #{targetId}")
    int countAllReactionsByTarget(@Param("targetType") Likes.TargetType targetType,
            @Param("targetId") Long targetId);
}