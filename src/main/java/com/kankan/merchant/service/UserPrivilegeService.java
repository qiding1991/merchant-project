package com.kankan.merchant.service;

import java.util.List;

public interface UserPrivilegeService {
  void addPrivilegeToUser(String userId, List<String> privilege);
}
