package com.kankan.merchant.module.merchant.food;

import lombok.Data;

@Data
public class Pictures {

    private String id;
    //1->shop upload 2->user upload
    private Integer type;
    private String url;
}
