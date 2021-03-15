package com.kankan.merchant.model.comment;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BaseComment {
  @Id
  private String id;
  private String userId;//评价人
  private String text;//评价内容
  private Long timestamp;//评价时间
  private Integer thumpCount;//点赞数
  private Integer status;//是否是草稿
}
