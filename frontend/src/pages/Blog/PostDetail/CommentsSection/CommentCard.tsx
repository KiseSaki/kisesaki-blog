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
    <div className='flex items-start gap-3'>
      <UserAvatar
        src={comment.user?.avatarUrl}
        name={comment.user?.displayName || '匿名用户'}
      />

      <div className='flex-1 flex flex-col gap-2'>
        {/* 用户信息 */}
        <div className='font-medium'>
          <span>{comment.user?.displayName || '匿名用户'}</span>
          {replyComment && (
            <>
              <span className='mx-1'>回复</span>
              <span>{replyComment?.user?.displayName || '匿名用户'} : </span>
            </>
          )}
        </div>

        {/* 评论内容 */}
        <div>{comment.content}</div>

        {/* 日期和操作按钮 */}
        <div className='text-muted-foreground text-sm flex gap-2'>
          <span>{formatTimeAgo(comment.createdAt)}</span>

          {/* 点赞 */}
          <div
            className='ml-2 cursor-pointer hover:underline items-center'
            onClick={handleLikeClick}
          >
            <LikeOutlined />
            <span className='ml-1'>{comment.likeCount || 0}</span>
            {isLiking && <span className='ml-1'>...</span>}
          </div>

          {/* 回复 */}
          <div
            className='inline-block ml-2 cursor-pointer hover:underline'
            onClick={handleReplyClick}
          >
            <MessageOutlined />
            <span className='ml-1'>
              {comment.replyCount > 0
                ? comment.replyCount
                : isShowReplyInput
                  ? '取消回复'
                  : '回复'}
            </span>
          </div>
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
          <div className='mt-4 border-l-2 border-muted pl-4 space-y-4'>
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
