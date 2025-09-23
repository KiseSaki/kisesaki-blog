import {
  Button,
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
  Input,
} from '@/components';
import { GithubOutlined, GitlabOutlined } from '@ant-design/icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import z from 'zod';

/**
 * 用户登录页面
 * 支持邮箱密码登录和 GitHub/Gitee OAuth 登录
 */

// 表单验证模式
const loginSchema = z.object({
  username: z
    .string()
    .min(3, '用户名至少3个字符')
    .max(50, '用户名最多50个字符'),
  password: z.string().min(6, '密码至少6个字符').max(100, '密码最多100个字符'),
  remember: z.boolean().default(false).optional(),
});
type LoginFormData = z.infer<typeof loginSchema>;

const LoginPage = () => {
  const form = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: '',
      password: '',
      remember: false,
    },
  });

  return (
    <div className='min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8 text-sm'>
      <div className='max-w-md w-full space-y-8'>
        <Card className='shadow-lg'>
          <CardHeader className='text-center'>
            <CardTitle className='text-lg font-bold'>
              欢迎访问KiseSaki的博客
            </CardTitle>
            <CardDescription>
              没有账户？
              <a href='/auth/register' className='text-primary'>
                {' '}
                注册一个
              </a>
            </CardDescription>
          </CardHeader>

          <CardContent>
            <Form {...form}>
              <form
                className='space-y-4'
                onSubmit={form.handleSubmit(data => console.log(data))}
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
                        <Input
                          type='password'
                          placeholder='请输入密码'
                          {...field}
                        />
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
                            checked={field.value}
                            onChange={field.onChange}
                          />
                        </FormControl>
                        <FormLabel className='text-sm font-normal'>
                          记住我
                        </FormLabel>
                      </FormItem>
                    )}
                  />
                  <a href='/auth/forgot-password' className='text-primary'>
                    忘记密码？
                  </a>
                </div>

                <Button type='submit' className='w-full'>
                  登录
                </Button>
              </form>
            </Form>
          </CardContent>

          <CardFooter className='flex flex-col space-y-4'>
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
          </CardFooter>
        </Card>
      </div>
    </div>
  );
};

export default LoginPage;
