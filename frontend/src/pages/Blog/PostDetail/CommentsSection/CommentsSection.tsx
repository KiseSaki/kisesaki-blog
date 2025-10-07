import { CommentInput, DotIcon, Loading } from '@/components';
import { MessageOutlined } from '@ant-design/icons';
import { CommentCard } from './CommentCard';
import type { CommentsSectionProps } from './types';
import { useComments } from './useComments';

// 评论区组件
export const CommentsSection = ({ postId }: CommentsSectionProps) => {
  const {
    user,
    comments,
    isFetchingComments,
    sortType,
    handleSortChange,
    handleSubmitComment,
    handleImageUpload,
    handleLike,
  } = useComments(postId);

  if (isFetchingComments || !comments) {
    return <Loading overlay text='正在加载评论...' />;
  }

  return (
    <div className='comments-section mt-12 flex flex-col gap-6'>
      <div className='flex items-center justify-between'>
        <h2 className='text-2xl font-bold flex items-center gap-2'>
          <MessageOutlined className='text-theme-primary' />
          评论
          {comments && comments.length > 0 && (
            <span className='text-sm font-normal text-muted-foreground'>
              ({comments.length})
            </span>
          )}
        </h2>
      </div>

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
      <div className='flex items-center gap-2 text-sm'>
        <span className='text-muted-foreground'>排序方式：</span>
        <button
          onClick={() => handleSortChange('hot')}
          className={
            sortType === 'hot'
              ? 'px-3 py-1 rounded-full bg-theme-primary text-primary-foreground hover:bg-theme-primary-hover transition-colors cursor-pointer'
              : 'px-3 py-1 rounded-full hover:bg-muted transition-colors cursor-pointer'
          }
        >
          最热
        </button>
        <DotIcon className='text-muted-foreground' />
        <button
          onClick={() => handleSortChange('latest')}
          className={
            sortType === 'latest'
              ? 'px-3 py-1 rounded-full bg-theme-primary text-primary-foreground hover:bg-theme-primary-hover transition-colors cursor-pointer'
              : 'px-3 py-1 rounded-full hover:bg-muted transition-colors cursor-pointer'
          }
        >
          最新
        </button>
      </div>

      {/* 评论列表 */}
      <div className='comments-list space-y-6 divide-y divide-border/50'>
        {comments && comments.length > 0 ? (
          comments.map((comment, index) => (
            <div key={comment.id} className={index > 0 ? 'pt-6' : ''}>
              <CommentCard
                comment={comment}
                user={user}
                postId={postId}
                handleSubmitComment={handleSubmitComment}
                handleImageUpload={handleImageUpload}
                onLike={handleLike}
              />
            </div>
          ))
        ) : (
          <div className='text-center py-12 rounded-lg border-2 border-dashed border-border bg-muted/30'>
            <MessageOutlined className='text-4xl text-muted-foreground mb-3' />
            <p className='text-muted-foreground text-lg'>暂无评论</p>
            <p className='text-muted-foreground text-sm mt-2'>
              快来发表第一条评论吧！
            </p>
          </div>
        )}
      </div>
    </div>
  );
};
