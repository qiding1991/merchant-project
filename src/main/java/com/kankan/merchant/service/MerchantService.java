package com.kankan.merchant.service;

import com.kankan.merchant.config.OrderRule;
import com.kankan.merchant.config.PriceRange;
import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.module.param.CollectLikeParam;
import com.kankan.merchant.module.param.MerchantApplyParam;
import com.kankan.merchant.module.param.MerchantQueryParam;
import com.kankan.merchant.module.param.RegisterShopParam;

import java.util.List;

public interface MerchantService {
  /**
   * 申请商铺
   */
  RegisterShopParam registerMerchant(RegisterShopParam registerShopParam);

  void applyMerchant(MerchantApplyParam merchantApplyParam);

  public List<RegisterShopParam> findShopList();

  RegisterShopParam findByIdForClient(String shopId);

  List<Merchant> findShop(String classifyId);

  List<Merchant> findShopBySecond(String secondClassifyId);

  void updateMerchant(RegisterShopParam registerShopParam);

  RegisterShopParam findShopByUserId(String userId);

  List<RegisterShopParam> findAllShopProductList ();

  void delMerchant(String  shopId);

  RegisterShopParam findById(String shopId);

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

  List<RegisterShopParam> firstPageMerchant(RegisterShopParam registerShopParam);

  List<RegisterShopParam> chooseShop (MerchantQueryParam merchantQueryParam);

  void shopCollect (CollectLikeParam collectLikeParam);

}
