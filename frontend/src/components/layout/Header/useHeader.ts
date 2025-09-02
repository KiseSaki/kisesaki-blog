import { useEffect, useRef, useState } from "react";

export const useHeader = (opts?: { scrollThreshold?: number }) => {
  const threshold = opts?.scrollThreshold ?? 10;

  // 上一次滚动位置
  const lastY = useRef(0);
  // 节流锁
  const ticking = useRef(false);

  // 磨砂效果状态
  const [frosted, setFrosted] = useState(false);

  useEffect(() => {
    // 初始化上一次滚动位置
    lastY.current = window.scrollY || 0;

    const onScroll = () => {
      // 如果正在滚动，则返回
      if (ticking.current) return;
      ticking.current = true;

      // 请求动画帧
      requestAnimationFrame(() => {
        const y = window.scrollY || 0; // 当前滚动位置
        const delta = y - lastY.current; // 计算滚动距离
        const isUp = delta < 0; // 是否向上滚动
        // 向上滚动且不在顶部时也显示磨砂效果，或者页面已滚动超过阈值时显示
        const shouldFrost =
          y > threshold || (isUp && y > Math.max(60, threshold));
        setFrosted(shouldFrost);
        // 更新上一次滚动位置
        lastY.current = y;
        // 重置节流锁
        ticking.current = false;
      });
    };

    // 监听滚动事件
    window.addEventListener("scroll", onScroll, { passive: true });

    // 清理函数，useEffect 要求通过返回一个“清理函数”来处理副作用的收尾工作
    return () => window.removeEventListener("scroll", onScroll);
  }, [threshold]);

  return { frosted };
};
