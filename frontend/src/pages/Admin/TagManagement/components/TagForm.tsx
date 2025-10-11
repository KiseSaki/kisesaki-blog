/**
 * 标签表单组件
 * 用于创建和编辑标签
 */

import type { AdminTagListResponse } from '@/types';
import { Checkbox, Form, Input, Modal, Select } from 'antd';
import { useEffect } from 'react';
import { tagColorOptions } from '../config';

const { TextArea } = Input;

interface TagFormProps {
  visible: boolean;
  initialValues?: Partial<AdminTagListResponse>;
  onSubmit: (values: {
    name: string;
    slug?: string;
    description?: string;
    color?: string;
    isApproved?: boolean;
    approvalNote?: string;
  }) => Promise<void>;
  onCancel: () => void;
}

export const TagForm = ({
  visible,
  initialValues,
  onSubmit,
  onCancel,
}: TagFormProps) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (visible && initialValues) {
      form.setFieldsValue(initialValues);
    } else {
      form.resetFields();
    }
  }, [visible, initialValues, form]);

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      await onSubmit(values);
      form.resetFields();
    } catch (error) {
      console.error('表单验证失败:', error);
    }
  };

  return (
    <Modal
      title={initialValues ? '编辑标签' : '创建标签'}
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      okText='确定'
      cancelText='取消'
      destroyOnClose
    >
      <Form form={form} layout='vertical' preserve={false}>
        <Form.Item
          label='标签名称'
          name='name'
          rules={[
            { required: true, message: '请输入标签名称' },
            { max: 50, message: '标签名称不能超过50个字符' },
          ]}
        >
          <Input placeholder='请输入标签名称' />
        </Form.Item>

        <Form.Item
          label='标签别名'
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
          label='标签描述'
          name='description'
          rules={[{ max: 500, message: '描述不能超过500个字符' }]}
        >
          <TextArea rows={3} placeholder='请输入标签描述' />
        </Form.Item>

        <Form.Item label='标签颜色' name='color'>
          <Select placeholder='选择标签颜色（可选）' allowClear>
            {tagColorOptions.map(opt => (
              <Select.Option key={opt.value} value={opt.value}>
                {opt.label}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item name='isApproved' valuePropName='checked'>
          <Checkbox>直接通过审核</Checkbox>
        </Form.Item>

        <Form.Item label='审核备注' name='approvalNote'>
          <TextArea rows={2} placeholder='审核备注（可选）' />
        </Form.Item>
      </Form>
    </Modal>
  );
};
