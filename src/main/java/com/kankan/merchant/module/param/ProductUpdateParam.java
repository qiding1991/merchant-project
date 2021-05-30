package com.kankan.merchant.module.param;

import com.kankan.merchant.module.merchant.food.Pictures;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class ProductUpdateParam {

    @ApiModelProperty(value = "产品编号",notes = "",required = true)
    private String productId;
    @ApiModelProperty(value = "所属商家",notes = "",required = true)
    private String shopId;
    @ApiModelProperty(value = "产品名称",notes = "",required = true)
    private String productName;//can to update
    @ApiModelProperty(value = "封面图片",notes = "",required = true)
    private String facePicture;
    @ApiModelProperty(value = "产品图片列表",notes = "")
    private List<Pictures> productPictures;
    @ApiModelProperty(value = "销售时间",notes = "",required = true)
    private String saleTime;//can to update
    @ApiModelProperty(value = "产品价格",notes = "",required = true)
    private Double price;//can to update
    @ApiModelProperty(value = "产品描述",notes = "",required = true)
    private String description;
}
