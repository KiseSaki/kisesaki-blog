/**
 * 用户头像上传组件
 * 支持点击上传和拖拽上传，带图片预览功能
 */
import { Upload, X } from 'lucide-react';
import React, { useCallback, useRef, useState } from 'react';
import { toast } from 'sonner';

import { useUser } from '@/hooks';
import { cn } from '@/lib';
import { Button } from '../ui';
import UserAvatar from './UserAvatar';

interface UserAvatarUploaderProps {
  // 当前头像 URL
  currentAvatarUrl?: string;
  // 用户名称，用于 fallback 显示
  userName?: string;
  // 上传成功回调
  onUploadSuccess?: (avatarUrl: string) => void;
  // 上传失败回调
  onUploadError?: (error: Error) => void;
  // 自定义样式
  className?: string;
  // 是否显示编辑按钮
  showEditButton?: boolean;
  // 允许的最大文件大小（字节），默认 5MB
  maxSize?: number;
  // 允许的文件类型，默认 image
  accept?: string;
}

/**
 * UserAvatarUploader 组件
 * 提供头像上传功能，支持点击上传和拖拽上传
 */
const UserAvatarUploader: React.FC<UserAvatarUploaderProps> = ({
  currentAvatarUrl,
  userName,
  onUploadSuccess,
  onUploadError,
  className,
  showEditButton = true,
  maxSize = 5 * 1024 * 1024, // 5MB
  accept = 'image/*',
}) => {
  // 使用 useUser hook
  const { profile, loading } = useUser();

  const [isDragging, setIsDragging] = useState(false);
  const [previewUrl, setPreviewUrl] = useState<string | undefined>(
    currentAvatarUrl
  );
  const fileInputRef = useRef<HTMLInputElement>(null);

  // 获取上传状态
  const isUploading = loading.uploadingAvatar;

  // 验证文件是否符合要求
  const validateFile = useCallback(
    (file: File): boolean => {
      // 检查文件类型
      if (!file.type.startsWith('image/')) {
        toast.error('只能上传图片文件');
        return false;
      }

      // 检查文件大小
      if (file.size > maxSize) {
        toast.error(`文件大小不能超过 ${(maxSize / 1024 / 1024).toFixed(0)}MB`);
        return false;
      }

      return true;
    },
    [maxSize]
  );

  // 处理文件上传
  const handleFileUpload = useCallback(
    async (file: File) => {
      if (!validateFile(file)) {
        return;
      }

      // 生成本地预览
      const reader = new FileReader();
      reader.onload = e => {
        setPreviewUrl(e.target?.result as string);
      };
      reader.readAsDataURL(file);

      // 使用 useUser hook 上传文件
      const userInfo = await profile.uploadAvatar(file);

      if (userInfo) {
        const newAvatarUrl = userInfo.avatarUrl;
        setPreviewUrl(newAvatarUrl);
        onUploadSuccess?.(newAvatarUrl ?? '');
      } else {
        // 上传失败，恢复到原始头像
        setPreviewUrl(currentAvatarUrl);
        onUploadError?.(new Error('头像上传失败'));
      }
    },
    [validateFile, currentAvatarUrl, onUploadSuccess, onUploadError, profile]
  );

  // 处理点击上传
  const handleClick = useCallback(() => {
    fileInputRef.current?.click();
  }, []);

  // 处理文件选择
  const handleFileChange = useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) => {
      const file = event.target.files?.[0];
      if (file) {
        handleFileUpload(file);
      }
      // 重置 input value，允许重复选择同一文件
      event.target.value = '';
    },
    [handleFileUpload]
  );

  // 处理拖拽进入
  const handleDragEnter = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(true);
  }, []);

  // 处理拖拽离开
  const handleDragLeave = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);
  }, []);

  // 处理拖拽悬停
  const handleDragOver = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
  }, []);

  // 处理拖拽释放
  const handleDrop = useCallback(
    (e: React.DragEvent) => {
      e.preventDefault();
      e.stopPropagation();
      setIsDragging(false);

      const files = e.dataTransfer.files;
      if (files.length > 0) {
        handleFileUpload(files[0]);
      }
    },
    [handleFileUpload]
  );

  // 清除预览（恢复到原始头像）
  const handleClearPreview = useCallback(() => {
    setPreviewUrl(currentAvatarUrl);
  }, [currentAvatarUrl]);

  return (
    <div className={cn('flex flex-col items-center gap-4', className)}>
      {/* 头像显示区域 */}
      <div
        className={cn(
          'group relative',
          isDragging && 'ring-2 ring-primary ring-offset-2'
        )}
        onDragEnter={handleDragEnter}
        onDragLeave={handleDragLeave}
        onDragOver={handleDragOver}
        onDrop={handleDrop}
      >
        {/* 用户头像 */}
        <UserAvatar
          src={previewUrl}
          name={userName}
          className='h-24 w-24 md:h-32 md:w-32'
        />

        {/* 上传遮罩层 */}
        <div
          className={cn(
            'absolute inset-0 flex items-center justify-center rounded-full bg-black/50 opacity-0 transition-opacity group-hover:opacity-100',
            isUploading && 'opacity-100'
          )}
        >
          {isUploading ? (
            <div className='flex flex-col items-center gap-1'>
              <div className='h-6 w-6 animate-spin rounded-full border-2 border-white border-t-transparent' />
              <span className='text-xs text-white'>上传中...</span>
            </div>
          ) : (
            <button
              onClick={handleClick}
              className='flex flex-col items-center gap-1 text-white transition-transform hover:scale-110'
              disabled={isUploading}
              aria-label='上传头像'
            >
              <Upload className='h-6 w-6' />
              <span className='text-xs'>更换头像</span>
            </button>
          )}
        </div>

        {/* 清除预览按钮（仅在有预览且与当前头像不同时显示） */}
        {previewUrl && previewUrl !== currentAvatarUrl && !isUploading && (
          <button
            onClick={handleClearPreview}
            className='absolute -right-2 -top-2 rounded-full bg-destructive p-1 text-white shadow-md transition-transform hover:scale-110'
            aria-label='取消预览'
          >
            <X className='h-4 w-4' />
          </button>
        )}
      </div>

      {/* 文件输入 */}
      <input
        ref={fileInputRef}
        type='file'
        accept={accept}
        onChange={handleFileChange}
        className='hidden'
        aria-label='选择头像文件'
      />

      {/* 上传按钮（可选） */}
      {showEditButton && (
        <div className='flex flex-col items-center gap-2'>
          <Button
            variant='outline'
            size='sm'
            onClick={handleClick}
            disabled={isUploading}
            loading={isUploading}
          >
            <Upload className='mr-2 h-4 w-4' />
            {isUploading ? '上传中...' : '选择图片'}
          </Button>
          <p className='text-xs text-muted-foreground'>
            支持 JPG、PNG、GIF 格式，最大 {(maxSize / 1024 / 1024).toFixed(0)}
            MB
          </p>
        </div>
      )}

      {/* 拖拽提示 */}
      {isDragging && (
        <div className='pointer-events-none absolute inset-0 flex items-center justify-center rounded-lg border-2 border-dashed border-primary bg-primary/5'>
          <p className='text-sm font-medium text-primary'>释放以上传图片</p>
        </div>
      )}
    </div>
  );
};

export default UserAvatarUploader;
