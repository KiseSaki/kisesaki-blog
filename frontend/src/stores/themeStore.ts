/**
 * 主题状态管理 Store
 * 管理主题切换、持久化存储等功能
 */

import type { ThemeColors, ThemeMode } from '@/config/theme';
import { themes } from '@/config/theme';
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface ThemeState {
  /** 当前主题模式 */
  mode: ThemeMode;
  /** 当前主题颜色配置 */
  colors: ThemeColors;
  /** 切换主题 */
  toggleTheme: () => void;
  /** 设置主题 */
  setTheme: (mode: ThemeMode) => void;
  /** 应用主题到 DOM */
  applyTheme: () => void;
}

/**
 * 主题 Store
 */
export const useThemeStore = create<ThemeState>()(
  persist(
    (set, get) => ({
      mode: 'light',
      colors: themes.light,
      
      toggleTheme: () => {
        const currentMode = get().mode;
        const newMode: ThemeMode = currentMode === 'light' ? 'dark' : 'light';
        set({
          mode: newMode,
          colors: themes[newMode],
        });
        get().applyTheme();
      },
      
      setTheme: (mode: ThemeMode) => {
        set({
          mode,
          colors: themes[mode],
        });
        get().applyTheme();
      },
      
      applyTheme: () => {
        const { mode, colors } = get();
        const root = document.documentElement;
        
        // 设置 CSS 自定义属性
        root.style.setProperty('--theme-background', colors.background);
        root.style.setProperty('--theme-card-background', colors.cardBackground);
        root.style.setProperty('--theme-navbar-background', colors.navbarBackground);
        root.style.setProperty('--theme-primary-text', colors.primaryText);
        root.style.setProperty('--theme-secondary-text', colors.secondaryText);
        root.style.setProperty('--theme-primary', colors.primary);
        root.style.setProperty('--theme-primary-hover', colors.primaryHover);
        root.style.setProperty('--theme-border', colors.border);
        root.style.setProperty('--theme-divider', colors.divider);
        root.style.setProperty('--theme-shadow', colors.shadow);
        root.style.setProperty('--theme-success', colors.success);
        root.style.setProperty('--theme-warning', colors.warning);
        root.style.setProperty('--theme-error', colors.error);
        root.style.setProperty('--theme-info', colors.info);
        
        // 设置 dark class 用于 Tailwind CSS
        if (mode === 'dark') {
          root.classList.add('dark');
        } else {
          root.classList.remove('dark');
        }
        
        // 设置主题属性用于其他样式
        root.setAttribute('data-theme', mode);
      },
    }),
    {
      name: 'theme-storage',
    }
  )
);

/**
 * 主题 Hook
 * 提供便捷的主题操作方法
 */
export const useTheme = () => {
  const { mode, colors, toggleTheme, setTheme } = useThemeStore();
  
  return {
    mode,
    colors,
    toggleTheme,
    setTheme,
    isDark: mode === 'dark',
    isLight: mode === 'light',
  };
};
