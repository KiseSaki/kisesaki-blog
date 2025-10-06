import { Image } from '@/components';
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
      className={`group flex items-center gap-x-3 p-3 h-24 w-full max-w-[48%] rounded-lg transition transform
         bg-[var(--theme-card-background)] hover:shadow-lg hover:-translate-y-0.5
         border border-transparent hover:border-[color:var(--theme-border)] cursor-pointer`}
      onClick={() => navigate(`/post/${post.slug}`)}
    >
      <div className='w-24 h-full flex-shrink-0 overflow-hidden rounded-md'>
        <Image
          src={post.coverImageUrl}
          alt={post.title}
          className='w-full h-full object-cover rounded-md'
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
    <div className='flex justify-between cursor-pointer'>
      {prevPost ? <AdjacentPostItem post={prevPost} type='prev' /> : null}
      {nextPost ? <AdjacentPostItem post={nextPost} type='next' /> : null}
    </div>
  );
};
