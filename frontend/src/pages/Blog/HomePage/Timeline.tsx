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
    date: '2025-07',
    title: '项目筹备',
    description: '开始规划个人博客系统，调研技术方案和架构设计',
    type: 'milestone',
    icon: <Calendar className='w-5 h-5' />,
  },
  {
    date: '2025-08',
    title: '技术栈确定',
    description: '确定前端使用 React + TypeScript，后端使用 Spring Boot',
    type: 'update',
    icon: <Star className='w-5 h-5' />,
  },
  {
    date: '2025-09',
    title: '开始博客开发',
    description: '启动个人技术博客项目，采用云原生架构设计',
    type: 'update',
    icon: <GitBranch className='w-5 h-5' />,
  },
  {
    date: '2025-10',
    title: '博客正式上线',
    description: '基于 React + Spring Boot 构建的全栈博客系统正式发布',
    type: 'milestone',
    icon: <Rocket className='w-5 h-5' />,
  },
];

const typeColors = {
  milestone: {
    iconBg: 'bg-primary/10',
    iconColor: 'text-primary',
    border: 'border-primary',
    dot: 'bg-primary',
  },
  article: {
    iconBg: 'bg-blue-500/10',
    iconColor: 'text-blue-500',
    border: 'border-blue-500',
    dot: 'bg-blue-500',
  },
  update: {
    iconBg: 'bg-green-500/10',
    iconColor: 'text-green-500',
    border: 'border-green-500',
    dot: 'bg-green-500',
  },
  achievement: {
    iconBg: 'bg-yellow-500/10',
    iconColor: 'text-yellow-500',
    border: 'border-yellow-500',
    dot: 'bg-yellow-500',
  },
};

export const Timeline = () => {
  return (
    <div className='relative overflow-x-auto pb-4'>
      <div className='min-w-max px-4'>
        {/* 时间线主线 */}
        <div className='relative h-2 bg-gradient-to-r from-primary/20 via-primary/50 to-primary/20 rounded-full mb-8' />

        {/* 时间线事件 */}
        <div className='relative flex gap-8 justify-between min-w-max'>
          {timelineEvents.map((event, index) => {
            const colors = typeColors[event.type];

            return (
              <div
                key={index}
                className='relative flex flex-col items-center'
                style={{ minWidth: '280px', maxWidth: '280px' }}
              >
                {/* 连接线和节点 */}
                <div className='absolute -top-[52px] left-1/2 -translate-x-1/2 flex flex-col items-center'>
                  <div className='w-0.5 h-6 bg-gradient-to-b from-transparent to-border' />
                  <div
                    className={`w-10 h-10 rounded-full ${colors.iconBg} ${colors.iconColor} border-3 ${colors.border} bg-background flex items-center justify-center shadow-lg hover:scale-110 transition-transform duration-300 cursor-pointer`}
                  >
                    {event.icon}
                  </div>
                  <div className='w-0.5 h-6 bg-gradient-to-b from-border to-transparent' />
                </div>

                {/* 内容卡片 */}
                <div className='group relative p-5 rounded-xl bg-card border border-border hover:border-primary/50 hover:shadow-lg transition-all duration-300 w-full mt-8'>
                  {/* 日期标签 */}
                  <div
                    className={`absolute -top-3 left-1/2 -translate-x-1/2 px-4 py-1 rounded-full ${colors.iconBg} ${colors.iconColor} text-xs font-semibold whitespace-nowrap shadow-sm`}
                  >
                    {event.date}
                  </div>

                  {/* 标题和描述 */}
                  <h3 className='text-base font-bold text-foreground group-hover:text-primary transition-colors text-center mt-2'>
                    {event.title}
                  </h3>
                  <p className='text-xs text-muted-foreground mt-2 leading-relaxed text-center'>
                    {event.description}
                  </p>
                </div>

                {/* 进度指示器 */}
                <div
                  className={`absolute -top-[46px] left-1/2 -translate-x-1/2 w-3 h-3 rounded-full ${colors.dot} shadow-md`}
                />
              </div>
            );
          })}
        </div>
      </div>

      {/* 滚动提示 */}
      <div className='absolute right-0 top-0 bottom-0 w-12 bg-gradient-to-l from-background to-transparent pointer-events-none md:hidden' />
    </div>
  );
};
