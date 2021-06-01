package com.kankan.merchant.service;

import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.food.Pictures;
import com.kankan.merchant.module.param.AppraiseParam;
import java.util.List;

public interface AppraiseService {

   CommonAppraise userAppraise (CommonAppraise commonAppraise);

   List<CommonAppraise> appraiseList (AppraiseParam commonAppraise);

   List<Pictures> getShopPictures(String shopId);

   void markLikeAppraise (String appraiseId,Integer type,String userId);

   void markLikeProduct(String appraiseId,Integer type);

}
