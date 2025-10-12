/**
 * 分类表单组件
 * 用于创建和编辑分类
 */

import type { CategoryTreeResponse } from '@/types';
import { Form, Input, InputNumber, Modal, Select } from 'antd';
import { useEffect } from 'react';

const { TextArea } = Input;

interface CategoryFormProps {
  visible: boolean;
  initialValues?: Partial<CategoryTreeResponse>;
  categories: CategoryTreeResponse[];
  onSubmit: (values: {
    name: string;
    slug?: string;
    description?: string;
    parentId?: number;
    sortOrder?: number;
  }) => Promise<void>;
  onCancel: () => void;
}

export const CategoryForm = ({
  visible,
  initialValues,
  categories,
  onSubmit,
  onCancel,
}: CategoryFormProps) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (!visible) {
      form.resetFields();
    }
  }, [visible, form]);

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      await onSubmit(values);
      form.resetFields();
    } catch (error) {
      console.error('表单验证失败:', error);
    }
  };

  // 过滤掉当前分类及其子分类（编辑时）
  const filteredCategories = categories.filter(
    cat => cat.id !== initialValues?.id
  );

  return (
    <Modal
      title={initialValues ? '编辑分类' : '创建分类'}
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      okText='确定'
      cancelText='取消'
      destroyOnHidden
    >
      <Form
        form={form}
        layout='vertical'
        preserve={false}
        initialValues={initialValues}
      >
        <Form.Item
          label='分类名称'
          name='name'
          rules={[
            { required: true, message: '请输入分类名称' },
            { max: 50, message: '分类名称不能超过50个字符' },
          ]}
        >
          <Input placeholder='请输入分类名称' />
        </Form.Item>

        <Form.Item
          label='分类别名'
          name='slug'
          rules={[
            { max: 50, message: '别名不能超过50个字符' },
            {
              pattern: /^[a-z0-9-]+$/,
              message: '别名只能包含小写字母、数字和连字符',
            },
          ]}
        >
          <Input placeholder='如不填写，将自动生成' />
        </Form.Item>

        <Form.Item
          label='分类描述'
          name='description'
          rules={[{ max: 500, message: '描述不能超过500个字符' }]}
        >
          <TextArea rows={3} placeholder='请输入分类描述' />
        </Form.Item>

        <Form.Item label='父分类' name='parentId'>
          <Select
            placeholder='选择父分类（可选）'
            allowClear
            showSearch
            optionFilterProp='label'
          >
            {filteredCategories.map(cat => (
              <Select.Option key={cat.id} value={cat.id} label={cat.name}>
                {cat.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label='排序'
          name='sortOrder'
          initialValue={0}
          rules={[{ type: 'number', min: 0, message: '排序值不能为负数' }]}
        >
          <InputNumber
            min={0}
            placeholder='排序值（越小越靠前）'
            style={{ width: '100%' }}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};
