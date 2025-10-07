import { BlogLayout, Loading } from '@/components';
import { usePost } from '@/hooks';
import { useEffect } from 'react';
import { useParams } from 'react-router';
import { AdjacentPosts } from './AdjacentPosts';
import { CommentsSection } from './CommentsSection';
import { PostContent } from './PostContent';
import { PostMeta } from './PostMeta';
import { RelatedPosts } from './RelatedPosts';

/**
 * 博客文章详情页面
 * 展示文章内容、评论区、相关文章推荐等
 */
const PostDetailPage = () => {
  const { slug } = useParams<{ slug: string }>();
  const { fetchPostDetailBySlug, postDetail, isFetchingDetail } = usePost();

  useEffect(() => {
    if (!slug) return;
    fetchPostDetailBySlug(slug);
  }, [slug, fetchPostDetailBySlug]);

  if (isFetchingDetail || !postDetail) {
    return <Loading overlay text='正在加载文章...' />;
  }

  return (
    <BlogLayout>
      <PostMeta
        title={postDetail.title}
        authorDisplayName={postDetail.author.displayName}
        authorAvatarUrl={postDetail.author.avatarUrl}
        publishedAt={postDetail.publishedAt}
        readingTime={postDetail.readingTime}
        categoryName={postDetail.category.name}
        tags={postDetail.tags}
        isFeatured={postDetail.isFeatured}
      />
      <PostContent htmlContent={postDetail.htmlContent} />

      <AdjacentPosts
        prevPost={postDetail.prevPost}
        nextPost={postDetail.nextPost}
      />

      <RelatedPosts posts={postDetail.relatedPosts} />

      <CommentsSection postId={postDetail.id} />
    </BlogLayout>
  );
};

export default PostDetailPage;
