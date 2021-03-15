package com.kankan.merchant.model;

import lombok.Data;

@Data
public class BaseAppraise {
  private Double frac;//总评分
  private Integer pc;//评级人数
  private Integer ap;//人均
  private Double sev;//服务
  private Double env;//环境
  private String text;//评价内容
}
