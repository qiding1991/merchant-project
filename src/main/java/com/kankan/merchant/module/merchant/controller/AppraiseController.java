package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import io.swagger.annotations.Api;
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
}
