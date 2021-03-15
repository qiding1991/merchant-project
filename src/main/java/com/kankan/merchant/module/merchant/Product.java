package com.kankan.merchant.module.merchant;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Product {
  @Id
  private String id;
  private String shopId;
}
