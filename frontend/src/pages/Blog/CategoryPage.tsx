import {
  BlogLayout,
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CategoryCloud,
  Image,
  Separator,
} from '@/components';
import { getCategoryPostsApi } from '@/api';
import { useCategory } from '@/hooks';
import type { PageResponse, PublishedPostListResponse } from '@/types';
import { Calendar, ChevronLeft, ChevronRight, Eye, Folder, Tag } from 'lucide-react';
import { useCallback, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router';

/**
 * 博客分类页面
 * 按分类展示文章列表，支持分类筛选和分页
 */
const CategoryPage = () => {
  const navigate = useNavigate();
  const { slug } = useParams<{ slug: string }>();

  const { popularCategories, fetchPopularCategories, isFetchingCategories } =
    useCategory();

  const [posts, setPosts] = useState<PageResponse<PublishedPostListResponse> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 12;

  // 当前选中的分类
  const currentCategory = popularCategories.find(cat => cat.slug === slug);

  // 获取分类下的文章列表
  const fetchCategoryPosts = useCallback(
    async (categoryId: number, page: number) => {
      setIsLoading(true);
      try {
        const res = await getCategoryPostsApi(categoryId, {
          pageable: {
            currentPage: page,
            pageSize,
            sort: 'publishedAt,desc',
          },
        });
        setPosts(res);
      } catch (error) {
        console.error('获取分类文章失败:', error);
      } finally {
        setIsLoading(false);
      }
    },
    []
  );

  useEffect(() => {
    fetchPopularCategories();
  }, [fetchPopularCategories]);

  useEffect(() => {
    if (currentCategory) {
      fetchCategoryPosts(currentCategory.id, currentPage);
    }
  }, [currentCategory, currentPage, fetchCategoryPosts]);

  const handleCategoryClick = (categorySlug: string) => {
    navigate(`/category/${categorySlug}`);
    setCurrentPage(1);
  };

  const handlePostClick = (postSlug: string) => {
    navigate(`/post/${postSlug}`);
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  if (isFetchingCategories) {
    return (
      <BlogLayout>
        <div className='flex items-center justify-center min-h-[60vh]'>
          <div className='text-center space-y-4'>
            <div className='animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto' />
            <p className='text-muted-foreground'>加载分类数据中...</p>
          </div>
        </div>
      </BlogLayout>
    );
  }

  return (
    <BlogLayout>
      {/* 页面标题 */}
      <section className='space-y-4'>
        <div className='flex items-center justify-between'>
          <h1 className='text-4xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
            文章分类
          </h1>
          <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
        </div>

        {currentCategory && (
          <div className='flex items-center gap-3 text-sm text-muted-foreground'>
            <Folder className='w-4 h-4 text-primary' />
            <span className='font-medium text-foreground'>{currentCategory.name}</span>
            <span>•</span>
            <span>{posts?.totalRecords || 0} 篇文章</span>
          </div>
        )}
      </section>

      <div className='grid grid-cols-1 lg:grid-cols-4 gap-8'>
        {/* 左侧：分类列表 */}
        <aside className='lg:col-span-1 space-y-4'>
          <Card className='sticky top-20'>
            <CardHeader className='pb-3'>
              <h2 className='text-lg font-semibold flex items-center gap-2'>
                <Folder className='w-5 h-5 text-primary' />
                所有分类
              </h2>
            </CardHeader>
            <CardContent className='space-y-2'>
              {popularCategories.length === 0 ? (
                <p className='text-sm text-muted-foreground text-center py-4'>
                  暂无分类
                </p>
              ) : (
                popularCategories.map(category => (
                  <button
                    key={category.id}
                    onClick={() => handleCategoryClick(category.slug)}
                    className={`w-full flex items-center justify-between px-4 py-3 rounded-lg transition-all duration-200 ${
                      category.slug === slug
                        ? 'bg-primary text-primary-foreground shadow-sm'
                        : 'hover:bg-muted'
                    }`}
                  >
                    <span className='font-medium'>{category.name}</span>
                    <span className='text-xs px-2 py-0.5 rounded-full bg-muted'>
                      {category.postCount}
                    </span>
                  </button>
                ))
              )}
            </CardContent>
          </Card>

          {/* 分类云 */}
          <Card>
            <CardHeader className='pb-3'>
              <h3 className='text-sm font-semibold text-muted-foreground'>
                快速浏览
              </h3>
            </CardHeader>
            <CardContent>
              <CategoryCloud categories={popularCategories} />
            </CardContent>
          </Card>
        </aside>

        {/* 右侧：文章列表 */}
        <main className='lg:col-span-3 space-y-6'>
          {!currentCategory ? (
            <Card className='p-12'>
              <div className='text-center text-muted-foreground space-y-2'>
                <Folder className='w-12 h-12 mx-auto opacity-50' />
                <p>请选择一个分类查看文章</p>
              </div>
            </Card>
          ) : isLoading ? (
            <div className='flex items-center justify-center py-12'>
              <div className='text-center space-y-4'>
                <div className='animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto' />
                <p className='text-muted-foreground'>加载文章中...</p>
              </div>
            </div>
          ) : !posts || posts.data.length === 0 ? (
            <Card className='p-12'>
              <div className='text-center text-muted-foreground space-y-2'>
                <Folder className='w-12 h-12 mx-auto opacity-50' />
                <p>该分类下暂无文章</p>
              </div>
            </Card>
          ) : (
            <>
              {/* 文章网格 */}
              <div className='grid grid-cols-1 md:grid-cols-2 gap-6'>
                {posts.data.map((post, index) => (
                  <Card
                    key={post.id}
                    onClick={() => handlePostClick(post.slug)}
                    className='group overflow-hidden border border-border hover:border-primary/50 transition-all duration-300 hover:shadow-lg cursor-pointer h-full flex flex-col animate-in fade-in slide-in-from-bottom-4'
                    style={{ animationDelay: `${index * 50}ms` }}
                  >
                    {/* 封面图片 */}
                    <div className='relative h-48 overflow-hidden bg-muted'>
                      <Image
                        src={post.coverImageUrl}
                        alt={post.title}
                        loading='lazy'
                        className='w-full h-full object-cover transition-transform duration-500 group-hover:scale-110'
                        placeholder='暂无封面'
                      />
                      {/* 渐变遮罩 */}
                      <div className='absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300' />
                    </div>

                    <CardHeader className='pb-3'>
                      <h3 className='text-xl font-bold line-clamp-2 group-hover:text-primary transition-colors'>
                        {post.title}
                      </h3>
                    </CardHeader>

                    <CardContent className='flex-1 pb-3'>
                      <p className='text-sm text-muted-foreground line-clamp-3 leading-relaxed'>
                        {post.excerpt || '暂无摘要...'}
                      </p>
                    </CardContent>

                    <CardFooter className='pt-3 border-t border-border/50 flex flex-col gap-3'>
                      {/* 标签 */}
                      {post.tags && post.tags.length > 0 && (
                        <div className='flex flex-wrap gap-2 w-full'>
                          {post.tags.slice(0, 3).map(tag => (
                            <span
                              key={tag.id}
                              className='inline-flex items-center gap-1 px-2 py-1 text-xs rounded-md bg-primary/10 text-primary hover:bg-primary/20 transition-colors'
                              style={{
                                backgroundColor: tag.color
                                  ? `${tag.color}20`
                                  : undefined,
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
                        {post.publishedAt && (
                          <div className='flex items-center gap-1'>
                            <Calendar className='w-3.5 h-3.5' />
                            <time dateTime={post.publishedAt}>
                              {new Date(post.publishedAt).toLocaleDateString('zh-CN', {
                                month: 'short',
                                day: 'numeric',
                              })}
                            </time>
                          </div>
                        )}

                        {post.viewCount !== undefined && (
                          <div className='flex items-center gap-1'>
                            <Eye className='w-3.5 h-3.5' />
                            <span>{post.viewCount}</span>
                          </div>
                        )}
                      </div>
                    </CardFooter>
                  </Card>
                ))}
              </div>

              {/* 分页器 */}
              {posts.totalPages > 1 && (
                <Card className='p-4'>
                  <div className='flex items-center justify-between'>
                    <button
                      onClick={() => handlePageChange(currentPage - 1)}
                      disabled={currentPage === 1}
                      className='inline-flex items-center gap-2 px-4 py-2 rounded-lg border border-border hover:border-primary hover:bg-primary/5 transition-all disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:border-border disabled:hover:bg-transparent'
                    >
                      <ChevronLeft className='w-4 h-4' />
                      上一页
                    </button>

                    <div className='flex items-center gap-2'>
                      {Array.from({ length: posts.totalPages }, (_, i) => i + 1).map(
                        page => {
                          // 只显示当前页附近的页码
                          if (
                            page === 1 ||
                            page === posts.totalPages ||
                            (page >= currentPage - 1 && page <= currentPage + 1)
                          ) {
                            return (
                              <button
                                key={page}
                                onClick={() => handlePageChange(page)}
                                className={`w-10 h-10 rounded-lg font-medium transition-all ${
                                  page === currentPage
                                    ? 'bg-primary text-primary-foreground shadow-sm'
                                    : 'hover:bg-muted'
                                }`}
                              >
                                {page}
                              </button>
                            );
                          }

                          // 显示省略号
                          if (page === currentPage - 2 || page === currentPage + 2) {
                            return (
                              <span key={page} className='px-2 text-muted-foreground'>
                                ...
                              </span>
                            );
                          }

                          return null;
                        }
                      )}
                    </div>

                    <button
                      onClick={() => handlePageChange(currentPage + 1)}
                      disabled={currentPage === posts.totalPages}
                      className='inline-flex items-center gap-2 px-4 py-2 rounded-lg border border-border hover:border-primary hover:bg-primary/5 transition-all disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:border-border disabled:hover:bg-transparent'
                    >
                      下一页
                      <ChevronRight className='w-4 h-4' />
                    </button>
                  </div>

                  <Separator className='my-3' />

                  <div className='text-center text-sm text-muted-foreground'>
                    共 {posts.totalRecords} 篇文章，第 {currentPage} / {posts.totalPages} 页
                  </div>
                </Card>
              )}
            </>
          )}
        </main>
      </div>
    </BlogLayout>
  );
};

export default CategoryPage;
