/**
 * 系统设置页面
 * 管理员用于配置网站设置、SEO、系统参数等
 */

import { UserLayout } from '@/components';
import { Button, Card, Divider, Form, Tabs } from 'antd';
import { SettingFormItem } from './components/SettingFormItem';
import {
  emailSettings,
  generalSettings,
  securitySettings,
  seoSettings,
  type SettingField,
  settingGroups,
  storageSettings,
} from './config';
import { useSettings } from './hooks/useSettings';

const SettingsPage = () => {
  const { form, isSaving, handleSave, handleReset } = useSettings();

  // 分组设置的渲染函数
  const renderSettingGroup = (fields: SettingField[]) => (
    <div className='space-y-4'>
      {fields.map(field => (
        <SettingFormItem key={field.key} field={field} />
      ))}
    </div>
  );

  // Tabs 配置
  const tabItems = [
    {
      key: 'general',
      label: settingGroups[0].title,
      children: renderSettingGroup(generalSettings),
    },
    {
      key: 'seo',
      label: settingGroups[1].title,
      children: renderSettingGroup(seoSettings),
    },
    {
      key: 'security',
      label: settingGroups[2].title,
      children: renderSettingGroup(securitySettings),
    },
    {
      key: 'email',
      label: settingGroups[3].title,
      children: renderSettingGroup(emailSettings),
    },
    {
      key: 'storage',
      label: settingGroups[4].title,
      children: renderSettingGroup(storageSettings),
    },
  ];

  return (
    <UserLayout
      title='系统设置'
      description='配置网站基本信息、SEO、安全等参数'
    >
      <Card>
        <Form form={form} layout='vertical'>
          <Tabs items={tabItems} />

          <Divider />

          <div className='flex justify-end gap-4'>
            <Button onClick={handleReset}>重置</Button>
            <Button type='primary' onClick={handleSave} loading={isSaving}>
              保存设置
            </Button>
          </div>
        </Form>
      </Card>
    </UserLayout>
  );
};

export default SettingsPage;
