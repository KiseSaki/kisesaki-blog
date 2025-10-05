import { Code2, Database, Globe, Server } from 'lucide-react';

interface TechItem {
  name: string;
  icon: React.ReactNode;
  description: string;
  color: string;
}

const techStacks: { category: string; items: TechItem[] }[] = [
  {
    category: '前端技术',
    items: [
      {
        name: 'React',
        icon: <Globe className='w-6 h-6' />,
        description: '现代化的前端框架',
        color: 'text-blue-500',
      },
      {
        name: 'TypeScript',
        icon: <Code2 className='w-6 h-6' />,
        description: '类型安全的 JavaScript',
        color: 'text-blue-600',
      },
      {
        name: 'TailwindCSS',
        icon: <Globe className='w-6 h-6' />,
        description: '原子化 CSS 框架',
        color: 'text-cyan-500',
      },
    ],
  },
  {
    category: '后端技术',
    items: [
      {
        name: 'Spring Boot',
        icon: <Server className='w-6 h-6' />,
        description: 'Java 企业级框架',
        color: 'text-green-600',
      },
      {
        name: 'PostgreSQL',
        icon: <Database className='w-6 h-6' />,
        description: '强大的关系型数据库',
        color: 'text-blue-700',
      },
      {
        name: 'Redis',
        icon: <Database className='w-6 h-6' />,
        description: '高性能缓存数据库',
        color: 'text-red-500',
      },
    ],
  },
];

export const TechStack = () => {
  return (
    <div className='grid grid-cols-1 md:grid-cols-2 gap-8'>
      {techStacks.map(stack => (
        <div key={stack.category} className='space-y-4'>
          <h3 className='text-xl font-semibold text-foreground mb-4'>
            {stack.category}
          </h3>
          <div className='space-y-3'>
            {stack.items.map(tech => (
              <div
                key={tech.name}
                className='group flex items-start gap-4 p-4 rounded-lg bg-card border border-border hover:border-primary/50 hover:bg-primary/5 transition-all duration-300 cursor-pointer'
              >
                <div
                  className={`${tech.color} group-hover:scale-110 transition-transform duration-300`}
                >
                  {tech.icon}
                </div>
                <div className='flex-1'>
                  <h4 className='font-semibold text-foreground group-hover:text-primary transition-colors'>
                    {tech.name}
                  </h4>
                  <p className='text-sm text-muted-foreground mt-1'>
                    {tech.description}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};
