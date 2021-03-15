package com.kankan.merchant.service.impl;

import com.kankan.merchant.model.comment.MenuComment;
import com.kankan.merchant.service.CommentService;

import java.util.List;

public class MenuCommentService implements CommentService<MenuComment> {


  @Override
  public MenuComment addComment(MenuComment baseComment) {
    return null;
  }

  @Override
  public List<MenuComment> findComment(String id, Integer orderBy) {
    return null;
  }

  @Override
  public void delComment(List<String> id) {

  }

  @Override
  public void updateComment(MenuComment baseComment) {

  }

  @Override
  public List<MenuComment> findMyComment(String userId) {
    return null;
  }
}
