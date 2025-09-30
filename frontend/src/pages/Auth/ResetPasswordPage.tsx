import {
  Button,
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
  Input,
} from '@/components';
import AuthLayout from '@/components/common/AuthLayout';
import { FailedCard } from '@/components/feedback/FailedCard';
import { usePassword } from '@/hooks';
import { passwordSchema, type PasswordField } from '@/shcema';
import { zodResolver } from '@hookform/resolvers/zod';
import { CheckCircle, Mail } from 'lucide-react';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import { toast } from 'sonner';
import z from 'zod';

/**
 * 重置密码页面
 * 用户通过邮件链接重置密码的页面
 */
const ResetPasswordPage = () => {
  const { resetPassword, resetLoading } = usePassword();

  // 验证状态
  const [isVerified, setIsVerified] = useState(false);
  // 是否验证失败
  const [verificationFailed, setVerificationFailed] = useState(false);

  const navigate = useNavigate();
  // 获取token
  const tokenFromUrl = new URL(window.location.href).searchParams.get('token');

  // form 实例
  const form = useForm<{ password: PasswordField }>({
    resolver: zodResolver(z.object({ password: passwordSchema })),
    defaultValues: {
      password: '',
    },
  });

  // 有 token 且未验证成功或失败
  if (tokenFromUrl && !isVerified && !verificationFailed) {
    // 提交表单
    const handleSubmit = async (data: { password: PasswordField }) => {
      const success = await resetPassword(tokenFromUrl, data.password);
      if (success) {
        setIsVerified(true);
        toast.success('密码重置成功！请使用新密码登录。');

        setTimeout(() => {
          navigate('/auth/login');
        }, 5000);
      } else {
        setVerificationFailed(true);
        toast.error('密码重置失败，即将跳转重置页面。');

        setTimeout(() => {
          navigate('/auth/forgot-password');
        }, 5000);
      }
    };

    return (
      <AuthLayout title='重置密码' description='请输入您的新密码以完成重置'>
        <Form {...form}>
          <form
            className='space-y-4'
            onSubmit={form.handleSubmit(handleSubmit)}
          >
            <FormField
              name='password'
              control={form.control}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>新密码</FormLabel>
                  <Input {...field} type='password' placeholder='新密码' />
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button loading={resetLoading} type='submit' className='w-full'>
              重置密码
            </Button>
          </form>
        </Form>
      </AuthLayout>
    );
  }

  // 如果已经验证成功，就显示成功状态并跳转
  if (isVerified) {
    return (
      <AuthLayout
        title='密码重置成功'
        description='您的密码已成功重置，即将跳转到登录页面'
      >
        <div className='text-center space-y-4'>
          <div className='flex justify-center'>
            <CheckCircle className='h-16 w-16 text-theme-success' />
          </div>
          <p className='text-theme-success'>
            密码重置成功！您的新密码已生效，可以正常登录使用。
          </p>
          <Button onClick={() => navigate('/auth/login')} className='w-full'>
            前往登录
          </Button>
        </div>
      </AuthLayout>
    );
  }

  if (verificationFailed) {
    return (
      <AuthLayout
        title='密码重置失败'
        description='您的密码重置请求已失败，请重试'
      >
        <div className='text-center space-y-4'>
          <div className='flex justify-center'>
            <Mail className='h-16 w-16 text-theme-error' />
          </div>
          <p className='text-theme-error'>
            密码重置失败！请检查您的链接是否有效。
          </p>
          <Button
            onClick={() => navigate('/auth/forgot-password')}
            className='w-full'
          >
            重新发送验证邮件
          </Button>
        </div>
      </AuthLayout>
    );
  }

  return (
    <FailedCard title='重置密码' description=''>
      <div className='text-center space-y-4'>
        <div className='flex justify-center'>
          <Mail className='h-16 w-16 text-muted-foreground' />
        </div>
        <div className='space-y-2'>
          <p className='text-muted-foreground'>
            我们已向您的邮箱发送了验证邮件，请点击邮件中的链接来验证您的邮箱。
          </p>
        </div>
        <div className='space-y-2'>
          <p className='text-sm text-muted-foreground'>
            没有收到邮件？请检查垃圾邮件文件夹，或者重新发送验证邮件。
          </p>
        </div>
      </div>
    </FailedCard>
  );
};

export default ResetPasswordPage;
