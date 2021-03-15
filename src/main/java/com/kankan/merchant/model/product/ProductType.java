package com.kankan.merchant.model.product;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class ProductType {
  @Id
  private String id;
  private String shopId;
  private String name;
}
