package com.kankan.merchant.module.merchant.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.module.merchant.Expand;
import com.kankan.merchant.service.ExpandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "客户端扩展服务")
@RequestMapping("client/expand")
public class ExpandController {

    @Autowired
    private ExpandService expandService;

    @ApiOperation(value = "客户端提交扩展服务")
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    public CommonResponse commitExpand (Expand expand) {
        if (null == expand) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        expandService.commitExpand(expand);
        return CommonResponse.success();
    }

    @ApiOperation(value = "客户端查询扩展服务")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public CommonResponse findExpand (Expand expand) {
        if (null == expand || null == expand.getType() || 0 >= expand.getType()) {
            return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
        }
        return CommonResponse.success(expandService.findExpand(expand));
    }
}
