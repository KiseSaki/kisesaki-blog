import { BLOG_ARCHIVE_LINK, BLOG_CATEGORY_BASE, BLOG_TAG_BASE } from '@/config';

export interface IHeaderMenu {
  label: string;
  path: string;
  icon?: React.ReactNode;
  submenu?: IHeaderMenu[];
}

// Header 菜单配置
export const headerMenusConfig: IHeaderMenu[] = [
  {
    label: '首页',
    path: '/',
  },
  {
    label: '归档',
    path: BLOG_ARCHIVE_LINK,
  },
  {
    label: '分类',
    path: BLOG_CATEGORY_BASE,
  },
  {
    label: '标签',
    path: BLOG_TAG_BASE,
  },
  {
    label: '关于我',
    path: '/blog/aboutMe',
  },
];
