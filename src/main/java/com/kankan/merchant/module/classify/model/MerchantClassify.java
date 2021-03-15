package com.kankan.merchant.module.classify.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class MerchantClassify {
  @Id
  private String id;//商铺分类
  private String picture;//分类logo
  private String name;//分类名称
  private List<ItemClassify>  itemTypeList;//商铺详细分类
}
