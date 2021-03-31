package com.kankan.merchant.service.impl;

import com.kankan.merchant.model.comment.FoodComment;
import com.kankan.merchant.model.comment.MenuComment;
import com.kankan.merchant.model.comment.ShopComment;
import com.kankan.merchant.service.CommentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ShopCommentService implements CommentService<ShopComment> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ShopComment addComment(ShopComment baseComment) {
        ShopComment comment = mongoTemplate.insert(baseComment);
        return comment;
    }

    @Override
    public List<ShopComment> findComment(String id, Integer orderBy) {
        Query query = Query.query(Criteria.where("shopId").is(id));
        return mongoTemplate.find(query, ShopComment.class);
    }

    @Override
    public void delComment(List<String> id) {
        Query query = Query.query(Criteria.where("_id").in(id));
        mongoTemplate.remove(query, MenuComment.class);
    }

    @Override
    public void updateComment(ShopComment baseComment) {
        Query query = Query.query(Criteria.where("_id").in(baseComment.getId()));
        ShopComment shopComment = mongoTemplate.findOne(query, ShopComment.class);
        shopComment.setRjMoney(baseComment.getRjMoney());
        shopComment.setScore(baseComment.getScore());
        mongoTemplate.save(shopComment);
    }

    @Override
    public List<ShopComment> findMyComment(String userId) {
        Query query = Query.query(Criteria.where("userId").in(userId));
        return mongoTemplate.find(query, ShopComment.class);
    }

    @Override
    public void thumpComment(String id) {
        Query query = Query.query(Criteria.where("_id").in(id));
        FoodComment foodComment = mongoTemplate.findOne(query, FoodComment.class);
        foodComment.setThumpCount(foodComment.getThumpCount() + 1);
        mongoTemplate.save(foodComment);
    }
}
