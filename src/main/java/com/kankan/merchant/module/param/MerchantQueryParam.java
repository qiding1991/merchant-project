package com.kankan.merchant.module.param;

import lombok.Data;

@Data
public class MerchantQueryParam {

    private String category2;

    private int areaCode;

    private int intelligentType;
    private String location;

    private int shopType;
    private int price;
    private int payType;
    private int wholeScore;

}
