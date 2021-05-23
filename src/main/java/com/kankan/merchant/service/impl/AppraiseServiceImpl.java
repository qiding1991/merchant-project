package com.kankan.merchant.service.impl;

import com.google.gson.Gson;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import com.kankan.merchant.utils.DateUtils;
import com.kankan.merchant.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
@Slf4j
public class AppraiseServiceImpl implements AppraiseService {

    @Autowired
    private MongoTemplate mongoTemplate;
    private Gson gson = new Gson();

    @Override
    public CommonAppraise userAppraise(CommonAppraise commonAppraise) {
        commonAppraise.setTime(DateUtils.getCurDateTime());
        if ("1".equals(commonAppraise.getType())) {
            commonAppraise.setProductId(null);
        }
        if ("2".equals(commonAppraise.getType())) {
            commonAppraise.setShopId(null);
        }
        LogUtil.printLog(log,"add appraise input param ========>>{}",commonAppraise);
        return mongoTemplate.insert(commonAppraise);
    }

    @Override
    public List<CommonAppraise> appraiseList(AppraiseParam commonAppraise) {
        LogUtil.printLog(log,"get appraise input param ========>>{}",commonAppraise);
        Query query = new Query();
        if ("1".equals(commonAppraise.getType())) {
            query.addCriteria(Criteria.where("shopId").is(commonAppraise.getShopId()));
        } else {
            query.addCriteria(Criteria.where("productId").is(commonAppraise.getProductId()));
        }
        if (!StringUtils.isEmpty(commonAppraise.getHot())) {
            query.addCriteria(Criteria.where("hot").is(commonAppraise.getHot()));
        }
        return mongoTemplate.find(query,CommonAppraise.class);
    }

    @Override
    public void markLikeAppraise(String appraiseId,Integer type) {
        LogUtil.printLog(log,"markLikeAppraise appraiseId",appraiseId);
        LogUtil.printLog(log,"markLikeAppraise type",type);
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
        LogUtil.printLog(log,"markLikeProduct productId",productId);
        LogUtil.printLog(log,"markLikeProduct type",type);
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
