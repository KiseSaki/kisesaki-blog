/**
 * 用户相关类型定义
 * 包含用户信息、权限、个人资料等用户相关的数据类型
 */

/**
 * 用户基础信息
 * 对应后端 User 实体的核心字段
 */
export interface User {
  // 用户唯一ID (Snowflake)
  id: number;
  // 用户名，唯一
  username: string;
  // 电子邮箱
  email: string;
  // 账号类型，例如 'local' 或 'oauth'
  accountType?: string;
  // 用户状态，例如 'active', 'inactive', 'banned'
  status?: string;
  // 邮箱是否已验证
  emailVerified?: boolean;
  // 邮箱验证时间
  emailVerifiedAt?: string;
  // 最后登录时间
  lastLoginAt?: string;
  // 最后登录 IP
  lastLoginIp?: string;
  // 账号创建时间
  createdAt: string;
  // 账号更新时间
  updatedAt: string;
}

/**
 * 用户扩展资料
 * 对应后端 UserProfile 实体
 */
export interface UserProfile {
  // 主键ID
  id?: number;
  // 用户ID
  userId: number;
  // 显示名称
  displayName?: string;
  // 名字
  firstName?: string;
  // 姓氏
  lastName?: string;
  // 个人简介
  bio?: string;
  // 头像 URL
  avatarUrl?: string;
  // 封面图 URL
  coverImageUrl?: string;
  // 个人网站 URL
  websiteUrl?: string;
  // 所在地
  location?: string;
  // 公司/组织
  company?: string;
  // 职位标题
  title?: string;
  // 社交媒体链接
  socialLinks?: Record<string, string>;
  // 出生日期
  birthDate?: string;
  // 性别，例如 'male','female','other'
  gender?: string;
  // 时区
  timezone?: string;
  // 首选语言
  language?: string;
  // 主题偏好，例如 'light','dark','system'
  themePreference?: string;
  // 隐私级别，例如 'public','friends','private'
  privacyLevel?: string;
  // 创建时间
  createdAt?: string;
  // 更新时间
  updatedAt?: string;
}

/**
 * 用户设置
 * 对应后端 UserSettings 实体
 */
export interface UserSettings {
  // 设置唯一ID
  id?: number;
  // 用户ID
  userId: number;
  // 设置键名
  settingKey: string;
  // 设置值
  settingValue: string;
  // 更新时间
  updatedAt?: string;
}

/**
 * 用户关注关系
 * 对应后端 UserFollow 实体
 */
export interface UserFollow {
  // 关注关系ID
  id?: number;
  // 关注者ID (谁关注)
  followerId: number;
  // 被关注者ID (被谁关注)
  followingId: number;
  // 关注状态 ('active', 'blocked')
  status?: string;
  // 创建时间
  createdAt?: string;
  // 更新时间
  updatedAt?: string;
}

/**
 * 完整用户信息
 * 对应后端 UserInfoDto，包含用户基础信息、扩展资料和设置
 */
export interface UserInfo extends User {
  // UserProfile 字段
  displayName?: string;
  firstName?: string;
  lastName?: string;
  bio?: string;
  avatarUrl?: string;
  coverImageUrl?: string;
  websiteUrl?: string;
  location?: string;
  company?: string;
  title?: string;
  socialLinks?: Record<string, string>;
  birthDate?: string;
  gender?: string;
  timezone?: string;
  language?: string;
  themePreference?: string;
  privacyLevel?: string;

  // UserSettings 字段
  settings?: Record<string, string>;
}

/**
 * 用户列表项
 * 用于用户列表、搜索结果等场景的简化用户信息
 */
export interface UserListItem {
  id: number;
  username: string;
  email: string;
  displayName?: string;
  avatarUrl?: string;
  bio?: string;
  status: string;
  createdAt: string;
}

/**
 * 用户统计信息
 */
export interface UserStats {
  // 关注数
  followingCount: number;
  // 粉丝数
  followersCount: number;
  // 文章数
  postsCount: number;
  // 评论数
  commentsCount: number;
  // 点赞数
  likesCount: number;
}

/**
 * 用户资料更新参数
 */
export interface UpdateUserProfileParams {
  displayName?: string;
  firstName?: string;
  lastName?: string;
  bio?: string;
  avatarUrl?: string;
  coverImageUrl?: string;
  websiteUrl?: string;
  location?: string;
  company?: string;
  title?: string;
  socialLinks?: Record<string, string>;
  birthDate?: string;
  gender?: string;
  timezone?: string;
  language?: string;
  themePreference?: string;
  privacyLevel?: string;
}

/**
 * 用户设置更新参数
 */
export interface UpdateUserSettingsParams {
  settings: Record<string, string>;
}

/**
 * 关注/取消关注参数
 */
export interface FollowUserParams {
  userId: number;
}

/**
 * 用户搜索参数
 */
export interface SearchUsersParams {
  // 搜索关键词
  keyword?: string;
  // 状态筛选
  status?: string;
  // 账号类型筛选
  accountType?: string;
  // 页码
  page?: number;
  // 每页大小
  size?: number;
  // 排序字段
  sortBy?: string;
  // 排序方向
  sortOrder?: "asc" | "desc";
}

/**
 * 用户搜索响应
 */
export interface SearchUsersResponse {
  users: UserListItem[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}
