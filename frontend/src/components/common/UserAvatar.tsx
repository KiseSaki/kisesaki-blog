import React from 'react';
import { Avatar, AvatarFallback, AvatarImage } from '../ui';

interface UserAvatarProps {
  src?: string;
  name?: string;
  className?: string;
  onClick?: () => void;
}

/**
 * 通用用户头像组件
 * - forwardRef 支持 Radix asChild / trigger 场景
 * - 统一 AvatarImage / AvatarFallback 行为
 */
const UserAvatar = React.forwardRef<HTMLDivElement, UserAvatarProps>(
  ({ src, name, className, onClick }: UserAvatarProps, ref) => {
    return (
      <Avatar
        className={`${className} cursor-pointer`}
        onClick={onClick}
        ref={ref}
      >
        <AvatarImage src={src} alt={name ?? 'avatar'} />
        <AvatarFallback identifier={name}>
          {name ? name[0] : 'U'}
        </AvatarFallback>
      </Avatar>
    );
  }
);

// 设置 displayName 以便在调试时更容易识别组件
UserAvatar.displayName = 'UserAvatar';

export default UserAvatar;
