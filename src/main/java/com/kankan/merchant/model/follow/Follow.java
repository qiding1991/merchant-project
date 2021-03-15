package com.kankan.merchant.model.follow;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Follow {
  @Id
  private String id;
  private String userId;
  private String targetId;
  private String type;//1=商家，2=商品
}
