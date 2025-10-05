import type { TagCloudItem } from '@/types';
import { Hash } from 'lucide-react';
import { useNavigate } from 'react-router';

interface TagCloudProps {
  tags: TagCloudItem[];
}

export const TagCloud = ({ tags }: TagCloudProps) => {
  const navigate = useNavigate();

  const handleTagClick = (slug: string) => {
    navigate(`/tag/${slug}`);
  };

  // 计算字体大小和权重基于文章数量
  const getTagStyle = (count: number, maxCount: number) => {
    const ratio = count / maxCount;
    const minSize = 0.875; // 14px
    const maxSize = 1.25; // 20px
    const fontSize = minSize + ratio * (maxSize - minSize);
    
    // 根据权重设置透明度
    const opacity = 0.7 + ratio * 0.3;
    
    return { fontSize, opacity };
  };

  const maxCount = Math.max(...tags.map(tag => tag.postCount));

  return (
    <div className='flex flex-wrap gap-2 items-center'>
      {tags.map(tag => {
        const { fontSize, opacity } = getTagStyle(tag.postCount, maxCount);
        const hasColor = tag.color && tag.color !== '';
        const tagColor = hasColor ? tag.color : undefined;
        
        return (
          <button
            key={tag.id}
            onClick={() => handleTagClick(tag.slug)}
            className='group relative inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-muted/50 hover:bg-primary/10 border border-transparent hover:border-primary/30 transition-all duration-300 cursor-pointer overflow-hidden'
            style={{
              fontSize: `${fontSize}rem`,
              opacity,
            }}
          >
            {/* 背景渐变效果 */}
            {hasColor && tagColor && (
              <div
                className='absolute inset-0 opacity-0 group-hover:opacity-10 transition-opacity duration-300'
                style={{
                  background: `linear-gradient(135deg, ${tagColor}20, ${tagColor}05)`,
                }}
              />
            )}
            
            {/* 图标 */}
            <Hash
              className='transition-all duration-300 group-hover:rotate-12'
              style={{
                width: `${fontSize * 0.875}rem`,
                height: `${fontSize * 0.875}rem`,
                color: tagColor || undefined,
              }}
            />
            
            {/* 标签名 */}
            <span
              className='font-medium transition-all duration-300 group-hover:translate-x-0.5 relative z-10'
              style={{ 
                color: tagColor || undefined,
              }}
            >
              {tag.name}
            </span>
            
            {/* 文章数量徽章 */}
            <span
              className={`ml-1 px-1.5 py-0.5 text-xs rounded-full font-semibold transition-all duration-300 ${
                hasColor 
                  ? 'bg-white/80 group-hover:bg-white' 
                  : 'bg-primary/10 group-hover:bg-primary/20'
              }`}
              style={{
                fontSize: `${fontSize * 0.65}rem`,
                color: tagColor || undefined,
              }}
            >
              {tag.postCount}
            </span>
            
            {/* 悬浮光效 */}
            <div className='absolute inset-0 bg-gradient-to-r from-transparent via-white/5 to-transparent -translate-x-full group-hover:translate-x-full transition-transform duration-700 pointer-events-none' />
          </button>
        );
      })}
    </div>
  );
};
