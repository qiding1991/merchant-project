package com.kankan.merchant.module.regiter.param;

import com.kankan.merchant.model.OpenTime;
import lombok.Data;

import java.util.List;

@Data
public class ShopInfoParam {
  private String phone;
  private String email;
  private String website;
  private String wx;
}
