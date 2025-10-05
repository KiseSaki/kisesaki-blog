import hljs from 'highlight.js';
import 'highlight.js/styles/github-dark.css';
import { useEffect, useRef } from 'react';
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

  useEffect(() => {
    if (!contentRef.current) return;

    const container = contentRef.current;

    // 1. 代码高亮
    const codeBlocks = container.querySelectorAll('pre code');
    codeBlocks.forEach(block => {
      hljs.highlightElement(block as HTMLElement);
    });

    // 2. 为代码块添加复制按钮
    const preElements = container.querySelectorAll('pre');
    preElements.forEach(pre => {
      // 避免重复添加按钮
      if (pre.querySelector('.copy-button')) return;

      // 创建复制按钮容器
      const buttonContainer = document.createElement('div');
      buttonContainer.className = 'copy-button-container';

      // 创建复制按钮
      const copyButton = document.createElement('button');
      copyButton.className = 'copy-button';
      copyButton.innerHTML = `
        <svg class="copy-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect>
          <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path>
        </svg>
        <svg class="check-icon hidden" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="20 6 9 17 4 12"></polyline>
        </svg>
      `;

      // 复制功能
      copyButton.addEventListener('click', async () => {
        const code = pre.querySelector('code');
        if (!code) return;

        try {
          await navigator.clipboard.writeText(code.textContent || '');

          // 切换图标
          const copyIcon = copyButton.querySelector('.copy-icon');
          const checkIcon = copyButton.querySelector('.check-icon');
          copyIcon?.classList.add('hidden');
          checkIcon?.classList.remove('hidden');

          toast.success('代码已复制到剪贴板');

          // 2秒后恢复图标
          setTimeout(() => {
            copyIcon?.classList.remove('hidden');
            checkIcon?.classList.add('hidden');
          }, 2000);
        } catch {
          toast.error('复制失败，请手动复制');
        }
      });

      buttonContainer.appendChild(copyButton);
      pre.style.position = 'relative';
      pre.appendChild(buttonContainer);
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
      className='prose prose-lg max-w-none dark:prose-invert
        prose-headings:scroll-mt-20
        prose-a:text-theme-primary hover:prose-a:text-theme-primary-hover
        prose-img:rounded-lg prose-img:shadow-md
        prose-pre:bg-[#0d1117] prose-pre:border prose-pre:border-theme-border
        prose-code:text-theme-primary prose-code:bg-muted prose-code:px-1 prose-code:py-0.5 prose-code:rounded
        prose-blockquote:border-l-4 prose-blockquote:border-theme-primary prose-blockquote:bg-muted prose-blockquote:py-2
        prose-table:border prose-table:border-theme-border'
      dangerouslySetInnerHTML={{ __html: htmlContent }}
    />
  );
};
