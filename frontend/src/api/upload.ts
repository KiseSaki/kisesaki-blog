import client from '../lib/client';

/**
 * 图片上传 API
 */
export const uploadApi = {
  /**
   * 上传图片
   * @param file 图片文件
   * @param type 上传类型 ('avatar' | 'post' | 'comment')
   * @returns 图片 URL
   */
  uploadImage: async (
    file: File,
    type: 'avatar' | 'post' | 'comment' = 'comment'
  ): Promise<string> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    const response = await client.post<string>('/file/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response;
  },

  /**
   * 批量上传图片
   * @param files 图片文件数组
   * @param type 上传类型
   * @returns 图片 URL 数组
   */
  uploadImages: async (
    files: File[],
    type: 'avatar' | 'post' | 'comment' = 'comment'
  ): Promise<string[]> => {
    // 批量上传：逐个调用单图上传接口
    const uploadPromises = files.map(file => uploadApi.uploadImage(file, type));
    return Promise.all(uploadPromises);
  },
};
