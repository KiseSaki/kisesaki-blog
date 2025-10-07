import { CommentInput, UserAvatar } from '@/components';
import { formatTimeAgo } from '@/lib';
import { LikeOutlined, MessageOutlined } from '@ant-design/icons';
import { useState } from 'react';
import type { CommentCardProps } from './types';

// 单条评论组件
export const CommentCard = ({
  comment,
  replyComment,
  user,
  postId,
  handleSubmitComment,
  handleImageUpload,
  onLike,
}: CommentCardProps) => {
  const [isShowReplyInput, setIsShowReplyInput] = useState(false);
  const [isLiking, setIsLiking] = useState(false);

  // 处理回复输入框显示状态
  const handleReplyClick = () => {
    setIsShowReplyInput(!isShowReplyInput);
  };

  // 处理点赞操作
  const handleLikeClick = async () => {
    if (isLiking || !onLike) return;
    setIsLiking(true);
    try {
      await onLike(comment.id);
    } finally {
      setIsLiking(false);
    }
  };

  // 处理回复提交
  const handleReplySubmit = async (content: string) => {
    await handleSubmitComment(content, comment.id);
    setIsShowReplyInput(false);
  };

  return (
    <div className='flex items-start gap-4 p-1'>
      <UserAvatar
        src={comment.user?.avatarUrl}
        name={comment.user?.displayName || '匿名用户'}
      />

      <div className='flex-1 flex flex-col gap-2'>
        {/* 用户信息 */}
        <div className='font-medium flex items-center gap-2 flex-wrap'>
          <span className='text-foreground'>
            {comment.user?.displayName || '匿名用户'}
          </span>
          {replyComment && (
            <div className='inline-flex items-center gap-1.5 text-sm'>
              <MessageOutlined className='text-muted-foreground text-xs' />
              <span className='text-muted-foreground'>回复</span>
              <span className='px-2 py-0.5 rounded-md bg-muted text-foreground font-medium'>
                {replyComment?.user?.displayName || '匿名用户'}
              </span>
            </div>
          )}
        </div>

        {/* 评论内容 */}
        <div className='text-foreground leading-relaxed'>{comment.content}</div>

        {/* 日期和操作按钮 */}
        <div className='text-muted-foreground text-sm flex items-center gap-1 flex-wrap'>
          <span className='mr-2'>{formatTimeAgo(comment.createdAt)}</span>

          {/* 点赞 */}
          <button
            className='inline-flex items-center gap-1.5 px-3 py-1 rounded-full hover:bg-muted transition-colors cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed group'
            onClick={handleLikeClick}
            disabled={isLiking}
          >
            <LikeOutlined className='group-hover:text-theme-primary transition-colors' />
            <span className='group-hover:text-foreground transition-colors'>
              {comment.likeCount || 0}
            </span>
            {isLiking && <span className='ml-0.5'>...</span>}
          </button>

          {/* 回复 */}
          <button
            className='inline-flex items-center gap-1.5 px-3 py-1 rounded-full hover:bg-muted transition-colors cursor-pointer group'
            onClick={handleReplyClick}
          >
            <MessageOutlined className='group-hover:text-theme-primary transition-colors' />
            <span className='group-hover:text-foreground transition-colors'>
              {comment.replyCount > 0
                ? comment.replyCount
                : isShowReplyInput
                  ? '取消回复'
                  : '回复'}
            </span>
          </button>
        </div>

        {/* 回复输入框 */}
        {isShowReplyInput && (
          <div className='mt-2'>
            <CommentInput
              user={user || undefined}
              postId={postId}
              onSubmit={handleReplySubmit}
              placeholder='写下你的评论，支持 Markdown 语法...'
              enableImageUpload
              onImageUpload={handleImageUpload}
              enableEmoji
            />
          </div>
        )}

        {/* 子评论（递归渲染） */}
        {comment.replies && comment.replies.length > 0 && (
          <div className='mt-4 border-l-2 border-theme-primary/20 pl-4 space-y-4 relative before:absolute before:left-0 before:top-0 before:bottom-0 before:w-0.5 before:bg-gradient-to-b before:from-theme-primary/40 before:to-transparent'>
            {comment.replies.map(reply => (
              <CommentCard
                key={reply.id}
                comment={reply}
                user={user}
                postId={postId}
                handleSubmitComment={handleSubmitComment}
                handleImageUpload={handleImageUpload}
                onLike={onLike}
                replyComment={
                  reply.replyToId && comment.replies
                    ? comment.replies.find(c => c.id === reply.replyToId)
                    : undefined
                }
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
