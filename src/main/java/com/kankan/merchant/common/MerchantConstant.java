package com.kankan.merchant.common;

public class MerchantConstant {
  public int shop_type_discount = 1;//优惠商铺
  public int shop_type_recommend = 2;//推荐商铺


  public int paymentType_cash = 1;//现金支持
  public int paymentType_wx = 2;//微信支持

  public int produce_is_hot=1;//是热门菜品
  public int produce_is_not_hot=2;//不是热门菜品

  public static int merchant_wait_apply = 1;//待审核
  public static int merchant_done_apply = 2;//已审核
  public static int merchant_done_reject = 3;//已拒绝

  public static int merchant_hot = 1;//热门
  public static int merchant_local = 2;//附近
  public static int merchant_nice = 3;//优惠

  public static int merchant_source_local = 0;//本地来源
  public static int merchant_source_yelp = 1;//yelp来源
}

