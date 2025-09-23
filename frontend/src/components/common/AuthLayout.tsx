import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components';
import React from 'react';

/**
 * Auth 布局 — 用于登录/注册/重置等页面的统一居中 Card 布局
 */
export interface AuthLayoutProps {
  title?: string;
  description?: React.ReactNode;
  mainClassName?: string;
  cardClassName?: string;
  children?: React.ReactNode;
  footer?: React.ReactNode;
}

export const AuthLayout: React.FC<AuthLayoutProps> = ({
  title,
  description,
  children,
  mainClassName = '',
  cardClassName = '',
  footer,
}) => {
  return (
    <div
      className={`min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8 text-sm ${mainClassName}`}
    >
      <div className='max-w-md w-full space-y-8'>
        <Card className={`shadow-lg ${cardClassName}`}>
          {(title || description) && (
            <CardHeader className='text-center'>
              {title && (
                <CardTitle className='text-lg font-bold'>{title}</CardTitle>
              )}
              {description && <CardDescription>{description}</CardDescription>}
            </CardHeader>
          )}

          <CardContent>{children}</CardContent>

          {footer && <CardFooter>{footer}</CardFooter>}
        </Card>
      </div>
    </div>
  );
};

export default AuthLayout;
