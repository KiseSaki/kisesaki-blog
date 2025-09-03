import { ArrowUp, Github, Heart, Mail, Twitter } from "lucide-react";
import { useEffect, useState } from "react";
import { Button } from "../ui";

export const Footer = () => {
  const [showBackToTop, setShowBackToTop] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setShowBackToTop(window.scrollY > 300);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  return (
    <footer className="relative bg-theme-card-background border-t border-theme-border">
      {/* 回到顶部按钮 */}
      {showBackToTop && (
        <Button
          onClick={scrollToTop}
          size="icon"
          variant="secondary"
          className="fixed bottom-8 right-8 z-40 rounded-full shadow-lg hover:shadow-xl transition-all duration-300"
          aria-label="回到顶部"
        >
          <ArrowUp className="h-4 w-4" />
        </Button>
      )}

      <div className="max-w-7xl mx-auto px-6 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* 网站信息 */}
          <div className="space-y-4">
            <h3 className="text-lg font-semibold text-theme-foreground">
              Kisesaki Blog
            </h3>
            <p className="text-sm text-theme-muted-foreground leading-relaxed">
              分享技术见解，记录成长历程。专注于云原生、全栈开发和前沿技术探索。
            </p>
            <div className="flex items-center space-x-2 text-sm text-theme-muted-foreground">
              <span>Made with</span>
              <Heart className="h-4 w-4 text-red-500 fill-current" />
              <span>by KiseSaki</span>
            </div>
          </div>

          {/* 快速链接 */}
          <div className="space-y-4">
            <h4 className="font-medium text-theme-foreground">快速链接</h4>
            <ul className="space-y-2">
              {[
                { name: "首页", href: "/" },
                { name: "文章", href: "/blog" },
                { name: "分类", href: "/categories" },
                { name: "标签", href: "/tags" },
                { name: "关于", href: "/about" },
              ].map((link) => (
                <li key={link.name}>
                  <a
                    href={link.href}
                    className="text-sm text-theme-muted-foreground hover:text-theme-foreground transition-colors duration-200"
                  >
                    {link.name}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* 技术栈 */}
          <div className="space-y-4">
            <h4 className="font-medium text-theme-foreground">技术栈</h4>
            <ul className="space-y-2">
              {[
                { name: "React + TypeScript", href: "https://react.dev" },
                { name: "Vite + TailwindCSS", href: "https://vitejs.dev" },
                {
                  name: "Spring Boot",
                  href: "https://spring.io/projects/spring-boot",
                },
                { name: "PostgreSQL", href: "https://www.postgresql.org" },
                { name: "Docker + K8s", href: "https://kubernetes.io" },
              ].map((tech) => (
                <li key={tech.name}>
                  <a
                    href={tech.href}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-sm text-theme-muted-foreground hover:text-theme-foreground transition-colors duration-200"
                  >
                    {tech.name}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          {/* 联系方式 */}
          <div className="space-y-4">
            <h4 className="font-medium text-theme-foreground">联系我</h4>
            <div className="flex space-x-4">
              <a
                href="https://github.com/KiseSaki"
                target="_blank"
                rel="noopener noreferrer"
                className="p-2 rounded-lg bg-theme-muted hover:bg-theme-muted/80 text-theme-muted-foreground hover:text-theme-foreground transition-all duration-200"
                aria-label="GitHub"
              >
                <Github className="h-5 w-5" />
              </a>
              <a
                href="mailto:contact@kisesaki.com"
                className="p-2 rounded-lg bg-theme-muted hover:bg-theme-muted/80 text-theme-muted-foreground hover:text-theme-foreground transition-all duration-200"
                aria-label="邮箱"
              >
                <Mail className="h-5 w-5" />
              </a>
              <a
                href="https://twitter.com/KiseSaki"
                target="_blank"
                rel="noopener noreferrer"
                className="p-2 rounded-lg bg-theme-muted hover:bg-theme-muted/80 text-theme-muted-foreground hover:text-theme-foreground transition-all duration-200"
                aria-label="Twitter"
              >
                <Twitter className="h-5 w-5" />
              </a>
            </div>
            <div className="space-y-2">
              <p className="text-sm text-theme-muted-foreground">
                <a
                  href="/privacy"
                  className="hover:text-theme-foreground transition-colors duration-200"
                >
                  隐私政策
                </a>
                {" · "}
                <a
                  href="/terms"
                  className="hover:text-theme-foreground transition-colors duration-200"
                >
                  使用条款
                </a>
              </p>
            </div>
          </div>
        </div>

        {/* 分割线 */}
        <div className="border-t border-theme-border my-8"></div>

        {/* 底部版权信息 */}
        <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
          <div className="text-sm text-theme-muted-foreground">
            © {new Date().getFullYear()} Kisesaki Blog. All rights reserved.
          </div>
          <div className="flex items-center space-x-4 text-sm text-theme-muted-foreground">
            <span>ICP备案号: 京ICP备XXXXXXXX号</span>
            <span>·</span>
            <span>
              Powered by{" "}
              <a
                href="https://github.com/KiseSaki/kisesaki-blog"
                target="_blank"
                rel="noopener noreferrer"
                className="hover:text-theme-foreground transition-colors duration-200"
              >
                KiseSaki Blog
              </a>
            </span>
          </div>
        </div>
      </div>
    </footer>
  );
};
