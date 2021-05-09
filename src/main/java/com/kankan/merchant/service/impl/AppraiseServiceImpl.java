package com.kankan.merchant.service.impl;

import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import com.kankan.merchant.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class AppraiseServiceImpl implements AppraiseService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public CommonAppraise userAppraise(CommonAppraise commonAppraise) {
        commonAppraise.setTime(DateUtils.getCurDateTime());
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

    @Override
    public void markLikeAppraise(String appraiseId,Integer type) {
        Update update = new Update();
        if (!StringUtils.isEmpty(appraiseId)) {
            Query query = Query.query(Criteria.where("_id").is(appraiseId));
            if (0 < type) {
                update.inc("likeNum",1);
            } else {
                update.inc("likeNum",-1);
            }
            mongoTemplate.upsert(query,update,CommonAppraise.class);
        }
    }

    @Override
    public void markLikeProduct(String productId,Integer type) {
        Update update = new Update();
        if (!StringUtils.isEmpty(productId)) {
            Query query = Query.query(Criteria.where("_id").is(productId));
            if (0 < type) {
                update.inc("likeNum",1);
            } else {
                update.inc("likeNum",-1);
            }
            mongoTemplate.upsert(query,update, CommonProduct.class);
        }
    }
}
