package com.kankan.merchant.service;

import com.kankan.merchant.model.product.ProductType;

import java.util.List;

public interface ProductTypeService {

  ProductType addProductType(ProductType productType);

  List<ProductType> productTypeList(String shopId);

  void delProductType(String id);

  void updateProductType(ProductType productType);

}
