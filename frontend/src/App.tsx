import { Toaster } from 'sonner'
import { RouterProvider } from 'react-router';
import routes from './router';

function App() {

  return (
    <>
      {/* 页面内容 */}
      <RouterProvider router={routes}/>
      
      {/* Toast 通知组件 */}
      <Toaster richColors position="top-right" />
    </>
  )
}

export default App
