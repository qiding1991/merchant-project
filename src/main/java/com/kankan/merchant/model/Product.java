package com.kankan.merchant.model;

import lombok.Data;

import java.util.List;

@Data
public class Product {
  private String id;
  private String photo; //照片
  private Double price;//价格
  private String name;//名称
  private Integer isHot;//是否是热门菜品
  private String thumpCount;//点赞数
  private List<String> relatedPhoto;//相关照片
  private List<UserComment> userCommentList;//用户评价
}

