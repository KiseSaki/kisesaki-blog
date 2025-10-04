import { Calendar, GitBranch, Rocket, Star } from 'lucide-react';

interface TimelineEvent {
  date: string;
  title: string;
  description: string;
  type: 'milestone' | 'article' | 'update' | 'achievement';
  icon: React.ReactNode;
}

const timelineEvents: TimelineEvent[] = [
  {
    date: '2025-10',
    title: '博客正式上线',
    description: '基于 React + Spring Boot 构建的全栈博客系统正式发布',
    type: 'milestone',
    icon: <Rocket className='w-5 h-5' />,
  },
  {
    date: '2025-09',
    title: '开始博客开发',
    description: '启动个人技术博客项目，采用云原生架构设计',
    type: 'update',
    icon: <GitBranch className='w-5 h-5' />,
  },
  {
    date: '2025-08',
    title: '技术栈确定',
    description: '确定前端使用 React + TypeScript，后端使用 Spring Boot',
    type: 'update',
    icon: <Star className='w-5 h-5' />,
  },
  {
    date: '2025-07',
    title: '项目筹备',
    description: '开始规划个人博客系统，调研技术方案和架构设计',
    type: 'milestone',
    icon: <Calendar className='w-5 h-5' />,
  },
];

const typeColors = {
  milestone: {
    iconBg: 'bg-primary/10',
    iconColor: 'text-primary',
    border: 'border-primary',
  },
  article: {
    iconBg: 'bg-blue-500/10',
    iconColor: 'text-blue-500',
    border: 'border-blue-500',
  },
  update: {
    iconBg: 'bg-green-500/10',
    iconColor: 'text-green-500',
    border: 'border-green-500',
  },
  achievement: {
    iconBg: 'bg-yellow-500/10',
    iconColor: 'text-yellow-500',
    border: 'border-yellow-500',
  },
};

export const Timeline = () => {
  return (
    <div className='relative'>
      {/* 时间线主线 */}
      <div className='absolute left-8 md:left-1/2 top-0 bottom-0 w-0.5 bg-gradient-to-b from-primary via-primary/50 to-transparent' />

      {/* 时间线事件 */}
      <div className='space-y-12'>
        {timelineEvents.map((event, index) => {
          const colors = typeColors[event.type];
          const isLeft = index % 2 === 0;

          return (
            <div
              key={index}
              className={`relative flex items-center ${
                isLeft ? 'md:flex-row' : 'md:flex-row-reverse'
              } flex-row`}
            >
              {/* 时间线节点 */}
              <div className='absolute left-8 md:left-1/2 -translate-x-1/2 z-10'>
                <div
                  className={`w-12 h-12 rounded-full ${colors.iconBg} ${colors.iconColor} border-4 ${colors.border} bg-background flex items-center justify-center shadow-lg`}
                >
                  {event.icon}
                </div>
              </div>

              {/* 内容卡片 */}
              <div
                className={`ml-24 md:ml-0 w-full md:w-5/12 ${
                  isLeft ? 'md:pr-12' : 'md:pl-12'
                }`}
              >
                <div className='group relative p-6 rounded-xl bg-card border border-border hover:border-primary/50 hover:shadow-lg transition-all duration-300'>
                  {/* 日期标签 */}
                  <div
                    className={`absolute -top-3 ${
                      isLeft ? 'left-6' : 'md:right-6 left-6'
                    } px-3 py-1 rounded-full ${colors.iconBg} ${colors.iconColor} text-sm font-semibold`}
                  >
                    {event.date}
                  </div>

                  {/* 连接线 */}
                  <div
                    className={`hidden md:block absolute top-1/2 ${
                      isLeft ? 'right-0 translate-x-full' : 'left-0 -translate-x-full'
                    } w-12 h-0.5 bg-gradient-to-${isLeft ? 'r' : 'l'} from-border to-transparent`}
                  />

                  {/* 标题和描述 */}
                  <h3 className='text-xl font-bold text-foreground group-hover:text-primary transition-colors mt-2'>
                    {event.title}
                  </h3>
                  <p className='text-sm text-muted-foreground mt-2 leading-relaxed'>
                    {event.description}
                  </p>
                </div>
              </div>

              {/* 占位符（用于保持布局） */}
              <div className='hidden md:block w-5/12' />
            </div>
          );
        })}
      </div>

      {/* 底部渐变 */}
      <div className='absolute left-8 md:left-1/2 bottom-0 w-8 h-8 -translate-x-1/2 rounded-full bg-gradient-to-b from-primary/50 to-transparent blur-md' />
    </div>
  );
};
