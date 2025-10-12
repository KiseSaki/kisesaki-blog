import { uploadApi } from '@/api';
import { API_UTILS } from '@/config';
import type { FormInstance, UploadFile, UploadProps } from 'antd';
import { message } from 'antd';
import { useCallback, useState } from 'react';

/**
 * 图片上传配置常量
 */
const UPLOAD_CONFIG = {
  MAX_SIZE: 2 * 1024 * 1024, // 2MB
  ACCEPTED_TYPES: [
    'image/jpeg',
    'image/jpg',
    'image/png',
    'image/gif',
    'image/webp',
  ],
  ACCEPTED_EXTENSIONS: ['.jpg', '.jpeg', '.png', '.gif', '.webp'],
} as const;

interface UseImageUploadProps {
  form: FormInstance;
}

/**
 * 图片上传 Hook
 * 管理封面图片和特色图片的上传状态
 */
export const useImageUpload = ({ form }: UseImageUploadProps) => {
  // 封面图片列表
  const [coverImageList, setCoverImageList] = useState<UploadFile[]>([]);
  // 特色图片列表
  const [featuredImageList, setFeaturedImageList] = useState<UploadFile[]>([]);
  // 上传中状态
  const [uploading, setUploading] = useState(false);

  /**
   * 验证文件
   */
  const validateFile = (file: File): boolean => {
    // 验证文件类型
    const fileType = file.type as (typeof UPLOAD_CONFIG.ACCEPTED_TYPES)[number];
    if (!UPLOAD_CONFIG.ACCEPTED_TYPES.includes(fileType)) {
      message.error('只支持上传 JPG、PNG、GIF、WebP 格式的图片！');
      return false;
    }

    // 验证文件大小
    if (file.size > UPLOAD_CONFIG.MAX_SIZE) {
      message.error('图片大小不能超过 2MB！');
      return false;
    }

    return true;
  };

  /**
   * 处理图片上传
   * @param file 要上传的文件
   * @param type 上传类型（'cover' | 'featured'）
   */
  const handleUpload = async (
    file: File,
    type: 'cover' | 'featured'
  ): Promise<void> => {
    // 验证文件
    if (!validateFile(file)) {
      return;
    }

    const setFileList =
      type === 'cover' ? setCoverImageList : setFeaturedImageList;
    const formFieldName =
      type === 'cover' ? 'coverImageUrl' : 'featuredImageUrl';

    // 创建临时文件对象（显示上传中状态）
    const tempFile: UploadFile = {
      uid: file.name,
      name: file.name,
      status: 'uploading',
      percent: 0,
    };

    setFileList([tempFile]);
    setUploading(true);

    try {
      // 调用上传 API
      const url = await uploadApi.uploadImage(file, 'post');

      // 更新文件列表为成功状态
      const uploadedFile: UploadFile = {
        uid: file.name,
        name: file.name,
        status: 'done',
        url,
        thumbUrl: API_UTILS.buildFileUrl(url),
      };

      setFileList([uploadedFile]);

      // 同步更新表单字段
      form.setFieldValue(formFieldName, url);

      message.success('图片上传成功！');
    } catch (error) {
      console.error('图片上传失败:', error);

      // 更新文件列表为失败状态
      const failedFile: UploadFile = {
        uid: file.name,
        name: file.name,
        status: 'error',
      };

      setFileList([failedFile]);

      message.error('图片上传失败，请重试！');
    } finally {
      setUploading(false);
    }
  };

  /**
   * 处理图片移除
   * @param type 上传类型（'cover' | 'featured'）
   */
  const handleRemove = (type: 'cover' | 'featured') => {
    const setFileList =
      type === 'cover' ? setCoverImageList : setFeaturedImageList;
    const formFieldName =
      type === 'cover' ? 'coverImageUrl' : 'featuredImageUrl';

    // 清空文件列表
    setFileList([]);

    // 清空表单字段
    form.setFieldValue(formFieldName, undefined);
  };

  /**
   * 生成封面图片上传配置
   */
  const getCoverUploadProps = (): UploadProps => ({
    listType: 'picture-card',
    fileList: coverImageList,
    accept: UPLOAD_CONFIG.ACCEPTED_EXTENSIONS.join(','),
    maxCount: 1,
    beforeUpload: file => {
      handleUpload(file, 'cover');
      return false; // 阻止默认上传
    },
    onRemove: () => {
      handleRemove('cover');
    },
    disabled: uploading,
  });

  /**
   * 生成特色图片上传配置
   */
  const getFeaturedUploadProps = (): UploadProps => ({
    listType: 'picture-card',
    fileList: featuredImageList,
    accept: UPLOAD_CONFIG.ACCEPTED_EXTENSIONS.join(','),
    maxCount: 1,
    beforeUpload: file => {
      handleUpload(file, 'featured');
      return false; // 阻止默认上传
    },
    onRemove: () => {
      handleRemove('featured');
    },
    disabled: uploading,
  });

  /**
   * 初始化图片列表（用于编辑模式）
   */
  const initImageLists = useCallback(
    (coverUrl?: string, featuredUrl?: string) => {
      if (coverUrl) {
        setCoverImageList([
          {
            uid: '-1',
            name: 'cover.jpg',
            status: 'done',
            url: coverUrl,
            thumbUrl: API_UTILS.buildFileUrl(coverUrl),
          },
        ]);
      }

      if (featuredUrl) {
        setFeaturedImageList([
          {
            uid: '-1',
            name: 'featured.jpg',
            status: 'done',
            url: featuredUrl,
            thumbUrl: API_UTILS.buildFileUrl(featuredUrl),
          },
        ]);
      }
    },
    []
  );

  return {
    coverImageList,
    featuredImageList,
    uploading,
    getCoverUploadProps,
    getFeaturedUploadProps,
    initImageLists,
  };
};
