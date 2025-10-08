/**
 * 主题提供者组件
 * 为整个应用提供主题支持，包括 Ant Design 主题配置
 */

import { getAntdTheme } from '@/config/theme';
import { useThemeStore } from '@/stores/themeStore';
import { ConfigProvider, theme } from 'antd';
import React, { useMemo } from 'react';

interface ThemeProviderProps {
  children: React.ReactNode;
}

/**
 * 主题提供者组件
 */
export const ThemeProvider: React.FC<ThemeProviderProps> = ({ children }) => {
  const { mode, applyTheme } = useThemeStore();

  // 初始化时应用主题 - 使用 useLayoutEffect 确保在渲染前同步执行
  React.useLayoutEffect(() => {
    applyTheme();
  }, [applyTheme]);

  // Ant Design 主题配置
  const antdTheme = useMemo(() => {
    const themeConfig = getAntdTheme(mode);

    return {
      ...themeConfig,
      algorithm: mode === 'dark' ? theme.darkAlgorithm : theme.defaultAlgorithm,
    };
  }, [mode]);

  return <ConfigProvider theme={antdTheme}>{children}</ConfigProvider>;
};
