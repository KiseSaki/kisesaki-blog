import { Card, CardContent, CardFooter, CardHeader, Image } from '@/components';
import { BLOG_POST_DETAIL_BASE } from '@/config';
import type { PublishedPostListResponse } from '@/types';
import { Calendar, Eye, Tag } from 'lucide-react';
import { useNavigate } from 'react-router';

export const RecentArticleCard = ({
  title,
  slug,
  coverImageUrl,
  excerpt,
  publishedAt,
  tags,
  viewCount,
}: PublishedPostListResponse) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`${BLOG_POST_DETAIL_BASE}/${slug}`);
  };

  return (
    <Card
      onClick={handleClick}
      className='group overflow-hidden border border-border hover:border-primary/50 transition-all duration-300 hover:shadow-lg cursor-pointer h-full flex flex-col'
    >
      {/* 封面图片 */}
      <div className='relative h-48 overflow-hidden bg-muted'>
        <Image
          src={coverImageUrl}
          alt={title}
          loading='lazy'
          className='w-full h-full object-cover transition-transform duration-500 group-hover:scale-110'
          placeholder='暂无封面'
        />
        {/* 渐变遮罩 */}
        <div className='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300' />
      </div>

      <CardHeader className='pb-3'>
        <h3 className='text-xl font-bold line-clamp-2 group-hover:text-primary transition-colors'>
          {title}
        </h3>
      </CardHeader>

      <CardContent className='flex-1 pb-3'>
        <p className='text-sm text-muted-foreground line-clamp-3 leading-relaxed'>
          {excerpt || '暂无摘要...'}
        </p>
      </CardContent>

      <CardFooter className='pt-3 border-t border-border/50 flex flex-col gap-3'>
        {/* 标签 */}
        {tags && tags.length > 0 && (
          <div className='flex flex-wrap gap-2 w-full'>
            {tags.slice(0, 3).map(tag => (
              <span
                key={tag.id}
                className='inline-flex items-center gap-1 px-2 py-1 text-xs rounded-md bg-primary/10 text-primary hover:bg-primary/20 transition-colors'
                style={{
                  backgroundColor: tag.color ? `${tag.color}20` : undefined,
                  color: tag.color || undefined,
                }}
              >
                <Tag className='w-3 h-3' />
                {tag.name}
              </span>
            ))}
          </div>
        )}

        {/* 元信息 */}
        <div className='flex items-center justify-between w-full text-xs text-muted-foreground'>
          {publishedAt && (
            <div className='flex items-center gap-1'>
              <Calendar className='w-3.5 h-3.5' />
              <time dateTime={publishedAt}>
                {new Date(publishedAt).toLocaleDateString('zh-CN', {
                  month: 'short',
                  day: 'numeric',
                })}
              </time>
            </div>
          )}

          {viewCount !== undefined && (
            <div className='flex items-center gap-1'>
              <Eye className='w-3.5 h-3.5' />
              <span>{viewCount}</span>
            </div>
          )}
        </div>
      </CardFooter>
    </Card>
  );
};
