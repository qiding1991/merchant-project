package com.kankan.merchant.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.kankan.merchant.common.MerchantConstant;
import com.kankan.merchant.model.Address;
import com.kankan.merchant.module.merchant.ApplyInfo;
import com.kankan.merchant.module.regiter.param.RegisterShopParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.kankan.merchant.config.OrderRule;
import com.kankan.merchant.config.PriceRange;
import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.service.MerchantService;


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

    private Merchant paramToMerchant (RegisterShopParam registerShopParam) {
        Merchant merchant = new Merchant();
        merchant.setName(registerShopParam.getShopName());
        merchant.setClassifyId(registerShopParam.getCategory1());
        merchant.setItemId(registerShopParam.getCategory2());
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
        registerShopParam.setCategory1(merchant.getClassifyId());
        registerShopParam.setCategory2(merchant.getItemId());
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
        registerShopParam.setPercapitaPrice("0");
        if (null != merchant.getApplyInfo()) {
            registerShopParam.setCompanyName(merchant.getApplyInfo().getCompanyName());
            registerShopParam.setFile(merchant.getApplyInfo().getIDUrl());
            registerShopParam.setSourceFrom(merchant.getApplyInfo().getYelp()?0:1);
            registerShopParam.setShopPicture(merchant.getApplyInfo().getPhotos());
        }
        registerShopParam.setWholeScore(0);
        registerShopParam.setEnvScore(0);
        registerShopParam.setFlavorScore(0);
        registerShopParam.setServiceScore(0);
        if (null != merchant.getIsHot()) {
            registerShopParam.setHot(merchant.getIsHot());
        }
        registerShopParam.setStatus(merchant.getApplyInfo().getApplyStatus());
        registerShopParam.setRegisterTime(merchant.getRegisterTime());
        registerShopParam.setUpdateTime(merchant.getUpdateTime());
        registerShopParam.setApplyStatus(merchant.getApplyInfo().getApplyStatus());
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
        //TODO 类型不知道，暂时只返回所有的数据
        return mongoTemplate.findAll(Merchant.class);
    }

    @Override
    public void thumpMerchant(String shopId) {
        Merchant merchant=paramToMerchant(findById(shopId));
        merchant.setThumpCount(merchant.getThumpCount()+1);
        mongoTemplate.save(merchant);
    }
}
