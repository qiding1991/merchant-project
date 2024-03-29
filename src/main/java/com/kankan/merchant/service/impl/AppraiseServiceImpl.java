package com.kankan.merchant.service.impl;

import com.google.gson.Gson;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.merchant.food.Pictures;
import com.kankan.merchant.module.param.AppraiseParam;
import com.kankan.merchant.service.AppraiseService;
import com.kankan.merchant.service.ProductService;
import com.kankan.merchant.utils.DateUtils;
import com.kankan.merchant.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppraiseServiceImpl implements AppraiseService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProductService productService;

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
        if (!StringUtils.isEmpty(commonAppraise.getHot()) && "1".equals(commonAppraise.getHot())) {
            query.with(Sort.by(Sort.Order.desc("wholeScore")));
        }
        if (!StringUtils.isEmpty(commonAppraise.getHot()) && "2".equals(commonAppraise.getHot())) {
            query.with(Sort.by(Sort.Order.desc("_id")));
        }
        List<CommonAppraise> appraisesList = mongoTemplate.find(query,CommonAppraise.class);
        if (!CollectionUtils.isEmpty(appraisesList)) {
            for (CommonAppraise appraise : appraisesList) {
                appraise.setLikeNum(CollectionUtils.isEmpty(appraise.getLikeUsers())?0:appraise.getLikeUsers().size());
                if (!CollectionUtils.isEmpty(appraise.getLikeUsers()) && !StringUtils.isEmpty(commonAppraise.getUserId())) {
                    appraise.setIsLike(appraise.getLikeUsers().contains(Integer.valueOf(commonAppraise.getUserId())));
                } else {
                    appraise.setIsLike(false);
                }
            }
        }
        return appraisesList;
    }

    @Override
    public List<Pictures> getShopPictures(String shopId) {
        List<Pictures> result = new ArrayList<>();
        AppraiseParam appraiseParam = new AppraiseParam();
        appraiseParam.setType("1");
        appraiseParam.setShopId(shopId);
        List<CommonAppraise> shopList = appraiseList(appraiseParam);
        for (CommonAppraise commonAppraise : shopList) {
            if (null != commonAppraise && !CollectionUtils.isEmpty(commonAppraise.getAppraisePictures())) {
                result.addAll(commonAppraise.getAppraisePictures());
            }
        }
        List<CommonProduct> productList = productService.findAllProduct(shopId);
        if (CollectionUtils.isEmpty(productList)) {
            return result;
        }
        List<String> productIdList = productList.stream().map(CommonProduct::getId).collect(Collectors.toList());
        Query query = new Query(Criteria.where("productId").in(productIdList));
        List<CommonAppraise> appraisesList = mongoTemplate.find(query,CommonAppraise.class);
        if (!CollectionUtils.isEmpty(appraisesList)) {
            for (CommonAppraise commonAppraise : appraisesList) {
                if (null != commonAppraise && !CollectionUtils.isEmpty(commonAppraise.getAppraisePictures())) {
                    result.addAll(commonAppraise.getAppraisePictures());
                }
            }
        }
        return result;
    }

    @Override
    public void markLikeAppraise(String appraiseId,Integer type,String userId) {
        LogUtil.printLog(log,"markLikeAppraise appraiseId",appraiseId);
        LogUtil.printLog(log,"markLikeAppraise type",type);
        LogUtil.printLog(log,"markLikeAppraise userId",userId);
        Update update = new Update();
        Query query = Query.query(Criteria.where("_id").is(appraiseId));
        CommonAppraise appraise = mongoTemplate.findOne(query,CommonAppraise.class);
        if (null == appraise) {
            return;
        }
        List<String> likeUsers = appraise.getLikeUsers();
        if (CollectionUtils.isEmpty(likeUsers)) {
            likeUsers = new ArrayList<>();
        }
        if (1 == type) {
            likeUsers.add(userId);
        }
        if (0 == type) {
            likeUsers.remove(userId);
        }
        appraise.setLikeUsers(likeUsers);
        /*if (0 < type) {
            update.inc("likeNum",1);
        } else {
            update.inc("likeNum",-1);
        }*/
        mongoTemplate.save(appraise);
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
