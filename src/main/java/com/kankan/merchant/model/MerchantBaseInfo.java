package com.kankan.merchant.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class MerchantBaseInfo {
  @Id
  private String id;

  private String classifyId;

  private String itemId;

  private List<String> shopPhotos;//商家相册

  private List<String> userPhotos;//用户相册

  private String name; //商铺名称

  private ShopInfo remark; //简介

  private List<Integer> paymentType;//支付方式

  private List<Product> products; //产品

  private List<MenuDetail> menuDetails;//菜单

  private List<UserAppraise> userAppraises;//用户评价


  private Boolean isYelp;

  private Boolean isHot;


}
