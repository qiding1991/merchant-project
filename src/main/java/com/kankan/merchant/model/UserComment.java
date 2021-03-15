package com.kankan.merchant.model;

import lombok.Data;

@Data
public class UserComment {
  private String userId;//评价人
  private String text;//评价内容
  private Long timestamp;//评价时间
  private Integer thumpCount;//点赞数
}
