package com.kankan.merchant.service.impl;

import com.kankan.merchant.model.comment.FoodComment;
import com.kankan.merchant.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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


    return null;
  }

  @Override
  public void delComment(List<String> id) {

  }

  @Override
  public void updateComment(FoodComment baseComment) {

  }

  @Override
  public List<FoodComment> findMyComment(String userId) {
    return null;
  }

  @Override
  public void thumpComment(String id) {

  }

}
