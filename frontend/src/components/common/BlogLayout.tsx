/**
 * 博客主页布局组件
 * 提供响应式的内容容器和统一的间距管理
 */
export const BlogLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className='container mx-auto px-4 sm:px-6 lg:px-8 xl:px-16 2xl:px-32 py-8 md:py-12 lg:py-16'>
      <div className='max-w-7xl mx-auto flex flex-col space-y-12 md:space-y-16 lg:space-y-20'>
        {children}
      </div>
    </div>
  );
};
