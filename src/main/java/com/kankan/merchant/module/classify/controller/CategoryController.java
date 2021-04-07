package com.kankan.merchant.module.classify.controller;

import com.kankan.merchant.common.CommonResponse;
import com.kankan.merchant.common.ErrorCode;
import com.kankan.merchant.module.classify.model.Category;
import com.kankan.merchant.module.classify.param.CategoryParam;
import com.kankan.merchant.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Api(tags = "管理后台-商家分类信息")
@RequestMapping("admin/category")
@RestController
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @ApiOperation("添加分类")
  @PostMapping(value = "add")
  public CommonResponse addClassify(@RequestBody CategoryParam categoryParam) {
    Category category = new Category();
    BeanUtils.copyProperties(categoryParam, category);
    return CommonResponse.success(categoryService.addCategory(category));
  }

  @ApiOperation(value = "查询分类",notes = "不传parentId默认查询所有一级分类,反之查询parentId子分类")
  @DeleteMapping(value = "query")
  public CommonResponse queryCategory(String parentId) {
    return CommonResponse.success(categoryService.findAllCategory(parentId));
  }

  @ApiOperation("更新分类")
  @DeleteMapping(value = "updateCategory")
  public CommonResponse updateCategory(CategoryParam categoryParam) {
    if (null == categoryParam) {
      return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
    }
    categoryService.updateCategory(categoryParam);
    return CommonResponse.success();
  }

  @ApiOperation("删除分类")
  @DeleteMapping(value = "deleteCategory")
  public CommonResponse delCategory(String categoryId) {
    if (null == categoryId) {
      return CommonResponse.error(ErrorCode.PARAM_CHECK_ERROR);
    }
    categoryService.delCategory(categoryId);
    return CommonResponse.success();
  }
}
