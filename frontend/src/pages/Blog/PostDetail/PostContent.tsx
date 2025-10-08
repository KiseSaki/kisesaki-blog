import hljs from 'highlight.js';
import 'highlight.js/styles/github-dark.css';
import { useLayoutEffect, useRef } from 'react';
import { toast } from 'sonner';
import './PostContent.css';

interface PostContentProps {
  htmlContent: string;
}

/**
 * 文章内容展示组件
 * 提供代码高亮、代码复制、图片懒加载、外链处理等增强功能
 */
export const PostContent = ({ htmlContent }: PostContentProps) => {
  const contentRef = useRef<HTMLDivElement>(null);

  useLayoutEffect(() => {
    if (!contentRef.current || !htmlContent) return;

    const container = contentRef.current;

    // 首先设置 HTML 内容
    container.innerHTML = htmlContent;

    // 立即执行增强操作(在 React 渲染之前)
    // 1. 代码高亮（使用 highlightElement 自动处理）
    const codeBlocks = container.querySelectorAll('pre code');
    codeBlocks.forEach(block => {
      const htmlBlock = block as HTMLElement;
      // 检查是否已经高亮过（避免重复处理）
      if (!htmlBlock.dataset.highlighted) {
        try {
          hljs.highlightElement(htmlBlock);
          htmlBlock.dataset.highlighted = 'yes';
        } catch (error) {
          console.error('代码高亮失败:', error);
        }
      }
    });

    // 2. 为代码块添加复制按钮和语言标签
    const preElements = container.querySelectorAll('pre');
    preElements.forEach(pre => {
      // 避免重复添加按钮
      if (pre.querySelector('.code-block-header')) return;

      const codeElement = pre.querySelector('code');
      if (!codeElement) return;

      // 获取代码语言
      const languageClass = Array.from(codeElement.classList).find(
        cls => cls.startsWith('language-') || cls.startsWith('hljs-')
      );
      const language = languageClass
        ? languageClass
            .replace('language-', '')
            .replace('hljs-', '')
            .toUpperCase()
        : 'CODE';

      // 创建代码块头部容器
      const headerContainer = document.createElement('div');
      headerContainer.className = 'code-block-header';

      // 创建 Mac 风格的三色按钮
      const macButtons = document.createElement('div');
      macButtons.className = 'mac-window-buttons';
      macButtons.innerHTML = `
        <span class="mac-btn mac-btn-close" title="关闭"></span>
        <span class="mac-btn mac-btn-minimize" title="最小化"></span>
        <span class="mac-btn mac-btn-maximize" title="最大化"></span>
      `;

      // 创建语言标签
      const languageLabel = document.createElement('span');
      languageLabel.className = 'code-language-label';
      languageLabel.textContent = language;

      // 创建复制按钮
      const copyButton = document.createElement('button');
      copyButton.className = 'copy-button';
      copyButton.innerHTML = `
        <svg class="copy-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect>
          <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path>
        </svg>
        <svg class="check-icon hidden" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="20 6 9 17 4 12"></polyline>
        </svg>
        <span class="copy-text">复制代码</span>
      `;

      // 复制功能
      copyButton.addEventListener('click', async () => {
        const code = pre.querySelector('code');
        if (!code) return;

        const text = code.textContent || '';
        try {
          // 优先使用 Clipboard API；若不可用则回退到 textarea + execCommand
          if (navigator?.clipboard?.writeText) {
            await navigator.clipboard.writeText(text);
          } else {
            const textarea = document.createElement('textarea');
            textarea.value = text;
            // 防止页面跳动，放到视窗外
            textarea.style.position = 'fixed';
            textarea.style.left = '-9999px';
            document.body.appendChild(textarea);
            textarea.select();
            const successful = document.execCommand('copy');
            document.body.removeChild(textarea);
            if (!successful) throw new Error('execCommand copy failed');
          }

          // 切换图标和文字
          const copyIcon = copyButton.querySelector('.copy-icon');
          const checkIcon = copyButton.querySelector('.check-icon');
          const copyText = copyButton.querySelector('.copy-text');

          copyIcon?.classList.add('hidden');
          checkIcon?.classList.remove('hidden');
          if (copyText) copyText.textContent = '已复制';
          copyButton.classList.add('copied');

          toast.success('代码已复制到剪贴板');

          // 2秒后恢复图标
          setTimeout(() => {
            copyIcon?.classList.remove('hidden');
            checkIcon?.classList.add('hidden');
            if (copyText) copyText.textContent = '复制代码';
            copyButton.classList.remove('copied');
          }, 2000);
        } catch (error) {
          console.error('复制失败', error);
          toast.error('复制失败，请手动复制');
        }
      });

      headerContainer.appendChild(macButtons);
      headerContainer.appendChild(languageLabel);
      headerContainer.appendChild(copyButton);
      pre.insertBefore(headerContainer, pre.firstChild);
      pre.classList.add('has-header');
    });

    // 3. 为所有外部链接添加 target="_blank" 和 rel="noopener noreferrer"
    const links = container.querySelectorAll('a');
    links.forEach(link => {
      const href = link.getAttribute('href');
      if (href && (href.startsWith('http://') || href.startsWith('https://'))) {
        link.setAttribute('target', '_blank');
        link.setAttribute('rel', 'noopener noreferrer');
      }
    });

    // 4. 图片懒加载和错误处理
    const images = container.querySelectorAll('img');
    images.forEach(img => {
      // 添加懒加载
      img.setAttribute('loading', 'lazy');

      // 添加错误处理
      img.addEventListener('error', () => {
        img.setAttribute('alt', '图片加载失败');
        img.style.display = 'none';
      });

      // 为图片添加点击放大功能（可选）
      img.style.cursor = 'pointer';
      img.addEventListener('click', () => {
        window.open(img.src, '_blank');
      });
    });

    // 5. 为表格添加响应式容器
    const tables = container.querySelectorAll('table');
    tables.forEach(table => {
      if (table.parentElement?.classList.contains('table-wrapper')) return;

      const wrapper = document.createElement('div');
      wrapper.className = 'table-wrapper';
      table.parentNode?.insertBefore(wrapper, table);
      wrapper.appendChild(table);
    });

    // 6. 为标题添加锚点链接
    const headings = container.querySelectorAll('h1, h2, h3, h4, h5, h6');
    headings.forEach(heading => {
      const id = heading.textContent?.trim().replace(/\s+/g, '-').toLowerCase();
      if (id && heading instanceof HTMLElement) {
        heading.id = id;
        heading.style.cursor = 'pointer';
        heading.addEventListener('click', () => {
          window.location.hash = id;
          toast.success('锚点链接已复制');
        });
      }
    });
  }, [htmlContent]);

  return (
    <article
      ref={contentRef}
      className='post-content prose prose-lg max-w-none dark:prose-invert
        prose-headings:font-bold prose-headings:tracking-tight prose-headings:scroll-mt-20
        prose-h1:text-4xl prose-h1:mb-6 prose-h1:mt-8 prose-h1:pb-3 prose-h1:border-b prose-h1:border-theme-border
        prose-h2:text-3xl prose-h2:mb-4 prose-h2:mt-8 prose-h2:pb-2 prose-h2:border-b prose-h2:border-theme-border/50
        prose-h3:text-2xl prose-h3:mb-3 prose-h3:mt-6
        prose-h4:text-xl prose-h4:mb-2 prose-h4:mt-4
        prose-p:text-base prose-p:leading-8 prose-p:mb-6 prose-p:text-theme-secondary-text
        prose-a:text-theme-primary prose-a:no-underline prose-a:font-medium prose-a:transition-colors
        hover:prose-a:text-theme-primary-hover hover:prose-a:underline
        prose-strong:text-theme-primary-text prose-strong:font-semibold
        prose-em:text-theme-secondary-text prose-em:italic
        prose-img:rounded-xl prose-img:shadow-lg prose-img:my-8
        prose-pre:bg-transparent prose-pre:p-0 prose-pre:my-6
        prose-code:text-theme-primary prose-code:bg-muted/80 prose-code:px-1.5 prose-code:py-0.5 prose-code:rounded prose-code:font-mono prose-code:text-sm prose-code:font-normal prose-code:before:content-none prose-code:after:content-none
        prose-blockquote:border-l-4 prose-blockquote:border-theme-primary prose-blockquote:bg-muted/50 prose-blockquote:py-4 prose-blockquote:px-6 prose-blockquote:my-6 prose-blockquote:rounded-r-lg prose-blockquote:italic prose-blockquote:text-theme-secondary-text
        prose-ul:my-6 prose-ul:list-disc prose-ul:pl-6
        prose-ol:my-6 prose-ol:list-decimal prose-ol:pl-6
        prose-li:my-2 prose-li:text-theme-secondary-text prose-li:leading-7
        prose-table:border prose-table:border-theme-border prose-table:my-8 prose-table:shadow-md
        prose-thead:bg-muted/50 prose-thead:border-b prose-thead:border-theme-border
        prose-th:px-4 prose-th:py-3 prose-th:text-left prose-th:font-semibold prose-th:text-theme-primary-text
        prose-td:px-4 prose-td:py-3 prose-td:border-t prose-td:border-theme-border/50 prose-td:text-theme-secondary-text
        prose-hr:my-10 prose-hr:border-theme-border/30'
    />
  );
};
