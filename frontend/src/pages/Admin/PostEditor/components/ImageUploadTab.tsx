import { PlusOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { Col, Form, Row, Upload } from 'antd';

interface ImageUploadTabProps {
  // 封面图片上传配置
  coverUploadProps: UploadProps;
  // 特色图片上传配置
  featuredUploadProps: UploadProps;
  // 上传中状态
  uploading: boolean;
}

/**
 * 图片上传标签页组件
 * 包含封面图片和特色图片的上传功能
 */
export const ImageUploadTab = ({
  coverUploadProps,
  featuredUploadProps,
  uploading,
}: ImageUploadTabProps) => {
  return (
    <Row gutter={16}>
      <Col span={12}>
        <Form.Item label='封面图片' name='coverImageUrl'>
          <Upload {...coverUploadProps}>
            {(coverUploadProps.fileList?.length ?? 0) === 0 && (
              <div>
                <PlusOutlined />
                <div style={{ marginTop: 8 }}>
                  {uploading ? '上传中...' : '上传封面'}
                </div>
              </div>
            )}
          </Upload>
        </Form.Item>
        <p className='text-sm text-theme-secondary-text mt-2'>
          支持 JPG、PNG、GIF、WebP 格式，推荐尺寸：1200x630px，不超过 2MB
        </p>
      </Col>

      <Col span={12}>
        <Form.Item label='特色图片' name='featuredImageUrl'>
          <Upload {...featuredUploadProps}>
            {(featuredUploadProps.fileList?.length ?? 0) === 0 && (
              <div>
                <PlusOutlined />
                <div style={{ marginTop: 8 }}>
                  {uploading ? '上传中...' : '上传特色图片'}
                </div>
              </div>
            )}
          </Upload>
        </Form.Item>
        <p className='text-sm text-theme-secondary-text mt-2'>
          支持 JPG、PNG、GIF、WebP 格式，推荐尺寸：1200x630px，不超过 2MB
        </p>
      </Col>
    </Row>
  );
};
