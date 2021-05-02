package com.kankan.merchant.module.merchant.common;

import com.kankan.merchant.module.merchant.Appraise;
import com.kankan.merchant.module.merchant.food.Pictures;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class CommonAppraise extends Appraise {

    @ApiModelProperty(value = "是否热门-1是2否", notes = "", required = true)
    private String hot;
    @ApiModelProperty(value = "1商家2产品", notes = "", required = true)
    private String type;
    @ApiModelProperty(value = "产品ID(餐饮的话就是菜品ID)", notes = "", required = false)
    private String productId;
    @ApiModelProperty(value = "商家ID", notes = "", required = false)
    private String shopId;
    @ApiModelProperty(value = "总评分", notes = "", required = true)
    private String wholeScore;
    @ApiModelProperty(value = "环境评分", notes = "", required = false)
    private String envScore;
    @ApiModelProperty(value = "口味评分", notes = "", required = false)
    private String flavorScore;
    @ApiModelProperty(value = "服务评分", notes = "", required = false)
    private String serviceScore;
    @ApiModelProperty(value = "人均", notes = "", required = true)
    private String averagePrice;
    @ApiModelProperty(value = "评价图片", notes = "", required = true)
    private List<Pictures> productPictures;
    @ApiModelProperty(value = "相关推荐", notes = "", required = false)
    private List<String> recommends;


}
