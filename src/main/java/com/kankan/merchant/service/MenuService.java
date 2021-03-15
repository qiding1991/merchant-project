package com.kankan.merchant.service;

import com.kankan.merchant.model.MenuDetail;

import java.util.List;

public interface MenuService {
  MenuDetail addMenu(MenuDetail menuDetail);

  MenuDetail findById(String id);

  List<MenuDetail> findByShopId(String shopId);

  void updateMenu(MenuDetail menuDetail);

  void delMenu(MenuDetail menuDetail);
}
