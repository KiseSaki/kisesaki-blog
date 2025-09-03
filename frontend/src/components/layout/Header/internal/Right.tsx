import { ThemeToggle } from "@/components/common";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui";

export const Right = () => {
  return (
    <div className="flex items-center space-x-4">
      <ThemeToggle className="text-theme-primary-text" />
      {/* TODO 完成登陆后需要头像显示、identifier、fallback的首字母 */}
      <Avatar>
        <AvatarImage src="/path/to/image.jpg" alt="User Avatar" />
        <AvatarFallback identifier="username">U</AvatarFallback>
      </Avatar>
    </div>
  );
};
