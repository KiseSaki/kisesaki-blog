import * as AvatarPrimitive from "@radix-ui/react-avatar";
import * as React from "react";

import { cn, getDeterministicColor } from "@/lib/utils"; // 用于合并 Tailwind CSS 类名的工具函数

// 备用内容组件的属性类型
type AvatarFallbackProps = React.ComponentProps<
  typeof AvatarPrimitive.Fallback
> & {
  identifier?: string;
};

// 1. Avatar 容器组件
// 它作为根元素，包裹图片和备用内容
function Avatar({
  className,
  ...props
}: React.ComponentProps<typeof AvatarPrimitive.Root>) {
  return (
    <AvatarPrimitive.Root
      data-slot="avatar"
      className={cn(
        "relative flex size-8 shrink-0 overflow-hidden rounded-full",
        className
      )}
      {...props}
    />
  );
}

// 2. AvatarImage 图片组件
// 负责显示真实的图片。当图片加载成功时，它会自动隐藏备用内容
function AvatarImage({
  className,
  ...props
}: React.ComponentProps<typeof AvatarPrimitive.Image>) {
  return (
    <AvatarPrimitive.Image
      data-slot="avatar-image"
      className={cn("aspect-square size-full", className)}
      {...props}
    />
  );
}

// 3. AvatarFallback 备用内容组件
// 在图片加载失败或尚未加载完成时显示。通常用来显示用户名的首字母
const AvatarFallback = React.forwardRef<HTMLSpanElement, AvatarFallbackProps>(
  function AvatarFallback({ className, identifier, ...props }, ref) {
    const bg = identifier ? getDeterministicColor(identifier) : "bg-gray-200";
    return (
      <AvatarPrimitive.Fallback
        ref={ref}
        data-slot="avatar-fallback"
        className={cn(
          `${bg} flex size-full items-center justify-center rounded-full text-white`,
          className
        )}
        {...props}
      />
    );
  }
);

export { Avatar, AvatarFallback, AvatarImage };
