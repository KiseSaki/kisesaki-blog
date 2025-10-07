import { uploadApi } from '@/api';
import { useAuth, usePost } from '@/hooks';
import type { CommentListResponse } from '@/types';
import { useEffect, useState } from 'react';

// 评论管理 Hook
export const useComments = (postId: number) => {
  const {
    postComments,
    isFetchingComments,
    fetchPostComments,
    createComment,
    likeComment,
  } = usePost();
  const { user } = useAuth();
  const [localComments, setLocalComments] = useState<CommentListResponse[]>([]);

  // 初始加载评论
  useEffect(() => {
    if (!postId) return;
    fetchPostComments(postId, 1, 10);
  }, [postId, fetchPostComments]);

  // 同步服务器数据到本地状态
  useEffect(() => {
    if (postComments) {
      setLocalComments(postComments);
    }
  }, [postComments]);

  /**
   * 递归更新评论点赞数（乐观更新辅助函数）
   */
  const updateCommentLikeCount = (
    comments: CommentListResponse[],
    commentId: number,
    increment: number
  ): CommentListResponse[] => {
    return comments.map(comment => {
      if (comment.id === commentId) {
        return {
          ...comment,
          likeCount: (comment.likeCount || 0) + increment,
        };
      }
      if (comment.replies && comment.replies.length > 0) {
        return {
          ...comment,
          replies: updateCommentLikeCount(
            comment.replies,
            commentId,
            increment
          ),
        };
      }
      return comment;
    });
  };

  /**
   * 递归添加回复到指定评论（乐观更新辅助函数）
   */
  const addReplyToComment = (
    comments: CommentListResponse[],
    replyToId: number,
    tempComment: CommentListResponse
  ): CommentListResponse[] => {
    return comments.map(comment => {
      if (comment.id === replyToId) {
        return {
          ...comment,
          replyCount: (comment.replyCount || 0) + 1,
          replies: [...(comment.replies || []), tempComment],
        };
      }
      if (comment.replies && comment.replies.length > 0) {
        return {
          ...comment,
          replies: addReplyToComment(comment.replies, replyToId, tempComment),
        };
      }
      return comment;
    });
  };

  /**
   * 提交评论（支持回复）
   * 使用乐观更新策略：先更新 UI，后调用 API
   */
  const handleSubmitComment = async (content: string, replyToId?: number) => {
    if (!postId || !user) return;

    // 创建临时评论对象（乐观更新）
    const tempComment: CommentListResponse = {
      id: Date.now(), // 临时 ID
      postId,
      userId: user.id,
      content,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      likeCount: 0,
      dislikeCount: 0,
      replyCount: 0,
      level: replyToId ? 1 : 0,
      path: replyToId ? `/${replyToId}/${Date.now()}` : `/${Date.now()}`,
      status: 'NORMAL',
      isPinned: false,
      isAuthorReply: false,
      hasMoreReplies: false,
      user: {
        id: user.id,
        username: user.username,
        displayName: user.displayName,
        avatarUrl: user.avatarUrl,
        email: user.email,
        roles: user.roles,
        permissions: user.permissions,
        createdAt: user.createdAt,
        updatedAt: user.updatedAt,
      },
      replies: [],
    };

    // 乐观更新：立即添加到本地状态
    if (replyToId) {
      setLocalComments(prev => addReplyToComment(prev, replyToId, tempComment));
    } else {
      setLocalComments(prev => [tempComment, ...prev]);
    }

    try {
      // 发送请求到服务器
      await createComment({
        postId,
        content,
        replyToId,
      });

      // 成功后，重新获取评论列表（静默刷新，不显示 loading）
      await fetchPostComments(postId, 1, 10);
    } catch (error) {
      // 如果失败，回滚本地状态
      if (postComments) {
        setLocalComments(postComments);
      }
      throw error;
    }
  };

  /**
   * 图片上传处理
   */
  const handleImageUpload = async (file: File): Promise<string> => {
    return await uploadApi.uploadImage(file);
  };

  /**
   * 点赞评论
   * 使用乐观更新策略
   */
  const handleLike = async (commentId: number) => {
    // 乐观更新：立即更新点赞数
    setLocalComments(prev => updateCommentLikeCount(prev, commentId, 1));

    try {
      // 发送请求到服务器
      await likeComment(commentId);
    } catch (error) {
      // 如果失败，回滚点赞数
      setLocalComments(prev => updateCommentLikeCount(prev, commentId, -1));
      throw error;
    }
  };

  return {
    user,
    comments: localComments.length > 0 ? localComments : postComments,
    isFetchingComments,
    handleSubmitComment,
    handleImageUpload,
    handleLike,
  };
};
