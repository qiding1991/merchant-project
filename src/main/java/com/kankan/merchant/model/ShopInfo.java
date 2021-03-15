package com.kankan.merchant.model;

import lombok.Data;

import java.util.List;

@Data
public class ShopInfo {
  private List<OpenTime> openTimeList;
  private String phone;
  private String email;
  private String website;
  private String wx;
}
