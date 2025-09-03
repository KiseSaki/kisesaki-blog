import { RouterProvider } from "react-router";
import { Toaster } from "sonner";
import "./App.css";
import { ErrorBoundary, ScrollArea, ThemeProvider } from "./components";
import { router } from "./router";

function App() {
  return (
    <ThemeProvider>
      <ErrorBoundary
        enableErrorReporting={true}
        onError={(error, errorInfo) => {
          // 全局错误处理逻辑
          console.error("[App] Global error caught:", error, errorInfo);

          // 这里可以添加错误上报到监控系统的逻辑
        }}
      >
        <ScrollArea className="h-screen w-full overflow-hidden">
          {/* 页面内容 */}
          <div className="min-h-screen bg-background theme-bg">
            <RouterProvider router={router}></RouterProvider>
          </div>
        </ScrollArea>

        {/* Toast 通知组件 */}
        <Toaster richColors position="top-right" />
      </ErrorBoundary>
    </ThemeProvider>
  );
}

export default App;
