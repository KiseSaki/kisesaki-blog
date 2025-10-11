/**
 * 用户角色编辑表单组件
 */

import { Checkbox, Form, Modal } from 'antd';
import { useEffect } from 'react';
import { roleOptions } from '../config';

interface UserRoleFormProps {
  visible: boolean;
  initialRoles?: string[];
  onSubmit: (roles: string[]) => Promise<void>;
  onCancel: () => void;
}

export const UserRoleForm = ({
  visible,
  initialRoles = [],
  onSubmit,
  onCancel,
}: UserRoleFormProps) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (visible) {
      form.setFieldsValue({ roles: initialRoles });
    } else {
      form.resetFields();
    }
  }, [visible, initialRoles, form]);

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      await onSubmit(values.roles || []);
      form.resetFields();
    } catch (error) {
      console.error('表单验证失败:', error);
    }
  };

  return (
    <Modal
      title='编辑用户角色'
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      okText='确定'
      cancelText='取消'
      destroyOnClose
    >
      <Form form={form} layout='vertical' preserve={false}>
        <Form.Item
          label='用户角色'
          name='roles'
          rules={[{ required: true, message: '请至少选择一个角色' }]}
        >
          <Checkbox.Group options={roleOptions} />
        </Form.Item>
      </Form>
    </Modal>
  );
};
