package com.kankan.merchant.module.classify.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Category {
  private String id;//分类ID
  private String icon;//分类logo
  private String name;//分类名称
  private String parentId;//父级分类ID
  private Boolean isMenu;//是否餐饮
}
