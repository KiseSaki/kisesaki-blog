import type { CommentListResponse, UserInfo } from '@/types';

export interface CommentCardProps {
  // 评论数据
  comment: CommentListResponse;
  // 被该评论回复的评论
  replyComment?: CommentListResponse;
  // 当前登录用户信息
  user: UserInfo | null;
  // 文章 ID
  postId: number;
  // 提交评论的回调函数
  handleSubmitComment: (content: string, replyToId?: number) => Promise<void>;
  // 图片上传回调函数
  handleImageUpload?: (file: File) => Promise<string>;
  // 点赞回调函数
  onLike?: (commentId: number) => Promise<void>;
}

//评论区组件的属性
export interface CommentsSectionProps {
  // 文章 ID
  postId: number;
}
