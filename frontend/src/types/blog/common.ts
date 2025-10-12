/**
 * 博客通用类型定义
 */

// =================== 作者信息 ===================

/**
 * 作者信息
 * 对应后端：AuthorInfo
 */
export interface AuthorInfo {
  id: number;
  username: string;
  displayName: string;
  avatarUrl: string | null;
  bio: string | null;
}
