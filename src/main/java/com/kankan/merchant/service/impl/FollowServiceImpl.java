package com.kankan.merchant.service.impl;

import com.kankan.merchant.model.follow.Follow;
import com.kankan.merchant.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Follow addFollow(Follow follow) {
    Follow result = mongoTemplate.insert(follow);
    return result;
  }

  @Override
  public List<Follow> myFollow(String userId) {
    Query query = Query.query(Criteria.where("userId").is(userId));
    return mongoTemplate.find(query, Follow.class);
  }
}
