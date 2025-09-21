package com.kisesaki.blog.content.interaction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.interaction.entity.Favorites;

/**
 * 收藏数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorites> {

    /**
     * 查找用户对指定文章的收藏记录
     *
     * @param userId 用户ID
     * @param postId 文章ID
     * @return 收藏记录，如果不存在则返回null
     */
    Favorites findUserFavorite(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 批量查找用户对多篇文章的收藏记录
     *
     * @param userId  用户ID
     * @param postIds 文章ID列表
     * @return 收藏记录列表
     */
    List<Favorites> findUserFavoritesByPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);

    /**
     * 统计文章的收藏数量
     *
     * @param postId 文章ID
     * @return 收藏数量
     */
    int countFavoritesByPost(@Param("postId") Long postId);

    /**
     * 统计用户的收藏总数
     *
     * @param userId 用户ID
     * @return 收藏总数
     */
    int countUserFavorites(@Param("userId") Long userId);

    /**
     * 分页查询文章的收藏用户列表
     *
     * @param page   分页参数
     * @param postId 文章ID
     * @return 收藏记录分页列表
     */
    Page<Favorites> selectFavoritesByPost(Page<Favorites> page, @Param("postId") Long postId);

    /**
     * 分页查询用户的收藏列表
     *
     * @param page   分页参数
     * @param userId 用户ID
     * @return 收藏记录分页列表
     */
    Page<Favorites> selectFavoritesByUser(Page<Favorites> page, @Param("userId") Long userId);

    /**
     * 删除用户对指定文章的收藏
     *
     * @param userId 用户ID
     * @param postId 文章ID
     * @return 删除的记录数
     */
    int deleteFavorite(@Param("userId") Long userId, @Param("postId") Long postId);

    /**
     * 批量删除用户的收藏记录
     *
     * @param userId  用户ID
     * @param postIds 文章ID列表
     * @return 删除的记录数
     */
    int deleteFavoritesByPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);
}