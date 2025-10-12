import { useTheme } from '@/stores/themeStore';
import MDEditor, { commands as Commands } from '@uiw/react-md-editor';
import { useEffect, useLayoutEffect, useState } from 'react';
import rehypeSanitize from 'rehype-sanitize';
import { createEmojiCommand, createImageUploadCommand } from './commands';
import { EmojiPickerComponent } from './EmojiPicker';
import './styles.css';
import type { MarkdownEditorProps } from './types';

/**
 * Markdown 编辑器组件
 *
 * @description
 * 功能丰富的 Markdown 编辑器，支持：
 * - 实时预览
 * - 工具栏快捷操作
 * - 图片上传
 * - 表情选择器
 * - 代码高亮
 * - 全屏模式
 *
 * @example
 * ```tsx
 * const [content, setContent] = useState('');
 *
 * <MarkdownEditor
 *   value={content}
 *   onChange={setContent}
 *   placeholder="请输入内容..."
 *   minHeight={300}
 *   enableImageUpload
 *   enableEmoji
 * />
 * ```
 */

export const MarkdownEditor = ({
  value,
  onChange = () => {},
  placeholder = '请输入内容，支持 Markdown 语法...',
  minHeight = 200,
  maxHeight,
  preview = 'live',
  hideToolbar = false,
  disabled = false,
  fullscreen = false,
  commands,
  extraCommands = [],
  enableSyntaxHighlight = true,
  enableImageUpload = false,
  onImageUpload,
  enableEmoji = false,
  className = '',
}: MarkdownEditorProps) => {
  const { mode: theme } = useTheme();
  const [isFullscreen, setIsFullscreen] = useState(fullscreen);
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);
  const [editorValue, setEditorValue] = useState(value);

  // 处理内容变化
  const handleChange = (val?: string) => {
    const newValue = val || '';
    setEditorValue(newValue);
    onChange(newValue);
  };

  // 同步外部 value 变化
  useEffect(() => {
    setEditorValue(value);
  }, [value]);

  // 增强预览区域（与 PostContent 一致的处理逻辑）
  useLayoutEffect(() => {
    // 只在预览模式下执行
    if (preview === 'edit') return;

    // 增强函数
    const enhancePreview = () => {
      const previewContainer = document.querySelector(
        '.markdown-editor-wrapper .wmde-markdown'
      ) as HTMLElement;

      if (!previewContainer) return;

      // 1. 代码高亮
      const codeBlocks = previewContainer.querySelectorAll('pre code');
      codeBlocks.forEach(block => {
        const htmlBlock = block as HTMLElement;
        if (!htmlBlock.dataset.highlighted) {
          htmlBlock.dataset.highlighted = 'true';
          // 使用动态导入避免阻塞
          import('highlight.js').then(({ default: hljs }) => {
            hljs.highlightElement(htmlBlock);
          });
        }
      });

      // 2. 为代码块添加复制按钮和语言标签（Mac 风格）
      const preElements = previewContainer.querySelectorAll('pre');
      preElements.forEach(pre => {
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
          const code = codeElement.textContent || '';
          try {
            await navigator.clipboard.writeText(code);
            const copyIcon = copyButton.querySelector('.copy-icon');
            const checkIcon = copyButton.querySelector('.check-icon');
            const copyText = copyButton.querySelector('.copy-text');

            if (copyIcon && checkIcon && copyText) {
              copyIcon.classList.add('hidden');
              checkIcon.classList.remove('hidden');
              copyText.textContent = '已复制';
              copyButton.classList.add('copied');

              setTimeout(() => {
                copyIcon.classList.remove('hidden');
                checkIcon.classList.add('hidden');
                copyText.textContent = '复制代码';
                copyButton.classList.remove('copied');
              }, 2000);
            }
          } catch (err) {
            console.error('复制失败:', err);
          }
        });

        headerContainer.appendChild(macButtons);
        headerContainer.appendChild(languageLabel);
        headerContainer.appendChild(copyButton);
        pre.insertBefore(headerContainer, codeElement);
        pre.classList.add('has-header');
      });

      // 3. 为所有外部链接添加 target="_blank"
      const links = previewContainer.querySelectorAll('a');
      links.forEach(link => {
        const href = link.getAttribute('href');
        if (href && (href.startsWith('http') || href.startsWith('//'))) {
          link.setAttribute('target', '_blank');
          link.setAttribute('rel', 'noopener noreferrer');
        }
      });

      // 4. 图片懒加载和错误处理
      const images = previewContainer.querySelectorAll('img');
      images.forEach(img => {
        img.setAttribute('loading', 'lazy');
        if (!img.dataset.errorHandled) {
          img.dataset.errorHandled = 'true';
          img.addEventListener('error', () => {
            img.setAttribute(
              'src',
              'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="400" height="300"%3E%3Crect fill="%23f0f0f0" width="400" height="300"/%3E%3Ctext fill="%23999" x="50%25" y="50%25" text-anchor="middle" dy=".3em"%3E图片加载失败%3C/text%3E%3C/svg%3E'
            );
          });
        }
      });

      // 5. 为表格添加响应式容器
      const tables = previewContainer.querySelectorAll('table');
      tables.forEach(table => {
        if (!table.parentElement?.classList.contains('table-wrapper')) {
          const wrapper = document.createElement('div');
          wrapper.className = 'table-wrapper';
          table.parentNode?.insertBefore(wrapper, table);
          wrapper.appendChild(table);
        }
      });
    };

    // 初始执行一次
    setTimeout(enhancePreview, 100);

    // 监听预览区域的变化
    const observer = new MutationObserver(() => {
      enhancePreview();
    });

    const previewContainer = document.querySelector(
      '.markdown-editor-wrapper .w-md-editor-preview'
    );

    if (previewContainer) {
      observer.observe(previewContainer, {
        childList: true,
        subtree: true,
      });
    }

    return () => {
      observer.disconnect();
    };
  }, [preview, editorValue]);

  // 处理表情选择
  const handleEmojiClick = (emoji: string) => {
    const newValue = editorValue + emoji;
    setEditorValue(newValue);
    onChange(newValue);
    setShowEmojiPicker(false);
  };

  // 构建自定义命令列表
  const customCommands = [];

  // 添加图片上传命令
  if (enableImageUpload) {
    customCommands.push(createImageUploadCommand(onImageUpload));
  }

  // 添加表情选择器命令
  if (enableEmoji) {
    customCommands.push(
      createEmojiCommand(() => setShowEmojiPicker(!showEmojiPicker))
    );
  }

  // 默认工具栏命令配置（使用导入的 Commands，而非 props.commands）
  const defaultCommands = [
    Commands.group([Commands.title1, Commands.title2, Commands.title3], {
      name: 'title',
      groupName: 'title',
      buttonProps: { 'aria-label': '插入标题' },
    }),
    Commands.divider,
    Commands.bold,
    Commands.italic,
    Commands.strikethrough,
    Commands.divider,
    Commands.link,
    Commands.quote,
    Commands.code,
    Commands.codeBlock,
    Commands.divider,
    Commands.unorderedListCommand,
    Commands.orderedListCommand,
    Commands.checkedListCommand,
    Commands.divider,
    Commands.image,
    Commands.table,
    Commands.divider,
    ...customCommands,
    Commands.divider,
    Commands.help,
  ];

  // 合并自定义命令
  const finalCommands = commands || [...defaultCommands, ...extraCommands];

  // 监听全屏 prop 变化，同步内部状态
  useEffect(() => {
    setIsFullscreen(fullscreen);
  }, [fullscreen]);

  // 同步 body 滚动锁定（进入/退出全屏）
  useEffect(() => {
    if (isFullscreen) {
      const prev = document.body.style.overflow;
      document.body.style.overflow = 'hidden';
      return () => {
        document.body.style.overflow = prev || '';
      };
    }
    return undefined;
  }, [isFullscreen]);

  return (
    <div
      className={`markdown-editor-wrapper ${className} ${
        isFullscreen ? 'markdown-editor-fullscreen' : ''
      }`}
      data-color-mode={theme === 'dark' ? 'dark' : 'light'}
      style={
        isFullscreen
          ? {
              position: 'fixed',
              inset: 0,
              zIndex: 9999,
              background: 'var(--md-editor-bg-color, var(--card))',
              padding: 12,
            }
          : undefined
      }
    >
      <MDEditor
        value={editorValue}
        onChange={handleChange}
        preview={preview}
        height={isFullscreen ? '100%' : maxHeight || minHeight}
        minHeight={minHeight}
        maxHeight={maxHeight}
        visibleDragbar={false}
        hideToolbar={hideToolbar}
        enableScroll={true}
        highlightEnable={enableSyntaxHighlight}
        textareaProps={{
          placeholder,
          disabled,
        }}
        previewOptions={
          enableSyntaxHighlight
            ? {
                rehypePlugins: [[rehypeSanitize]],
              }
            : undefined
        }
        commands={finalCommands}
        extraCommands={[
          // 使用导入的 Commands，避免引用 props.commands 导致未定义问题
          Commands.codeEdit,
          Commands.codeLive,
          Commands.codePreview,
          Commands.divider,
          Commands.fullscreen,
        ]}
      />

      {/* 表情选择器 */}
      {enableEmoji && showEmojiPicker && (
        <EmojiPickerComponent
          onEmojiClick={handleEmojiClick}
          onClose={() => setShowEmojiPicker(false)}
        />
      )}
    </div>
  );
};
