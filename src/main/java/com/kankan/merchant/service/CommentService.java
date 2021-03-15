package com.kankan.merchant.service;

import java.util.List;

public interface CommentService<T> {
  /**
   * 添加评论
   * @param baseComment
   * @return
   */
   T addComment(T baseComment);
  /**
   * 获取评论列表
   * @param id shopId或者productId
   * @return
   */
   List<T> findComment(String id,Integer orderBy);

   void delComment(List<String> id);

   void updateComment(T baseComment);

   List<T> findMyComment(String userId);

   void thumpComment(String id);

}
