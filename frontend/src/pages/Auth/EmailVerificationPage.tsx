import { Button } from '@/components';
import AuthLayout from '@/components/common/AuthLayout';
import { FailedCard } from '@/components/common/FailedCard';
import { useAuth, useRegister } from '@/hooks';
import { AlertCircle, CheckCircle, Mail } from 'lucide-react';
import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import { toast } from 'sonner';

/**
 * 邮箱验证页面
 * 用户邮箱验证和激活账户的页面
 * 支持URL参数中的token自动验证，支持重新发送验证邮件
 */
const EmailVerificationPage = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { verifyEmail, verifyEmailLoading } = useRegister();
  const { user } = useAuth();

  // 验证状态
  const [isVerified, setIsVerified] = useState(false);
  const [verificationFailed, setVerificationFailed] = useState(false);

  // 从URL参数中获取token和email
  const tokenFromUrl = searchParams.get('token') || '';

  // 自动验证token
  useEffect(() => {
    if (tokenFromUrl && !isVerified && !verificationFailed) {
      const autoVerify = async () => {
        try {
          const success = await verifyEmail({
            emailToken: tokenFromUrl,
          });

          if (success) {
            console.log(success);
            setIsVerified(true);
            toast.success('邮箱验证成功！3秒后跳转到登录页面');

            // 3秒后自动跳转到登录页面
            setTimeout(() => {
              navigate('/auth/login');
            }, 3000);
          } else {
            setVerificationFailed(true);
          }
        } catch (error) {
          console.error('自动验证失败:', error);
          setVerificationFailed(true);
        }
      };

      void autoVerify();
    }
  }, [tokenFromUrl, isVerified, verificationFailed, verifyEmail, navigate]);

  // 正在验证状态
  if (
    verifyEmailLoading &&
    tokenFromUrl &&
    !isVerified &&
    !verificationFailed
  ) {
    return (
      <AuthLayout
        title='正在验证邮箱'
        description='请稍候，正在验证您的邮箱...'
      >
        <div className='text-center space-y-4'>
          <div className='flex justify-center'>
            <div className='animate-spin rounded-full h-16 w-16 border-b-2 border-primary'></div>
          </div>
          <p className='text-muted-foreground'>正在验证您的邮箱，请稍候...</p>
        </div>
      </AuthLayout>
    );
  }

  // 如果已验证，显示成功状态
  if (isVerified) {
    return (
      <AuthLayout
        title='邮箱验证成功'
        description='您的邮箱已成功验证，即将跳转到登录页面'
      >
        <div className='text-center space-y-4'>
          <div className='flex justify-center'>
            <CheckCircle className='h-16 w-16 text-theme-success' />
          </div>
          <p className='text-theme-success'>
            邮箱验证成功！您的账户已激活，可以正常登录使用。
          </p>
          <Button onClick={() => navigate('/auth/login')} className='w-full'>
            前往登录
          </Button>
        </div>
      </AuthLayout>
    );
  }

  // 如果验证失败，显示错误状态
  if (verificationFailed) {
    return (
      <FailedCard title='邮箱验证' description='验证令牌无效或已过期'>
        <div className='text-center space-y-4'>
          <div className='flex justify-center'>
            <AlertCircle className='h-16 w-16 text-theme-success' />
          </div>
          <div className='space-y-2'>
            <p className=' text-theme-success'>
              验证令牌无效或已过期，请登录后进入个人中心重新发送验证邮件。
            </p>
          </div>
          <div className='space-y-2'>
            {user && user.username ? (
              <Button
                onClick={() => navigate('/user/profile')}
                className='w-full'
              >
                跳转个人中心
              </Button>
            ) : (
              <Button
                onClick={() => navigate('/auth/login')}
                className='w-full'
              >
                登录
              </Button>
            )}
          </div>
        </div>
      </FailedCard>
    );
  }

  // 如果没有token，显示提示信息
  return (
    <FailedCard title='邮箱验证' description='验证令牌无效或已过期'>
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

export default EmailVerificationPage;
