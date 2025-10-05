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
    <div className='space-y-4'>
      <div className='flex items-center space-x-2'>
        <h1 className='text-5xl text-theme-primary-text'>{title}</h1>
        {isFeatured && <Tag color='red'>精选</Tag>}
      </div>

      <div className='flex items-center text-theme-secondary-text'>
        <span>{authorDisplayName}</span>
        {authorAvatarUrl && (
          <img
            className='h-8 rounded-full mx-1'
            src={authorAvatarUrl}
            alt={authorDisplayName}
          />
        )}
        <span>
          <i className='px-1'>•</i>
          发布于 {new Date(publishedAt).toLocaleDateString()}
        </span>
        {readingTime && (
          <span>
            <i className='px-1'>•</i>阅读完需要 {readingTime} 分钟
          </span>
        )}
      </div>

      <div className='flex'>
        {categoryName && (
          <span>
            分类：<Tag>{categoryName}</Tag>
          </span>
        )}
        {tags && tags.length > 0 && (
          <div className='ml-4'>
            标签：
            {tags.map(tag => (
              <Tag key={tag.id} className='mr-2' color={tag.color || 'default'}>
                {tag.name}
              </Tag>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
