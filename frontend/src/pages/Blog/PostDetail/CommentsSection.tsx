import { uploadApi } from '@/api';
import { CommentInput, Loading, UserAvatar } from '@/components';
import { useAuth, usePost } from '@/hooks';
import { useEffect } from 'react';

export const CommentsSection = ({ postId }: { postId: number }) => {
  const { postComments, isFetchingComments, fetchPostComments, createComment } =
    usePost();
  const { user } = useAuth();

  useEffect(() => {
    if (!postId) return;
    fetchPostComments(postId, 1, 10);
  }, [postId, fetchPostComments]);

  // 处理提交评论
  const handleSubmitComment = async (content: string) => {
    if (!postId) return;

    await createComment({
      postId,
      content,
      parentId: undefined,
    });

    // 刷新评论列表
    await fetchPostComments(postId, 1, 10);
  };

  // 处理图片上传
  const handleImageUpload = async (file: File): Promise<string> => {
    return await uploadApi.uploadImage(file, 'comment');
  };

  if (isFetchingComments || !postComments) {
    return <Loading overlay text='正在加载评论...' />;
  }

  console.log(postComments);

  return (
    <div className='comments-section mt-12'>
      <h2 className='text-2xl font-bold mb-6'>评论</h2>

      {/* 评论输入框 */}
      <div className='mb-8'>
        <CommentInput
          user={user || undefined}
          postId={postId}
          onSubmit={handleSubmitComment}
          placeholder='写下你的评论，支持 Markdown 语法...'
          enableImageUpload
          onImageUpload={handleImageUpload}
          enableEmoji
        />
      </div>

      {/* 评论列表 */}
      <div className='comments-list space-y-6'>
        {postComments && postComments.length > 0 ? (
          postComments.map(comment => (
            <div key={comment.id} className='comment-item'>
              <div className='flex items-start gap-3'>
                <UserAvatar
                  src={comment.user?.avatarUrl}
                  name={comment.user?.displayName || '匿名用户'}
                />
                <div className='flex-1'>
                  <div className='font-medium'>
                    {comment.user?.displayName || '匿名用户'}
                  </div>
                  <div className='text-sm text-muted-foreground mt-1'>
                    {comment.content}
                  </div>
                </div>
              </div>
            </div>
          ))
        ) : (
          <div className='text-center text-muted-foreground py-8'>
            暂无评论，快来发表第一条评论吧！
          </div>
        )}
      </div>
    </div>
  );
};
