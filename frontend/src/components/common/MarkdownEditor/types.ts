import type { ICommand } from '@uiw/react-md-editor';

export interface MarkdownEditorProps {
  // 编辑器内容
  value: string;
  // 内容变化回调
  onChange: (value: string) => void;
  // 占位符文本
  placeholder?: string;
  // 编辑器最小高度
  minHeight?: number;
  // 编辑器最大高度
  maxHeight?: number;
  // 预览模式：edit-仅编辑 | live-分栏预览 | preview-仅预览
  preview?: 'edit' | 'live' | 'preview';
  // 是否显示工具栏
  hideToolbar?: boolean;
  // 是否禁用
  disabled?: boolean;
  // 是否全屏
  fullscreen?: boolean;
  // 自定义工具栏命令
  commands?: ICommand[];
  // 额外的工具栏命令
  extraCommands?: ICommand[];
  // 是否启用代码高亮
  enableSyntaxHighlight?: boolean;
  // 自定义类名
  className?: string;
  // 是否启用图片上传
  enableImageUpload?: boolean;
  // 图片上传回调
  onImageUpload?: (file: File) => Promise<string>;
  // 是否启用表情选择器
  enableEmoji?: boolean;
}

/**
 * Emoji 选择器属性
 */
export interface EmojiPickerProps {
  onEmojiClick: (emoji: string) => void;
  onClose: () => void;
}
