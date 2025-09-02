import React from "react";
import { Center } from "./internal/Center";
import { Left } from "./internal/Left";
import { Right } from "./internal/Right";
import { useHeader } from "./useHeader";

export const Header: React.FC = () => {
  const { frosted } = useHeader();
  return (
    <header
      className={
        // 固定高度、固定顶部及过渡
        `h-14 px-3 py-2 fixed top-0 left-0 right-0 z-50 transition-colors duration-200 ` +
        // 根据 frosted 状态切换背景/毛玻璃/阴影/边框
        (frosted
          ? "backdrop-blur-sm bg-white/60 dark:bg-gray-900/60 shadow-sm border-b"
          : "bg-transparent")
      }
    >
      <div className="max-w-7xl mx-auto flex justify-between items-center">
        <Left />
        <Center />
        <Right />
      </div>
    </header>
  );
};
