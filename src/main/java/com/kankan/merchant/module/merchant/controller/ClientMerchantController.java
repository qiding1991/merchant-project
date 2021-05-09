package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "客户端商家服务")
@RequestMapping("client/merchant")
public class ClientMerchantController {

    @Autowired
    private MerchantService merchantService;

    @ApiOperation(value = "商家详情服务")
    @RequestMapping(value = "/findDetail", method = RequestMethod.GET)
    public CommonResponse findProduct (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.findByIdForClient(shopId));
    }
}
