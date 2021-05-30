package com.kankan.merchant.service;

import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.merchant.dto.ProductResultDto;
import com.kankan.merchant.module.param.CollectLikeParam;
import com.kankan.merchant.module.param.ProductUpdateParam;
import java.util.List;

public interface ProductService {

  List<CommonProduct> findProduct(String typeId);

  CommonProduct addProduct(CommonProduct product);

  void approveApply(CommonProduct product);

  void updateProduct(ProductUpdateParam product);

  void delUpdateProduct(String id);

  CommonProduct findById(String id);

  List<CommonProduct> findAllProduct (String shopId);

  List<ProductResultDto> findAllProduct ();

  List<CommonProduct> getCollectProductListByUserId (final String userId);

  void productCollect (CollectLikeParam param);

  void productMarkLike (CollectLikeParam param);
}
