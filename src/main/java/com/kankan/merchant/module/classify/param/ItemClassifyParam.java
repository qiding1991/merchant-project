package com.kankan.merchant.module.classify.param;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ItemClassifyParam {
  private String classifyId;
  private List<String> itemName;
}
