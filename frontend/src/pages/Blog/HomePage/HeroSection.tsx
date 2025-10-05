import { Button } from '@/components';
import { BLOG_ARCHIVE_LINK } from '@/config';
import { ArrowRight, Github, Mail } from 'lucide-react';
import { ReactTyped } from 'react-typed';
import { useNavigate } from 'react-router';

export const HeroSection = () => {
  const navigate = useNavigate();

  const handleExploreClick = () => {
    navigate(BLOG_ARCHIVE_LINK);
  };

  const handleGithubClick = () => {
    window.open('https://github.com/KiseSaki', '_blank');
  };

  const handleContactClick = () => {
    navigate('/contact');
  };

  return (
    <section className='relative h-[500px] md:h-[600px] lg:h-[700px] flex items-center justify-center overflow-hidden rounded-xl md:rounded-2xl bg-gradient-to-br from-primary/10 via-background to-secondary/10'>
      {/* 背景装饰 */}
      <div className='absolute inset-0 bg-grid-white/10 [mask-image:radial-gradient(ellipse_at_center,transparent_20%,black)]' />

      {/* 动态背景光效 */}
      <div className='absolute top-1/4 -left-20 w-72 h-72 bg-primary/30 rounded-full blur-3xl animate-pulse' />
      <div className='absolute bottom-1/4 -right-20 w-96 h-96 bg-secondary/20 rounded-full blur-3xl animate-pulse delay-700' />

      {/* 主要内容 */}
      <div className='relative z-10 text-center space-y-6 md:space-y-8 px-4 max-w-4xl mx-auto'>
        {/* 标题区域 */}
        <div className='space-y-3 md:space-y-4'>
          <h1 className='text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary animate-in fade-in slide-in-from-bottom-4 duration-1000'>
            Hi, I'm <span className='text-primary'>KiseSaki</span>
          </h1>

          {/* 打字机效果 */}
          <div className='text-xl sm:text-2xl md:text-3xl text-muted-foreground h-10 md:h-12 flex items-center justify-center animate-in fade-in slide-in-from-bottom-4 duration-1000 delay-300'>
            <ReactTyped
              strings={['全栈开发', '云开发', '终身学习']}
              typeSpeed={80}
              backSpeed={50}
              backDelay={2000}
              loop
              className='font-semibold'
            />
          </div>
        </div>

        {/* 描述文字 */}
        <p className='text-base md:text-lg lg:text-xl text-muted-foreground max-w-2xl mx-auto leading-relaxed animate-in fade-in slide-in-from-bottom-4 duration-1000 delay-500'>
          欢迎来到我的技术博客！在这里分享关于前端、后端、云原生等领域的技术文章和实践经验。
        </p>

        {/* 行动按钮 */}
        <div className='flex flex-col sm:flex-row gap-3 md:gap-4 justify-center items-center animate-in fade-in slide-in-from-bottom-4 duration-1000 delay-700'>
          <Button onClick={handleExploreClick} size='lg' className='group gap-2 w-full sm:w-auto'>
            探索文章
            <ArrowRight className='w-4 h-4 group-hover:translate-x-1 transition-transform' />
          </Button>
          <Button onClick={handleGithubClick} size='lg' variant='outline' className='gap-2 w-full sm:w-auto'>
            <Github className='w-4 h-4' />
            GitHub
          </Button>
          <Button onClick={handleContactClick} size='lg' variant='outline' className='gap-2 w-full sm:w-auto'>
            <Mail className='w-4 h-4' />
            联系我
          </Button>
        </div>
      </div>

      {/* 滚动提示 */}
      <div className='absolute bottom-6 md:bottom-8 left-1/2 -translate-x-1/2 animate-bounce'>
        <div className='w-5 h-8 md:w-6 md:h-10 border-2 border-muted-foreground rounded-full flex justify-center'>
          <div className='w-1 h-2 md:w-1.5 md:h-3 bg-muted-foreground rounded-full mt-2 animate-pulse' />
        </div>
      </div>
    </section>
  );
};
