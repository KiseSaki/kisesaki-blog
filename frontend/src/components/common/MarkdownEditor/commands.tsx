import type { ICommand } from '@uiw/react-md-editor';
import type { ReactElement } from 'react';

/**
 * 创建图片上传命令
 */
export const createImageUploadCommand = (
  onImageUpload?: (file: File) => Promise<string>
): ICommand => ({
  name: 'image-upload',
  keyCommand: 'image-upload',
  buttonProps: { 'aria-label': '上传图片', title: '上传图片 (支持拖拽)' },
  icon: (
    <svg width='12' height='12' viewBox='0 0 20 20'>
      <path
        fill='currentColor'
        d='M15 9c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2zm4-7H1c-.55 0-1 .45-1 1v14c0 .55.45 1 1 1h18c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm-1 13l-6-5-2 2-4-5-4 8V4h16v11z'
      />
    </svg>
  ),
  execute: (state, api) => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';
    input.onchange = async (e) => {
      const file = (e.target as HTMLInputElement).files?.[0];
      if (!file) return;

      // 验证文件类型
      if (!file.type.startsWith('image/')) {
        alert('请选择图片文件');
        return;
      }

      // 验证文件大小 (5MB)
      if (file.size > 5 * 1024 * 1024) {
        alert('图片大小不能超过 5MB');
        return;
      }

      try {
        let imageUrl: string;

        if (onImageUpload) {
          // 使用自定义上传函数
          imageUrl = await onImageUpload(file);
        } else {
          // 默认转换为 base64
          imageUrl = await new Promise<string>((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result as string);
            reader.onerror = reject;
            reader.readAsDataURL(file);
          });
        }

        // 插入图片 Markdown 语法
        const imageText = `![${file.name}](${imageUrl})`;
        api.replaceSelection(imageText);
      } catch (error) {
        console.error('图片上传失败:', error);
        alert('图片上传失败，请重试');
      }
    };
    input.click();
  },
});

/**
 * 创建表情选择器命令
 */
export const createEmojiCommand = (
  onToggleEmojiPicker: () => void
): ICommand => ({
  name: 'emoji',
  keyCommand: 'emoji',
  buttonProps: { 'aria-label': '插入表情', title: '插入表情' },
  icon: (
    <svg width='12' height='12' viewBox='0 0 24 24'>
      <path
        fill='currentColor'
        d='M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm3.5-9c.83 0 1.5-.67 1.5-1.5S16.33 8 15.5 8 14 8.67 14 9.5s.67 1.5 1.5 1.5zm-7 0c.83 0 1.5-.67 1.5-1.5S9.33 8 8.5 8 7 8.67 7 9.5 7.67 11 8.5 11zm3.5 6.5c2.33 0 4.31-1.46 5.11-3.5H6.89c.8 2.04 2.78 3.5 5.11 3.5z'
      />
    </svg>
  ),
  execute: () => {
    onToggleEmojiPicker();
  },
});
