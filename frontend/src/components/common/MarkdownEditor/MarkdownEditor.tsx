import { ScrollArea } from '@/components/ui';
import { useTheme } from '@/stores/themeStore';
import MDEditor, { commands as Commands } from '@uiw/react-md-editor';
import { useEffect, useState } from 'react';
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
    <ScrollArea
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
    </ScrollArea>
  );
};
