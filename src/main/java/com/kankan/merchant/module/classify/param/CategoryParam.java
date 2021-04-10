package com.kankan.merchant.module.classify.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryParam {

  @ApiModelProperty(value = "分类ID-修改或者删除必输,查询可选")
  private String id;
  @ApiModelProperty(value = "分类图标-添加必输,修改可选")
  private String icon;//分类logo
  @ApiModelProperty(value = "分类名称-添加必输,修改可选")
  private String name;//分类名称
  @ApiModelProperty(value = "分类父级ID-均可选")
  private String parentId;
  @ApiModelProperty(value = "是否餐饮-添加必输")
  private Boolean isMenu;

}
