package com.kankan.merchant.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.kankan.merchant.common.MerchantConstant;
import com.kankan.merchant.model.Address;
import com.kankan.merchant.module.classify.model.Category;
import com.kankan.merchant.module.merchant.ApplyInfo;
import com.kankan.merchant.module.param.MerchantApplyParam;
import com.kankan.merchant.module.param.MerchantQueryParam;
import com.kankan.merchant.module.param.RegisterShopParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.kankan.merchant.config.OrderRule;
import com.kankan.merchant.config.PriceRange;
import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.service.MerchantService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RegisterShopParam registerMerchant(RegisterShopParam registerShopParam) {
        Merchant merchant = paramToMerchant(registerShopParam);
        merchant.setRegisterTime(System.currentTimeMillis());
        merchant = mongoTemplate.insert(merchant);
        return new RegisterShopParam(merchant.getId(),merchant.getApplyInfo().getApplyStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyMerchant(MerchantApplyParam merchantApplyParam) {
        Query query = Query.query(Criteria.where("_id").is(merchantApplyParam.getApplyId()));
        Update update = new Update();
        if (!StringUtils.isEmpty(merchantApplyParam.getNewStatus())) {
            update.set("applyInfo.$.applyStatus", merchantApplyParam.getNewStatus());
        }
        mongoTemplate.upsert(query, update, Merchant.class);
    }

    private Merchant paramToMerchant (RegisterShopParam registerShopParam) {
        Merchant merchant = new Merchant();
        merchant.setUserId(registerShopParam.getUserId());
        merchant.setName(registerShopParam.getShopName());
        merchant.setCategory1(registerShopParam.getCategory1());
        merchant.setCategory2(registerShopParam.getCategory2());
        Address address = new Address();
        address.setArea(registerShopParam.getRegion());
        address.setName(registerShopParam.getAddress());
        address.setLang(Double.parseDouble(registerShopParam.getLocation().split(";")[0]));
        address.setLat(Double.parseDouble(registerShopParam.getLocation().split(";")[1]));
        merchant.setAddress(address);
        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setPhotos(Arrays.asList(registerShopParam.getFile().split(";")));
        applyInfo.setYelp(MerchantConstant.merchant_source_yelp == registerShopParam.getSourceFrom());
        applyInfo.setApplyStatus(MerchantConstant.merchant_wait_apply);
        merchant.setAveragePrice(registerShopParam.getAveragePrice());
        merchant.setWholeScore(registerShopParam.getWholeScore());
        merchant.setEnvScore(registerShopParam.getEnvScore());
        merchant.setFlavorScore(registerShopParam.getFlavorScore());
        merchant.setServiceScore(registerShopParam.getServiceScore());
        merchant.setApplyInfo(applyInfo);
        merchant.setEmail(registerShopParam.getEmail());
        merchant.setPhone(registerShopParam.getContact());
        merchant.setWebsite(registerShopParam.getWebsite());
        merchant.setWx(registerShopParam.getWelChat());
        merchant.setFaxNo(registerShopParam.getFaxNo());
        merchant.setPaymentType(registerShopParam.getPayType());
        merchant.setServiceTime(registerShopParam.getServiceTime());
        merchant.setRegisterTime(registerShopParam.getRegisterTime());
        merchant.setUpdateTime(registerShopParam.getUpdateTime());
        return merchant;
    }

    private RegisterShopParam merchantToParam (Merchant merchant) {
        RegisterShopParam registerShopParam = new RegisterShopParam();
        if (null == merchant) {
            return registerShopParam;
        }
        registerShopParam.setId(merchant.getId());
        registerShopParam.setShopName(merchant.getName());
        //registerShopParam.setCompanyName(merchant.getApplyInfo().getCompanyName());
        registerShopParam.setCategory1(merchant.getCategory1());
        registerShopParam.setCategory2(merchant.getCategory2());
        if (null != merchant.getAddress()) {
            registerShopParam.setRegion(merchant.getAddress().getArea());
            registerShopParam.setAddress(merchant.getAddress().getName());
            registerShopParam.setLocation(merchant.getAddress().getLang() + ";" + merchant.getAddress().getLat());
        }
        registerShopParam.setContact(merchant.getPhone());
        registerShopParam.setFaxNo(merchant.getFaxNo());
        registerShopParam.setServiceTime(merchant.getServiceTime());
        registerShopParam.setEmail(merchant.getEmail());
        registerShopParam.setWebsite(merchant.getWebsite());
        registerShopParam.setWelChat(merchant.getWx());
        registerShopParam.setPayType(merchant.getPaymentType());
        registerShopParam.setAveragePrice(merchant.getAveragePrice());
        if (null != merchant.getApplyInfo()) {
            registerShopParam.setCompanyName(merchant.getApplyInfo().getCompanyName());
            registerShopParam.setFile(merchant.getApplyInfo().getIDUrl());
            registerShopParam.setSourceFrom(merchant.getApplyInfo().getYelp()?0:1);
            registerShopParam.setShopPicture(merchant.getApplyInfo().getPhotos());
        }
        registerShopParam.setWholeScore(merchant.getWholeScore());
        registerShopParam.setEnvScore(merchant.getEnvScore());
        registerShopParam.setFlavorScore(merchant.getFlavorScore());
        registerShopParam.setServiceScore(merchant.getServiceScore());
        if (null != merchant.getIsHot()) {
            registerShopParam.setHot(merchant.getIsHot());
        }
        registerShopParam.setStatus(merchant.getApplyInfo().getApplyStatus());
        registerShopParam.setRegisterTime(merchant.getRegisterTime());
        registerShopParam.setUpdateTime(merchant.getUpdateTime());
        registerShopParam.setApplyStatus(merchant.getApplyInfo().getApplyStatus());
        registerShopParam.setUserId(merchant.getUserId());
        return registerShopParam;
    }

    @Override
    public List<Merchant> findShop(String classifyId) {
        Query query = Query.query(Criteria.where("classifyId").is(classifyId));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public List<Merchant> findShopBySecond(String secondClassifyId) {
        Query query = Query.query(Criteria.where("itemId").is(secondClassifyId));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public void updateMerchant(RegisterShopParam registerShopParam) {
        mongoTemplate.save(paramToMerchant(registerShopParam));
    }

    @Override
    public void delMerchant(String shopId) {
        Query query = Query.query(Criteria.where("_id").is(shopId));
        mongoTemplate.remove(query, Merchant.class);
    }

    @Override
    public RegisterShopParam findById(String shopId) {
        Query query = Query.query(Criteria.where("_id").is(shopId));
        return merchantToParam(mongoTemplate.findOne(query, Merchant.class));
    }

    @Override
    public List<RegisterShopParam> findShopList() {
        List<Merchant> list = mongoTemplate.findAll(Merchant.class);
        return list.stream().map(item -> {
            return merchantToParam(item);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Merchant> findHotShop() {
        Query query = Query.query(Criteria.where("isHot").is(true));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public List<Merchant> findHotShop(String classifyId) {
        Query query = Query.query(Criteria.where("classifyId").is(classifyId));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public List<Merchant> findNearShop(String longitude, String latitude) {
        return null;
    }

    @Override
    public List<Merchant> findNearByShop(String secondClassifyId, String area) {
        Query query = Query.query(Criteria.where("itemId").is(secondClassifyId).and("address.area").is(area));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public List<Merchant> findShop(String secondClassifyId, OrderRule rule, String longitude, String latitude) {
        return null;
    }

    @Override
    public List<Merchant> searchShop(String keyWord) {
        Query query = Query.query(Criteria.where("name").regex(keyWord));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public List<Merchant> filterShop(Integer shopType, PriceRange priceRange, Integer paymentType, Double minScore, Double maxScore) {
        //TODO ????????????????????????????????????????????????
        return mongoTemplate.findAll(Merchant.class);
    }

    @Override
    public void thumpMerchant(String shopId) {
        Merchant merchant=paramToMerchant(findById(shopId));
        merchant.setThumpCount(merchant.getThumpCount()+1);
        mongoTemplate.save(merchant);
    }

    @Override
    public List<RegisterShopParam> firstPageMerchant(RegisterShopParam registerShopParam) {
        Query query = new Query();
        if (!StringUtils.isEmpty(registerShopParam.getCategory1())) {
            query.addCriteria(Criteria.where("category1").is(registerShopParam.getCategory1()));
        }
        if (!StringUtils.isEmpty(registerShopParam.getCategory2())) {
            query.addCriteria(Criteria.where("category2").is(registerShopParam.getCategory2()));
        }
        List<RegisterShopParam> result = new ArrayList<>();
        List<Merchant> merchantList = new ArrayList<>();
        if (registerShopParam.isHot()) {
            query.addCriteria(Criteria.where("isHot").is(registerShopParam.isHot()));
            merchantList = mongoTemplate.find(query, Merchant.class);
        }
        if (!StringUtils.isEmpty(registerShopParam.getLocation())) {
            query.addCriteria(Criteria.where("location").is(registerShopParam.getLocation()));
            merchantList = mongoTemplate.find(query, Merchant.class);
        }
        if (!CollectionUtils.isEmpty(merchantList)) {
            for (Merchant merchant : merchantList) {
                result.add(merchantToParam(merchant));
            }
        }
        return result;
    }

    @Override
    public List<RegisterShopParam> chooseShop (MerchantQueryParam merchantQueryParam) {
        Query query = new Query();
        if (null != merchantQueryParam.getCategory2()) {
            query.addCriteria(Criteria.where("category2").is(merchantQueryParam.getCategory2()));
        }
        if (merchantQueryParam.getAreaCode() > 0) {
            //todo ??????????????????????????????
            query.addCriteria(Criteria.where("address.$.area").is(merchantQueryParam.getAreaCode()));
        }
        if (merchantQueryParam.getIntelligentType() > 0) {
            if (1 == merchantQueryParam.getIntelligentType() && null != merchantQueryParam.getLocation()) {
                query.addCriteria(Criteria.where("location").is(merchantQueryParam.getLocation()));
            }
            if (2 == merchantQueryParam.getIntelligentType()) {
                query.with(Sort.by(Sort.Order.desc("wholeScore")));
            }
            if (3 == merchantQueryParam.getIntelligentType()) {
                query.with(Sort.by(Sort.Order.asc("averagePrice")));
            }
            if (4 == merchantQueryParam.getIntelligentType()) {
                query.with(Sort.by(Sort.Order.desc("averagePrice")));
            }
        }
        if (merchantQueryParam.getShopType() > 0) {
            //todo ????????????????????????????????????
            switch (merchantQueryParam.getShopType()) {
                case 1:
                    query.addCriteria(Criteria.where("averagePrice").lt(10));
                    break;
                case 2:
                    query.addCriteria(Criteria.where("averagePrice").lt(30));
                    break;
                case 3:
                    query.addCriteria(Criteria.where("averagePrice").lt(60));
                    break;
            }
        }
        if (merchantQueryParam.getPrice() > 0) {
            switch (merchantQueryParam.getPrice()) {
                case 1:
                    query.addCriteria(Criteria.where("averagePrice").lt(10));
                    break;
                case 2:
                    query.addCriteria(Criteria.where("averagePrice").lt(30));
                    break;
                case 3:
                    query.addCriteria(Criteria.where("averagePrice").lt(60));
                    break;
                case 4:
                    query.addCriteria(Criteria.where("averagePrice").gt(60));
                    break;
            }
        }
        if (merchantQueryParam.getPayType() > 0) {
            query.addCriteria(Criteria.where("paymentType").is(merchantQueryParam.getPayType()));
        }
        if (merchantQueryParam.getWholeScore() > 0) {
            query.addCriteria(Criteria.where("wholeScore").is(merchantQueryParam.getWholeScore()));
        }
        List<Merchant> merchantList = mongoTemplate.find(query, Merchant.class);
        List<RegisterShopParam> result = new ArrayList<>(merchantList.size());
        if (!CollectionUtils.isEmpty(merchantList)) {
            for (Merchant merchant : merchantList) {
                result.add(merchantToParam(merchant));
            }
        }
        return result;
    }
}
