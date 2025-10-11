import { Col, Form, Input, Row } from 'antd';

const { TextArea } = Input;

/**
 * SEO 设置标签页组件
 * 包含 SEO 标题、描述、关键词等设置
 */
export const SeoSettingsTab = () => {
  return (
    <Row gutter={16}>
      <Col span={24}>
        <Form.Item
          label='SEO 标题'
          name='seoTitle'
          tooltip='留空则使用文章标题'
          rules={[
            {
              max: 200,
              message: 'SEO标题长度不能超过200个字符',
            },
          ]}
        >
          <Input placeholder='SEO 标题（可选）' />
        </Form.Item>
      </Col>

      <Col span={24}>
        <Form.Item
          label='SEO 描述'
          name='seoDescription'
          tooltip='留空则使用文章摘要'
          rules={[
            {
              max: 500,
              message: 'SEO描述长度不能超过500个字符',
            },
          ]}
        >
          <TextArea
            placeholder='SEO 描述（可选）'
            rows={3}
            showCount
            maxLength={500}
          />
        </Form.Item>
      </Col>

      <Col span={24}>
        <Form.Item
          label='SEO 关键词'
          name='seoKeywords'
          tooltip='多个关键词用逗号分隔'
          rules={[
            {
              max: 1000,
              message: 'SEO关键词长度不能超过1000个字符',
            },
          ]}
        >
          <Input placeholder='关键词1, 关键词2, 关键词3' />
        </Form.Item>
      </Col>
    </Row>
  );
};
