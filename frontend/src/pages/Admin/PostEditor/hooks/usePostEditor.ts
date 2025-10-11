import { createPostApi, getPostEditDetailApi, updatePostApi } from '@/api';
import { ADMIN_POSTS_LINK } from '@/config';
import type { PostFormData } from '@/types';
import type { FormInstance } from 'antd';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';

interface UsePostEditorOptions {
  /** 文章 ID（编辑模式） */
  id?: string;
  /** 表单实例 */
  form: FormInstance<PostFormData>;
  /** 初始化图片列表函数 */
  initImageLists?: (coverUrl?: string, featuredUrl?: string) => void;
}

/**
 * 文章编辑器 Hook
 * 管理文章的加载、保存、提交等核心逻辑
 */
export const usePostEditor = ({
  id,
  form,
  initImageLists,
}: UsePostEditorOptions) => {
  const navigate = useNavigate();
  const isEditMode = Boolean(id);

  // 保存中状态
  const [isSaving, setIsSaving] = useState(false);

  // 初始化表单默认值
  useEffect(() => {
    if (!isEditMode) {
      // 新建模式：设置默认值
      form.setFieldsValue({
        status: 'draft',
        visibility: 'public',
        isFeatured: false,
        isTop: false,
        allowComments: true,
        publishNow: false,
        tagIds: [],
      });
    } else {
      // 编辑模式：加载文章数据
      getPostEditDetailApi(Number(id)).then(response => {
        form.setFieldsValue({
          ...response,
          tags: response.tags || [],
          tagIds: response.tags?.map(tag => tag.id) || [],
        });
        // 初始化图片列表
        if (initImageLists) {
          initImageLists(response.coverImageUrl, response.featuredImageUrl);
        }
      });
    }
  }, [isEditMode, form, id, initImageLists]);

  // 处理表单提交
  const handleSubmit = async (values: PostFormData) => {
    setIsSaving(true);
    try {
      if (isEditMode) {
        await updatePostApi(Number(id), values);
      } else {
        await createPostApi(values);
      }

      // 提交成功后跳转回列表页
      navigate(ADMIN_POSTS_LINK);
    } catch (error) {
      console.error('保存失败：', error);
    } finally {
      setIsSaving(false);
    }
  };

  // 处理取消
  const handleCancel = () => {
    navigate(ADMIN_POSTS_LINK);
  };

  return {
    isEditMode,
    isSaving,
    handleSubmit,
    handleCancel,
  };
};
