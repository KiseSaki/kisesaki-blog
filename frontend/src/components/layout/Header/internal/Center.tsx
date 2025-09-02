import { headerMenusConfig } from "../config";
import { HeaderMenu } from "./Menu";

export const Center = () => {
  return (
    <div className="flex justify-center">
      {headerMenusConfig.map((menu) => (
        <HeaderMenu key={menu.path} label={menu.label} path={menu.path} />
      ))}
    </div>
  );
};
