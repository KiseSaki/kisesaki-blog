import { Col, DatePicker, Form, Input, Row, Select, Switch } from 'antd';

interface PublishSettingsTabProps {
  // 是否为编辑模式
  isEditMode: boolean;
}

/**
 * 发布设置标签页组件
 * 包含状态、可见性、密码保护、计划发布时间等设置
 */
export const PublishSettingsTab = ({ isEditMode }: PublishSettingsTabProps) => {
  return (
    <Row gutter={16}>
      <Col span={8}>
        <Form.Item label='状态' name='status'>
          <Select>
            <Select.Option value='draft'>草稿</Select.Option>
            <Select.Option value='published'>已发布</Select.Option>
            <Select.Option value='archived'>已归档</Select.Option>
          </Select>
        </Form.Item>
      </Col>

      <Col span={8}>
        <Form.Item label='可见性' name='visibility'>
          <Select>
            <Select.Option value='public'>公开</Select.Option>
            <Select.Option value='private'>私密</Select.Option>
            <Select.Option value='password_protected'>密码保护</Select.Option>
          </Select>
        </Form.Item>
      </Col>

      <Col span={8}>
        <Form.Item
          noStyle
          shouldUpdate={(prevValues, currentValues) =>
            prevValues.visibility !== currentValues.visibility
          }
        >
          {({ getFieldValue }) => {
            const visibility = getFieldValue('visibility');
            return visibility === 'password_protected' ? (
              <Form.Item
                label='访问密码'
                name='password'
                rules={[
                  {
                    required: true,
                    message: '密码保护时必须设置访问密码',
                  },
                ]}
              >
                <Input.Password placeholder='请输入访问密码' />
              </Form.Item>
            ) : null;
          }}
        </Form.Item>
      </Col>

      <Col span={12}>
        <Form.Item label='计划发布时间' name='scheduledAt'>
          <DatePicker
            showTime
            placeholder='选择发布时间'
            className='w-full'
            format='YYYY-MM-DD HH:mm:ss'
          />
        </Form.Item>
      </Col>

      {!isEditMode && (
        <Col span={12}>
          <Form.Item label='立即发布' name='publishNow' valuePropName='checked'>
            <Switch />
          </Form.Item>
          <p className='text-sm text-theme-secondary-text mt-2'>
            开启后将忽略状态设置，立即发布文章
          </p>
        </Col>
      )}
    </Row>
  );
};
