import {
  Button,
  Form,
  FormField,
  FormItem,
  FormLabel,
  Input,
} from '@/components';
import AuthLayout from '@/components/common/AuthLayout';
import { usePassword } from '@/hooks';
import { emailSchema, type EmailFormData } from '@/shcema';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import z from 'zod';

/**
 * 忘记密码页面
 * 用户请求重置密码的页面
 */

const ForgotPasswordPage = () => {
  // 表单实例
  const form = useForm<{ email: EmailFormData }>({
    resolver: zodResolver(z.object({ email: emailSchema })),
    defaultValues: {
      email: '',
    },
  });

  const { forgotPassword } = usePassword();
  // 提交表单
  const handleSubmit = async (data: { email: EmailFormData }) => {
    const success = await forgotPassword(data.email);
    if (success) {
      form.reset();
    }
  };

  return (
    <AuthLayout
      title='忘记密码'
      description='请输入您的注册邮箱，我们会发送一封重置密码的邮件给您。'
    >
      <Form {...form}>
        <form className='space-y-4' onSubmit={form.handleSubmit(handleSubmit)}>
          <FormField
            name='email'
            control={form.control}
            render={({ field }) => (
              <FormItem>
                <FormLabel>邮箱地址</FormLabel>
                <Input type='email' placeholder='请输入您的邮箱' {...field} />
              </FormItem>
            )}
          />
          <Button type='submit' className='w-full'>
            发送重置密码邮件
          </Button>
        </form>
      </Form>
    </AuthLayout>
  );
};

export default ForgotPasswordPage;
