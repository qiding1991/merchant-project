package com.kankan.merchant.service.impl;

import com.google.common.collect.ImmutableList;
import com.kankan.merchant.service.UserPrivilegeService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserPrivilegeServiceImplTest {

  @Autowired
  private UserPrivilegeService userPrivilegeService;

  @Test
  void addPrivilegeToUser() {
    userPrivilegeService.addPrivilegeToUser("1", ImmutableList.of("aa","bb"));
  }
}
