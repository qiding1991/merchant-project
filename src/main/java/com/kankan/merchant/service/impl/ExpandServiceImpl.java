package com.kankan.merchant.service.impl;

import com.kankan.merchant.module.merchant.Expand;
import com.kankan.merchant.service.ExpandService;
import com.kankan.merchant.utils.DateUtils;
import com.kankan.merchant.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
@Slf4j
public class ExpandServiceImpl implements ExpandService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void commitExpand(Expand expand) {
        LogUtil.printLog(log,"commitExpand",expand);
        expand.setCreateTime(DateUtils.getCurDateTime());
        mongoTemplate.insert(expand);
    }

    @Override
    public List<Expand> findExpand(Expand expand) {
        LogUtil.printLog(log,"findExpand",expand);
        Query query = Query.query(Criteria.where("type").is(expand.getType()));
        if (!StringUtils.isEmpty(expand.getShopId())) {
            query.addCriteria(Criteria.where("shopId").is(expand.getShopId()));
        }
        if (!StringUtils.isEmpty(expand.getUserId())) {
            query.addCriteria(Criteria.where("userId").is(expand.getUserId()));
        }
        if (null != expand.getItemsType() && 0 < expand.getItemsType()) {
            query.addCriteria(Criteria.where("itemsType").is(expand.getItemsType()));
        }
        if (!StringUtils.isEmpty(expand.getId())) {
            query.addCriteria(Criteria.where("_id").is(expand.getId()));
        }
        return mongoTemplate.find(query,Expand.class);
    }
}
