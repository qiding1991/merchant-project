package com.kankan.merchant.model;

import lombok.Data;

import java.util.List;

import org.springframework.data.annotation.Id;

@Data
public class MenuDetail {
   @Id
   private String id;
   private String name;
   private List<String> photo;
   private Double price;
   private String shopId;
   private String shopName;
   private List<UserComment> userComments;
}
