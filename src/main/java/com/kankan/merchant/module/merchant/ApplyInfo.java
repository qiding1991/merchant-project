package com.kankan.merchant.module.merchant;

import lombok.Data;
import java.util.List;

@Data
public class ApplyInfo {
  private String companyName;
  private String companyCZ;
  private String IDUrl;
  private List<String> photos;
  private Boolean yelp;
  private Integer applyStatus;
}
