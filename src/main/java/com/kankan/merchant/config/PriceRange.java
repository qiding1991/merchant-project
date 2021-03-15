package com.kankan.merchant.config;

public enum  PriceRange {
  down_10(1,0,10,"$10以下"),
  between_10_30(2,10,30,"$11-$0"),
  between_30_60(3,30,60,"$31-$60"),
  over_60(4,0,10,"$60以上");
  private int code;
  private int min;
  private int max;
  private String name;

  PriceRange(int code, int min, int max, String name) {
    this.code = code;
    this.min = min;
    this.max = max;
    this.name = name;
  }
}
