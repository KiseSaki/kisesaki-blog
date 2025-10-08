import { Card, CardContent, CardHeader, TagCloud } from '@/components';
import type { TagCloudItem } from '@/types';
import { BarChart3, Hash } from 'lucide-react';

interface TagSidebarProps {
  tags: TagCloudItem[];
}

/**
 * 标签侧边栏组件
 * 展示标签云和标签统计信息
 */
export const TagSidebar = ({ tags }: TagSidebarProps) => {
  // 计算标签统计
  const totalTags = tags.length;
  const totalPosts = tags.reduce((sum, tag) => sum + tag.postCount, 0);
  const avgPostsPerTag =
    totalTags > 0 ? (totalPosts / totalTags).toFixed(1) : 0;

  return (
    <aside className='lg:col-span-1 space-y-4'>
      {/* 标签云 */}
      <Card className='sticky top-20'>
        <CardHeader className='pb-3'>
          <h2 className='text-lg font-semibold flex items-center gap-2'>
            <Hash className='w-5 h-5 text-primary' />
            标签云
          </h2>
        </CardHeader>
        <CardContent>
          <TagCloud tags={tags} />
        </CardContent>
      </Card>

      {/* 标签统计 */}
      <Card>
        <CardHeader className='pb-3'>
          <h3 className='text-sm font-semibold text-muted-foreground flex items-center gap-2'>
            <BarChart3 className='w-4 h-4' />
            标签统计
          </h3>
        </CardHeader>
        <CardContent className='space-y-3'>
          <div className='flex items-center justify-between py-2 border-b border-border/50'>
            <span className='text-sm text-muted-foreground'>标签总数</span>
            <span className='font-semibold text-primary'>{totalTags}</span>
          </div>
          <div className='flex items-center justify-between py-2 border-b border-border/50'>
            <span className='text-sm text-muted-foreground'>文章总数</span>
            <span className='font-semibold text-primary'>{totalPosts}</span>
          </div>
          <div className='flex items-center justify-between py-2'>
            <span className='text-sm text-muted-foreground'>平均文章数</span>
            <span className='font-semibold text-primary'>{avgPostsPerTag}</span>
          </div>
        </CardContent>
      </Card>
    </aside>
  );
};
