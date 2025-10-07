import { CommentInput, DotIcon, Loading } from '@/components';
import { CommentCard } from './CommentCard';
import type { CommentsSectionProps } from './types';
import { useComments } from './useComments';

// 评论区组件
export const CommentsSection = ({ postId }: CommentsSectionProps) => {
  const {
    user,
    comments,
    isFetchingComments,
    handleSubmitComment,
    handleImageUpload,
    handleLike,
  } = useComments(postId);

  if (isFetchingComments || !comments) {
    return <Loading overlay text='正在加载评论...' />;
  }

  return (
    <div className='comments-section mt-12 flex flex-col gap-5'>
      <h2 className='text-2xl font-bold'>评论</h2>

      {/* 评论输入框 */}
      <CommentInput
        user={user || undefined}
        postId={postId}
        onSubmit={handleSubmitComment}
        placeholder='写下你的评论，支持 Markdown 语法...'
        enableImageUpload
        onImageUpload={handleImageUpload}
        enableEmoji
      />

      {/* 排序选项 */}
      <div className='flex items-center gap-2'>
        <span className='cursor-pointer hover:text-theme-primary-hover'>
          最热
        </span>
        <DotIcon className='text-muted-foreground' />
        <span className='cursor-pointer hover:text-theme-primary-hover'>
          最新
        </span>
      </div>

      {/* 评论列表 */}
      <div className='comments-list space-y-6'>
        {comments && comments.length > 0 ? (
          comments.map(comment => (
            <CommentCard
              key={comment.id}
              comment={comment}
              user={user}
              postId={postId}
              handleSubmitComment={handleSubmitComment}
              handleImageUpload={handleImageUpload}
              onLike={handleLike}
            />
          ))
        ) : (
          <div className='text-center text-muted-foreground py-8'>
            暂无评论,快来发表第一条评论吧!
          </div>
        )}
      </div>
    </div>
  );
};
