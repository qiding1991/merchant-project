package com.kankan.merchant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.kankan.merchant.model.product.Product;
import com.kankan.merchant.service.ProductService;

/**
 * @author <qiding@kuaishou.com>
 * Created on 2021-04-04
 */
public class ProductServiceImpl implements ProductService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Product> findProduct(String typeId) {
        Query query = Query.query(Criteria.where("typeId").is(typeId));
        return mongoTemplate.find(query, Product.class);
    }

    @Override
    public Product addProduct(Product product) {
        Product result = mongoTemplate.insert(product);
        return result;
    }

    @Override
    public Product updateProduct(Product product) {
        Product result = mongoTemplate.save(product);
        return result;
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
}
