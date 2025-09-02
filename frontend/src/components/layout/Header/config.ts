export interface IHeaderMenu {
    label: string;
    path: string;
    icon?: React.ReactNode;
    submenu?: IHeaderMenu[];
}

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
        path: "/blog/aboutMe"
    }
];
