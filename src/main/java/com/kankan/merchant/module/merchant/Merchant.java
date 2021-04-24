package com.kankan.merchant.module.merchant;



import com.kankan.merchant.model.Address;
import lombok.Data;
import org.springframework.data.annotation.Id;
import java.util.List;

@Data
public class Merchant {
  @Id
  private String id;
  private String userId;
  private String category1;
  private String category2;
  private String name; //商铺名称
  private Address address;
  private String location;
  private List<Integer> paymentType;//支付方式
  private String phone;
  private String email;
  private String website;
  private String wx;
  private String faxNo;
  private String serviceTime;
  private Boolean isHot;
  private ApplyInfo applyInfo;
  private String averagePrice;
  private Integer wholeScore;
  private Integer envScore;
  private Integer flavorScore;
  private Integer serviceScore;
  private List<Appraise> appraiseList;
  private Integer thumpCount;
  private Long registerTime;
  private Long updateTime;
}
