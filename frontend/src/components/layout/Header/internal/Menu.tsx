import type React from 'react';
import { NavLink } from 'react-router';
import type { IHeaderMenu } from '../config';

export const HeaderMenu: React.FC<IHeaderMenu> = ({ label, path }) => {
  return (
    <NavLink
      to={path}
      className={({ isActive }) =>
        `no-underline px-3 py-2 rounded ${
          isActive ? 'text-theme-primary-text' : 'text-theme-secondary-text'
        }`
      }
    >
      {label}
    </NavLink>
  );
};
