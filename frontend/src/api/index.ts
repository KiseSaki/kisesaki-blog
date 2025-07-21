import client from './client';

/**
 * API 客户端测试函数
 * 用于验证 API 客户端配置是否正确
 */
export const testApiClient = async () => {
  try {
    // 这里可以测试一个简单的健康检查接口
    const response = await client.get('/health');
    console.log('API 客户端配置正常:', response);
    return response;
  } catch (error) {
    console.error('API 客户端测试失败:', error);
    throw error;
  }
};

// 导出客户端供其他模块使用
export { default } from './client';
