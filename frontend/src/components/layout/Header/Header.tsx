import React from "react";
import { Center } from "./internal/Center";
import { Left } from "./internal/Left";
import { Right } from "./internal/Right";

export const Header: React.FC = () => {
  return (
    <header className="h-14 border-b px-3 py-2 fixed top-0 left-0 right-0 z-50">
      <div className="max-w-7xl mx-auto flex justify-between items-center">
        <Left />
        <Center />
        <Right />
      </div>
    </header>
  );
};
