package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "产品")
@RequestMapping("admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "管理后台添加产品服务")
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public CommonResponse addProduct (@RequestBody CommonProduct product) {
        if (null == product) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(productService.addProduct(product));
    }

    @ApiOperation(value = "管理后台审核产品服务")
    @RequestMapping(value = "/approveApply", method = RequestMethod.POST)
    public CommonResponse approveApply (@RequestBody CommonProduct product) {
        if (null == product) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        productService.approveApply(product);
        return CommonResponse.success();
    }

    @ApiOperation(value = "管理后台修改产品服务")
    @RequestMapping(value = "/updateProduct", method = RequestMethod.PUT)
    public CommonResponse updateProduct (@RequestBody CommonProduct product) {
        if (null == product) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(productService.updateProduct(product));
    }

    @ApiOperation(value = "管理后台查询产品服务")
    @RequestMapping(value = "/findProduct", method = RequestMethod.GET)
    public CommonResponse findProduct (String shopId) {
        if (null == shopId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(productService.findAllProduct(shopId));
    }

    @ApiOperation(value = "管理后台删除产品服务")
    @RequestMapping(value = "/delProduct", method = RequestMethod.DELETE)
    public CommonResponse delProduct (String productId) {
        if (null == productId) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        productService.delUpdateProduct(productId);
        return CommonResponse.success();
    }
}
