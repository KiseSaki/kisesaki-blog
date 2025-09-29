import { useSidebar } from './hooks/useSidebar';
import { SidebarItem } from './internal/SidebarItem';

interface SidebarProps {
  className?: string;
}

export const Sidebar = ({ className }: SidebarProps) => {
  const { sidebarConfig, activeItemId, handleItemClick } = useSidebar();

  return (
    <div
      className={`${className} relative w-48 p-4 overflow-y-auto
      bg-sidebar text-sidebar-foreground
      after:content-[''] after:block after:w-px after:bg-border after:absolute after:top-14 after:right-0
      `}
    >
      {sidebarConfig &&
        sidebarConfig.groups.map(group => (
          <div className='flex flex-col gap-2' key={group.id}>
            {group.items.map(item => (
              <SidebarItem
                key={item.id}
                {...item}
                active={activeItemId === item.id}
                onClick={handleItemClick}
              />
            ))}
          </div>
        ))}
    </div>
  );
};
