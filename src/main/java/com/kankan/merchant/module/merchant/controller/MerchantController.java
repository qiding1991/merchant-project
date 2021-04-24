package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.module.param.MerchantApplyParam;
import com.kankan.merchant.module.param.MerchantQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.module.param.RegisterShopParam;
import com.kankan.merchant.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@Api(tags = "后台-商家")
@RequestMapping("admin/merchant")
@RestController
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @ApiOperation(value = "管理后台商家注册服务")
    @RequestMapping(value = "/applyShop", method = RequestMethod.POST)
    public CommonResponse applyShop (@RequestBody RegisterShopParam registerShopParam) {
        if (null == registerShopParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.registerMerchant(registerShopParam));
    }

    @ApiOperation(value = "管理后台商家审核服务")
    @RequestMapping(value = "/approveApply", method = RequestMethod.POST)
    public CommonResponse applyShop (@RequestBody MerchantApplyParam merchantApplyParam) {
        if (null == merchantApplyParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        merchantService.applyMerchant(merchantApplyParam);
        return CommonResponse.success();
    }

    @ApiOperation(value = "管理后台商家列表服务")
    @RequestMapping(value = "/shopAllList",method = RequestMethod.GET)
    public CommonResponse shopList () {
        return CommonResponse.success(merchantService.findShopList());
    }

    @ApiOperation(value = "管理后台获取商家详情")
    @RequestMapping(value = "/shopDetail", method = RequestMethod.GET)
    public CommonResponse applyShop (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.findById(shopId));
    }

    @ApiOperation(value = "管理后台删除商家信息")
    @RequestMapping(value = "/delDetail", method = RequestMethod.DELETE)
    public CommonResponse delShop (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        merchantService.delMerchant(shopId);
        return CommonResponse.success();
    }

    @ApiOperation(value = "管理后台修改商家信息")
    @RequestMapping(value = "/updateShop", method = RequestMethod.PUT)
    public CommonResponse updateShop (@RequestBody RegisterShopParam registerShopParam) {
        if (null == registerShopParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        merchantService.updateMerchant(registerShopParam);
        return CommonResponse.success();
    }

    @ApiOperation(value = "客户端首页获取商家信息",notes = "type:1-热门,2-附近好店,3-优惠")
    @RequestMapping(value = "/findFacePageShop", method = RequestMethod.PUT)
    public CommonResponse findFacePageShop (@RequestBody RegisterShopParam registerShopParam) {
        if (null == registerShopParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.firstPageMerchant(registerShopParam));
    }

    @ApiOperation(value = "客户端首页筛选商家",notes = "根据二级类目和地区,智能排序,店家筛选")
    @RequestMapping(value = "/chooseShop", method = RequestMethod.PUT)
    public CommonResponse findFacePageShop (@RequestBody MerchantQueryParam merchantQueryParam) {
        if (null == merchantQueryParam) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.chooseShop(merchantQueryParam));
    }

}
