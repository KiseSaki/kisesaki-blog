import z from 'zod';

/**
 * 认证相关基础模式（改进版）
 */

/** 用户名规则：去除两端空白，长度限制，并限制字符集（字母、数字、下划线、短横线、支持 Unicode 字母） */
const usernameSchema = z
  .string()
  .trim()
  .min(3, '用户名至少3个字符')
  .max(50, '用户名最多50个字符')
  .regex(/^[\p{L}\p{N}_-]+$/u, '用户名仅允许字母、数字、下划线或短横线');

export const passwordSchema = z
  .string()
  .min(6, '密码至少6个字符')
  .max(100, '密码最多100个字符')
  .refine(p => /[A-Za-z]/.test(p) && /[0-9]/.test(p), {
    message: '密码需包含字母和数字',
  });

/**
 * 认证基础表单验证模式
 */
export const authBaseSchema = z.object({
  username: usernameSchema,
  password: passwordSchema,
});
export type AuthBase = z.infer<typeof authBaseSchema>;

/**
 * 登录表单验证模式
 */
export const loginSchema = authBaseSchema.extend({
  remember: z.boolean().default(false).optional(),
});
export type LoginFormData = z.infer<typeof loginSchema>;

/**
 * 注册表单验证模式
 * - email 统一小写
 * - 使用 superRefine 将密码不一致的错误定位到 confirmPassword 字段
 */
export const registerSchema = z
  .object({
    username: usernameSchema,
    email: z.email('请输入有效的邮箱地址').transform(e => e.toLowerCase()),
    password: passwordSchema,
    confirmPassword: z
      .string()
      .min(6, '确认密码至少6个字符')
      .max(100, '确认密码最多100个字符'),
  })
  .superRefine((data, ctx) => {
    if (data.password !== data.confirmPassword) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: '两次输入的密码不一致',
        path: ['confirmPassword'],
      });
    }
  });

export type RegisterFormData = z.infer<typeof registerSchema>;

/**
 * 发送到后端的注册请求模式
 */
export const registerRequestSchema = registerSchema.omit({
  confirmPassword: true,
});
export type RegisterRequest = z.infer<typeof registerRequestSchema>;

/**
 * 邮箱验证表单验证模式
 */
export const emailVerificationSchema = z.object({
  emailToken: z.string().trim().min(1, '验证令牌不能为空'),
});
export type EmailVerificationFormData = z.infer<typeof emailVerificationSchema>;

/**
 * 邮箱验证
 */
export const emailSchema = z.email('请输入有效的邮箱地址').transform(e => e.toLowerCase());
export type EmailFormData = z.infer<typeof emailSchema>;