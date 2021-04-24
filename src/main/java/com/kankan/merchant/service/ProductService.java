package com.kankan.merchant.service;

import com.kankan.merchant.module.merchant.Product;
import java.util.List;

public interface ProductService {

  List<Product> findProduct(String typeId);

  Product addProduct(Product product);

  void approveApply(Product product);

  Product updateProduct(Product product);

  void delUpdateProduct(String id);

  Product findById(String id);

  List<Product> findAllProduct (String shopId);
}
