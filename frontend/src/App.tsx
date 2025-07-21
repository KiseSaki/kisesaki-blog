import './App.css'
import { Toaster } from 'sonner'

function App() {

  return (
    <>
      {/* 页面内容 */}
      <div className="min-h-screen bg-background">
        <h1 className="text-2xl font-bold text-center py-8">
          Kisesaki Blog
        </h1>
        <p className="text-center text-muted-foreground">
          API 客户端已配置完成
        </p>
      </div>
      
      {/* Toast 通知组件 */}
      <Toaster richColors position="top-right" />
    </>
  )
}

export default App
