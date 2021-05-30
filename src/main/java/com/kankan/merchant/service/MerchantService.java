package com.kankan.merchant.service;

import com.kankan.merchant.config.OrderRule;
import com.kankan.merchant.config.PriceRange;
import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.module.merchant.dto.SearchResultDto;
import com.kankan.merchant.module.param.*;

import java.util.List;

public interface MerchantService {
  /**
   * 申请商铺
   */
  RegisterShopParam registerMerchant(RegisterShopParam registerShopParam);

  void applyMerchant(MerchantApplyParam merchantApplyParam);

  public List<RegisterShopParam> findShopList();

  List<RegisterShopParam> getCollectShopListByUserId (final String userId);

  RegisterShopParam findByIdForClient(String shopId,String userId);

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

  List<SearchResultDto> search (SearchParam searchParam);

  void shopCollect (CollectLikeParam collectLikeParam);

}
