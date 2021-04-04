package com.kankan.merchant.module.merchant.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.module.regiter.param.RegisterShopParam;
import com.kankan.merchant.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;

@CrossOrigin(origins = "*")
@Api(tags = "管理后台-商家")
@RequestMapping("admin/merchant")
@RestController
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @ApiOperation(value = "管理后台商家注册服务")
    @RequestMapping("/applyShop")
    public CommonResponse applyShop (@RequestBody RegisterShopParam registerShopParam) {
        if (null == registerShopParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.registerMerchant(registerShopParam));
    }

    @ApiOperation(value = "管理后台商家列表服务")
    @RequestMapping("/shopAllList")
    public CommonResponse shopList () {
        return CommonResponse.success(merchantService.findShopList());
    }

    @ApiOperation(value = "管理后台获取商家详情")
    @RequestMapping("/shopDetail")
    public CommonResponse applyShop (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.findById(shopId));
    }

    @ApiOperation(value = "管理后台删除商家信息")
    @DeleteMapping
    public CommonResponse delShop (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        merchantService.delMerchant(shopId);
        return CommonResponse.success();
    }

}
