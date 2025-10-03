import client from '../lib/client';

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
export { default } from '../lib/client';

// 导出 API 工具函数
export * from '../lib/api';

// 导出各模块 API 接口
export * from './admin';
export * from './analytics';
export * from './auth';
export * from './blog';
export * from './comment';
export * from './interaction';
export * from './user';
