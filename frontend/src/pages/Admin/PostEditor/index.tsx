import { UserLayout } from '@/components';
import type { PostFormData } from '@/types';
import { SaveOutlined } from '@ant-design/icons';
import { Button, Card, Form, Space, Tabs } from 'antd';
import { useParams } from 'react-router';
import {
  BasicInfoForm,
  ImageUploadTab,
  PostOptionsTab,
  PublishSettingsTab,
  SeoSettingsTab,
} from './components';
import { useImageUpload, usePostEditor, usePostFormData } from './hooks';

/**
 * 文章编辑器页面
 * 支持新建和编辑两种模式
 */
const PostEditor = () => {
  const { id } = useParams<{ id?: string }>();
  const [form] = Form.useForm<PostFormData>();

  // 使用自定义 hooks 管理业务逻辑
  const {
    uploading,
    getCoverUploadProps,
    getFeaturedUploadProps,
    initImageLists,
  } = useImageUpload({ form });
  const { isEditMode, isSaving, handleSubmit, handleCancel } = usePostEditor({
    id,
    form,
    initImageLists,
  });
  const {
    categories,
    tags,
    handleCategorySearch,
    handleCategoryScroll,
    handleTagSearch,
    handleTagScroll,
  } = usePostFormData({ form });

  return (
    <UserLayout
      title={isEditMode ? '编辑文章' : '新建文章'}
      description={
        isEditMode ? '编辑已有文章内容和设置' : '创建一篇新的博客文章'
      }
      showBack
      onBack={handleCancel}
    >
      <Form
        form={form}
        layout='vertical'
        onFinish={handleSubmit}
        className='flex-1 flex flex-col'
      >
        <div className='flex-1 flex flex-col gap-6'>
          {/* 基础信息表单 */}
          <BasicInfoForm
            categories={categories}
            tags={tags}
            onCategorySearch={handleCategorySearch}
            onCategoryScroll={handleCategoryScroll}
            onTagSearch={handleTagSearch}
            onTagScroll={handleTagScroll}
          />

          {/* 高级设置 - 使用 Tabs */}
          <Card title='高级设置' className='bg-card'>
            <Tabs
              defaultActiveKey='1'
              items={[
                {
                  key: '1',
                  label: '图片',
                  children: (
                    <ImageUploadTab
                      coverUploadProps={getCoverUploadProps()}
                      featuredUploadProps={getFeaturedUploadProps()}
                      uploading={uploading}
                    />
                  ),
                },
                {
                  key: '2',
                  label: '发布',
                  children: <PublishSettingsTab isEditMode={isEditMode} />,
                },
                {
                  key: '3',
                  label: '选项',
                  children: <PostOptionsTab form={form} />,
                },
                {
                  key: '4',
                  label: 'SEO',
                  children: <SeoSettingsTab />,
                },
              ]}
            />
          </Card>
        </div>

        {/* 底部操作按钮 */}
        <div className='sticky bottom-0 z-10 bg-theme-background py-4 border-t border-theme-border mt-6'>
          <Space className='w-full justify-end'>
            <Button size='large' onClick={handleCancel}>
              取消
            </Button>
            <Button
              type='default'
              size='large'
              icon={<SaveOutlined />}
              onClick={() => {
                form.setFieldValue('status', 'draft');
                form.submit();
              }}
              loading={isSaving}
            >
              保存草稿
            </Button>
            <Button
              type='primary'
              size='large'
              htmlType='submit'
              loading={isSaving}
            >
              {isEditMode ? '更新文章' : '发布文章'}
            </Button>
          </Space>
        </div>
      </Form>
    </UserLayout>
  );
};

export default PostEditor;
