package com.futureshop.dao;

import com.futureshop.pojo.Category;

import java.util.List;

/**
 * Created by yangzhou on 2018-05-16.
 */
public interface CategoryMapper {
    int insert(Category category);
    int updateByPrimaryKeySelective(Category category);
    List<Category> selectCategoryByParentId(Integer parent_id);
    Category selectByPrimaryKey(Integer id);
}
