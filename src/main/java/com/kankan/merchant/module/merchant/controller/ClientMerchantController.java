package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.service.MerchantService;
import com.kankan.merchant.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "客户端商家服务")
@RequestMapping("client/")
public class ClientMerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "商家详情服务")
    @RequestMapping(value = "findShopDetail", method = RequestMethod.GET)
    public CommonResponse findProduct (String shopId,String userId) {
        if (StringUtils.isEmpty(shopId) || StringUtils.isEmpty(userId)) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.findByIdForClient(shopId,userId));
    }

    @ApiOperation(value = "商家详情服务")
    @RequestMapping(value = "findProductDetail", method = RequestMethod.GET)
    public CommonResponse findProductDetail (String productId,String userId) {
        if (StringUtils.isEmpty(productId) || StringUtils.isEmpty(userId)) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(productService.findDetailForClient(productId,Integer.valueOf(userId)));
    }
}
