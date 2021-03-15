package com.kankan.merchant.service;

import com.kankan.merchant.model.follow.Follow;

import java.util.List;


public interface FollowService {

  Follow addFollow(Follow follow);

  List<Follow> myFollow(String userId);
}
