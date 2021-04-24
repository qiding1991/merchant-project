package com.kankan.merchant.module.merchant.common;

import com.kankan.merchant.module.merchant.Product;
import com.kankan.merchant.module.merchant.food.MenuAppraise;
import com.kankan.merchant.module.merchant.food.Pictures;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class CommonProduct extends Product {

    @ApiModelProperty(value = "产品类型",notes = "")
    private Integer productType;
    @ApiModelProperty(value = "是否菜单",notes = "")
    private Boolean isMenu;
    @ApiModelProperty(value = "菜单评价列表",notes = "")
    List<MenuAppraise> menuAppraiseList;

    @ApiModelProperty(value = "店铺评价列表",notes = "")
    List<CommonAppraise> appraiseList;
    @ApiModelProperty(value = "产品名称",notes = "")
    private String productName;//can to update
    @ApiModelProperty(value = "封面图片",notes = "")
    private String facePicture;
    @ApiModelProperty(value = "产品图片列表",notes = "")
    private List<Pictures> productPictures;

    private Long saleTime;//can to update
    private Double price;//can to update
    private Boolean isCollection;//app
    private Long likeNum;//app
    private String description;//can to update
    private Integer productPhone;
    private Long createTime;
    private Long updateTime;

}
