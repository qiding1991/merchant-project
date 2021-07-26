package com.kankan.merchant.module.merchant.controller;


import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import org.springframework.util.StringUtils;
import com.kankan.merchant.module.param.CollectLikeParam;
import com.kankan.merchant.service.MerchantService;
import com.kankan.merchant.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "客户端收藏点赞服务")
@RequestMapping("client/")
public class ClientCollectController {

    @Autowired
    private ProductService productService;
    @Autowired
    private MerchantService merchantService;

    @ApiOperation(value = "客户端产品收藏服务")
    @RequestMapping(value = "productCollect", method = RequestMethod.POST)
    public CommonResponse productCollect (@RequestBody CollectLikeParam param) {
        productService.productCollect(param);
        return CommonResponse.success();
    }

    @ApiOperation(value = "客户端产品点赞服务")
    @RequestMapping(value = "productMarkLike", method = RequestMethod.POST)
    public CommonResponse productMarkLike (@RequestBody CollectLikeParam param) {
        productService.productMarkLike(param);
        return CommonResponse.success();
    }

    @ApiOperation(value = "客户端店铺收藏服务")
    @RequestMapping(value = "shopCollect", method = RequestMethod.POST)
    public CommonResponse shopCollect (@RequestBody CollectLikeParam param) {
        merchantService.shopCollect(param);
        return CommonResponse.success();
    }

    @ApiOperation(value = "获取我的店铺收藏列表服务")
    @RequestMapping(value = "collectShopList", method = RequestMethod.GET)
    public CommonResponse shopCollect (String userId,Integer startIndex) {
        if (StringUtils.isEmpty(userId)) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(merchantService.getCollectShopListByUserId(userId,startIndex));
    }

    @ApiOperation(value = "获取我的产品收藏列表服务")
    @RequestMapping(value = "collectProductList", method = RequestMethod.GET)
    public CommonResponse productCollect (String userId) {
        if (StringUtils.isEmpty(userId)) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(productService.getCollectProductListByUserId(userId));
    }
}
