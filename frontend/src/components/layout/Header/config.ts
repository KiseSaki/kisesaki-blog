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
    path: '/archive',
  },
  {
    label: '分类',
    path: '/category',
  },
  {
    label: '标签',
    path: '/tag',
  },
  {
    label: '关于我',
    path: '/blog/aboutMe',
  },
];
