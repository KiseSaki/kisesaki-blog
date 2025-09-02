import { Outlet } from "react-router";
import { Footer } from "./Footer";
import { Header } from "./Header";

export const MainLayout = () => {
  return (
    <div>
      <Header />
      <main className="pt-14">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};
