import { useEffect, useRef, useState } from "react";

export const useHeader = (opts?: { scrollThreshold?: number }) => {
  const threshold = opts?.scrollThreshold ?? 10;

  // 上一次滚动位置
  const lastY = useRef(0);
  // 节流锁
  const ticking = useRef(false);

  // 磨砂效果状态
  const [frosted, setFrosted] = useState(false);
  // Header 悬停状态
  const [isHeaderHovered, setIsHeaderHovered] = useState(false);

  useEffect(() => {
    // 初始化上一次滚动位置
    lastY.current = window.scrollY || 0;

    const onScroll = (e: Event) => {
      // 如果正在滚动，则返回
      if (ticking.current) return;
      ticking.current = true;

      // 请求动画帧
      requestAnimationFrame(() => {
        const target = e.target as HTMLElement | Window;
        const y =
          target === window
            ? window.pageYOffset
            : (target as HTMLElement).scrollTop || 0;
        // 页面已滚动超过阈值时显示磨砂效果
        const shouldFrost = y > threshold;
        setFrosted(shouldFrost);
        // 更新上一次滚动位置
        lastY.current = y;
        // 重置节流锁
        ticking.current = false;
      });
    };

    // 监听滚动事件
    window.addEventListener("scroll", onScroll, {
      passive: true,
      capture: true,
    });

    // 清理函数，useEffect 要求通过返回一个“清理函数”来处理副作用的收尾工作
    return () => window.removeEventListener("scroll", onScroll, true);
  }, [threshold]);

  const handleHeaderMouseEnter = () => {
    setIsHeaderHovered(true);
  };

  const handleHeaderMouseLeave = () => {
    setIsHeaderHovered(false);
  };

  return {
    frosted,
    isHeaderHovered,
    handleHeaderMouseEnter,
    handleHeaderMouseLeave,
  };
};
