package com.kankan.merchant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kankan.merchant.model.MenuDetail;
import com.kankan.merchant.service.MenuService;


@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MenuDetail addMenu(MenuDetail menuDetail) {
        return mongoTemplate.insert(menuDetail);
    }

    @Override
    public MenuDetail findById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, MenuDetail.class);
    }

    @Override
    public List<MenuDetail> findByShopId(String shopId) {
        Query query = Query.query(Criteria.where("shopId").is(shopId));
        return mongoTemplate.find(query, MenuDetail.class);
    }

    @Override
    public void updateMenu(MenuDetail menuDetail) {
        mongoTemplate.save(menuDetail);
    }

    @Override
    public void delMenu(MenuDetail menuDetail) {
        Query query = Query.query(Criteria.where("_id").is(menuDetail.getId()));
        mongoTemplate.remove(query, MenuDetail.class);
        return;

    }
}
