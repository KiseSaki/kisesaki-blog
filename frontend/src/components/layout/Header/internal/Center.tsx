import React from 'react';
import { headerMenusConfig } from '../config';
import { HeaderMenu } from './Menu';

interface CenterProps {
  frosted: boolean;
  isHeaderHovered: boolean;
}

export const Center: React.FC<CenterProps> = ({ frosted, isHeaderHovered }) => {
  // 当 frosted 为 false 时，或者 frosted 为 true 且悬停时，显示菜单
  const showMenu = !frosted || (frosted && isHeaderHovered);

  return (
    <div className='flex justify-center self-center relative h-10 min-w-[280px]'>
      {/* 菜单 */}
      <div className={`flex  ${showMenu ? 'opacity-100' : 'opacity-0'}`}>
        {headerMenusConfig.map(menu => (
          <div
            key={menu.path}
            className={`flex justify-center self-center ${
              showMenu ? 'opacity-100' : 'opacity-0'
            }`}
          >
            <HeaderMenu label={menu.label} path={menu.path} />
          </div>
        ))}
      </div>
    </div>
  );
};
