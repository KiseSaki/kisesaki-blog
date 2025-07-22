/**
 * API 客户端使用示例
 * 演示如何使用封装好的 API 客户端和加载状态工具
 */

import client from './client';
// import { useApiCall, useApiData } from '../lib/api'; // 在实际使用时取消注释

// 示例：定义一个简单的 API 服务函数
export const getUserProfile = async (): Promise<{ name: string; email: string }> => {
  return client.get('/user/profile');
};

// 示例：带参数的 API 服务函数
export const getUserById = async (id: number): Promise<{ id: number; name: string }> => {
  return client.get(`/user/${id}`);
};

// 示例：POST 请求
export const updateUserProfile = async (data: {
  name: string;
  email: string;
}): Promise<{ success: boolean }> => {
  return client.post('/user/profile', data);
};

// 使用示例 Hook 的组件示例（注释形式）
/*
// 1. 使用 useApiCall 进行手动触发的请求
export const ProfileComponent = () => {
  const [state, fetchProfile] = useApiCall(getUserProfile);
  
  const handleLoadProfile = () => {
    fetchProfile();
  };
  
  if (state.loading) return <div>加载中...</div>;
  if (state.error) return <div>错误: {state.error}</div>;
  
  return (
    <div>
      <button onClick={handleLoadProfile}>加载用户信息</button>
      {state.data && (
        <div>
          <p>姓名: {state.data.name}</p>
          <p>邮箱: {state.data.email}</p>
        </div>
      )}
    </div>
  );
};

// 2. 使用 useApiData 进行自动加载的请求
export const UserDetailComponent = ({ userId }: { userId: number }) => {
  const [state, refetch] = useApiData(getUserById, userId);
  
  if (state.loading) return <div>加载中...</div>;
  if (state.error) return <div>错误: {state.error}</div>;
  
  return (
    <div>
      <button onClick={refetch}>刷新</button>
      {state.data && (
        <div>
          <p>ID: {state.data.id}</p>
          <p>姓名: {state.data.name}</p>
        </div>
      )}
    </div>
  );
};

// 3. 结合表单使用的示例
export const UpdateProfileComponent = () => {
  const [updateState, updateProfile] = useApiCall(updateUserProfile);
  const [formData, setFormData] = useState({ name: '', email: '' });
  
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateProfile(formData);
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <input 
        value={formData.name}
        onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
        placeholder="姓名"
      />
      <input 
        value={formData.email}
        onChange={(e) => setFormData(prev => ({ ...prev, email: e.target.value }))}
        placeholder="邮箱"
      />
      <button type="submit" disabled={updateState.loading}>
        {updateState.loading ? '更新中...' : '更新'}
      </button>
      {updateState.error && <div>错误: {updateState.error}</div>}
      {updateState.data?.success && <div>更新成功！</div>}
    </form>
  );
};
*/
