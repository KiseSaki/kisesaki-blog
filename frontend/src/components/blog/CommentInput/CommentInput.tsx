import { MarkdownEditor, UserAvatar } from '@/components/common';
import { Button } from '@/components/ui';
import { useLocalStorage } from '@/hooks';
import { useEffect, useState } from 'react';
import { toast } from 'sonner';
import type { CommentInputProps } from './types';

/**
 * 评论输入组件
 *
 * @description
 * 支持 Markdown 语法的评论输入框，功能包括：
 * - Markdown 实时预览
 * - 图片上传
 * - 表情选择
 * - 草稿自动保存
 * - 快捷键提交 (Ctrl/Cmd + Enter)
 *
 * @example
 * ```tsx
 * <CommentInput
 *   user={currentUser}
 *   postId={articleId}
 *   onSubmit={handleSubmitComment}
 *   enableImageUpload
 *   enableEmoji
 * />
 * ```
 */

export const CommentInput = ({
  user,
  replyTo,
  onSubmit,
  onCancelReply,
  placeholder = '写下你的评论，支持 Markdown 语法...',
  minHeight = 150,
  postId,
  enableImageUpload = true,
  onImageUpload,
  enableEmoji = true,
  className = '',
}: CommentInputProps) => {
  // 草稿保存的 key
  const storageKey = `comment-draft-${postId || 'global'}-${replyTo?.id || 'main'}`;
  const [draft, setDraft] = useLocalStorage(storageKey, '');

  const [content, setContent] = useState(draft);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // 自动保存草稿（防抖）
  useEffect(() => {
    const timer = setTimeout(() => {
      setDraft(content);
    }, 1000);

    return () => clearTimeout(timer);
  }, [content, setDraft]);

  // 提交评论
  const handleSubmit = async () => {
    // 验证内容
    if (!content.trim()) {
      toast.error('评论内容不能为空');
      return;
    }

    if (content.length > 5000) {
      toast.error('评论内容不能超过 5000 字');
      return;
    }

    setIsSubmitting(true);
    try {
      await onSubmit(content, replyTo?.id);
      setContent(''); // 清空输入框
      setDraft(''); // 清除草稿
      toast.success(replyTo ? '回复成功' : '评论发表成功');
    } catch (error) {
      toast.error('提交失败，请稍后重试');
      console.error('提交评论失败:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  // 取消操作
  const handleCancel = () => {
    if (replyTo && onCancelReply) {
      onCancelReply();
    } else {
      setContent('');
    }
  };

  // 键盘快捷键：Ctrl/Cmd + Enter 提交
  const handleKeyDown = (e: React.KeyboardEvent) => {
    if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
      e.preventDefault();
      handleSubmit();
    }
  };

  return (
    <div className={`comment-input ${className}`}>
      {user ? (
        // 已登录用户评论
        <div className='flex items-start gap-3'>
          <UserAvatar src={user.avatarUrl} name={user.displayName} />
          <div className='flex-1'>
            {replyTo && (
              <div className='mb-2 flex items-center gap-2 text-sm text-muted-foreground'>
                <span>回复</span>
                <span className='font-medium text-primary'>
                  @{replyTo.userName}
                </span>
              </div>
            )}

            <div onKeyDown={handleKeyDown}>
              <MarkdownEditor
                value={content}
                onChange={setContent}
                placeholder={placeholder}
                minHeight={minHeight}
                preview='edit'
                enableImageUpload={enableImageUpload}
                onImageUpload={onImageUpload}
                enableEmoji={enableEmoji}
              />
            </div>

            <div className='mt-3 flex items-center justify-between'>
              <div className='text-xs text-muted-foreground'>
                {content.length} / 5000
                <span className='ml-2'>支持 Markdown 语法</span>
                <span className='ml-2'>Ctrl+Enter 快速提交</span>
                {draft && content !== draft && (
                  <span className='ml-2 text-amber-600'>• 草稿已自动保存</span>
                )}
              </div>

              <div className='flex gap-2'>
                {(content.trim() || replyTo) && (
                  <Button
                    variant='outline'
                    size='sm'
                    onClick={handleCancel}
                    disabled={isSubmitting}
                  >
                    取消
                  </Button>
                )}
                <Button
                  size='sm'
                  onClick={handleSubmit}
                  disabled={!content.trim() || isSubmitting}
                >
                  {isSubmitting ? '发送中...' : replyTo ? '回复' : '发表评论'}
                </Button>
              </div>
            </div>
          </div>
        </div>
      ) : (
        // 未登录提示
        <div className='rounded-lg border border-dashed border-gray-300 bg-gray-50 p-6 text-center'>
          <p className='mb-3 text-sm text-muted-foreground'>
            登录后即可发表评论
          </p>
          <Button
            size='sm'
            onClick={() => {
              /* 跳转登录 */
            }}
          >
            立即登录
          </Button>
        </div>
      )}
    </div>
  );
};
