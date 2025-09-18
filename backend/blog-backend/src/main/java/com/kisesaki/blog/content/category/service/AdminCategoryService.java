package com.kisesaki.blog.content.category.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.SlugUtils;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryCreateRequest;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryListResponse;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryParentRequest;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryQueryParams;
import com.kisesaki.blog.content.category.dto.admin.AdminCategorySortRequest;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryTreeResponse;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryUpdateRequest;
import com.kisesaki.blog.content.category.entity.Categories;
import com.kisesaki.blog.content.category.mapper.CategoriesMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理员分类服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryService {

    private final CategoriesMapper categoriesMapper;

    /**
     * 获取管理员分类列表（分页）
     *
     * @param params 查询参数
     * @return 分页分类列表
     */
    public PageResponse<AdminCategoryListResponse> getAdminCategoryList(AdminCategoryQueryParams params) {
        log.debug("获取管理员分类列表，参数: {}", params);

        // 创建分页对象
        Page<Categories> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize()
        );

        // 构建查询条件
        LambdaQueryWrapper<Categories> queryWrapper = new LambdaQueryWrapper<Categories>()
                .like(params.getKeyword() != null, Categories::getName, params.getKeyword())
                .eq(params.getParentId() != null, Categories::getParentId, params.getParentId())
                .isNull(params.getOnlyRoot() != null && params.getOnlyRoot(), Categories::getParentId)
                .eq(!"all".equals(params.getVisibility()) && "visible".equals(params.getVisibility()), Categories::getIsVisible, true)
                .eq(!"all".equals(params.getVisibility()) && "hidden".equals(params.getVisibility()), Categories::getIsVisible, false)
                .orderByAsc(Categories::getSortOrder)
                .orderByDesc(Categories::getCreatedAt);

        // 执行分页查询
        Page<Categories> categoryPage = categoriesMapper.selectPage(page, queryWrapper);

        // 转换为响应DTO
        List<AdminCategoryListResponse> responseList = categoryPage.getRecords().stream()
                .map(this::convertToAdminListResponse)
                .toList();

        return PageResponse.of(responseList, categoryPage.getTotal(), params.getPageable());
    }

    /**
     * 获取管理员分类树形结构
     *
     * @return 树形分类列表
     */
    public List<AdminCategoryTreeResponse> getAdminCategoryTree() {
        log.debug("获取管理员分类树形结构");

        // 查询所有分类
        List<Categories> allCategories = categoriesMapper.selectList(
                new LambdaQueryWrapper<Categories>()
                        .orderByAsc(Categories::getSortOrder)
                        .orderByDesc(Categories::getCreatedAt)
        );

        // 转换为树形结构
        return buildCategoryTree(allCategories);
    }

    /**
     * 创建分类
     *
     * @param request 创建请求
     * @return 创建的分类信息
     */
    @Transactional
    public AdminCategoryListResponse createCategory(AdminCategoryCreateRequest request) {
        log.debug("创建分类，请求: {}", request);

        // 1. 验证父分类是否存在
        if (request.getParentId() != null) {
            Categories parentCategory = categoriesMapper.selectById(request.getParentId());
            if (parentCategory == null) {
                throw BusinessException.paramError("父分类不存在");
            }
        }

        // 2. 生成 slug
        String slug = request.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = SlugUtils.generateSlug(request.getName());
        }

        // 3. 检查 slug 是否重复
        Categories existingCategory = categoriesMapper.selectOne(
                new LambdaQueryWrapper<Categories>()
                        .eq(Categories::getSlug, slug)
        );
        if (existingCategory != null) {
            throw BusinessException.paramError("分类别名已存在");
        }

        // 4. 创建分类
        Categories category = new Categories();
        category.setName(request.getName());
        category.setSlug(slug);
        category.setDescription(request.getDescription());
        category.setParentId(request.getParentId());
        category.setSortOrder(request.getSortOrder());
        category.setIsVisible(request.getIsVisible());
        category.setPostCount(0);
        category.setCreatedAt(OffsetDateTime.now());
        category.setUpdatedAt(OffsetDateTime.now());

        categoriesMapper.insert(category);

        log.info("分类创建成功，ID: {}, 名称: {}", category.getId(), category.getName());
        return convertToAdminListResponse(category);
    }

    /**
     * 更新分类
     *
     * @param id      分类ID
     * @param request 更新请求
     * @return 更新后的分类信息
     */
    @Transactional
    public AdminCategoryListResponse updateCategory(Long id, AdminCategoryUpdateRequest request) {
        log.debug("更新分类，ID: {}, 请求: {}", id, request);

        // 1. 检查分类是否存在
        Categories category = categoriesMapper.selectById(id);
        if (category == null) {
            throw BusinessException.notFound("分类不存在");
        }

        // 2. 验证父分类
        if (request.getParentId() != null) {
            // 不能设置自己为父分类
            if (request.getParentId().equals(id)) {
                throw BusinessException.paramError("不能设置自己为父分类");
            }

            // 检查父分类是否存在
            Categories parentCategory = categoriesMapper.selectById(request.getParentId());
            if (parentCategory == null) {
                throw BusinessException.paramError("父分类不存在");
            }

            // 检查是否会形成循环引用
            if (wouldCreateCircularReference(id, request.getParentId())) {
                throw BusinessException.paramError("设置父分类会导致循环引用");
            }
        }

        // 3. 检查 slug 是否重复（排除自己）
        if (request.getSlug() != null && !request.getSlug().equals(category.getSlug())) {
            Categories existingCategory = categoriesMapper.selectOne(
                    new LambdaQueryWrapper<Categories>()
                            .eq(Categories::getSlug, request.getSlug())
                            .ne(Categories::getId, id)
            );
            if (existingCategory != null) {
                throw BusinessException.paramError("分类别名已存在");
            }
        }

        // 4. 更新分类
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setParentId(request.getParentId());
        category.setSortOrder(request.getSortOrder());
        category.setIsVisible(request.getIsVisible());
        category.setUpdatedAt(OffsetDateTime.now());

        categoriesMapper.updateById(category);

        log.info("分类更新成功，ID: {}, 名称: {}", id, category.getName());
        return convertToAdminListResponse(category);
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    @Transactional
    public void deleteCategory(Long id) {
        log.debug("删除分类，ID: {}", id);

        // 1. 检查分类是否存在
        Categories category = categoriesMapper.selectById(id);
        if (category == null) {
            throw BusinessException.notFound("分类不存在");
        }

        // 2. 检查是否有子分类
        long childCount = categoriesMapper.selectCount(
                new LambdaQueryWrapper<Categories>()
                        .eq(Categories::getParentId, id)
        );
        if (childCount > 0) {
            throw BusinessException.paramError("该分类下还有子分类，请先删除子分类");
        }

        // 3. 检查是否有文章使用该分类
        if (category.getPostCount() > 0) {
            throw BusinessException.paramError("该分类下还有文章，请先移动或删除相关文章");
        }

        // 4. 删除分类
        categoriesMapper.deleteById(id);

        log.info("分类删除成功，ID: {}, 名称: {}", id, category.getName());
    }

    /**
     * 更新分类排序
     *
     * @param id      分类ID
     * @param request 排序请求
     */
    @Transactional
    public void updateCategorySort(Long id, AdminCategorySortRequest request) {
        log.debug("更新分类排序，ID: {}, 排序: {}", id, request.getSortOrder());

        // 检查分类是否存在
        Categories category = categoriesMapper.selectById(id);
        if (category == null) {
            throw BusinessException.notFound("分类不存在");
        }

        // 更新排序
        category.setSortOrder(request.getSortOrder());
        category.setUpdatedAt(OffsetDateTime.now());
        categoriesMapper.updateById(category);

        log.info("分类排序更新成功，ID: {}, 新排序: {}", id, request.getSortOrder());
    }

    /**
     * 修改分类父级关系
     *
     * @param id      分类ID
     * @param request 父级请求
     */
    @Transactional
    public void updateCategoryParent(Long id, AdminCategoryParentRequest request) {
        log.debug("修改分类父级关系，ID: {}, 父分类ID: {}", id, request.getParentId());

        // 1. 检查分类是否存在
        Categories category = categoriesMapper.selectById(id);
        if (category == null) {
            throw BusinessException.notFound("分类不存在");
        }

        // 2. 验证父分类
        if (request.getParentId() != null) {
            // 不能设置自己为父分类
            if (request.getParentId().equals(id)) {
                throw BusinessException.paramError("不能设置自己为父分类");
            }

            // 检查父分类是否存在
            Categories parentCategory = categoriesMapper.selectById(request.getParentId());
            if (parentCategory == null) {
                throw BusinessException.paramError("父分类不存在");
            }

            // 检查是否会形成循环引用
            if (wouldCreateCircularReference(id, request.getParentId())) {
                throw BusinessException.paramError("设置父分类会导致循环引用");
            }
        }

        // 3. 更新父分类
        category.setParentId(request.getParentId());
        category.setUpdatedAt(OffsetDateTime.now());
        categoriesMapper.updateById(category);

        log.info("分类父级关系更新成功，ID: {}, 新父分类ID: {}", id, request.getParentId());
    }

    /**
     * 检查是否会形成循环引用
     *
     * @param categoryId 当前分类ID
     * @param parentId   要设置的父分类ID
     * @return 是否会形成循环引用
     */
    private boolean wouldCreateCircularReference(Long categoryId, Long parentId) {
        if (parentId == null) {
            return false;
        }

        Long currentParentId = parentId;
        while (currentParentId != null) {
            if (currentParentId.equals(categoryId)) {
                return true;
            }
            Categories parent = categoriesMapper.selectById(currentParentId);
            currentParentId = parent != null ? parent.getParentId() : null;
        }
        return false;
    }

    /**
     * 转换为管理员列表响应
     *
     * @param category 分类实体
     * @return 管理员列表响应
     */
    private AdminCategoryListResponse convertToAdminListResponse(Categories category) {
        AdminCategoryListResponse response = new AdminCategoryListResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setDescription(category.getDescription());
        response.setParentId(category.getParentId());
        response.setSortOrder(category.getSortOrder());
        response.setPostCount(category.getPostCount());
        response.setIsVisible(category.getIsVisible());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        // 查询父分类名称
        if (category.getParentId() != null) {
            Categories parent = categoriesMapper.selectById(category.getParentId());
            if (parent != null) {
                response.setParentName(parent.getName());
            }
        }

        return response;
    }

    /**
     * 构建分类树形结构
     *
     * @param categories 所有分类
     * @return 树形结构
     */
    private List<AdminCategoryTreeResponse> buildCategoryTree(List<Categories> categories) {
        // 转换为树形响应对象
        List<AdminCategoryTreeResponse> treeResponses = categories.stream()
                .map(this::convertToAdminTreeResponse)
                .toList();

        // 构建父子关系映射
        return treeResponses.stream()
                .filter(item -> item.getParentId() == null)
                .peek(root -> buildChildren(root, treeResponses))
                .toList();
    }

    /**
     * 递归构建子分类
     *
     * @param parent    父分类
     * @param allNodes  所有节点
     */
    private void buildChildren(AdminCategoryTreeResponse parent, List<AdminCategoryTreeResponse> allNodes) {
        List<AdminCategoryTreeResponse> children = allNodes.stream()
                .filter(node -> parent.getId().equals(node.getParentId()))
                .peek(child -> buildChildren(child, allNodes))
                .toList();

        parent.setChildren(children.isEmpty() ? null : children);
    }

    /**
     * 转换为管理员树形响应
     *
     * @param category 分类实体
     * @return 管理员树形响应
     */
    private AdminCategoryTreeResponse convertToAdminTreeResponse(Categories category) {
        AdminCategoryTreeResponse response = new AdminCategoryTreeResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setDescription(category.getDescription());
        response.setParentId(category.getParentId());
        response.setSortOrder(category.getSortOrder());
        response.setPostCount(category.getPostCount());
        response.setIsVisible(category.getIsVisible());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
