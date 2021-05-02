package com.kankan.merchant.module.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppraiseParam {

    @ApiModelProperty(value = "是否热门-1是2否", notes = "", required = true)
    private String hot;
    @ApiModelProperty(value = "1商家2产品", notes = "", required = true)
    private String type;
    @ApiModelProperty(value = "产品ID(餐饮的话就是菜品ID)", notes = "")
    private String productId;
    @ApiModelProperty(value = "商家ID", notes = "")
    private String shopId;
    @ApiModelProperty(value = "用户ID", notes = "")
    private String userId;
}
