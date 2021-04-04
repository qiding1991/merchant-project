package com.kankan.merchant.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kankan.merchant.common.MerchantConstant;
import com.kankan.merchant.config.OrderRule;
import com.kankan.merchant.config.PriceRange;
import com.kankan.merchant.model.Address;
import com.kankan.merchant.module.merchant.ApplyInfo;
import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.module.regiter.param.RegisterShopParam;
import com.kankan.merchant.service.MerchantService;


@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RegisterShopParam registerMerchant(RegisterShopParam registerShopParam) {
        Merchant merchant = buildData(registerShopParam);
        merchant = mongoTemplate.insert(merchant);
        return new RegisterShopParam(merchant.getId(),merchant.getApplyInfo().getApplyStatus());
    }

    private Merchant buildData (RegisterShopParam registerShopParam) {
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
        return merchant;
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
    public void updateMerchant(Merchant merchant) {
        //TODO 设置更新信息
        mongoTemplate.save(merchant);
        return;
    }

    @Override
    public void delMerchant(String shopId) {
        Query query = Query.query(Criteria.where("_id").is(shopId));
        mongoTemplate.remove(query, Merchant.class);
    }

    @Override
    public Merchant findById(String shopId) {
        Query query = Query.query(Criteria.where("_id").is(shopId));
        return mongoTemplate.findOne(query, Merchant.class);
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
        Merchant merchant=findById(shopId);
        merchant.setThumpCount(merchant.getThumpCount()+1);
        mongoTemplate.save(merchant);
    }
}
