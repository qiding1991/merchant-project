package com.kankan.merchant.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.param.CollectLikeParam;
import com.kankan.merchant.module.param.ProductUpdateParam;
import com.kankan.merchant.utils.DateUtils;
import com.kankan.merchant.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.kankan.merchant.service.ProductService;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl implements ProductService {

    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<CommonProduct> findProduct(String typeId) {
        LogUtil.printLog(logger,"findProduct",typeId);
        Query query = Query.query(Criteria.where("typeId").is(typeId));
        return mongoTemplate.find(query, CommonProduct.class);
    }

    @Override
    public CommonProduct addProduct(CommonProduct product) {
        LogUtil.printLog(logger,"addProduct",product);
        product.setCreateTime(DateUtils.getCurDateTime());
        return mongoTemplate.insert(product);
    }

    @Override
    public void approveApply(CommonProduct product) {
        LogUtil.printLog(logger,"approveApply",product);
        Query query = Query.query(Criteria.where("_id").is(product.getId()));
        Update update = new Update();
        if (!StringUtils.isEmpty(product.getApplyStatus())) {
            update.set("applyStatus", product.getApplyStatus());
            mongoTemplate.upsert(query, update, CommonProduct.class);
        }
    }

    @Override
    public void updateProduct(ProductUpdateParam product) {
        LogUtil.printLog(logger,"updateProduct",product);
        Query query = Query.query(Criteria.where("_id").is(product.getProductId()));
        mongoTemplate.upsert(query,buildUpdate(product), CommonProduct.class);
    }

    private Update buildUpdate (ProductUpdateParam product) {
        Update update = new Update();
        if (!StringUtils.isEmpty(product.getFacePicture())) {
            update.set("facePicture",product.getFacePicture());
        }
        if (!StringUtils.isEmpty(product.getProductName())) {
            update.set("productName",product.getProductName());
        }
        if (!StringUtils.isEmpty(product.getShopId())) {
            update.set("shopId",product.getShopId());
        }
        if (!StringUtils.isEmpty(product.getSaleTime())) {
            update.set("saleTime",product.getSaleTime());
        }
        if (!StringUtils.isEmpty(product.getPrice())) {
            update.set("price",product.getPrice());
        }
        if (!StringUtils.isEmpty(product.getDescription())) {
            update.set("description",product.getDescription());
        }
        update.set("updateTime", DateUtils.getCurDateTime());
        return update;
    }

    @Override
    public void delUpdateProduct(String id) {
        LogUtil.printLog(logger,"delUpdateProduct",id);
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, CommonProduct.class);
    }

    @Override
    public CommonProduct findById(String id) {
        LogUtil.printLog(logger,"findById",id);
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, CommonProduct.class);
    }

    @Override
    public List<CommonProduct> findAllProduct (String shopId) {
        LogUtil.printLog(logger,"findAllProduct",shopId);
        if (null != shopId) {
            Query query = Query.query(Criteria.where("shopId").is(shopId));
            return mongoTemplate.find(query, CommonProduct.class);
        }
        return mongoTemplate.findAll(CommonProduct.class);
    }


    public List<CommonProduct> findAllProduct () {
        return mongoTemplate.findAll(CommonProduct.class);
    }

    @Override
    public void productCollect(CollectLikeParam param) {
        LogUtil.printLog(logger,"productCollect",param);
        Query query = Query.query(Criteria.where("_id").is(param.getTargetId()));
        CommonProduct product = mongoTemplate.findOne(query,CommonProduct.class);
        if (null == product) {
            return;
        }
        List<Integer> collectUsers = product.getCollectUsers();
        if (CollectionUtils.isEmpty(collectUsers)) {
            if (2 == param.getType()) {
                collectUsers = new ArrayList<>(1);
                collectUsers.add(param.getUserId());
            }
        } else {
            if (2 == param.getType()) {
                collectUsers.add(param.getUserId());
            } else if (1 == param.getType()) {
                collectUsers.remove(param.getUserId());
            }
        }
        product.setCollectUsers(collectUsers);
        mongoTemplate.save(product);
    }

    @Override
    public void productMarkLike(CollectLikeParam param) {
        LogUtil.printLog(logger,"productMarkLike",param);
        Query query = Query.query(Criteria.where("_id").is(param.getTargetId()));
        CommonProduct product = mongoTemplate.findOne(query,CommonProduct.class);
        if (null == product) {
            return;
        }
        List<Integer> markLikeUsers = product.getLikeUsers();
        if (CollectionUtils.isEmpty(markLikeUsers)) {
            if (2 == param.getType()) {
                markLikeUsers = new ArrayList<>(1);
                markLikeUsers.add(param.getUserId());
            }
        } else {
            if (2 == param.getType()) {
                markLikeUsers.add(param.getUserId());
            } else if (1 == param.getType()) {
                markLikeUsers.remove(param.getUserId());
            }
        }
        product.setLikeUsers(markLikeUsers);
        if (!CollectionUtils.isEmpty(markLikeUsers)) {
            product.setLikeNum(markLikeUsers.size());
        }
        mongoTemplate.save(product);
    }
}
