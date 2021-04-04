package com.kankan.merchant.service.impl;

import com.kankan.merchant.model.comment.FoodComment;
import com.kankan.merchant.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 餐饮评论
 */
@Service
public class FoodCommentService implements CommentService<FoodComment> {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public FoodComment addComment(FoodComment foodComment) {
        FoodComment comment = mongoTemplate.insert(foodComment);
        return comment;
    }

    @Override
    public List<FoodComment> findComment(String id, Integer orderBy) {
        Query query = Query.query(Criteria.where("foodId").is(id));
        return mongoTemplate.find(query,FoodComment.class);
    }

    @Override
    public void delComment(List<String> id) {
        Query query = Query.query(Criteria.where("_id").in(id));
        mongoTemplate.remove(query, FoodComment.class);
    }

    @Override
    public void updateComment(FoodComment baseComment) {
        Query query = Query.query(Criteria.where("_id").in(baseComment.getId()));
        FoodComment foodComment= mongoTemplate.findOne(query,FoodComment.class);
        foodComment.setFwScore(baseComment.getFwScore());
        foodComment.setHjScore(baseComment.getHjScore());
        foodComment.setKwScore(baseComment.getKwScore());
        mongoTemplate.save(foodComment);
    }

    @Override
    public List<FoodComment> findMyComment(String userId) {
        Query query = Query.query(Criteria.where("userId").in(userId));
        return mongoTemplate.find(query,FoodComment.class);
    }

    @Override
    public void thumpComment(String id) {
        Query query = Query.query(Criteria.where("_id").in(id));
        FoodComment foodComment= mongoTemplate.findOne(query,FoodComment.class);
        foodComment.setThumpCount(foodComment.getThumpCount()+1);
        mongoTemplate.save(foodComment);
    }

}
