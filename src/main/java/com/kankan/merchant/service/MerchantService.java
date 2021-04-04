package com.kankan.merchant.service;

import com.kankan.merchant.config.OrderRule;
import com.kankan.merchant.config.PriceRange;
import com.kankan.merchant.module.merchant.Merchant;

import java.util.List;

public interface MerchantService {
  /**
   * 申请商铺
   */
  Merchant registerMerchant(Merchant merchant);

  List<Merchant> findShop(String classifyId);

  List<Merchant> findShopBySecond(String secondClassifyId);

  void updateMerchant(Merchant merchant);

  void delMerchant(String  shopId);

  Merchant findById(String shopId);

  /**
   *
   */
  List<Merchant> findHotShop();

  List<Merchant> findHotShop(String classifyId);

  List<Merchant> findNearShop(String longitude, String latitude);

  List<Merchant> findNearByShop(String secondClassifyId, String area);

  List<Merchant> findShop(String secondClassifyId, OrderRule rule, String longitude, String latitude);

  List<Merchant> searchShop(String keyWord);

  List<Merchant> filterShop(Integer shopType, PriceRange priceRange, Integer paymentType, Double minScore, Double maxScore);


  void thumpMerchant(String shopId);

}
