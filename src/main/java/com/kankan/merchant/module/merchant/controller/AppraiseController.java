package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@Api(tags = "客户端评价服务")
@RequestMapping("client/appraise")
@RestController
public class AppraiseController {

    @Autowired
    private AppraiseService appraiseService;

    @ApiOperation(value = "客户端添加用户评价服务")
    @PostMapping
    public CommonResponse userAppraise (CommonAppraise commonAppraise) {
        appraiseService.userAppraise(commonAppraise);
        return CommonResponse.success();
    }

    @ApiOperation(value = "客户端获取评价列表服务")
    @GetMapping
    public CommonResponse getAppraise (AppraiseParam commonAppraise) {
        return CommonResponse.success(appraiseService.appraiseList(commonAppraise));
    }

    @ApiOperation(value = "客户端评价点赞服务")
    @ApiImplicitParams({@ApiImplicitParam(name = "appraiseId", value = "评论ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "1点赞0取消点赞", required = true, paramType = "query", dataType = "Integer")})
    @PutMapping
    public CommonResponse markLikeAppraise (String appraiseId,Integer type) {
        appraiseService.markLikeAppraise(appraiseId,type);
        return CommonResponse.success();
    }

    @ApiOperation(value = "客户端产品点赞服务")
    @ApiImplicitParams({@ApiImplicitParam(name = "productId", value = "产品ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "1点赞0取消点赞", required = true, paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "markLike",method = RequestMethod.POST)
    public CommonResponse markLikeProduct (String productId,Integer type) {
        appraiseService.markLikeProduct(productId,type);
        return CommonResponse.success();
    }
}
