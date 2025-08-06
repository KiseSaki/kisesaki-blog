import { RouterProvider } from 'react-router'
import { Toaster } from 'sonner'
import './App.css'
import { router } from './router'

function App() {

  return (
    <>
      {/* 页面内容 */}
      <div className="min-h-screen bg-background">
        <RouterProvider router={router}>
        </RouterProvider>
      </div>

      {/* Toast 通知组件 */}
      <Toaster richColors position="top-right" />
    </>
  )
}

export default App
