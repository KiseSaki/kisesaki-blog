package com.kisesaki.blog.content.interaction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.interaction.entity.Favorites;

/**
 * 收藏数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorites> {

    /**
     * 批量查找用户对多篇文章的收藏记录
     *
     * @param userId  用户ID
     * @param postIds 文章ID列表
     * @return 收藏记录列表
     */
    List<Favorites> findUserFavoritesByPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);

    /**
     * 批量删除用户的收藏记录
     *
     * @param userId  用户ID
     * @param postIds 文章ID列表
     * @return 删除的记录数
     */
    int deleteFavoritesByPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);
}