import { MarkdownEditor } from '@/components';
import { Card, Col, Form, Input, Row, Select } from 'antd';

const { TextArea } = Input;

interface BasicInfoFormProps {
  // 分类列表
  categories: { label: string; value: number }[];
  // 标签列表
  tags: { label: string; value: number }[];
  // 分类搜索处理
  onCategorySearch?: (value: string) => void;
  // 分类下拉框滚动处理
  onCategoryScroll?: (event: React.UIEvent<HTMLElement>) => void;
  // 标签搜索处理
  onTagSearch?: (value: string) => void;
  // 标签下拉框滚动处理
  onTagScroll?: (event: React.UIEvent<HTMLElement>) => void;
}

/**
 * 基础信息表单组件
 * 包含文章的基本字段：标题、URL别名、分类、摘要、正文、标签、作者
 */
export const BasicInfoForm = ({
  categories,
  tags,
  onCategorySearch,
  onCategoryScroll,
  onTagSearch,
  onTagScroll,
}: BasicInfoFormProps) => {
  return (
    <Card title='基础信息' className='bg-card'>
      <Row gutter={16}>
        <Col span={24}>
          <Form.Item
            label='文章标题'
            name='title'
            rules={[
              { required: true, message: '请输入文章标题' },
              { max: 200, message: '标题长度不能超过200个字符' },
            ]}
          >
            <Input placeholder='请输入文章标题' size='large' />
          </Form.Item>
        </Col>

        <Col span={24}>
          <Form.Item
            label='URL 别名'
            name='slug'
            tooltip='留空则自动生成，只能包含小写字母、数字和短横线'
            rules={[
              {
                pattern: /^[a-z0-9-]*$/,
                message: 'URL别名只能包含小写字母、数字和短横线',
              },
              { max: 200, message: 'URL别名长度不能超过200个字符' },
            ]}
          >
            <Input placeholder='auto-generated-slug' />
          </Form.Item>
        </Col>

        <Col span={12}>
          <Form.Item
            label='分类'
            name='categoryId'
            rules={[{ required: true, message: '请选择文章分类' }]}
          >
            <Select
              placeholder='选择分类'
              options={categories}
              showSearch
              filterOption={false} // 使用服务端搜索
              onSearch={onCategorySearch}
              onPopupScroll={onCategoryScroll}
            />
          </Form.Item>
        </Col>

        <Col span={12}>
          <Form.Item label='标签' name='tagIds' tooltip='最多选择5个标签'>
            <Select
              mode='multiple'
              placeholder='选择标签'
              options={tags}
              maxCount={5}
              showSearch
              filterOption={false} // 使用服务端搜索
              onSearch={onTagSearch}
              onPopupScroll={onTagScroll}
            />
          </Form.Item>
        </Col>

        <Col span={24}>
          <Form.Item
            label='摘要'
            name='excerpt'
            tooltip='文章摘要将显示在列表页和SEO描述中'
            rules={[{ max: 500, message: '摘要长度不能超过500个字符' }]}
          >
            <TextArea
              placeholder='请输入文章摘要（可选，留空则自动从正文提取）'
              rows={3}
              showCount
              maxLength={500}
            />
          </Form.Item>
        </Col>

        <Col span={24}>
          <Form.Item
            label='正文内容'
            name='content'
            rules={[{ required: true, message: '请输入文章正文' }]}
          >
            <MarkdownEditor
              placeholder='请输入正文'
              minHeight={400}
              preview='live'
              enableImageUpload
            />
          </Form.Item>
        </Col>
      </Row>
    </Card>
  );
};
