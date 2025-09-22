import { ThemeToggle } from "@/components/common";
import { Avatar, AvatarFallback, AvatarImage, Button } from "@/components/ui";
import { useAuth } from "@/hooks";
import { useNavigate } from "react-router";
import { LOGIN_LINK } from "../config";

export const Right = () => {
  const { isAuthenticated } = useAuth();

  // 登录跳转
  const navigate = useNavigate();
  const handleLogin = () => {
    navigate(LOGIN_LINK);
  };

  return (
    <div className="flex items-center space-x-4">
      <ThemeToggle className="text-theme-primary-text" />
      {/* TODO 完成登陆后需要头像显示、identifier、fallback的首字母 */}
      {isAuthenticated ? (
        <Avatar className="cursor-pointer">
          <AvatarImage src="/path/to/image.jpg" alt="User Avatar" />
          <AvatarFallback identifier="username">U</AvatarFallback>
        </Avatar>
      ) : (
        <Button size="sm" onClick={handleLogin}>
          登录
        </Button>
      )}
    </div>
  );
};
