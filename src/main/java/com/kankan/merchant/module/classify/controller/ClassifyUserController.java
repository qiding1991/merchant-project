package com.kankan.merchant.module.classify.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.module.classify.model.MerchantClassify;
import com.kankan.merchant.service.ClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "用户接口-商家分类信息")
@RequestMapping("user/classify")
@RestController
public class ClassifyUserController {

  @Autowired
  private ClassifyService merchantClassifyService;


  @ApiOperation("获取商家分类信息")
  @GetMapping("list")
  public CommonResponse list() {
    List<MerchantClassify> merchantClassifyList = merchantClassifyService.findAllClassify();
    return CommonResponse.success(merchantClassifyList);
  }
}
