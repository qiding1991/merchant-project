package com.kankan.merchant.service.impl;

import java.util.List;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.module.merchant.Product;
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
    public List<Product> findProduct(String typeId) {
        Query query = Query.query(Criteria.where("typeId").is(typeId));
        return mongoTemplate.find(query, Product.class);
    }

    @Override
    public Product addProduct(Product product) {
        return mongoTemplate.insert(product);
    }

    @Override
    public void approveApply(Product product) {
        Query query = Query.query(Criteria.where("_id").is(product.getId()));
        Update update = new Update();
        if (!StringUtils.isEmpty(product.getApplyStatus())) {
            update.set("applyStatus", product.getApplyStatus());
        }
        mongoTemplate.upsert(query, update, Product.class);
    }

    @Override
    public Product updateProduct(Product product) {
        return mongoTemplate.save(product);
    }

    @Override
    public void delUpdateProduct(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Product.class);
    }

    @Override
    public Product findById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Product.class);
    }

    @Override
    public List<Product> findAllProduct (String shopId) {
        if (null != shopId) {
            Query query = Query.query(Criteria.where("shopId").is(shopId));
            return mongoTemplate.find(query, Product.class);
        }
        return mongoTemplate.findAll(Product.class);
    }
}
