package com.kankan.merchant.module.classify.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.module.classify.model.ItemClassify;
import com.kankan.merchant.module.classify.model.MerchantClassify;
import com.kankan.merchant.module.classify.param.ClassifyParam;
import com.kankan.merchant.module.classify.param.ItemClassifyParam;
import com.kankan.merchant.service.ClassifyService;
import com.kankan.merchant.service.impl.MerchantClassifyServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
@Api(tags = "管理后台-商家分类信息")
@RequestMapping("admin/classify")
@RestController
public class ClassifyAdminController {

  @Autowired
  private ClassifyService merchantClassifyService;

  @ApiOperation("添加商家大类")
  @PostMapping("add")
  public CommonResponse addClassify(@RequestBody ClassifyParam classifyParam) {
    MerchantClassify merchantClassify = new MerchantClassify();
    BeanUtils.copyProperties(classifyParam, merchantClassify);
    merchantClassify = merchantClassifyService.addMerchantClassify(merchantClassify);
    return CommonResponse.success(merchantClassify.getId());
  }


  @ApiOperation("添加商家分类小类")
  @PostMapping("addItem")
  public CommonResponse addClassifyItem(@RequestBody ItemClassifyParam itemParam) {
    String classifyId = itemParam.getClassifyId();
    List<ItemClassify> itemClassifyList = itemParam.getItemName().stream().map(ItemClassify::new).collect(Collectors.toList());
    merchantClassifyService.addMerchantItemClassify(classifyId,itemClassifyList);
    return CommonResponse.success();
  }
}
