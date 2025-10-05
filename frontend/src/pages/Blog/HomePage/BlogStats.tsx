import { BookOpen, Eye, Heart, MessageCircle } from 'lucide-react';
import { useEffect, useState } from 'react';

interface StatItem {
  label: string;
  value: number;
  icon: React.ReactNode;
  color: string;
  bgColor: string;
  suffix?: string;
}

export const BlogStats = () => {
  // TODO: 从 API 获取真实数据
  const [stats, setStats] = useState<StatItem[]>([
    {
      label: '文章总数',
      value: 0,
      icon: <BookOpen className='w-8 h-8' />,
      color: 'text-blue-500',
      bgColor: 'bg-blue-500/10',
    },
    {
      label: '总浏览量',
      value: 0,
      icon: <Eye className='w-8 h-8' />,
      color: 'text-green-500',
      bgColor: 'bg-green-500/10',
      suffix: '+',
    },
    {
      label: '总点赞数',
      value: 0,
      icon: <Heart className='w-8 h-8' />,
      color: 'text-red-500',
      bgColor: 'bg-red-500/10',
    },
    {
      label: '总评论数',
      value: 0,
      icon: <MessageCircle className='w-8 h-8' />,
      color: 'text-purple-500',
      bgColor: 'bg-purple-500/10',
    },
  ]);

  useEffect(() => {
    // 模拟数据加载和数字动画
    const targetValues = [42, 12580, 356, 189];
    const animationDuration = 1500; // ms
    const steps = 60;
    const stepDuration = animationDuration / steps;

    let currentStep = 0;
    const timer = setInterval(() => {
      currentStep++;
      const progress = currentStep / steps;

      setStats(prevStats =>
        prevStats.map((stat, index) => ({
          ...stat,
          value: Math.floor(targetValues[index] * progress),
        }))
      );

      if (currentStep >= steps) {
        clearInterval(timer);
        // 确保最终值准确
        setStats(prevStats =>
          prevStats.map((stat, index) => ({
            ...stat,
            value: targetValues[index],
          }))
        );
      }
    }, stepDuration);

    return () => clearInterval(timer);
  }, []);

  return (
    <div className='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6'>
      {stats.map((stat, index) => (
        <div
          key={stat.label}
          className='group relative overflow-hidden rounded-xl bg-card border border-border p-6 hover:border-primary/50 hover:shadow-lg transition-all duration-300'
          style={{
            animationDelay: `${index * 100}ms`,
          }}
        >
          {/* 背景装饰 */}
          <div
            className={`absolute top-0 right-0 w-24 h-24 ${stat.bgColor} rounded-full blur-2xl opacity-50 group-hover:opacity-70 transition-opacity -translate-y-8 translate-x-8`}
          />

          {/* 内容 */}
          <div className='relative z-10 flex flex-col gap-4'>
            <div className='flex items-center justify-between'>
              <div className={`${stat.color} ${stat.bgColor} p-3 rounded-lg`}>
                {stat.icon}
              </div>
              <span className='text-xs text-muted-foreground'>{stat.label}</span>
            </div>

            <div className='flex items-baseline gap-1'>
              <span className='text-3xl font-bold text-foreground'>
                {stat.value.toLocaleString()}
              </span>
              {stat.suffix && (
                <span className='text-lg text-muted-foreground'>{stat.suffix}</span>
              )}
            </div>
          </div>

          {/* 悬浮效果 */}
          <div className='absolute inset-0 bg-gradient-to-r from-primary/0 via-primary/5 to-primary/0 opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none' />
        </div>
      ))}
    </div>
  );
};
