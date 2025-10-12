/**
 * 系统设置数据 Hook
 * 封装系统设置相关数据获取逻辑和操作方法
 */

import { Form, message } from 'antd';
import { useCallback, useState } from 'react';

export const useSettings = () => {
  const [form] = Form.useForm();
  const [isSaving, setIsSaving] = useState(false);

  // 保存设置
  const handleSave = useCallback(async () => {
    try {
      const values = await form.validateFields();
      setIsSaving(true);

      // TODO: 调用保存设置的API
      console.log('保存设置:', values);

      // 模拟API请求
      await new Promise(resolve => setTimeout(resolve, 1000));

      message.success('设置保存成功');
    } catch (error) {
      console.error('保存设置失败:', error);
      message.error('设置保存失败');
    } finally {
      setIsSaving(false);
    }
  }, [form]);

  // 重置设置
  const handleReset = useCallback(() => {
    form.resetFields();
    message.info('已重置为默认值');
  }, [form]);

  return {
    form,
    isSaving,
    handleSave,
    handleReset,
  };
};
