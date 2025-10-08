import { Image } from '@/components';
import type { RelatedPost } from '@/types';
import { useNavigate } from 'react-router';

const RelatedPostCard = ({ post }: { post: RelatedPost }) => {
  const navigate = useNavigate();
  return (
    <div
      role='link'
      tabIndex={0}
      aria-label={`阅读推荐文章：${post.title}`}
      onClick={() => navigate(`/post/${post.slug}`)}
      className={`group flex items-start gap-3 p-3 rounded-lg transition transform bg-[var(--theme-card-background)] 
        border border-transparent hover:border-[color:var(--theme-border)] hover:shadow-lg hover:-translate-y-0.5 cursor-pointer`}
    >
      <div className='w-28 h-20 md:w-32 md:h-24 flex-shrink-0 overflow-hidden rounded-md bg-[color:var(--border)]'>
        <Image
          src={post.coverImageUrl}
          alt={post.title}
          className='w-full h-full object-cover transition-opacity duration-200'
        />
      </div>

      <div className='flex-1 min-w-0'>
        <h4 className='font-semibold text-sm md:text-base text-[color:var(--theme-primary-text)] line-clamp-1'>
          {post.title}
        </h4>
        <p className='text-xs text-[color:var(--theme-secondary-text)] mt-1 line-clamp-2'>
          {post.excerpt}
        </p>
        <div className='text-xs text-[color:var(--muted-foreground)] mt-2 flex items-center gap-2'></div>
      </div>
    </div>
  );
};

export const RelatedPosts = ({ posts }: { posts: RelatedPost[] }) => {
  return (
    <div>
      <h3 className='font-semibold text-lg text-[color:var(--theme-primary-text)] mb-4'>
        相关文章
      </h3>
      <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
        {posts.map(post => (
          <RelatedPostCard key={post.id} post={post} />
        ))}
      </div>
    </div>
  );
};
