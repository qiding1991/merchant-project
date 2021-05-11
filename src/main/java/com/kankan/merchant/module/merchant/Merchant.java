package com.kankan.merchant.module.merchant;



import com.kankan.merchant.model.Address;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document
@Data
public class Merchant {
  @Id
  private String id;
  private String userId;
  private String category1;
  private String category2;
  private String name; //商铺名称
  private Address address;
  @GeoSpatialIndexed
  private List<Double> location;
  private List<Integer> paymentType;//支付方式
  private String phone;
  private String email;
  private String website;
  private String wx;
  private String faxNo;
  private String serviceTime;
  private String hot;
  private ApplyInfo applyInfo = new ApplyInfo();
  private String averagePrice;
  private String wholeScore;
  private String envScore;
  private String flavorScore;
  private String serviceScore;
  private List<Appraise> appraiseList;
  private Integer thumpCount;
  private String registerTime;
  private String updateTime;
}
