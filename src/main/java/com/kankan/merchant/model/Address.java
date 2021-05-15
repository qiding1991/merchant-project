package com.kankan.merchant.model;

import lombok.Data;

@Data
public class Address {
  private Double lat;//经度
  private Double lang;//纬度
  private String area;
  private String name;
}
