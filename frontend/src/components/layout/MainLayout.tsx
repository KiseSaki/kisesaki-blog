import { Outlet } from 'react-router';
import { Footer } from './Footer';
import { Header } from './Header';
import { Sidebar } from './Siderbar';
import { useSidebar } from './Siderbar/hooks/useSidebar';

export const MainLayout = () => {
  const { sidebarType } = useSidebar();

  return (
    <div className='min-h-screen flex flex-col bg-background'>
      <a
        href='#main-content'
        className='sr-only focus:not-sr-only focus:absolute focus:top-2 focus:left-2 z-[60] bg-primary text-primary-foreground px-4 py-2 rounded-md font-medium'
      >
        跳到主要内容
      </a>

      {/* Fixed Header */}
      <Header />

      {/* Main Content Area */}
      <div className='flex flex-1'>
        {sidebarType && <Sidebar className='hidden md:block flex-shrink-0' />}
        <main
          id='main-content'
          className='flex-1 transition-all duration-300 ease-in-out'
          role='main'
          aria-label='主要内容区域'
        >
          <Outlet />
        </main>
      </div>

      {/* Footer */}
      <Footer />
    </div>
  );
};
