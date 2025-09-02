import { Outlet } from "react-router";
import { Footer } from "./Footer";
import { Header } from "./Header/Header";

export const MainLayout = () => {
    return (
        <div>
            <Header />
            <main>
                <Outlet />
            </main>
            <Footer />
        </div>
    );
};