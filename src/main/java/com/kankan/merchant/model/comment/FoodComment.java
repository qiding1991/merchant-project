package com.kankan.merchant.model.comment;

import lombok.Data;

@Data
public class FoodComment  extends ShopComment{
  private Double kwScore;//口味评分
  private Double hjScore;//环境
  private Double fwScore;//服务
}
