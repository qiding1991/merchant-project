package com.kankan.merchant.model.comment;

import lombok.Data;

@Data
public class ShopComment extends BaseComment {
  private String shopId;
  private Double score;//总评分
  private Double rjMoney;//人均
}
