import { REGISTER_LINK } from '@/config';
import { useNavigate } from 'react-router';
import AuthLayout from '../common/AuthLayout';

interface FailedCardProps {
  title: string;
  description: string;
  children: React.ReactNode;
}

/**
 * 邮箱验证失败卡片
 * @param title 标题
 * @param description 描述
 * @param children 卡片内容
 * @returns 组件
 */
export const FailedCard = ({
  title,
  description,
  children,
}: FailedCardProps) => {
  const navigation = useNavigate();
  return (
    <AuthLayout
      title={title}
      description={description}
      footer={
        <div className='w-full text-center space-y-2'>
          <div className='text-sm'>
            还没有账户？{' '}
            <a
              className='text-primary cursor-pointer'
              onClick={() => navigation(REGISTER_LINK)}
            >
              立即注册
            </a>
          </div>
        </div>
      }
    >
      {children}
    </AuthLayout>
  );
};
