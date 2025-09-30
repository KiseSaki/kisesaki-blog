import { ChevronLeftIcon } from '@/components/icons';
import { cn } from '@/lib';
import type { CSSProperties } from 'react';
import { useSidebar } from './hooks/useSidebar';
import { SidebarItem } from './internal/SidebarItem';

interface SidebarProps {
  className?: string;
}

export const Sidebar = ({ className }: SidebarProps) => {
  const {
    sidebarConfig,
    activeItemId,
    handleItemClick,
    toggleCollapsed,
    collapsed,
    width,
    collapsedWidth,
  } = useSidebar();

  // 动态计算宽度
  const dynamicWidth = collapsed ? collapsedWidth : width;
  const containerClasses = cn(
    'relative flex flex-col overflow-y-auto overflow-x-hidden border-r bg-sidebar text-sidebar-foreground border-border transition-all duration-300 ease-out',
    collapsed ? 'shadow-sm' : 'shadow-lg',
    className
  );
  const sidebarStyle: CSSProperties = {
    width: `${dynamicWidth}px`,
    minWidth: `${dynamicWidth}px`,
    transition:
      'width 320ms cubic-bezier(0.4, 0, 0.2, 1), min-width 320ms cubic-bezier(0.4, 0, 0.2, 1), box-shadow 220ms ease',
  };

  return (
    <div className={containerClasses} style={sidebarStyle}>
      {/* 折叠控制按钮 */}
      <div
        className={`sticky top-0 z-10 bg-sidebar border-b border-border/50 transition-[padding] duration-300 ease-out
          ${collapsed ? 'p-2' : 'p-4'}
        `}
      >
        <div
          className={`flex items-center cursor-pointer rounded-md p-2 justify-start
            hover:bg-sidebar-accent hover:text-sidebar-accent-foreground
            transition-[background-color,color,box-shadow] duration-300 ease-out
          `}
          onClick={toggleCollapsed}
          title={collapsed ? '展开侧边栏' : '折叠侧边栏'}
          style={{
            paddingLeft: collapsed ? 'calc((100% - 1.25rem) / 2)' : '0.5rem',
            paddingRight: collapsed ? 'calc((100% - 1.25rem) / 2)' : '0.5rem',
            transition:
              'padding 260ms cubic-bezier(0.4, 0, 0.2, 1), background-color 200ms ease, color 200ms ease',
            transitionDelay: collapsed ? '80ms' : '0ms',
          }}
        >
          {/* 图标包装器 - 和菜单项保持一致 */}
          <div
            className='flex-shrink-0 transition-[transform,margin] duration-300 ease-out'
            style={{
              transition:
                'transform 280ms cubic-bezier(0.4, 0, 0.2, 1), margin 280ms cubic-bezier(0.4, 0, 0.2, 1)',
              transform: collapsed ? 'translateX(0)' : 'translateX(0)',
              marginRight: collapsed ? 0 : '0.5rem',
              marginLeft: collapsed ? 0 : 0,
              transitionDelay: collapsed ? '100ms' : '30ms',
            }}
          >
            <ChevronLeftIcon
              className={`w-5 h-5 flex-shrink-0 transition-transform duration-300 ease-in-out ${collapsed ? 'rotate-180' : 'rotate-0'}`}
            />
          </div>

          {/* 按钮文字 - 和菜单项保持一致的动画 */}
          <span
            className={`overflow-hidden whitespace-nowrap transition-[opacity,max-width,transform] duration-300 ease-out ${
              collapsed
                ? 'max-w-0 opacity-0 -translate-y-1'
                : 'max-w-[160px] opacity-100 translate-y-0'
            }`}
            style={{
              transitionDelay: collapsed ? '0ms' : '80ms',
            }}
          >
            <span className='text-sm font-medium'>折叠侧边栏</span>
          </span>
        </div>
      </div>

      {/* 导航菜单 */}
      <div
        className={cn(
          'min-w-0 transition-[padding] duration-300 ease-out',
          collapsed ? 'p-2' : 'p-4'
        )}
      >
        {sidebarConfig &&
          sidebarConfig.groups.map((group, groupIndex) => {
            const hasLabel = Boolean(group.label);
            const labelDelay = collapsed ? '0ms' : '90ms';
            const itemsDelay = collapsed
              ? hasLabel
                ? '90ms'
                : '45ms'
              : '45ms';

            return (
              <div
                key={group.id}
                className={cn(
                  'transition-[margin] duration-300 ease-out',
                  groupIndex > 0 && (collapsed ? 'mt-4' : 'mt-6')
                )}
              >
                {/* 分组标题容器 - 使用固定高度确保平滑动画 */}
                {group.label && (
                  <div
                    className={cn(
                      'overflow-hidden transition-[max-height,opacity,transform,margin] duration-300 ease-out',
                      collapsed
                        ? 'max-h-0 opacity-0 -translate-y-2 mb-0'
                        : 'max-h-8 opacity-100 translate-y-0 mb-3'
                    )}
                    style={{ transitionDelay: labelDelay }}
                  >
                    <div className='h-6 flex items-center'>
                      <h3 className='text-xs font-semibold text-muted-foreground uppercase tracking-wider px-2 whitespace-nowrap'>
                        {group.label}
                      </h3>
                    </div>
                  </div>
                )}

                {/* 分组分隔线容器 - 折叠状态下使用，确保平滑切换 */}
                {groupIndex > 0 && (
                  <div
                    className={cn(
                      'overflow-hidden transition-[max-height,opacity,margin] duration-300 ease-out',
                      collapsed
                        ? 'max-h-4 opacity-100 my-3'
                        : 'max-h-0 opacity-0 my-0'
                    )}
                  >
                    <div className='h-px bg-border mx-2'></div>
                  </div>
                )}

                {/* 菜单项 */}
                <div
                  className='space-y-1 transition-[transform,padding] duration-300 ease-out'
                  style={{
                    paddingTop: hasLabel ? (collapsed ? 0 : 4) : 0,
                    transform:
                      collapsed && hasLabel
                        ? 'translateY(-4px)'
                        : 'translateY(0)',
                    transitionDelay: itemsDelay,
                  }}
                >
                  {group.items.map(item => (
                    <SidebarItem
                      key={item.id}
                      {...item}
                      active={activeItemId === item.id}
                      collapsed={collapsed}
                      onClick={handleItemClick}
                    />
                  ))}
                </div>
              </div>
            );
          })}
      </div>

      {/* 底部空间 - 防止内容被遮挡 */}
      <div className='h-4'></div>
    </div>
  );
};
