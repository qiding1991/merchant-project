package com.kankan.merchant.service;

import com.kankan.merchant.module.merchant.food.Pictures;
import java.util.List;

public interface PictureService {

    List<Pictures> getPictureList(String shopId);
}
