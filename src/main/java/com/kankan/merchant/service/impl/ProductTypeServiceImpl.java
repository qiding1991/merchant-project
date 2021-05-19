package com.kankan.merchant.service.impl;

import java.util.List;

import com.kankan.merchant.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.kankan.merchant.model.product.ProductType;
import com.kankan.merchant.service.ProductTypeService;


@Component
@Slf4j
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public ProductType addProductType(ProductType productType) {
        LogUtil.printLog(log,"addProductType",productType);
        ProductType result=  mongoTemplate.save(productType);
        return  result;
    }

    @Override
    public List<ProductType> productTypeList(String shopId) {
        LogUtil.printLog(log,"productTypeList",shopId);
      Query query = Query.query(Criteria.where("shopId").is(shopId));
      return mongoTemplate.find(query,ProductType.class);
    }

    @Override
    public void delProductType(String id) {
        LogUtil.printLog(log,"delProductType",id);
         Query query = Query.query(Criteria.where("_id").is(id));
         mongoTemplate.remove(query,ProductType.class);
         return;
    }

    @Override
    public void updateProductType(ProductType productType) {
        LogUtil.printLog(log,"updateProductType",productType);
        mongoTemplate.save(productType);
    }
}
