package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@Api(tags = "客户端相册服务")
@RequestMapping("client/picture")
@RestController
public class PictureController {

    @Autowired
    private AppraiseService appraiseService;

    @ApiOperation(value = "客户端获取商家相册图片列表服务")
    @GetMapping
    public CommonResponse getPictureList (String shopId) {
        AppraiseParam appraiseParam = new AppraiseParam();
        appraiseParam.setType("1");
        appraiseParam.setShopId(shopId);
        return CommonResponse.success(appraiseService.appraiseList(appraiseParam));
    }
}
