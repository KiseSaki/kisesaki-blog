/**
 * 设置表单组件
 * 动态生成不同类型的设置表单字段
 */

import type { SettingField } from '../config';
import { Form, Input, InputNumber, Select, Switch, Upload } from 'antd';
import { UploadOutlined } from '@ant-design/icons';

const { TextArea } = Input;
const { Password } = Input;

interface SettingFormItemProps {
  field: SettingField;
}

export const SettingFormItem = ({ field }: SettingFormItemProps) => {
  const renderInput = () => {
    switch (field.type) {
      case 'textarea':
        return <TextArea rows={4} placeholder={field.placeholder} />;

      case 'number':
        return (
          <InputNumber
            placeholder={field.placeholder}
            min={field.min}
            max={field.max}
            style={{ width: '100%' }}
          />
        );

      case 'switch':
        return <Switch />;

      case 'select':
        return (
          <Select
            placeholder={field.placeholder}
            options={field.options}
            style={{ width: '100%' }}
          />
        );

      case 'image':
        return (
          <Upload listType='picture' maxCount={1}>
            <button type='button' className='flex items-center gap-2 px-4 py-2 border rounded'>
              <UploadOutlined />
              点击上传
            </button>
          </Upload>
        );

      case 'password':
        return <Password placeholder={field.placeholder} />;

      case 'text':
      default:
        return <Input placeholder={field.placeholder} />;
    }
  };

  return (
    <Form.Item
      label={field.label}
      name={field.key}
      rules={field.required ? [{ required: true, message: `请输入${field.label}` }] : []}
      valuePropName={field.type === 'switch' ? 'checked' : 'value'}
      initialValue={field.defaultValue}
    >
      {renderInput()}
    </Form.Item>
  );
};
