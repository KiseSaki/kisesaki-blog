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
import { useRegister } from '@/hooks';
import { type RegisterFormData, registerSchema } from '@/shcema';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';

/**
 * 用户注册页面
 * 支持邮箱注册和表单验证
 */
const RegisterPage = () => {
  // React Hook Form 表单实例
  const form = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
  });

  const { register } = useRegister();
  // 表单提交处理函数
  const handleSubmit = async (data: RegisterFormData) => {
    const success = await register(data);
    if (success) {
      form.reset(); // 注册成功后重置表单
    }
  };

  return (
    <AuthLayout
      title='欢迎注册KiseSaki的博客'
      description={
        <>
          已有账户？{' '}
          <a href='/auth/login' className='text-primary'>
            去登录
          </a>
        </>
      }
    >
      <Form {...form}>
        <form className='space-y-4' onSubmit={form.handleSubmit(handleSubmit)}>
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

          <FormField
            name='confirmPassword'
            control={form.control}
            render={({ field }) => (
              <FormItem>
                <FormLabel>确认密码</FormLabel>
                <FormControl>
                  <Input type='password' placeholder='请确认密码' {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            name='email'
            control={form.control}
            render={({ field }) => (
              <FormItem>
                <FormLabel>邮箱</FormLabel>
                <FormControl>
                  <Input type='email' placeholder='请输入邮箱' {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <Button className='w-full'>注册</Button>
        </form>
      </Form>
    </AuthLayout>
  );
};

export default RegisterPage;
