package com.kankan.merchant.module.classify.param;

import lombok.Data;

@Data
public class CategoryParam {
  private String id;
  private String icon;//分类logo
  private String name;//分类名称
  private String parentId;
  private Boolean isMenu;

}
