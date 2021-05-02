package com.kankan.merchant.service.impl;

import java.util.List;

import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.kankan.merchant.service.ProductService;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<CommonProduct> findProduct(String typeId) {
        Query query = Query.query(Criteria.where("typeId").is(typeId));
        return mongoTemplate.find(query, CommonProduct.class);
    }

    @Override
    public CommonProduct addProduct(CommonProduct product) {
        product.setCreateTime(DateUtils.getCurDateTime());
        return mongoTemplate.insert(product);
    }

    @Override
    public void approveApply(CommonProduct product) {
        Query query = Query.query(Criteria.where("_id").is(product.getId()));
        Update update = new Update();
        if (!StringUtils.isEmpty(product.getApplyStatus())) {
            update.set("applyStatus", product.getApplyStatus());
        }
        mongoTemplate.upsert(query, update, CommonProduct.class);
    }

    @Override
    public void updateProduct(CommonProduct product) {
        Query query = Query.query(Criteria.where("_id").is(product.getId()));
        mongoTemplate.upsert(query,buildUpdate(product), CommonProduct.class);

    }

    private Update buildUpdate (CommonProduct product) {
        Update update = new Update();
        if (!StringUtils.isEmpty(product.getFacePicture())) {
            update.set("facePicture",product.getFacePicture());
        }
        if (!StringUtils.isEmpty(product.getFacePicture())) {
            update.set("productName",product.getProductName());
        }
        if (!StringUtils.isEmpty(product.getFacePicture())) {
            update.set("shopId",product.getShopId());
        }
        if (StringUtils.isEmpty(product.getFacePicture())) {
            update.set("saleTime",product.getSaleTime());
        }
        if (StringUtils.isEmpty(product.getFacePicture())) {
            update.set("price",product.getPrice());
        }
        if (StringUtils.isEmpty(product.getFacePicture())) {
            update.set("description",product.getDescription());
        }
        update.set("updateTime", DateUtils.getCurDateTime());
        return update;
    }

    @Override
    public void delUpdateProduct(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, CommonProduct.class);
    }

    @Override
    public CommonProduct findById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, CommonProduct.class);
    }

    @Override
    public List<CommonProduct> findAllProduct (String shopId) {
        if (null != shopId) {
            Query query = Query.query(Criteria.where("shopId").is(shopId));
            return mongoTemplate.find(query, CommonProduct.class);
        }
        return mongoTemplate.findAll(CommonProduct.class);
    }


    public List<CommonProduct> findAllProduct () {
        return mongoTemplate.findAll(CommonProduct.class);
    }
}
