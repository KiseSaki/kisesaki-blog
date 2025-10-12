import type { PostFormData } from '@/types';
import { Col, Form, Input, Row, Switch, type FormInstance } from 'antd';
import { useParams } from 'react-router';

/**
 * 文章选项标签页组件
 * 包含精选、置顶、允许评论等选项
 */
export const PostOptionsTab = ({
  form,
}: {
  form: FormInstance<PostFormData>;
}) => {
  const { id } = useParams<{ id?: string }>();

  // 监听 createRevision 字段，响应式触发渲染
  const createRevision = Form.useWatch('createRevision', form);

  return (
    <Row gutter={16}>
      <Col xs={24} sm={12} md={8}>
        <Form.Item label='精选文章' name='isFeatured' valuePropName='checked'>
          <Switch />
        </Form.Item>
      </Col>

      <Col xs={24} sm={12} md={8}>
        <Form.Item label='置顶' name='isTop' valuePropName='checked'>
          <Switch />
        </Form.Item>
      </Col>

      <Col xs={24} sm={12} md={8}>
        <Form.Item
          label='允许评论'
          name='allowComments'
          valuePropName='checked'
        >
          <Switch />
        </Form.Item>
      </Col>

      {id && (
        // 保持响应式列，和下面备注合并在同一行
        <Col xs={24} sm={12} md={8}>
          <Form.Item
            label='是否创建新版本'
            name='createRevision'
            valuePropName='checked'
          >
            <Switch />
          </Form.Item>
        </Col>
      )}

      {id && createRevision && (
        <Col xs={24} sm={12} md={16}>
          <Form.Item label='版本备注' name='revisionNote'>
            <Input.TextArea rows={4} />
          </Form.Item>
        </Col>
      )}
    </Row>
  );
};
