// 评论输入组件属性
export interface CommentInputProps {
  // 当前登录用户信息
  user?: {
    id: number;
    avatarUrl?: string;
    displayName: string;
    email?: string;
  };
  // 回复的评论信息
  replyTo?: {
    id: number;
    userName: string;
  };
  // 提交评论回调
  onSubmit: (content: string, replyToId?: number) => Promise<void>;
  // 取消回复回调
  onCancelReply?: () => void;
  // 占位符文本
  placeholder?: string;
  // 最小高度
  minHeight?: number;
  // 是否自动聚焦
  autoFocus?: boolean;
  // 自定义类名
  className?: string;
  // 文章 ID（用于草稿保存）
  postId?: number;
  // 是否启用图片上传
  enableImageUpload?: boolean;
  // 图片上传回调
  onImageUpload?: (file: File) => Promise<string>;
  // 是否启用表情
  enableEmoji?: boolean;
}
