import { cn } from '@/lib';
import type { CSSProperties } from 'react';
import type { SidebarItemType } from '../types';

/**
 * SidebarItem 组件的属性接口，扩展自 SidebarItemType。
 * @interface SidebarItemProps
 * @extends SidebarItemType
 * @property {boolean} [active] - 是否为激活状态。
 * @property {boolean} [collapsed] - 是否为折叠状态。
 * @property {(item: SidebarItemType) => void} [onClick] - 点击事件处理函数。
 */
interface SidebarItemProps extends SidebarItemType {
  active?: boolean;
  collapsed?: boolean;
  onClick?: (item: SidebarItemType) => void;
}

/**
 * SidebarItem 组件，用于渲染侧边栏中的单个项。
 * 支持激活、禁用、折叠状态，并提供悬浮提示和动画过渡。
 * @param {SidebarItemProps} props - 组件属性。
 * @returns {JSX.Element} 渲染的侧边栏项元素。
 */
export const SidebarItem = ({
  id,
  label,
  icon,
  path,
  children,
  disabled,
  active,
  collapsed = false,
  onClick,
  ...rest
}: SidebarItemProps) => {
  const Icon = icon;

  // 处理点击事件，如果禁用则不执行
  const handleClick = () => {
    if (disabled) return;
    onClick?.({ id, label, icon, path, children, disabled, ...rest });
  };

  // 基础样式类
  const baseClasses =
    'cursor-pointer rounded-md flex items-center relative group transition-[background-color,color,box-shadow] duration-300 ease-out py-2';

  // 悬浮样式类
  const hoverClasses =
    !active && !disabled
      ? 'hover:bg-sidebar-accent hover:text-sidebar-accent-foreground'
      : '';

  // 激活样式类
  const activeClasses = active
    ? 'bg-sidebar-primary text-sidebar-primary-foreground'
    : '';

  // 禁用样式类
  const disabledClasses = disabled ? 'opacity-50 cursor-not-allowed' : '';

  // 折叠状态下的样式调整
  const collapsedClasses = collapsed ? 'justify-start' : 'justify-start';

  // 容器样式
  const containerStyle: CSSProperties = {
    paddingLeft: collapsed ? 'calc((100% - 1.25rem) / 2)' : '0.5rem',
    paddingRight: collapsed ? 'calc((100% - 1.25rem) / 2)' : '0.5rem',
    transition:
      'padding 260ms cubic-bezier(0.4, 0, 0.2, 1), background-color 200ms ease, color 200ms ease',
    transitionDelay: collapsed ? '80ms' : '0ms',
  };

  // 图标包装器样式
  const iconWrapperStyle: CSSProperties = {
    transition:
      'transform 280ms cubic-bezier(0.4, 0, 0.2, 1), margin 280ms cubic-bezier(0.4, 0, 0.2, 1)',
    transform: collapsed ? 'translateX(0)' : 'translateX(0)',
    marginRight: collapsed ? 0 : '0.5rem',
    marginLeft: collapsed ? 0 : 0,
    transitionDelay: collapsed ? '100ms' : '30ms',
  };

  // 图标包装器类
  const iconWrapperClasses = cn(
    'flex-shrink-0 transition-[transform,margin] duration-300 ease-out',
    collapsed ? 'translate-x-0' : 'translate-x-0'
  );

  // 标签样式
  const labelStyle: CSSProperties = {
    transitionDelay: collapsed ? '0ms' : '80ms',
  };

  // 标签包装器类
  const labelWrapperClasses = cn(
    'overflow-hidden whitespace-nowrap transition-[opacity,max-width,transform] duration-300 ease-out',
    collapsed
      ? 'max-w-0 opacity-0 -translate-y-1'
      : 'max-w-[160px] opacity-100 translate-y-0'
  );

  return (
    <div
      className={`${baseClasses} ${hoverClasses} ${activeClasses} ${disabledClasses} ${collapsedClasses}`}
      onClick={handleClick}
      title={collapsed ? label : ''} // 折叠状态下显示tooltip
      style={containerStyle}
    >
      <div className={iconWrapperClasses} style={iconWrapperStyle}>
        <Icon className='w-5 h-5 flex-shrink-0' />
      </div>

      {/* 标签文字 - 根据折叠状态显示/隐藏 */}
      <span className={labelWrapperClasses} style={labelStyle}>
        {label}
      </span>

      {/* 折叠状态下的悬浮提示 */}
      {collapsed && (
        <div
          className={`absolute left-full ml-2 px-2 py-1 bg-popover text-popover-foreground text-sm rounded-md 
            shadow-md border opacity-0 invisible 
            group-hover:opacity-100 group-hover:visible z-50 pointer-events-none whitespace-nowrap`}
        >
          {label}
        </div>
      )}
    </div>
  );
};
