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

  return (
    <div
      className={`flex gap-2 cursor-pointer p-2 rounded-md transition-colors
        hover:bg-sidebar-accent hover:text-sidebar-accent-foreground
        ${active ? 'bg-sidebar-primary text-sidebar-primary-foreground' : ''}
        ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
      `}
      onClick={handleClick}
    >
      <Icon />
      <span>{label}</span>
    </div>
  );
};
