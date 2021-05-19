package com.kankan.merchant.service.impl;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.food.Pictures;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import com.kankan.merchant.service.PictureService;
import com.kankan.merchant.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PictureServiceImpl implements PictureService {

    @Autowired
    private AppraiseService appraiseService;

    @Override
    public List<Pictures> getPictureList(String shopId) {
        LogUtil.printLog(log,"getPictureList",shopId);
        AppraiseParam appraiseParam = new AppraiseParam();
        appraiseParam.setType("1");
        appraiseParam.setShopId(shopId);
        List<CommonAppraise> commonAppraiseList = appraiseService.appraiseList(appraiseParam);
        if (CollectionUtils.isEmpty(commonAppraiseList)) {
            return new ArrayList<>();
        }
        List<Pictures> result = new ArrayList<>();
        for (CommonAppraise commonAppraise : commonAppraiseList) {
            List<Pictures> picturesList = commonAppraise.getAppraisePictures();
            result.addAll(picturesList);
        }
        return result;
    }
}
