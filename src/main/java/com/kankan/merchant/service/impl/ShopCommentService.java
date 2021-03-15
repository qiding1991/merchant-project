package com.kankan.merchant.service.impl;

import com.kankan.merchant.model.comment.ShopComment;
import com.kankan.merchant.service.CommentService;

import java.util.List;

public class ShopCommentService implements CommentService<ShopComment> {
  @Override
  public ShopComment addComment(ShopComment baseComment) {
    return null;
  }

  @Override
  public List<ShopComment> findComment(String id, Integer orderBy) {
    return null;
  }

  @Override
  public void delComment(List<String> id) {

  }

  @Override
  public void updateComment(ShopComment baseComment) {

  }

  @Override
  public List<ShopComment> findMyComment(String userId) {
    return null;
  }


}
