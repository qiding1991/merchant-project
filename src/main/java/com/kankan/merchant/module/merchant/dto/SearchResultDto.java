package com.kankan.merchant.module.merchant.dto;

import lombok.Data;

@Data
public class SearchResultDto {

    private String id;
    private String name;
    private String shopName;
    private String picture;
    private double wholeScore;
    private String averagePrice;
    private String area;
    private String categoryName;
    private String serviceTime;
    private String location;
}
