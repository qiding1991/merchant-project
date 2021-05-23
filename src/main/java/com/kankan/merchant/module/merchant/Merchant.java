package com.kankan.merchant.module.merchant;



import com.kankan.merchant.model.Address;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
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
  @GeoSpatialIndexed(name = "location")
  private Point location;
  private List<Integer> paymentType;//支付方式
  private String phone;
  private String email;
  private String website;
  private String wx;
  private String faxNo;
  private String serviceTime;
  private String hot;
  private ApplyInfo applyInfo = new ApplyInfo();
  private Integer averagePrice;
  private Double wholeScore;
  private Double envScore;
  private Double flavorScore;
  private Double serviceScore;
  private List<Appraise> appraiseList;
  private Integer thumpCount;
  private List<Integer> collectUsers;
  private Boolean isCollection;
  private String registerTime;
  private String updateTime;
}
