package com.kankan.merchant.service;

import com.kankan.merchant.module.merchant.Product;
import com.kankan.merchant.module.merchant.common.CommonProduct;

import java.util.List;

public interface ProductService {

  List<CommonProduct> findProduct(String typeId);

  CommonProduct addProduct(CommonProduct product);

  void approveApply(CommonProduct product);

  CommonProduct updateProduct(CommonProduct product);

  void delUpdateProduct(String id);

  CommonProduct findById(String id);

  List<CommonProduct> findAllProduct (String shopId);

  List<CommonProduct> findAllProduct ();
}
