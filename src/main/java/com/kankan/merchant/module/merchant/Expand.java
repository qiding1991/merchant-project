package com.kankan.merchant.module.merchant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Expand {

    private String id;
    @ApiModelProperty(value = "提报类型1-商户投诉2-信息报错",notes = "",required = true)
    private Integer type;
    @ApiModelProperty(value = "提报用户ID",notes = "",required = true)
    private String userId;
    @ApiModelProperty(value = "提报关联商家店铺",notes = "",required = true)
    private String shopId;
    @ApiModelProperty(value = "商户投诉(1-产品质量投诉2-服务态度投诉3-配送服务投诉4-售后服务投诉5-其他)/信息报错(1-名称,地址2-价格3-产品或服务描述4-配送方式5-其他)",notes = "",required = true)
    private Integer itemsType;
    @ApiModelProperty(value = "提报内容",notes = "",required = true)
    private String content;
    @ApiModelProperty(value = "提报资料",notes = "")
    private String file;
    @ApiModelProperty(value = "提报时间",notes = "")
    private String createTime;
}
