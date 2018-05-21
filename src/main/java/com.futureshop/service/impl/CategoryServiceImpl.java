package com.futureshop.service.impl;

import com.futureshop.common.ServerResponse;
import com.futureshop.dao.CategoryMapper;
import com.futureshop.pojo.Category;
import com.futureshop.service.ICategoryService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yangzhou on 2018-05-16.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加类别参数错误");
        }

        Category category = new Category();

        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);

        int count = categoryMapper.insert(category);

        if (count > 0) {
            return ServerResponse.createBySuccess("添加类别成功");
        } else {
            return ServerResponse.createByErrorMessage("添加类别失败");
        }
    }

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加类别参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int count = categoryMapper.updateByPrimaryKeySelective(category);

        if (count > 0) {
            return ServerResponse.createBySuccess("品类更新名称成功");
        }
        return ServerResponse.createByErrorMessage("品类更新名称失败");
    }

    public ServerResponse getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(categoryId);

        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类子分类");
        }

        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse getCategoryAndDeepChildrenCategory(Integer categoryId) {
        Set<Category> categoryList = getDeepChildrenCategory(categoryId);

        categoryList.add(categoryMapper.selectByPrimaryKey(categoryId));

        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类子分类");
        }

        return ServerResponse.createBySuccess(categoryList);
    }

    private Set<Category> getDeepChildrenCategory(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet(categoryMapper.selectCategoryByParentId(categoryId));
        if (CollectionUtils.isEmpty(categorySet)) {
            // leaf node will return here
            return categorySet;
        }

        for (Category category : categorySet) {
            categorySet.addAll(getDeepChildrenCategory(category.getId()));
        }
        return categorySet;
    }
}
