package com.kankan.merchant.module.merchant;



import com.kankan.merchant.model.Address;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Merchant {
  @Id
  private String id;
  private String classifyId;
  private String itemId;
  private String name; //商铺名称
  private Address address;
  private List<Integer> paymentType;//支付方式
  private String phone;
  private String email;
  private String website;
  private String wx;

  private ApplyInfo applyInfo;
  private List<Appraise> appraiseList;
}
