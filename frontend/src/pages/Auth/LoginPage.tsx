import {
  Button,
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
  Input,
} from '@/components';
import AuthLayout from '@/components/common/AuthLayout';
import { FORGOT_PASSWORD_LINK, REGISTER_LINK } from '@/config';
import { useAuth } from '@/hooks';
import { loginSchema, type LoginFormData } from '@/shcema';
import { GithubOutlined, GitlabOutlined } from '@ant-design/icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';

/**
 * 用户登录页面
 * 支持邮箱密码登录和 GitHub/Gitee OAuth 登录
 */
const LoginPage = () => {
  const { login, loginLoading } = useAuth();
  const navigation = useNavigate();

  /**
   * 提交登录表单
   * @param data
   */
  const handleSubmit = async (data: LoginFormData) => {
    const success = await login(data);
    if (success) {
      navigation('/'); // 登录成功后跳转到首页
    }
  };

  // React Hook Form 表单实例
  const form = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: '',
      password: '',
      remember: false,
    },
  });

  return (
    <AuthLayout
      title='欢迎访问KiseSaki的博客'
      description={
        <>
          没有账户？
          <a
            className='text-primary cursor-pointer'
            onClick={() => navigation(REGISTER_LINK)}
          >
            {' '}
            注册一个
          </a>
        </>
      }
      footer={
        <>
          <div className='w-full flex flex-col space-y-4'>
            {/* 分割线 */}
            <div className='relative w-full'>
              <div className='absolute inset-0 flex items-center'>
                <span className='w-full border-t' />
              </div>
              <div className='relative flex justify-center text-xs uppercase'>
                <span className='px-4 text-muted-foreground'>
                  或使用第三方登录
                </span>
              </div>
            </div>

            {/* OAuth 登录按钮 */}
            <div className='grid grid-cols-2 gap-3'>
              <Button variant='outline' className='w-full'>
                <GithubOutlined />
                GitHub
              </Button>
              <Button variant='outline' className='w-full'>
                <GitlabOutlined className='mr-2 h-4 w-4' />
                Gitee
              </Button>
            </div>
          </div>
        </>
      }
    >
      <Form {...form}>
        <form
          className='space-y-4'
          onSubmit={form.handleSubmit(handleSubmit)} // 统一在这里处理提交
        >
          <FormField
            name='username'
            control={form.control}
            render={({ field }) => (
              <FormItem>
                <FormLabel>用户名</FormLabel>
                <FormControl>
                  <Input placeholder='请输入用户名' {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            name='password'
            control={form.control}
            render={({ field }) => (
              <FormItem>
                <FormLabel>密码</FormLabel>
                <FormControl>
                  <Input type='password' placeholder='请输入密码' {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className='flex items-center justify-between'>
            <FormField
              name='remember'
              control={form.control}
              render={({ field }) => (
                <FormItem className='flex items-center'>
                  <FormControl>
                    <Input
                      type='checkbox'
                      className='h-3 w-3 rounded-sm'
                      checked={!!field.value}
                      onChange={field.onChange}
                    />
                  </FormControl>
                  <FormLabel className='text-sm font-normal'>记住我</FormLabel>
                </FormItem>
              )}
            />
            <a
              className='text-primary cursor-pointer'
              onClick={() => navigation(FORGOT_PASSWORD_LINK)}
            >
              忘记密码？
            </a>
          </div>

          <Button type='submit' className='w-full' loading={loginLoading}>
            登录
          </Button>
        </form>
      </Form>
    </AuthLayout>
  );
};

export default LoginPage;
