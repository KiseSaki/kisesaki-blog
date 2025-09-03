import React from "react";
import { Center } from "./internal/Center";
import { Left } from "./internal/Left";
import { Right } from "./internal/Right";
import { useHeader } from "./useHeader";

export const Header: React.FC = () => {
  const {
    frosted,
    isHeaderHovered,
    handleHeaderMouseEnter,
    handleHeaderMouseLeave,
  } = useHeader();

  // 只有在 frosted 状态下且鼠标悬停时才显示磨砂玻璃背景
  const showFrostedBackground = frosted && isHeaderHovered;

  return (
    <header
      className={
        // 固定高度、固定顶部及过渡
        `h-14 px-3 py-2 fixed top-0 left-0 right-0 z-50 transition-colors duration-300 ease-in-out box-border` +
        // 根据状态切换背景/毛玻璃/阴影/边框
        (showFrostedBackground
          ? "backdrop-blur-lg bg-theme-card-background/90 shadow-sm border-b border-theme-border/90"
          : "bg-transparent")
      }
      onMouseEnter={handleHeaderMouseEnter}
      onMouseLeave={handleHeaderMouseLeave}
    >
      <div className="max-w-7xl mx-auto flex justify-between items-center h-full">
        <Left />
        <Center frosted={frosted} isHeaderHovered={isHeaderHovered} />
        <Right />
      </div>
    </header>
  );
};
