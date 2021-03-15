package com.kankan.merchant.model;

import lombok.Data;

import java.util.List;

@Data
public class MenuDetail {
   private String name;
   private List<String> photo;
   private Double price;
   private String shopName;
   private List<UserComment> userComments;
}
