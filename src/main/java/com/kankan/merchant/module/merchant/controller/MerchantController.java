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

    /*@ApiOperation(value = "管理后台商家注册服务",httpMethod = "post")
    @PostMapping
    public CommonResponse applyShop (RegisterShopParam registerShopParam) {
        if (null == registerShopParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.registerMerchant(registerShopParam));
    }

    @ApiOperation(value = "管理后台商家注册服务",httpMethod = "post")
    @PostMapping
    public CommonResponse applyShop (RegisterShopParam registerShopParam) {
        if (null == registerShopParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.registerMerchant(registerShopParam));
    }*/

}
