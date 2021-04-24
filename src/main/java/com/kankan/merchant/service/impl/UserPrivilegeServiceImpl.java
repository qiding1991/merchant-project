package com.kankan.merchant.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.kankan.merchant.service.UserPrivilegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class UserPrivilegeServiceImpl implements UserPrivilegeService {

  @Value("${user.privilege.addPrivilegeToUser}")
  public String addPrivilegeToUserUrl;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void addPrivilegeToUser(String userId, List<String> privilege) {
    String requestBody = new Gson().toJson(ImmutableMap.of("userId", userId, "privilege", privilege));
    MultiValueMap headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
    HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(addPrivilegeToUserUrl, requestEntity, String.class);
    log.info("请求完成,responseCode={},response={}",responseEntity.getStatusCode(),responseEntity.getBody());
  }
}
