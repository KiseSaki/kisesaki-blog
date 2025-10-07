import MDEditor, { commands as Commands } from '@uiw/react-md-editor';
import { useEffect, useState } from 'react';
import rehypeSanitize from 'rehype-sanitize';
import './styles.css';
import type { MarkdownEditorProps } from './types';

export const MarkdownEditor = ({
  value,
  onChange,
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
  className = '',
}: MarkdownEditorProps) => {
  const [isFullscreen, setIsFullscreen] = useState(fullscreen);

  // 处理内容变化
  const handleChange = (val?: string) => {
    onChange(val || '');
  };

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
      data-color-mode='light'
      style={
        isFullscreen
          ? {
              position: 'fixed',
              inset: 0,
              zIndex: 9999,
              background: 'var(--md-editor-bg, #fff)',
              padding: 12,
            }
          : undefined
      }
    >
      <MDEditor
        value={value}
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
    </div>
  );
};
