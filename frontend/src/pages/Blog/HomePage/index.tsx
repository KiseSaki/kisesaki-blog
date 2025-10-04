import {
  BlogLayout,
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components';
import { useBlog } from '@/hooks';
import { useEffect } from 'react';
import { CategoryCloud } from './CategoryCloud';
import { FeaturedPostsCard } from './FeaturedPostsCard';
import { HeroSection } from './HeroSection';
import { RecentArticleCard } from './RecentArticleCard';
import { TagCloud } from './TagCloud';

const HomePage = () => {
  const {
    featuredPosts,
    recentPosts,
    popularCategories,
    tagCloud,
    fetchFeaturedPosts,
    fetchRecentPosts,
    fetchPopularCategories,
    fetchTagCloud,
  } = useBlog();

  useEffect(() => {
    fetchFeaturedPosts();
    fetchRecentPosts();
    fetchPopularCategories();
    fetchTagCloud();
  }, [
    fetchFeaturedPosts,
    fetchRecentPosts,
    fetchPopularCategories,
    fetchTagCloud,
  ]);

  return (
    <BlogLayout>
      {/* Hero Section */}
      <HeroSection />

      {/* Featured Posts Section */}
      <section className='space-y-6'>
        <div className='flex items-center justify-between'>
          <h2 className='text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
            精选文章
          </h2>
          <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
        </div>

        {featuredPosts && featuredPosts.data.length > 0 ? (
          <div className='relative px-12'>
            <Carousel className='w-full'>
              <CarouselContent>
                {featuredPosts.data.map(post => (
                  <CarouselItem key={post.id}>
                    <FeaturedPostsCard {...post} />
                  </CarouselItem>
                ))}
              </CarouselContent>
              <CarouselPrevious className='absolute left-0 top-1/2 -translate-y-1/2 z-10' />
              <CarouselNext className='absolute right-0 top-1/2 -translate-y-1/2 z-10' />
            </Carousel>
          </div>
        ) : (
          <div className='flex items-center justify-center h-96 rounded-xl bg-muted/30'>
            <p className='text-muted-foreground'>暂无精选文章</p>
          </div>
        )}
      </section>

      {/* Recent Articles Section */}
      <section className='space-y-6'>
        <div className='flex items-center justify-between'>
          <h2 className='text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
            最新文章
          </h2>
          <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
        </div>

        {recentPosts && recentPosts.data.length > 0 ? (
          <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6'>
            {recentPosts.data.map(post => (
              <RecentArticleCard key={post.id} {...post} />
            ))}
          </div>
        ) : (
          <div className='flex items-center justify-center h-64 rounded-xl bg-muted/30'>
            <p className='text-muted-foreground'>暂无最新文章</p>
          </div>
        )}
      </section>

      {/* Categories & Tags Cloud Section */}
      <section className='grid grid-cols-1 lg:grid-cols-2 gap-8'>
        {/* Category Cloud */}
        <div className='space-y-6'>
          <div className='flex items-center justify-between'>
            <h2 className='text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
              热门分类
            </h2>
            <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
          </div>

          {popularCategories.length > 0 ? (
            <div className='p-6 rounded-xl bg-card border border-border'>
              <CategoryCloud categories={popularCategories} />
            </div>
          ) : (
            <div className='flex items-center justify-center h-40 rounded-xl bg-muted/30'>
              <p className='text-muted-foreground'>暂无分类</p>
            </div>
          )}
        </div>

        {/* Tag Cloud */}
        <div className='space-y-6'>
          <div className='flex items-center justify-between'>
            <h2 className='text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
              标签云
            </h2>
            <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
          </div>

          {tagCloud.length > 0 ? (
            <div className='p-6 rounded-xl bg-card border border-border'>
              <TagCloud tags={tagCloud} />
            </div>
          ) : (
            <div className='flex items-center justify-center h-40 rounded-xl bg-muted/30'>
              <p className='text-muted-foreground'>暂无标签</p>
            </div>
          )}
        </div>
      </section>
    </BlogLayout>
  );
};

export default HomePage;
