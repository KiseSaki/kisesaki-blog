/**
 * 管理员功能图标组件
 * 提供博客后台管理系统的功能图标
 */

import React from 'react';
import type { IconProps } from './NavigationIcons';

/**
 * 仪表盘图标组件
 * 用于管理员仪表盘和数据概览页面
 */
export const DashboardIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <rect
      x='3'
      y='3'
      width='18'
      height='18'
      rx='2'
      ry='2'
      stroke='currentColor'
      strokeWidth='2'
    />
    <rect
      x='9'
      y='9'
      width='6'
      height='6'
      stroke='currentColor'
      strokeWidth='2'
    />
    <path
      d='M9 1V3'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M15 1V3'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M9 21V23'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M15 21V23'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M1 9H3'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M21 9H23'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M1 15H3'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M21 15H23'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);

/**
 * 用户管理图标组件
 * 用于用户列表和用户管理功能
 */
export const UsersIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <path
      d='M17 21V19C17 17.9391 16.5786 16.9217 15.8284 16.1716C15.0783 15.4214 14.0609 15 13 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <circle cx='9' cy='7' r='4' stroke='currentColor' strokeWidth='2' />
    <path
      d='M23 21V19C22.9993 18.1137 22.7044 17.2528 22.1614 16.5523C21.6184 15.8519 20.8581 15.3516 20 15.13'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M16 3.13C16.8604 3.35031 17.623 3.85071 18.1676 4.55232C18.7122 5.25392 19.0078 6.11683 19.0078 7.005C19.0078 7.89318 18.7122 8.75608 18.1676 9.45769C17.623 10.1593 16.8604 10.6597 16 10.88'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);

/**
 * 评论管理图标组件
 * 用于评论审核和管理功能
 */
export const CommentIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <path
      d='M21 15C21 15.5304 20.7893 16.0391 20.4142 16.4142C20.0391 16.7893 19.5304 17 19 17H7L3 21V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H19C19.5304 3 20.0391 3.21071 20.4142 3.58579C20.7893 3.96086 21 4.46957 21 5V15Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);

/**
 * 个人资料图标组件
 * 用于个人资料管理和设置
 */
export const ProfileIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <path
      d='M15 10L11 14L17 20L21 16L15 10Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M12.5 7.5L15 10L10.5 14.5L8 12L12.5 7.5Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M14 12L9 17L3 11L5 9L9 13L12 10L14 12Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <circle cx='12' cy='8' r='3' stroke='currentColor' strokeWidth='2' />
  </svg>
);

/**
 * 文章管理图标组件
 * 用于文章列表、编辑和管理功能
 */
export const PostIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <path
      d='M14 2H6C4.89543 2 4 2.89543 4 4V20C4 21.1046 4.89543 22 6 22H18C19.1046 22 20 21.1046 20 20V8L14 2Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <polyline
      points='14,2 14,8 20,8'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <line
      x1='16'
      y1='13'
      x2='8'
      y2='13'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <line
      x1='16'
      y1='17'
      x2='8'
      y2='17'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <polyline
      points='10,9 9,9 8,9'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);

/**
 * Tag管理图标组件
 * 用于标签列表和管理功能
 */
export const TagIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <path
      d='M20.59 13.41L13.42 20.58C13.2343 20.766 13.0137 20.9135 12.7709 21.0141C12.5281 21.1148 12.2678 21.1666 12.005 21.1666C11.7422 21.1666 11.4819 21.1148 11.2391 21.0141C10.9963 20.9135 10.7757 20.766 10.59 20.58L2 12V2H12L20.59 10.59C20.9625 10.9647 21.1716 11.4716 21.1716 12C21.1716 12.5284 20.9625 13.0353 20.59 13.41Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <circle cx='9' cy='9' r='2' stroke='currentColor' strokeWidth='2' />
  </svg>
);

/**
 * Categories管理图标组件
 * 用于分类列表和管理功能
 */
export const CategoryIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <path
      d='M3 7V5C3 3.89543 3.89543 3 5 3H9L11 7H19C20.1046 7 21 7.89543 21 9V11C21 12.1046 20.1046 13 19 13H17'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M3 7V19C3 20.1046 3.89543 21 5 21H9'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M13 13V17C13 18.1046 13.8954 19 15 19H19'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);

/**
 * Setting管理图标组件
 * 用于系统设置和管理功能
 */
export const SettingIcon: React.FC<IconProps> = ({ size = 24, ...props }) => (
  <svg
    width={size}
    height={size}
    viewBox='0 0 24 24'
    fill='none'
    xmlns='http://www.w3.org/2000/svg'
    {...props}
  >
    <circle cx='12' cy='12' r='3' stroke='currentColor' strokeWidth='2' />
    <path
      d='M19.4 15C19.7822 14.4026 20.0607 13.7333 20.2166 13.0313C20.3725 12.3293 20.4013 11.6134 20.3016 10.9082C20.2019 10.203 19.9765 9.52432 19.6381 8.91318C19.2997 8.30203 18.8567 7.77144 18.34 7.35L17.6 8.1C17.0418 8.47343 16.5431 8.94381 16.1266 9.48797C15.7101 10.0321 15.383 10.6399 15.16 11.29C14.937 11.9401 14.8224 12.6213 14.8224 13.31C14.8224 14.0013 14.937 14.6851 15.16 15.3378C15.383 15.9905 15.7101 16.6009 16.1266 17.1476C16.5431 17.6942 17.0418 18.1662 17.6 18.54L18.34 19.29C18.8567 18.8686 19.2997 18.338 19.6381 17.7268C19.9765 17.1157 20.2019 16.437 20.3016 15.7318C20.4013 15.0266 20.3725 14.3107 20.2166 13.6087C20.0607 12.9067 19.7822 12.2374 19.4 11.64V15Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
    <path
      d='M4.6 9C4.21776 9.59741 3.93933 10.2667 3.78339 10.9687C3.62745 11.6707 3.59868 12.3866 3.69836 13.0918C3.79804 13.797 4.02354 14.4757 4.36193 15.0868C4.70032 15.698 5.14334 16.2286 5.66 16.65L6.4 15.9C6.95819 15.5266 7.45694 15.0562 7.87344 14.512C8.28994 13.9679 8.61705 13.3601 8.84 12.71C9.063 12.0599 9.17764 11.3787 9.17764 10.69C9.17764 9.99869 9.063 9.31485 8.84 8.66218C8.61705 8.00952 8.28994 7.39913 7.87344 6.85237C7.45694 6.30562 6.95819 5.83382 6.4 5.46L5.66 4.71C5.14334 5.13144 4.70032 5.66203 4.36193 6.27318C4.02354 6.88432 3.79804 7.563 3.69836 8.26818C3.59868 8.97339 3.62745 9.68929 3.78339 10.3913C3.93933 11.0933 4.21776 11.7626 4.6 12.36V9Z'
      stroke='currentColor'
      strokeWidth='2'
      strokeLinecap='round'
      strokeLinejoin='round'
    />
  </svg>
);
