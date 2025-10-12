import { useCategory, useTag } from '@/hooks';
import { createScrollLoadHandler } from '@/lib/utils';
import type { CategoryQueryParams, PostFormData, TagListParams } from '@/types';
import type { FormInstance } from 'antd';
import { useCallback, useEffect, useRef, useState } from 'react';

/**
 * 文章表单数据 Hook
 * 管理分类、标签、作者等表单选项数据
 */
export const usePostFormData = ({
  form,
}: {
  form: FormInstance<PostFormData>;
}) => {
  /**
   * 分类
   */
  const { pageCategories, isFetchingCategories, fetchPageCategories } =
    useCategory();

  // 分类查询参数
  const [categoryParams, setCategoryParams] = useState<CategoryQueryParams>({
    pageable: { currentPage: 1, pageSize: 10 },
  });

  // 分类当前页码，用于滚动加载
  const categoryCurrentPage = useRef(1);

  // 是否是滚动加载（用于区分搜索和滚动）
  const isScrollLoading = useRef(false);

  // 初始加载和搜索时触发
  useEffect(() => {
    // 如果是滚动加载，跳过这次 effect
    if (isScrollLoading.current) {
      isScrollLoading.current = false;
      return;
    }

    // 搜索或初始加载时，重置页码
    categoryCurrentPage.current = 1;
    fetchPageCategories({
      ...categoryParams,
      pageable: { ...categoryParams.pageable, currentPage: 1 },
    });
  }, [categoryParams, fetchPageCategories]);

  /**
   * 分类搜索处理
   */
  const handleCategorySearch = useCallback((searchValue: string) => {
    setCategoryParams(prev => ({
      ...prev,
      keyword: searchValue,
      pageable: { ...prev.pageable, currentPage: 1 },
    }));
  }, []);

  /**
   * 分类触底加载更多
   */
  const handleCategoryScroll = createScrollLoadHandler({
    hasMore:
      (pageCategories?.currentPage ?? 0) < (pageCategories?.totalPages ?? 0),
    isFetching: isFetchingCategories,
    onLoadMore: async () => {
      const nextPage = categoryCurrentPage.current + 1;

      // 标记为滚动加载，防止触发 useEffect
      isScrollLoading.current = true;

      await fetchPageCategories(
        {
          ...categoryParams,
          pageable: {
            ...categoryParams.pageable,
            currentPage: nextPage,
          },
        },
        true // shouldAppend = true，表示追加数据
      );

      // 加载成功后更新页码
      categoryCurrentPage.current = nextPage;
    },
  });

  /**
   * 标签
   */
  const { pageTags, isFetchingPageTags, fetchPageTags } = useTag();

  // 标签查询参数
  const [tagParams, setTagParams] = useState<TagListParams>({
    pageable: { currentPage: 1, pageSize: 10 },
  });

  // 标签当前页码，用于滚动加载
  const tagCurrentPage = useRef(1);

  // 是否是滚动加载（用于区分搜索和滚动）
  const isTagScrollLoading = useRef(false);

  // 初始加载和搜索时触发
  useEffect(() => {
    // 如果是滚动加载，跳过这次 effect
    if (isTagScrollLoading.current) {
      isTagScrollLoading.current = false;
      return;
    }

    // 搜索或初始加载时，重置页码
    tagCurrentPage.current = 1;
    fetchPageTags({
      ...tagParams,
      pageable: { ...tagParams.pageable, currentPage: 1 },
    });
  }, [tagParams, fetchPageTags]);

  /**
   * 标签搜索处理
   */
  const handleTagSearch = useCallback((searchValue: string) => {
    setTagParams(prev => ({
      ...prev,
      name: searchValue,
      pageable: { ...prev.pageable, currentPage: 1 },
    }));
  }, []);

  /**
   * 标签触底加载更多
   */
  const handleTagScroll = createScrollLoadHandler({
    hasMore: (pageTags?.currentPage ?? 0) < (pageTags?.totalPages ?? 0),
    isFetching: isFetchingPageTags,
    onLoadMore: async () => {
      const nextPage = tagCurrentPage.current + 1;

      // 标记为滚动加载，防止触发 useEffect
      isTagScrollLoading.current = true;

      await fetchPageTags(
        {
          ...tagParams,
          pageable: {
            currentPage: nextPage,
            pageSize: tagParams.pageable?.pageSize ?? 10,
          },
        },
        true // shouldAppend = true，表示追加数据
      );

      // 加载成功后更新页码
      tagCurrentPage.current = nextPage;
    },
  });

  /**
   * 合并后端分页获取的 tag options 与表单中已选的 tags，确保 options 中包含已选项的 label
   * 防止编辑模式下，已选标签不在当前分页数据中而无法显示 label
   */
  // 重构为 Select 组件需要的 options 格式
  const tagOptionsFromApi =
    pageTags?.data.map(item => ({ label: item.name, value: item.id })) || [];
  // 获取表单中已选的 tags
  const selectedTags = form.getFieldValue('tags') || [];
  // 重构已选标签为 Select 组件需要的格式
  const selectedTagOptions = (selectedTags as { id: number; name: string }[])
    .map(t => ({ label: t.name, value: t.id }))
    .filter(Boolean);
  // 去重合并（以 value 为准）
  const mergedTagOptions = [...selectedTagOptions, ...tagOptionsFromApi].reduce(
    (acc: { label: string; value: number }[], cur) => {
      if (!acc.find(a => a.value === cur.value)) acc.push(cur);
      return acc;
    },
    []
  );

  /**
   * 合并后端分页获取的 category options 与表单中已选的 category，确保 options 中包含已选项的 label
   * 防止编辑模式下，已选分类不在当前分页数据中而无法显示 label
   */
  // 重构为 Select 组件需要的 options 格式
  const categoriesFormated =
    pageCategories?.data.map(item => ({ label: item.name, value: item.id })) ||
    [];
  // 获取表单中已选的 category
  const selectedCategory = [
    {
      label: form.getFieldValue('categoryName'),
      value: form.getFieldValue('categoryId'),
    },
  ].filter(Boolean) as { label: string; value: number }[];
  // 去重合并（以 value 为准）
  const mergedCategoryOptions = [
    ...selectedCategory,
    ...categoriesFormated,
  ].reduce((acc: { label: string; value: number }[], cur) => {
    if (!acc.find(a => a.value === cur.value)) acc.push(cur);
    return acc;
  }, []);

  return {
    // 分类相关
    categories: mergedCategoryOptions,
    isFetchingCategories,
    handleCategorySearch,
    handleCategoryScroll,

    // 标签相关
    tags: mergedTagOptions,
    isFetchingTags: isFetchingPageTags,
    handleTagSearch,
    handleTagScroll,
  };
};
