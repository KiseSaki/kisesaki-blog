/**
 * 主题配置文件
 * 定义明亮和暗黑两套主题的颜色系统
 */

export type ThemeMode = 'light' | 'dark';

/**
 * 主题颜色配置
 */
export interface ThemeColors {
  // 背景色
  background: string;
  cardBackground: string;
  navbarBackground: string;

  // 文本色
  primaryText: string;
  secondaryText: string;

  // 主色调
  primary: string;
  primaryHover: string;

  // 边框和分割线
  border: string;
  divider: string;

  // 阴影
  shadow: string;

  // 状态色
  success: string;
  warning: string;
  error: string;
  info: string;
}

/**
 * 明亮主题配置
 */
export const lightTheme: ThemeColors = {
  background: '#F8F8F8',
  cardBackground: '#FFFFFF',
  navbarBackground: '#FFFFFF',

  primaryText: '#333333',
  secondaryText: '#666666',

  primary: '#28A745',
  primaryHover: '#218838',

  border: '#E0E0E0',
  divider: '#E0E0E0',

  shadow: 'rgba(0, 0, 0, 0.1)',

  success: '#28A745',
  warning: '#FFC107',
  error: '#DC3545',
  info: '#17A2B8',
};

/**
 * 暗黑主题配置
 */
export const darkTheme: ThemeColors = {
  background: '#2C2C2C',
  cardBackground: '#3C3C3C',
  navbarBackground: '#1A1A1A',

  primaryText: '#E0E0E0',
  secondaryText: '#A0A0A0',

  primary: '#64B5F6',
  primaryHover: '#42A5F5',

  border: '#555555',
  divider: '#555555',

  shadow: 'rgba(0, 0, 0, 0.5)',

  success: '#8BC34A',
  warning: '#FFEB3B',
  error: '#EF5350',
  info: '#29B6F6',
};

/**
 * 主题配置映射
 */
export const themes: Record<ThemeMode, ThemeColors> = {
  light: lightTheme,
  dark: darkTheme,
};

/**
 * Ant Design 主题令牌配置
 */
export const getAntdTheme = (mode: ThemeMode) => {
  const colors = themes[mode];

  return {
    token: {
      // 基础色彩
      colorPrimary: colors.primary,
      colorSuccess: colors.success,
      colorWarning: colors.warning,
      colorError: colors.error,
      colorInfo: colors.info,

      // 背景色
      colorBgContainer: colors.cardBackground,
      colorBgElevated: colors.cardBackground,
      colorBgLayout: colors.background,

      // 文本色
      colorText: colors.primaryText,
      colorTextSecondary: colors.secondaryText,
      colorTextTertiary: colors.secondaryText,

      // 边框色
      colorBorder: colors.border,
      colorBorderSecondary: colors.divider,

      // 基础配置
      borderRadius: 8,
      fontSize: 14,
      fontFamily:
        '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',

      // 阴影
      boxShadow: `0 2px 8px ${colors.shadow}`,
      boxShadowSecondary: `0 1px 4px ${colors.shadow}`,
    },
    components: {
      Button: {
        colorPrimary: colors.primary,
        colorPrimaryHover: colors.primaryHover,
      },
      Menu: {
        itemBg: 'transparent',
        itemSelectedBg: colors.primary + '15', // 15% 透明度
        itemHoverBg: colors.primary + '10', // 10% 透明度
      },
      Card: {
        colorBgContainer: colors.cardBackground,
        colorBorderSecondary: colors.border,
      },
      Layout: {
        colorBgContainer: colors.background,
        colorBgHeader: colors.navbarBackground,
        colorBgBody: colors.background,
      },
      Input: {
        colorBorder: colors.border,
        colorBgContainer: colors.cardBackground,
      },
      Table: {
        colorBgContainer: colors.cardBackground,
        colorBorderSecondary: colors.border,
      },
    },
    algorithm: mode === 'dark' ? 'darkAlgorithm' : 'defaultAlgorithm',
  };
};
