package com.kankan.merchant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kankan.merchant.model.comment.MenuComment;
import com.kankan.merchant.service.CommentService;

@Service
public class MenuCommentService implements CommentService<MenuComment> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MenuComment addComment(MenuComment baseComment) {
        MenuComment comment = mongoTemplate.insert(baseComment);
        return comment;
    }

    @Override
    public List<MenuComment> findComment(String id, Integer orderBy) {
        Query query = Query.query(Criteria.where("shopId").is(id));
        return mongoTemplate.find(query,MenuComment.class);
    }

    @Override
    public void delComment(List<String> id) {
        Query query = Query.query(Criteria.where("_id").in(id));
        mongoTemplate.remove(query, MenuComment.class);
    }

    @Override
    public void updateComment(MenuComment baseComment) {
        Query query = Query.query(Criteria.where("_id").in(baseComment.getId()));
        MenuComment foodComment= mongoTemplate.findOne(query,MenuComment.class);
        //TODO 菜单评价字段
//        foodComment.setFwScore(baseComment.getFwScore());
//        foodComment.setHjScore(baseComment.getHjScore());
//        foodComment.setKwScore(baseComment.getKwScore());
        mongoTemplate.save(foodComment);
    }

    @Override
    public List<MenuComment> findMyComment(String userId) {
        Query query = Query.query(Criteria.where("userId").in(userId));
        return mongoTemplate.find(query,MenuComment.class);
    }

    @Override
    public void thumpComment(String id) {
        Query query = Query.query(Criteria.where("_id").in(id));
        MenuComment menuComment= mongoTemplate.findOne(query,MenuComment.class);
        menuComment.setThumpCount(menuComment.getThumpCount()+1);
        mongoTemplate.save(menuComment);
    }
}
