/**
 * 管理员仪表板页面
 * 显示网站统计数据、快捷操作入口等管理员概览信息
 */

import { Loading, UserLayout } from '@/components';
import { Card, Col, List, Row, Statistic, Table, Typography } from 'antd';
import { useEffect } from 'react';
import {
  activityActionMap,
  activityTypeMap,
  generateStatCards,
} from './AdminDashboard/config';
import { useDashboard } from './AdminDashboard/hooks/useDashboard';

const { Title, Text, Link } = Typography;

const AdminDashboard = () => {
  const {
    stats,
    isFetchingStats,
    fetchStats,
    popularPosts,
    isFetchingPopularPosts,
    fetchPopularPosts,
    recentActivities,
    isFetchingActivities,
    fetchRecentActivities,
  } = useDashboard();

  // 初始加载数据
  useEffect(() => {
    fetchStats();
    fetchPopularPosts(5);
    fetchRecentActivities(10);
  }, [fetchStats, fetchPopularPosts, fetchRecentActivities]);

  // 生成统计卡片数据
  const statCards = generateStatCards(stats);

  // 热门文章表格列配置
  const popularPostsColumns = [
    {
      title: '文章标题',
      dataIndex: 'title',
      key: 'title',
      ellipsis: true,
      render: (text: string, record: { slug: string }) => (
        <Link href={`/posts/${record.slug}`} target='_blank'>
          {text}
        </Link>
      ),
    },
    {
      title: '浏览量',
      dataIndex: 'viewCount',
      key: 'viewCount',
      width: 100,
      sorter: (a: { viewCount: number }, b: { viewCount: number }) =>
        a.viewCount - b.viewCount,
    },
    {
      title: '点赞数',
      dataIndex: 'likeCount',
      key: 'likeCount',
      width: 100,
      sorter: (a: { likeCount: number }, b: { likeCount: number }) =>
        a.likeCount - b.likeCount,
    },
    {
      title: '评论数',
      dataIndex: 'commentCount',
      key: 'commentCount',
      width: 100,
      sorter: (a: { commentCount: number }, b: { commentCount: number }) =>
        a.commentCount - b.commentCount,
    },
  ];

  if (isFetchingStats) {
    return <Loading />;
  }

  return (
    <UserLayout title='仪表盘' description='网站数据统计与管理概览'>
      {/* 统计卡片区域 */}
      <Row gutter={[16, 16]}>
        {statCards.map(card => (
          <Col xs={24} sm={12} lg={6} key={card.title}>
            <Card>
              <Statistic
                title={card.title}
                value={card.value}
                valueStyle={{ color: card.color }}
                suffix={card.suffix}
                prefix={card.prefix}
              />
              {card.description && (
                <Text type='secondary' style={{ fontSize: 12 }}>
                  {card.description}
                </Text>
              )}
            </Card>
          </Col>
        ))}
      </Row>

      {/* 热门文章与最近活动 */}
      <Row gutter={[16, 16]} style={{ marginTop: 16 }}>
        <Col xs={24} lg={14}>
          <Card
            title={<Title level={5}>热门文章</Title>}
            loading={isFetchingPopularPosts}
          >
            <Table
              columns={popularPostsColumns}
              dataSource={popularPosts.map(post => ({
                ...post,
                key: post.id,
              }))}
              pagination={false}
              size='small'
            />
          </Card>
        </Col>

        <Col xs={24} lg={10}>
          <Card
            title={<Title level={5}>最近活动</Title>}
            loading={isFetchingActivities}
          >
            <List
              size='small'
              dataSource={recentActivities}
              renderItem={activity => (
                <List.Item>
                  <List.Item.Meta
                    title={
                      <Text>
                        {activity.username}{' '}
                        {activityActionMap[activity.action] || activity.action}
                        {activityTypeMap[activity.type] || activity.type}
                      </Text>
                    }
                    description={
                      <>
                        <div>{activity.content}</div>
                        <Text type='secondary' style={{ fontSize: 12 }}>
                          {new Date(activity.createdAt).toLocaleString()}
                        </Text>
                      </>
                    }
                  />
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>
    </UserLayout>
  );
};

export default AdminDashboard;

