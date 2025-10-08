import { Image } from '@/components';
import { BLOG_POST_DETAIL_BASE } from '@/config';
import type { AdjacentPost } from '@/types';
import { useNavigate } from 'react-router';

const AdjacentPostItem = ({
  post,
  type,
}: {
  post: AdjacentPost | null;
  type: 'prev' | 'next';
}) => {
  const navigate = useNavigate();

  if (!post) return null;

  return (
    <div
      role='button'
      tabIndex={0}
      aria-label={`${type === 'prev' ? '上一篇' : '下一篇'}: ${post.title}`}
      className={`group flex items-center gap-x-3 p-3 w-full max-w-[48%] rounded-lg transition transform
         bg-[var(--theme-card-background)] hover:shadow-lg hover:-translate-y-0.5
         border border-transparent hover:border-[color:var(--theme-border)] cursor-pointer`}
      onClick={() => navigate(`${BLOG_POST_DETAIL_BASE}/${post.slug}`)}
    >
      <div className='w-28 h-20 md:w-32 md:h-24 flex-shrink-0 overflow-hidden rounded-md bg-[color:var(--border)]'>
        <Image
          src={post.coverImageUrl}
          alt={post.title}
          className='w-full h-full object-cover transition-opacity duration-200'
        />
      </div>

      <div className='flex-1 min-w-0'>
        <div className='flex items-center justify-between'>
          <span className='text-xs text-[color:var(--muted-foreground)]'>
            {type === 'prev' ? '上一篇' : '下一篇'}
          </span>
        </div>

        <h3 className='font-semibold text-sm md:text-base text-[color:var(--theme-primary-text)] line-clamp-1'>
          {post.title}
        </h3>

        <p className='text-xs text-[color:var(--theme-secondary-text)] mt-1 line-clamp-2'>
          {post.excerpt}
        </p>
      </div>
    </div>
  );
};

export const AdjacentPosts = ({
  prevPost,
  nextPost,
}: {
  prevPost: AdjacentPost | null;
  nextPost: AdjacentPost | null;
}) => {
  return (
    <div>
      <h3 className='font-semibold text-lg text-[color:var(--theme-primary-text)] mb-4'>
        相邻文章
      </h3>
      <div className='flex justify-between'>
        {prevPost ? <AdjacentPostItem post={prevPost} type='prev' /> : null}
        {nextPost ? <AdjacentPostItem post={nextPost} type='next' /> : null}
      </div>
    </div>
  );
};
