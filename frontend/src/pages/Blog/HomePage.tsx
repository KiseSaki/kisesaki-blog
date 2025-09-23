/**
 * 博客首页
 * 展示最新文章列表、置顶文章、分页等
 */
import { Typography } from 'antd';
import React from 'react';

const { Title, Paragraph } = Typography;

const HomePage: React.FC = () => {
  return (
    <div className='theme-bg min-h-screen'>
      <div className='max-w-4xl mx-auto p-6'>
        <div className='text-center mb-8'>
          <Title level={1} className='theme-text-primary'>
            Kisesaki Blog
          </Title>
          <Paragraph className='theme-text-secondary text-lg'>
            欢迎来到我的个人博客
          </Paragraph>
        </div>

        {/* TODO: 在这里添加文章列表、分页等内容 */}
        <div className='text-center'>
          <Paragraph className='theme-text-secondary'>
            博客内容正在开发中...
          </Paragraph>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
