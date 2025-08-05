import About from "@/pages/About";
import Home from "@/pages/Home";
import { createBrowserRouter } from "react-router";

const routes = createBrowserRouter([
    {
        path: '/',
        Component: Home
    },
    {
        path: '/about',
        Component: About,
    }
]);
export default routes;
