package com.futureshop.service;

import com.futureshop.common.ServerResponse;

/**
 * Created by yangzhou on 2018-05-16.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
    ServerResponse getChildrenParallelCategory(Integer categoryId);
    ServerResponse getCategoryAndDeepChildrenCategory(Integer categoryId);
}
