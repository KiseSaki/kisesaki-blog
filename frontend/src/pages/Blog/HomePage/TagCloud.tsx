import type { TagCloudItem } from '@/types';
import { Hash } from 'lucide-react';

interface TagCloudProps {
  tags: TagCloudItem[];
}

export const TagCloud = ({ tags }: TagCloudProps) => {
  // 计算字体大小基于文章数量
  const getTagSize = (count: number, maxCount: number) => {
    const minSize = 0.75; // rem
    const maxSize = 1.5; // rem
    const ratio = count / maxCount;
    return minSize + ratio * (maxSize - minSize);
  };

  const maxCount = Math.max(...tags.map(tag => tag.postCount));

  return (
    <div className='flex flex-wrap gap-3 items-center'>
      {tags.map(tag => {
        const fontSize = getTagSize(tag.postCount, maxCount);
        return (
          <button
            key={tag.id}
            className='group inline-flex items-center gap-1.5 px-3 py-1.5 rounded-md hover:bg-primary/10 transition-all duration-300 cursor-pointer'
            style={{
              fontSize: `${fontSize}rem`,
            }}
            onClick={() => {
              // TODO: 导航到标签页面
              console.log('Navigate to tag:', tag.slug);
            }}
          >
            <Hash
              className='text-muted-foreground group-hover:text-primary transition-colors'
              style={{
                width: `${fontSize * 0.875}rem`,
                height: `${fontSize * 0.875}rem`,
              }}
            />
            <span
              className='font-medium group-hover:text-primary transition-colors'
              style={{ color: tag.color || undefined }}
            >
              {tag.name}
            </span>
            <span
              className='text-xs text-muted-foreground'
              style={{ fontSize: `${fontSize * 0.7}rem` }}
            >
              ({tag.postCount})
            </span>
          </button>
        );
      })}
    </div>
  );
};
