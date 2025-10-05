import { Tag } from '@/components';
import type { TagSimple } from '@/types';

interface PostMetaProps {
  title: string;
  authorDisplayName: string;
  authorAvatarUrl?: string | null;
  publishedAt: string;
  readingTime?: number;
  categoryName?: string;
  tags?: TagSimple[];
  isFeatured?: boolean;
}
export const PostMeta = ({
  title,
  authorDisplayName,
  authorAvatarUrl,
  publishedAt,
  readingTime,
  categoryName,
  tags,
  isFeatured,
}: PostMetaProps) => {
  return (
    <div className='space-y-6 mb-10 pb-8 border-b border-theme-border/30'>
      {/* 标题区域 */}
      <div className='flex items-center gap-3'>
        <h1 className='text-5xl font-extrabold text-theme-primary-text leading-tight tracking-tight'>
          {title}
        </h1>
        {isFeatured && (
          <Tag color='red' className='flex-shrink-0 text-sm font-semibold'>
            ✨ 精选
          </Tag>
        )}
      </div>

      {/* 作者和元信息区域 */}
      <div className='flex items-center gap-4 text-theme-secondary-text text-sm'>
        <div className='flex items-center gap-2'>
          {authorAvatarUrl && (
            <img
              className='h-10 w-10 rounded-full ring-2 ring-theme-border/50 transition-transform hover:scale-110'
              src={authorAvatarUrl}
              alt={authorDisplayName}
            />
          )}
          <span className='font-medium text-theme-primary-text'>
            {authorDisplayName}
          </span>
        </div>

        <span className='text-theme-border'>•</span>

        <time className='flex items-center gap-1.5'>
          <svg
            className='w-4 h-4'
            fill='none'
            stroke='currentColor'
            viewBox='0 0 24 24'
          >
            <path
              strokeLinecap='round'
              strokeLinejoin='round'
              strokeWidth={2}
              d='M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z'
            />
          </svg>
          {new Date(publishedAt).toLocaleDateString('zh-CN', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
          })}
        </time>

        {readingTime && (
          <>
            <span className='text-theme-border'>•</span>
            <span className='flex items-center gap-1.5'>
              <svg
                className='w-4 h-4'
                fill='none'
                stroke='currentColor'
                viewBox='0 0 24 24'
              >
                <path
                  strokeLinecap='round'
                  strokeLinejoin='round'
                  strokeWidth={2}
                  d='M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z'
                />
              </svg>
              {readingTime} 分钟阅读
            </span>
          </>
        )}
      </div>

      {/* 分类和标签区域 */}
      {(categoryName || (tags && tags.length > 0)) && (
        <div className='flex flex-wrap items-center gap-4 pt-2'>
          {categoryName && (
            <div className='flex items-center gap-2'>
              <svg
                className='w-4 h-4 text-theme-primary'
                fill='none'
                stroke='currentColor'
                viewBox='0 0 24 24'
              >
                <path
                  strokeLinecap='round'
                  strokeLinejoin='round'
                  strokeWidth={2}
                  d='M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z'
                />
              </svg>
              <span className='text-sm text-theme-secondary-text font-medium'>
                分类:
              </span>
              <Tag className='font-medium'>{categoryName}</Tag>
            </div>
          )}

          {tags && tags.length > 0 && (
            <div className='flex items-center gap-2 flex-wrap'>
              <svg
                className='w-4 h-4 text-theme-primary'
                fill='none'
                stroke='currentColor'
                viewBox='0 0 24 24'
              >
                <path
                  strokeLinecap='round'
                  strokeLinejoin='round'
                  strokeWidth={2}
                  d='M7 20l4-16m2 16l4-16M6 9h14M4 15h14'
                />
              </svg>
              <span className='text-sm text-theme-secondary-text font-medium'>
                标签:
              </span>
              {tags.map(tag => (
                <Tag
                  key={tag.id}
                  color={tag.color || 'default'}
                  className='font-medium hover:scale-105 transition-transform cursor-pointer'
                >
                  {tag.name}
                </Tag>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};
