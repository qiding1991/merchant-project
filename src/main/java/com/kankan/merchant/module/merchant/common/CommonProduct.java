package com.kankan.merchant.module.merchant.common;

import com.kankan.merchant.module.merchant.Product;
import lombok.Data;
import java.util.List;

@Data
public class CommonProduct extends Product {
    private Boolean isMenu;
    private String productName;//can to update
    private String facePicture;
    private List<String> productPictures;
    private Integer productType;
    private Long saleTime;//can to update
    private Double price;//can to update
    private Boolean isCollection;//app
    private Long likeNum;//app
    private String description;//can to update
    private Integer productPhone;
    private Long createTime;
    private Long updateTime;

}
