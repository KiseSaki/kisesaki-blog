import {
  Avatar,
  AvatarFallback,
  AvatarImage,
  Button,
  HoverCard,
  HoverCardContent,
} from '@/components';
import { ThemeToggle } from '@/components/common';
import { LOGIN_LINK } from '@/config/routeURL';
import { useAuth } from '@/hooks';
import { HoverCardTrigger } from '@radix-ui/react-hover-card';
import { useNavigate } from 'react-router';

export const Right = () => {
  const { isAuthenticated, user, logout } = useAuth();

  // 登录跳转
  const navigate = useNavigate();
  const handleLogin = () => {
    navigate(LOGIN_LINK);
  };

  // 个人主页跳转
  const handleProfile = () => {
    navigate('/user/profile');
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
            <Avatar className='cursor-pointer' onClick={handleProfile}>
              <AvatarImage src={user?.avatarUrl} alt='User Avatar' />
              <AvatarFallback identifier={user?.displayName}>U</AvatarFallback>
            </Avatar>
          </HoverCardTrigger>
          <HoverCardContent className='w-auto'>
            <div className='flex flex-col space-y-2'>
              <Button variant='ghost' onClick={handleProfile}>
                个人主页
              </Button>
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
