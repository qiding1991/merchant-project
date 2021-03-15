package com.kankan.merchant.service.impl;

import com.kankan.merchant.model.comment.FoodComment;
import com.kankan.merchant.service.CommentService;

import java.util.List;

/**
 *  餐饮评论
 */
public class FoodCommentService implements CommentService<FoodComment> {
  @Override
  public FoodComment addComment(FoodComment baseComment) {
    return null;
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

}
