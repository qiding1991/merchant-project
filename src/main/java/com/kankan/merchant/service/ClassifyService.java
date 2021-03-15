package com.kankan.merchant.service;

import com.kankan.merchant.module.classify.model.ItemClassify;
import com.kankan.merchant.module.classify.model.MerchantClassify;

import java.util.List;

public interface ClassifyService {

  /**
   * 添加一级分类
   *
   * @param merchantClassify
   * @return
   */
  MerchantClassify addMerchantClassify(MerchantClassify merchantClassify);

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
  List<MerchantClassify> findAllClassify();

  /**
   * 更新
    * @param merchantClassify
   */
  void  updateMerchantClassify(MerchantClassify merchantClassify);

  /**
   * 更新
   * @param itemClassify
   */
  void  updateMerchantClassifyItem(ItemClassify  itemClassify);


  void delMerchantClassify(String classifyId);

  void delMerchantClassifyItem(String classifyItemId);

}
