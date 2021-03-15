package com.kankan.merchant.config;

import io.swagger.models.auth.In;

public enum  ShopType {
  youhui(1,"智能排序"),
  address(2,"距离优先") ,
  score(3,"好评优先") ,
  price_asc(4,"价格由低到高") ,
  price_desc(5,"价格由高到低") ;
  private int code;
  private String desc;

  ShopType(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
