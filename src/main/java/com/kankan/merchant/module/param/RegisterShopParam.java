package com.kankan.merchant.module.param;

import com.kankan.merchant.model.Address;
import com.kankan.merchant.model.product.Product;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import lombok.Data;

import java.util.ArrayList;
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
  private String userId;
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
  private Integer averagePrice;
  private String file;
  private Integer sourceFrom;
  private List<String> shopPicture;
  private String wholeScore;
  private String envScore;
  private String flavorScore;
  private String serviceScore;
  private String hot;
  private Integer applyStatus = 1;
  private Integer status;
  private String registerTime;
  private String updateTime;
  private List<Product> productList = new ArrayList<>();
  private List<CommonProduct> clientProductList = new ArrayList<>();

  public RegisterShopParam () {}
  public RegisterShopParam (String id, Integer applyStatus) {
    this.id = id;
    this.applyStatus = applyStatus;
  }
}

