import React from 'react';
import { Avatar, AvatarFallback, AvatarImage } from '../ui';

interface UserAvatarProps
  extends React.ComponentPropsWithoutRef<typeof Avatar> {
  src?: string;
  name?: string;
}

/**
 * 通用用户头像组件
 * - forwardRef 支持 Radix asChild / trigger 场景
 * - 统一 AvatarImage / AvatarFallback 行为
 * - 完整传递所有 props，确保与 HoverCard 等 Radix 组件兼容
 */
const UserAvatar = React.forwardRef<HTMLDivElement, UserAvatarProps>(
  ({ src, name, className, ...props }, ref) => {
    return (
      <Avatar className={`${className} cursor-pointer`} ref={ref} {...props}>
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
