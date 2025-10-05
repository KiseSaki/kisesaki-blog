import type { PublishedPostListResponse } from '@/types';
import { Calendar, Tag, User } from 'lucide-react';
import { useNavigate } from 'react-router';

export const FeaturedPostsCard = ({
  title,
  slug,
  coverImage,
  author,
  excerpt,
  publishedAt,
  tags,
}: PublishedPostListResponse) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/post/${slug}`);
  };

  return (
    <article
      role='article'
      aria-label={title}
      onClick={handleClick}
      className='group relative w-full h-[400px] sm:h-[450px] md:h-[500px] lg:h-[550px] rounded-xl overflow-hidden bg-card border border-border shadow-lg hover:shadow-2xl transition-all duration-500 cursor-pointer'
    >
      {/* 图片或占位（放到最底层） */}
      {coverImage ? (
        <img
          className='absolute inset-0 w-full h-full object-cover transition-transform duration-700 group-hover:scale-110'
          src={coverImage}
          alt={title}
          loading='lazy'
        />
      ) : (
        <div className='absolute inset-0 bg-gradient-to-br from-muted via-muted/80 to-secondary/20 flex items-center justify-center'>
          <span className='text-muted-foreground text-lg font-medium'>
            暂无封面
          </span>
        </div>
      )}

      {/* 渐变遮罩，提升文字可读性 */}
      <div className='absolute inset-0 bg-gradient-to-t from-black/80 via-black/50 to-transparent opacity-90 group-hover:opacity-95 transition-opacity duration-500 pointer-events-none' />

      {/* 顶部标签区域 */}
      {tags && tags.length > 0 && (
        <div className='absolute top-4 left-4 z-20 flex gap-2 flex-wrap max-w-[calc(100%-2rem)]'>
          {tags.slice(0, 3).map(tag => (
            <span
              key={tag.id}
              className='inline-flex items-center gap-1 px-3 py-1 text-xs font-medium rounded-full bg-primary/90 text-primary-foreground backdrop-blur-sm shadow-md hover:bg-primary transition-colors'
              style={{ backgroundColor: tag.color || undefined }}
            >
              <Tag className='w-3 h-3' />
              {tag.name}
            </span>
          ))}
        </div>
      )}

      {/* 内容区域 */}
      <div className='absolute inset-x-0 bottom-0 z-10 p-6 space-y-3 transform translate-y-0 group-hover:-translate-y-2 transition-transform duration-500'>
        {/* 标题 */}
        <h2 className='text-2xl md:text-3xl font-bold text-white drop-shadow-lg leading-tight line-clamp-2 group-hover:text-primary/90 transition-colors'>
          {title}
        </h2>

        {/* 摘要 */}
        <p className='text-sm text-gray-100/95 leading-relaxed line-clamp-2 drop-shadow-md'>
          {excerpt || '暂无摘要...'}
        </p>

        {/* 元信息 */}
        <div className='flex items-center gap-4 text-xs text-gray-200/90 drop-shadow-sm'>
          {/* 作者信息 */}
          <div className='flex items-center gap-1.5'>
            <User className='w-3.5 h-3.5' />
            <span>{author?.displayName || 'Unknown Author'}</span>
          </div>

          {/* 发布时间 */}
          {publishedAt && (
            <div className='flex items-center gap-1.5'>
              <Calendar className='w-3.5 h-3.5' />
              <time dateTime={publishedAt}>
                {new Date(publishedAt).toLocaleDateString('zh-CN', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })}
              </time>
            </div>
          )}
        </div>
      </div>

      {/* 悬浮装饰光效 */}
      <div className='absolute inset-0 bg-gradient-to-r from-primary/0 via-primary/10 to-primary/0 opacity-0 group-hover:opacity-100 transition-opacity duration-700 pointer-events-none' />
    </article>
  );
};
