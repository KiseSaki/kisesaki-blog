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
import { FeaturedPostsCard } from './FeaturedPostsCard';
import { HeroSection } from './HeroSection';

const HomePage = () => {
  const { featuredPosts, fetchFeaturedPosts } = useBlog();

  useEffect(() => {
    fetchFeaturedPosts();
  }, [fetchFeaturedPosts]);

  return (
    <BlogLayout>
      <HeroSection />
      {featuredPosts && featuredPosts.data.length > 0 ? (
        <Carousel>
          <CarouselContent>
            {featuredPosts.data.map(post => (
              <CarouselItem key={post.id}>
                <FeaturedPostsCard {...post} />
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious className='absolute left-4 top-1/2 transform -translate-y-1/2' />
          <CarouselNext className='absolute right-4 top-1/2 transform -translate-y-1/2' />
        </Carousel>
      ) : (
        <p>No featured posts available.</p>
      )}
    </BlogLayout>
  );
};

export default HomePage;
