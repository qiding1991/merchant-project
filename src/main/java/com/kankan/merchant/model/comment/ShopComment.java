package com.kankan.merchant.model.comment;

import lombok.Data;

@Data
public class ShopComment extends BaseComment {
  private Double score;//总评分
  private Double rjMoney;//人均
}
