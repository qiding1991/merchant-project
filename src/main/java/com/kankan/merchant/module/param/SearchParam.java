package com.kankan.merchant.module.param;

import lombok.Data;

@Data
public class SearchParam extends MerchantQueryParam {

    private String name;
    private int type;
}
