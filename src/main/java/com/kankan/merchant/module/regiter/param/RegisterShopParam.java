package com.kankan.merchant.module.regiter.param;

import com.kankan.merchant.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class RegisterShopParam {
  private String classifyId;
  private String itemId;
  private String name; //商铺名称
  private Address address;
  private ShopInfoParam shopInfo;
  private List<Integer> paymentType;//支付方式
  private String companyName;
  private String companyCZ;
  private String IDUrl;
  private List<String> photos;
  private Boolean yelp;
}

