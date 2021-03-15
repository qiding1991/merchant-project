package com.kankan.merchant.model;

import lombok.Data;

@Data
public class Address {
  private double lat;//经度
  private double lang;//纬度
  private String area;
  private String name;
}
