import { Button, HoverCard, HoverCardContent } from '@/components';
import { ThemeToggle, UserAvatar } from '@/components/common';
import { LOGIN_LINK } from '@/config/routeURL';
import { useAuth, usePermissions } from '@/hooks';
import { HoverCardTrigger } from '@radix-ui/react-hover-card';
import { useNavigate } from 'react-router';

export const Right = () => {
  const { isAuthenticated, user, logout } = useAuth();
  const { isAuthor } = usePermissions();

  // 登录跳转
  const navigate = useNavigate();
  const handleLogin = () => {
    navigate(LOGIN_LINK);
  };

  // 个人主页跳转
  const handleProfile = () => {
    navigate('/user/profile');
  };

  // 管理后台跳转
  const handleAdmin = () => {
    navigate('/manage');
  };

  // 退出登录
  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className='flex items-center space-x-4'>
      <ThemeToggle className='text-theme-primary-text' />
      {isAuthenticated ? (
        <HoverCard openDelay={200} closeDelay={200}>
          <HoverCardTrigger asChild>
            <UserAvatar
              src={user?.avatarUrl}
              name={user?.displayName || user?.username || 'User'}
              className='h-8 w-8'
              onClick={handleProfile}
            />
          </HoverCardTrigger>
          <HoverCardContent className='w-auto'>
            <div className='flex flex-col space-y-2'>
              <Button variant='ghost' onClick={handleProfile}>
                个人主页
              </Button>
              {isAuthor() && (
                <Button variant='ghost' onClick={handleAdmin}>
                  管理后台
                </Button>
              )}
              <Button variant='ghost' onClick={handleLogout}>
                退出登录
              </Button>
            </div>
          </HoverCardContent>
        </HoverCard>
      ) : (
        <Button size='sm' onClick={handleLogin}>
          登录
        </Button>
      )}
    </div>
  );
};
