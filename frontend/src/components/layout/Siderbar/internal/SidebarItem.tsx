import type { SidebarItemType } from '../types';

interface SidebarItemProps extends SidebarItemType {
  active?: boolean;
  onClick?: (item: SidebarItemType) => void;
}

export const SidebarItem = ({
  id,
  label,
  icon,
  path,
  children,
  disabled,
  active,
  onClick,
  ...rest
}: SidebarItemProps) => {
  const Icon = icon;

  const handleClick = () => {
    onClick?.({ id, label, icon, path, children, disabled, ...rest });
  };

  // 不同状态的样式
  const baseClasses =
    'cursor-pointer p-2 rounded-md transition-colors flex gap-2 items-center';
  const hoverClasses =
    !active && !disabled
      ? 'hover:bg-sidebar-accent hover:text-sidebar-accent-foreground'
      : '';
  const activeClasses = active
    ? 'bg-sidebar-primary text-sidebar-primary-foreground'
    : '';
  const disabledClasses = disabled ? 'opacity-50 cursor-not-allowed' : '';

  return (
    <div
      className={`${baseClasses} ${hoverClasses} ${activeClasses} ${disabledClasses}`}
      onClick={handleClick}
    >
      <Icon className='w-5 h-5' />
      <span>{label}</span>
    </div>
  );
};
