export interface IHeaderMenu {
  label: string;
  path: string;
  icon?: React.ReactNode;
  submenu?: IHeaderMenu[];
}

// Header 菜单配置
export const headerMenusConfig: IHeaderMenu[] = [
  {
    label: "首页",
    path: "/",
  },
  {
    label: "归档",
    path: "/blog/archive",
  },
  {
    label: "标签",
    path: "/blog/tag/[tag]",
  },
  {
    label: "关于我",
    path: "/blog/aboutMe",
  },
];

// Avatar 跳转链接
export const AVATAR_LINK = "/user/profile";

// Login 跳转链接
export const LOGIN_LINK = "/auth/login";
