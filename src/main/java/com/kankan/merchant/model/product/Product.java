package com.kankan.merchant.model.product;

import org.springframework.data.annotation.Id;

public class Product {
    @Id
    private String id;

    private String typeId;
}
