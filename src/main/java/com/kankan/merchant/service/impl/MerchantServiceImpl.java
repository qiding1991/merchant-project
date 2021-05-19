package com.kankan.merchant.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.kankan.merchant.common.MerchantConstant;
import com.kankan.merchant.model.Address;
import com.kankan.merchant.model.product.Product;
import com.kankan.merchant.module.merchant.ApplyInfo;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.param.CollectLikeParam;
import com.kankan.merchant.module.param.MerchantApplyParam;
import com.kankan.merchant.module.param.MerchantQueryParam;
import com.kankan.merchant.module.param.RegisterShopParam;
import com.kankan.merchant.service.UserPrivilegeService;
import com.kankan.merchant.utils.DateUtils;
import com.kankan.merchant.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
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
@Slf4j
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserPrivilegeService userPrivilegeService;

    @Override
    public RegisterShopParam registerMerchant(RegisterShopParam registerShopParam) {
        LogUtil.printLog(log,"registerMerchant",registerShopParam);
        Merchant merchant = paramToMerchant(registerShopParam);
        merchant.setRegisterTime(DateUtils.getCurDateTime());
        merchant = mongoTemplate.insert(merchant);
        return new RegisterShopParam(merchant.getId(),merchant.getApplyInfo().getApplyStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyMerchant(MerchantApplyParam merchantApplyParam) {
        LogUtil.printLog(log,"applyMerchant",merchantApplyParam);
        Query query = Query.query(Criteria.where("_id").is(merchantApplyParam.getApplyId()));
        Update update = new Update();
        if (!StringUtils.isEmpty(merchantApplyParam.getNewStatus())) {
            update.set("applyInfo.$.applyStatus", merchantApplyParam.getNewStatus());
        }
        mongoTemplate.upsert(query, update, Merchant.class);
        //给商家用户添加权限
        RegisterShopParam registerShopParam = this.findById(merchantApplyParam.getApplyId());
        userPrivilegeService.addPrivilegeToUser(registerShopParam.getUserId(), merchantApplyParam.getNewApplyUserPrivilege());
    }

    private Merchant paramToMerchant (RegisterShopParam registerShopParam) {
        Merchant merchant = new Merchant();
        merchant.setId(registerShopParam.getId());
        merchant.setUserId(String.valueOf(registerShopParam.getUserId()));
        merchant.setName(registerShopParam.getShopName());
        merchant.setCategory1(registerShopParam.getCategory1());
        merchant.setCategory2(registerShopParam.getCategory2());
        Address address = new Address();
        address.setArea(registerShopParam.getRegion());
        address.setName(registerShopParam.getAddress());
        if (!StringUtils.isEmpty(registerShopParam.getLocation())) {
            address.setLang(Double.parseDouble(registerShopParam.getLocation().split(";")[0]));
            address.setLat(Double.parseDouble(registerShopParam.getLocation().split(";")[1]));
            List<Double> locationData = new ArrayList<>(2);
            Point point = new Point(address.getLang(), address.getLat());
            merchant.setLocation(point);
        }
        merchant.setAddress(address);
        ApplyInfo applyInfo = new ApplyInfo();
        if (!StringUtils.isEmpty(registerShopParam.getFile())) {
            applyInfo.setIDUrl(registerShopParam.getFile());
        }
        if (!CollectionUtils.isEmpty(registerShopParam.getShopPicture())) {
            applyInfo.setPhotos(registerShopParam.getShopPicture());
        }
        applyInfo.setYelp(registerShopParam.getSourceFrom()==1);
        applyInfo.setApplyStatus(MerchantConstant.merchant_wait_apply);
        merchant.setAveragePrice(registerShopParam.getAveragePrice());
        if (!StringUtils.isEmpty(registerShopParam.getWholeScore())) {
            merchant.setWholeScore(Double.parseDouble(registerShopParam.getWholeScore()));
        }
        if (!StringUtils.isEmpty(registerShopParam.getEnvScore())) {
            merchant.setEnvScore(Double.parseDouble(registerShopParam.getEnvScore()));
        }
        if (!StringUtils.isEmpty(registerShopParam.getFlavorScore())) {
            merchant.setFlavorScore(Double.parseDouble(registerShopParam.getFlavorScore()));
        }
        if (!StringUtils.isEmpty(registerShopParam.getServiceScore())) {
            merchant.setServiceScore(Double.parseDouble(registerShopParam.getServiceScore()));
        }
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
        merchant.setHot(registerShopParam.getHot());
        merchant.getApplyInfo().setCompanyName(registerShopParam.getCompanyName());
        merchant.getAddress().setArea(registerShopParam.getRegion());
        return merchant;
    }

    private RegisterShopParam merchantToParam (Merchant merchant,String userLocation) {
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
            if (null != merchant.getApplyInfo().getYelp()) {
                registerShopParam.setSourceFrom(merchant.getApplyInfo().getYelp()?1:0);
            }
            registerShopParam.setShopPicture(merchant.getApplyInfo().getPhotos());
            registerShopParam.setApplyStatus(merchant.getApplyInfo().getApplyStatus());
        }
        registerShopParam.setWholeScore(String.valueOf(merchant.getWholeScore()));
        registerShopParam.setEnvScore(String.valueOf(merchant.getEnvScore()));
        registerShopParam.setFlavorScore(String.valueOf(merchant.getFlavorScore()));
        registerShopParam.setServiceScore(String.valueOf(merchant.getServiceScore()));
        if (null != merchant.getHot()) {
            registerShopParam.setHot(merchant.getHot());
        }
        registerShopParam.setRegisterTime(merchant.getRegisterTime());
        registerShopParam.setUpdateTime(merchant.getUpdateTime());
        registerShopParam.setUserId(merchant.getUserId());
        registerShopParam.setRegion(merchant.getAddress().getArea());
        if (!StringUtils.isEmpty(userLocation)) {
            Point point = merchant.getLocation();
            registerShopParam.setDistance(String.valueOf(getDistance(point,registerShopParam.getUserLocation())));
        }
        registerShopParam.setCollectUsers(merchant.getCollectUsers());
        return registerShopParam;
    }

    private double getDistance(Point fromLocation,String toLocation) {
        GlobalCoordinates from = new GlobalCoordinates(fromLocation.getX(),fromLocation.getY());
        String [] toLocationData = toLocation.split(";");
        GlobalCoordinates to = new GlobalCoordinates(Double.parseDouble(toLocationData[0]),Double.parseDouble(toLocationData[1]));
        return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere,from,to).getEllipsoidalDistance();
    }

    @Override
    public List<Merchant> findShop(String classifyId) {
        LogUtil.printLog(log,"findShop",classifyId);
        Query query = Query.query(Criteria.where("classifyId").is(classifyId));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public List<Merchant> findShopBySecond(String secondClassifyId) {
        LogUtil.printLog(log,"findShopBySecond",secondClassifyId);
        Query query = Query.query(Criteria.where("itemId").is(secondClassifyId));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public void updateMerchant(RegisterShopParam registerShopParam) {
        LogUtil.printLog(log,"updateMerchant",registerShopParam);
        Query query = Query.query(Criteria.where("_id").is(registerShopParam.getId()));
        Update update = buildUpdate(registerShopParam);
        mongoTemplate.upsert(query,update,Merchant.class);
    }

    private Update buildUpdate(RegisterShopParam param) {
        Update update = new Update();
        update = !StringUtils.isEmpty(param.getUserId())?update.set("userId",param.getUserId()):update;
        update = !StringUtils.isEmpty(param.getShopName())?update.set("shopName",param.getShopName()):update;
        update = !StringUtils.isEmpty(param.getCompanyName())?update.set("applyInfo.$.companyName",param.getCompanyName()):update;
        update = !StringUtils.isEmpty(param.getFile())?update.set("applyInfo.$.IDUrl",param.getFile()):update;
        update = !StringUtils.isEmpty(param.getShopPicture())?update.set("applyInfo.$.photos",param.getShopPicture()):update;
        update = !StringUtils.isEmpty(param.getSourceFrom())?update.set("applyInfo.$.yelp",param.getSourceFrom()):update;
        update = !StringUtils.isEmpty(param.getCategory1())?update.set("category1",param.getCategory1()):update;
        update = !StringUtils.isEmpty(param.getCategory2())?update.set("category2",param.getCategory2()):update;
        update = !StringUtils.isEmpty(param.getRegion())?update.set("address.$.region",param.getRegion()):update;
        update = !StringUtils.isEmpty(param.getAddress())?update.set("address.$.name",param.getAddress()):update;
        update = !StringUtils.isEmpty(param.getLocation())?update.set("address.$.lang",Double.parseDouble(param.getLocation().split(";")[0])):update;
        update = !StringUtils.isEmpty(param.getLocation())?update.set("address.$.lat",Double.parseDouble(param.getLocation().split(";")[1])):update;
        update = !StringUtils.isEmpty(param.getContact())?update.set("phone",param.getContact()):update;
        update = !StringUtils.isEmpty(param.getFaxNo())?update.set("faxNo",param.getFaxNo()):update;
        update = !StringUtils.isEmpty(param.getServiceTime())?update.set("serviceTime",param.getServiceTime()):update;
        update = !StringUtils.isEmpty(param.getEmail())?update.set("email",param.getEmail()):update;
        update = !StringUtils.isEmpty(param.getWebsite())?update.set("website",param.getWebsite()):update;
        update = !StringUtils.isEmpty(param.getWelChat())?update.set("wx",param.getWelChat()):update;
        update = !StringUtils.isEmpty(param.getPayType())?update.set("paymentType",param.getPayType()):update;
        update = !StringUtils.isEmpty(param.getAveragePrice())?update.set("averagePrice",String.valueOf(param.getAveragePrice())):update;
        update = !StringUtils.isEmpty(param.getWholeScore())?update.set("wholeScore",param.getWholeScore()):update;
        update = !StringUtils.isEmpty(param.getEnvScore())?update.set("envScore",param.getEnvScore()):update;
        update = !StringUtils.isEmpty(param.getFlavorScore())?update.set("flavorScore",param.getFlavorScore()):update;
        update = !StringUtils.isEmpty(param.getServiceScore())?update.set("serviceScore",param.getServiceScore()):update;
        update = !StringUtils.isEmpty(param.getHot())?update.set("hot",param.getHot()):update;
        update.set("updateTime",DateUtils.getCurDateTime());
        return update;
    }

    @Override
    public void delMerchant(String shopId) {
        LogUtil.printLog(log,"delMerchant",shopId);
        Query query = Query.query(Criteria.where("_id").is(shopId));
        mongoTemplate.remove(query, Merchant.class);
    }

    @Override
    public RegisterShopParam findById(String shopId) {
        LogUtil.printLog(log,"findById",shopId);
        Query query = Query.query(Criteria.where("_id").is(shopId));
        return merchantToParam(mongoTemplate.findOne(query, Merchant.class),null);
    }

    @Override
    public RegisterShopParam findByIdForClient(String shopId) {
        LogUtil.printLog(log,"findByIdForClient",shopId);
        Query query = Query.query(Criteria.where("_id").is(shopId));
        RegisterShopParam result = merchantToParam(mongoTemplate.findOne(query, Merchant.class),null);
        query = Query.query(Criteria.where("shopId").is(shopId));
        List<CommonProduct> productList = mongoTemplate.find(query, CommonProduct.class);
        for (CommonProduct product : productList) {
            query = Query.query(Criteria.where("productId").is(product.getId()));
            product.setAppraiseList(mongoTemplate.find(query, CommonAppraise.class));
        }
        result.setClientProductList(productList);
        return result;
    }

    @Override
    public List<RegisterShopParam> findShopList() {
        List<Merchant> list = mongoTemplate.findAll(Merchant.class);
        return list.stream().map(item -> {
            return merchantToParam(item,null);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Merchant> findHotShop() {
        Query query = Query.query(Criteria.where("hot").is(true));
        return mongoTemplate.find(query, Merchant.class);
    }

    @Override
    public List<Merchant> findHotShop(String classifyId) {
        LogUtil.printLog(log,"findHotShop",classifyId);
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

    @Override
    public List<RegisterShopParam> firstPageMerchant(RegisterShopParam registerShopParam) {
        LogUtil.printLog(log,"firstPageMerchant",registerShopParam);
        Query query = new Query();
        if (!StringUtils.isEmpty(registerShopParam.getCategory1()) && !"0".equals(registerShopParam.getCategory1())) {
            query.addCriteria(Criteria.where("category1").is(registerShopParam.getCategory1()));
        }
        if (!StringUtils.isEmpty(registerShopParam.getCategory2()) && !"0".equals(registerShopParam.getCategory1())) {
            query.addCriteria(Criteria.where("category2").is(registerShopParam.getCategory2()));
        }
        List<RegisterShopParam> result = new ArrayList<>();
        List<Merchant> merchantList = new ArrayList<>();
        if (!StringUtils.isEmpty(registerShopParam.getHot())) {
            query.addCriteria(Criteria.where("hot").is(registerShopParam.getHot()));
            merchantList = mongoTemplate.find(query, Merchant.class);
        }
        if (!StringUtils.isEmpty(registerShopParam.getLocation())) {
            String [] locationArray = registerShopParam.getLocation().split(";");
            Point point = new Point(Double.parseDouble(locationArray[0]),Double.parseDouble(locationArray[1]));
            query.addCriteria(Criteria.where("location").nearSphere(point).maxDistance(5000));
            merchantList = mongoTemplate.find(query, Merchant.class);
        }
        if (!CollectionUtils.isEmpty(merchantList)) {
            for (Merchant merchant : merchantList) {
                result.add(merchantToParam(merchant,registerShopParam.getUserLocation()));
            }
        }
        return result;
    }

    @Override
    public List<RegisterShopParam> chooseShop (MerchantQueryParam merchantQueryParam) {
        LogUtil.printLog(log,"chooseShop",merchantQueryParam);
        Query query = new Query();
        if (null != merchantQueryParam.getCategory2()) {
            query.addCriteria(Criteria.where("category2").is(merchantQueryParam.getCategory2()));
        }
        if (!StringUtils.isEmpty(merchantQueryParam.getAreaCode()) && !"0".equals(merchantQueryParam.getAreaCode())) {
            query.addCriteria(Criteria.where("address.$.area").is(merchantQueryParam.getAreaCode()));
        }
        if (merchantQueryParam.getIntelligentType() > 0) {
            if (1 == merchantQueryParam.getIntelligentType() && null != merchantQueryParam.getLocation()) {
                String [] locationArray = merchantQueryParam.getLocation().split(";");
                Point point = new Point(Double.parseDouble(locationArray[0]),Double.parseDouble(locationArray[1]));
                query.addCriteria(Criteria.where("location").nearSphere(point).maxDistance(5.00));
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
        /*if (merchantQueryParam.getShopType() > 0) {
            //todo 商家需要添加商家类型字段
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
        }*/
        if (merchantQueryParam.getPrice() > 0) {
            switch (merchantQueryParam.getPrice()) {
                case 1:
                    query.addCriteria(Criteria.where("averagePrice").lt(10));
                    break;
                case 2:
                    query.addCriteria(Criteria.where("averagePrice").gte(10).lt(30));
                    break;
                case 3:
                    query.addCriteria(Criteria.where("averagePrice").gte(30).lt(60));
                    break;
                case 4:
                    query.addCriteria(Criteria.where("averagePrice").gte(60));
                    break;
            }
        }
        if (merchantQueryParam.getPayType() > 0) {
            query.addCriteria(Criteria.where("paymentType").is(merchantQueryParam.getPayType()));
        }
        if (!StringUtils.isEmpty(merchantQueryParam.getWholeScore())) {
            String [] scoreLimit = merchantQueryParam.getWholeScore().split(",");
            query.addCriteria(Criteria.where("wholeScore").gte(Double.parseDouble(scoreLimit[0])).lte(Double.parseDouble(scoreLimit[1])));
        }
        List<Merchant> merchantList = mongoTemplate.find(query, Merchant.class);
        List<RegisterShopParam> result = new ArrayList<>(merchantList.size());
        if (!CollectionUtils.isEmpty(merchantList)) {
            for (Merchant merchant : merchantList) {
                result.add(merchantToParam(merchant,merchantQueryParam.getUserLocation()));
            }
        }
        return result;
    }

    @Override
    public RegisterShopParam findShopByUserId(String userId) {
        LogUtil.printLog(log,"findShopByUserId",userId);
        Query query = Query.query(Criteria.where("userId").is(userId));
        Merchant merchant = mongoTemplate.findOne(query, Merchant.class);
        return merchantToParam(merchant,null);
    }

    @Override
    public List<RegisterShopParam> findAllShopProductList () {
        List<Merchant> merchantList = mongoTemplate.findAll(Merchant.class);
        if (CollectionUtils.isEmpty(merchantList)) {
            return new ArrayList<>();
        }
        List<RegisterShopParam> result = new ArrayList<>(merchantList.size());
        for (Merchant merchant : merchantList) {
            RegisterShopParam registerShopParam = merchantToParam(merchant,null);
            Query query = Query.query(Criteria.where("shopId").is(registerShopParam.getId()));
            List<Product> productList = mongoTemplate.find(query, Product.class);
            registerShopParam.setProductList(productList);
            result.add(registerShopParam);
        }
        return result;
    }

    @Override
    public void shopCollect(CollectLikeParam param) {
        LogUtil.printLog(log,"shopCollect",param);
        Query query = Query.query(Criteria.where("_id").is(param.getTargetId()));
        Merchant shop = mongoTemplate.findOne(query,Merchant.class);
        if (null == shop) {
            return;
        }
        List<Integer> collectUsers = shop.getCollectUsers();
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
        shop.setCollectUsers(collectUsers);
        mongoTemplate.save(shop);
    }
}
