/**
 * 系统设置配置文件
 */

/**
 * 设置字段类型
 */
export interface SettingField {
  key: string;
  label: string;
  type: 'text' | 'textarea' | 'number' | 'switch' | 'select' | 'image' | 'password';
  placeholder?: string;
  required?: boolean;
  defaultValue?: unknown;
  min?: number;
  max?: number;
  options?: { label: string; value: string }[];
}

/**
 * 设置分组
 */
export const settingGroups = [
  {
    key: 'general',
    title: '基本设置',
    description: '网站基本信息配置',
  },
  {
    key: 'seo',
    title: 'SEO 设置',
    description: '搜索引擎优化相关配置',
  },
  {
    key: 'security',
    title: '安全设置',
    description: '安全与隐私相关配置',
  },
  {
    key: 'email',
    title: '邮件设置',
    description: '邮件服务相关配置',
  },
  {
    key: 'storage',
    title: '存储设置',
    description: '文件存储相关配置',
  },
];

/**
 * 基本设置字段
 */
export const generalSettings: SettingField[] = [
  {
    key: 'siteName',
    label: '网站名称',
    type: 'text',
    placeholder: '请输入网站名称',
    required: true,
  },
  {
    key: 'siteDescription',
    label: '网站描述',
    type: 'textarea',
    placeholder: '请输入网站描述',
  },
  {
    key: 'siteKeywords',
    label: '网站关键词',
    type: 'text',
    placeholder: '请输入网站关键词，用逗号分隔',
  },
  {
    key: 'siteLogo',
    label: '网站 Logo',
    type: 'image',
  },
  {
    key: 'siteFavicon',
    label: '网站图标',
    type: 'image',
  },
];

/**
 * SEO 设置字段
 */
export const seoSettings: SettingField[] = [
  {
    key: 'enableSitemap',
    label: '启用站点地图',
    type: 'switch',
    defaultValue: true,
  },
  {
    key: 'enableRobots',
    label: '启用 robots.txt',
    type: 'switch',
    defaultValue: true,
  },
  {
    key: 'metaDescription',
    label: 'Meta 描述',
    type: 'textarea',
    placeholder: '默认的 Meta 描述',
  },
  {
    key: 'googleAnalytics',
    label: 'Google Analytics ID',
    type: 'text',
    placeholder: 'G-XXXXXXXXXX',
  },
];

/**
 * 安全设置字段
 */
export const securitySettings: SettingField[] = [
  {
    key: 'enableRegistration',
    label: '允许用户注册',
    type: 'switch',
    defaultValue: true,
  },
  {
    key: 'enableComments',
    label: '允许评论',
    type: 'switch',
    defaultValue: true,
  },
  {
    key: 'commentModeration',
    label: '评论需要审核',
    type: 'switch',
    defaultValue: true,
  },
  {
    key: 'maxLoginAttempts',
    label: '最大登录尝试次数',
    type: 'number',
    defaultValue: 5,
    min: 1,
    max: 10,
  },
];

/**
 * 邮件设置字段
 */
export const emailSettings: SettingField[] = [
  {
    key: 'smtpHost',
    label: 'SMTP 主机',
    type: 'text',
    placeholder: 'smtp.example.com',
  },
  {
    key: 'smtpPort',
    label: 'SMTP 端口',
    type: 'number',
    placeholder: '587',
    defaultValue: 587,
  },
  {
    key: 'smtpUser',
    label: 'SMTP 用户名',
    type: 'text',
  },
  {
    key: 'smtpPassword',
    label: 'SMTP 密码',
    type: 'password',
  },
  {
    key: 'emailFrom',
    label: '发件人邮箱',
    type: 'text',
    placeholder: 'noreply@example.com',
  },
];

/**
 * 存储设置字段
 */
export const storageSettings: SettingField[] = [
  {
    key: 'uploadMaxSize',
    label: '最大上传大小 (MB)',
    type: 'number',
    defaultValue: 10,
    min: 1,
    max: 100,
  },
  {
    key: 'allowedFileTypes',
    label: '允许的文件类型',
    type: 'text',
    placeholder: 'jpg,jpeg,png,gif,pdf',
  },
  {
    key: 'storageProvider',
    label: '存储服务提供商',
    type: 'select',
    options: [
      { label: '本地存储', value: 'local' },
      { label: '阿里云 OSS', value: 'aliyun' },
      { label: 'AWS S3', value: 'aws' },
    ],
    defaultValue: 'local',
  },
];
