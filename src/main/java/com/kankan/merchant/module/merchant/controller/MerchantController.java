package com.kankan.merchant.module.merchant.controller;


import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.module.regiter.param.RegisterShopParam;
import com.kankan.merchant.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

@Api(tags = "管理后台-商家")
@RequestMapping("admin/merchant")
@RestController
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @ApiOperation(value = "管理后台商家注册服务",httpMethod = "post")
    @PostMapping
    public CommonResponse applyShop (RegisterShopParam registerShopParam) {
        if (null == registerShopParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.registerMerchant(registerShopParam));
    }

    @ApiOperation(value = "管理后台商家列表服务",httpMethod = "get")
    @RequestMapping("/shopAllList")
    public CommonResponse shopList () {
        return CommonResponse.success(merchantService.findShopList());
    }

    @ApiOperation(value = "管理后台获取商家详情",httpMethod = "post")
    @RequestMapping("/shopDetail")
    public CommonResponse applyShop (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.findById(shopId));
    }

    @ApiOperation(value = "管理后台删除商家信息",httpMethod = "delete")
    @DeleteMapping
    public CommonResponse delShop (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        merchantService.delMerchant(shopId);
        return CommonResponse.success();
    }

}
