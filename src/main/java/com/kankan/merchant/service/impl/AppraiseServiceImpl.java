package com.kankan.merchant.service.impl;

import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class AppraiseServiceImpl implements AppraiseService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public CommonAppraise userAppraise(CommonAppraise commonAppraise) {
        return mongoTemplate.insert(commonAppraise);
    }

    @Override
    public List<CommonAppraise> appraiseList(AppraiseParam commonAppraise) {
        Query query = new Query();
        if ("1".equals(commonAppraise.getType())) {
            query.addCriteria(Criteria.where("shopId").is(commonAppraise.getShopId()));
        } else {
            query.addCriteria(Criteria.where("productId").is(commonAppraise.getShopId()));
        }
        if (!StringUtils.isEmpty(commonAppraise.getHot())) {
            query.addCriteria(Criteria.where("hot").is(commonAppraise.getHot()));
        }
        if (!StringUtils.isEmpty(commonAppraise.getUserId())) {
            query.addCriteria(Criteria.where("userId").is(commonAppraise.getUserId()));
        }
        return mongoTemplate.find(query,CommonAppraise.class);
    }
}
