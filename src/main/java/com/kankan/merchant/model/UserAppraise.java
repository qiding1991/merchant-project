package com.kankan.merchant.model;

import lombok.Data;

import java.util.List;

@Data
public class UserAppraise {
  private String userId;
  private BaseAppraise appraise;//
  private List<String> photos;
  private List<String> recommand;//推荐
  private Long timestamp;
  private Integer thumpCount;

}
