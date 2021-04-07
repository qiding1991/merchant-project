package com.kankan.merchant.service;

import com.kankan.merchant.module.classify.model.ItemClassify;
import com.kankan.merchant.module.classify.model.Category;
import com.kankan.merchant.module.classify.param.CategoryParam;

import java.util.List;

public interface CategoryService {

  /**
   * 添加一级分类
   *
   * @param category
   * @return
   */
  Category addCategory(Category category);

  /**
   * 添加二级分类
   *
   * @param classifyId
   * @param itemClassify
   */
  void addMerchantItemClassify(String classifyId, List<ItemClassify> itemClassify);

  /**
   * 获取所有分类
   *
   * @return
   */
  List<Category> findAllCategory(String categoryId);

  /**
   * 更新
   * @param categoryParam
   */
  void updateCategory(CategoryParam categoryParam);

  /**
   * 更新
   *
   * @param itemClassify
   */
  void updateMerchantClassifyItem(ItemClassify itemClassify);


  void delCategory(String categoryId);

  void delMerchantClassifyItem(String classifyItemId);

}
