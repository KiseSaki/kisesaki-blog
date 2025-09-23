/**
 * 主题切换按钮组件
 * 提供明亮/暗黑主题切换功能
 */

import { useTheme } from '@/stores/themeStore';
import { MoonOutlined, SunOutlined } from '@ant-design/icons';
import { Button } from 'antd';
import React from 'react';

interface ThemeToggleProps {
  className?: string;
  size?: 'small' | 'middle' | 'large';
  type?: 'default' | 'primary' | 'dashed' | 'link' | 'text';
  shape?: 'default' | 'circle' | 'round';
}

/**
 * 主题切换按钮组件
 */
export const ThemeToggle: React.FC<ThemeToggleProps> = ({
  className,
  size = 'middle',
  type = 'text',
  shape = 'circle',
}) => {
  const { mode, toggleTheme } = useTheme();

  return (
    <Button
      className={className}
      size={size}
      type={type}
      shape={shape}
      icon={mode === 'dark' ? <SunOutlined /> : <MoonOutlined />}
      onClick={toggleTheme}
      title={mode === 'dark' ? '切换到明亮主题' : '切换到暗黑主题'}
    />
  );
};
