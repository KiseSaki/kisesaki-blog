import { ThemeToggle } from '@/components/common';
import { Space, Typography } from 'antd';
import React from 'react';
import { Link } from 'react-router';

const { Title } = Typography;

export const Header: React.FC = () => {
    return (
        <header className="theme-navbar-bg theme-border border-b px-6 py-4">
            <div className="max-w-7xl mx-auto flex justify-between items-center">
                <Link to="/" className="no-underline">
                    <Title level={2} className="theme-text-primary m-0">
                        Kisesaki Blog
                    </Title>
                </Link>
                
                <Space>
                    <ThemeToggle />
                </Space>
            </div>
        </header>
    );
};