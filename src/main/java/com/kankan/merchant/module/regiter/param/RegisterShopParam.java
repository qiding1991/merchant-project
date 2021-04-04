package com.kankan.merchant.module.regiter.param;

import com.kankan.merchant.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class RegisterShopParam {
  /*private String classifyId;
  private String itemId;
  private String name; //商铺名称
  private Address address;
  private ShopInfoParam shopInfo;
  private List<Integer> paymentType;//支付方式
  private String companyName;
  private String companyCZ;
  private String IDUrl;
  private List<String> photos;
  private Boolean yelp;*/

  private String id;
  private String shopName;
  private String companyName;
  private String category1;
  private String category2;
  private String region;
  private String address;
  private String location;
  private String contact;
  private String faxNo;
  private String serviceTime;
  private String email;
  private String website;
  private String welChat;
  private List<Integer> payType;
  private String percapitaPrice;
  private String file;
  private int sourceFrom;
  private List<String> shopPicture;
  private Integer wholeScore;
  private Integer envScore;
  private Integer flavorScore;
  private Integer serviceScore;
  private boolean hot;
  private Integer applyStatus;
  private Integer status;
  private String registerTime;
  private String updateTime;

  public RegisterShopParam (String id, Integer applyStatus) {
    this.id = id;
    this.applyStatus = applyStatus;
  }
}

