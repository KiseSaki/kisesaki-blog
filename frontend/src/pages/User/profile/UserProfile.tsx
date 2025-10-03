import {
  Button,
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  Input,
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  UserAvatarUploader,
  UserLayout,
} from '@/components';
import { useUser } from '@/hooks';
import type { UserInfo } from '@/types';
import { SelectValue } from '@radix-ui/react-select';
import { useForm } from 'react-hook-form';
import { useUserProfile } from './hooks/useUserProfile';

/**
 * 用户个人资料页面
 * 显示和编辑用户个人信息、头像、密码修改等
 */
const UserProfile = () => {
  const { userInfo } = useUserProfile();
  const { profile, loading } = useUser();
  const form = useForm<UserInfo>({
    defaultValues: {
      displayName: userInfo?.displayName || '',
      bio: userInfo?.bio || '',
      gender: userInfo?.gender || '',
      email: userInfo?.email || '',
    },
  });

  return (
    <UserLayout title='个人资料' description='查看和编辑你的个人信息、头像等'>
      <div className='grid grid-cols-1 md:grid-cols-4 gap-6'>
        {/* 信息表单 */}
        <div className='md:col-span-3'>
          <div className='bg-card rounded-lg border p-6'>
            <Form {...form}>
              <form
                className='space-y-6'
                onSubmit={form.handleSubmit(profile.updateProfile)}
              >
                <FormField
                  name='displayName'
                  control={form.control}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>昵称</FormLabel>
                      <Input placeholder='昵称' {...field} />
                    </FormItem>
                  )}
                />

                <FormField
                  name='bio'
                  control={form.control}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>个人简介</FormLabel>
                      <Input placeholder='个人简介' {...field} />
                    </FormItem>
                  )}
                />

                <FormField
                  name='gender'
                  control={form.control}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>性别</FormLabel>
                      <Select
                        onValueChange={field.onChange}
                        defaultValue={field.value}
                      >
                        <FormControl>
                          <SelectTrigger className='w-[200px]'>
                            <SelectValue placeholder='选择性别' />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <SelectGroup>
                            <SelectItem value='male'>男</SelectItem>
                            <SelectItem value='female'>女</SelectItem>
                            <SelectItem value='other'>其他</SelectItem>
                          </SelectGroup>
                        </SelectContent>
                      </Select>
                    </FormItem>
                  )}
                />

                <FormField
                  name='email'
                  control={form.control}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>邮箱</FormLabel>
                      <Input disabled placeholder='邮箱' {...field} />
                    </FormItem>
                  )}
                />

                <Button
                  type='submit'
                  className='mt-6'
                  loading={loading.updatingProfile}
                >
                  保存
                </Button>
              </form>
            </Form>
          </div>
        </div>

        {/* 头像上传 */}
        <div className='md:col-span-1'>
          <div className='bg-card rounded-lg border p-6 flex flex-col items-center'>
            <UserAvatarUploader
              showEditButton={false}
              currentAvatarUrl={userInfo?.avatarUrl}
              userName={userInfo?.displayName}
              className='w-full'
            />
          </div>
        </div>
      </div>
    </UserLayout>
  );
};

export default UserProfile;
