package com.kankan.merchant.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.kankan.merchant.common.AreaEnum;
import com.kankan.merchant.common.MerchantConstant;
import com.kankan.merchant.model.Address;
import com.kankan.merchant.model.product.Product;
import com.kankan.merchant.module.classify.model.Category;
import com.kankan.merchant.module.merchant.ApplyInfo;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.merchant.dto.SearchResultDto;
import com.kankan.merchant.module.param.*;
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
        if (!StringUtils.isEmpty(param.getUserId())) {
            update.set("userId",param.getUserId());
        }
        if (!StringUtils.isEmpty(param.getShopName())) {
            update.set("name",param.getShopName());
        }
        if (!StringUtils.isEmpty(param.getCompanyName())) {
            update.set("applyInfo.$.companyName",param.getCompanyName());
        }
        if (!StringUtils.isEmpty(param.getFile())) {
            update.set("applyInfo.$.IDUrl",param.getFile());
        }
        if (!StringUtils.isEmpty(param.getShopPicture())) {
            update.set("applyInfo.$.photos",param.getShopPicture());
        }
        if (!StringUtils.isEmpty(param.getSourceFrom())) {
            update.set("applyInfo.$.yelp",1==param.getSourceFrom());
        }
        if (!StringUtils.isEmpty(param.getCategory1())) {
            update.set("category1",param.getCategory1());
        }
        if (!StringUtils.isEmpty(param.getCategory2())) {
            update.set("category2",param.getCategory2());
        }
        if (!StringUtils.isEmpty(param.getRegion())) {
            update.set("address.$.area",param.getRegion());
        }
        if (!StringUtils.isEmpty(param.getAddress())) {
            update.set("address.$.name",param.getAddress());
        }
        if (!StringUtils.isEmpty(param.getLocation())) {
            update.set("address.$.lang",Double.parseDouble(param.getLocation().split(";")[0]));
        }
        if (!StringUtils.isEmpty(param.getLocation())) {
            update.set("address.$.lat",Double.parseDouble(param.getLocation().split(";")[1]));
        }
        if (!StringUtils.isEmpty(param.getContact())) {
            update.set("phone",param.getContact());
        }
        if (!StringUtils.isEmpty(param.getFaxNo())) {
            update.set("faxNo",param.getFaxNo());
        }
        if (!StringUtils.isEmpty(param.getServiceTime())) {
            update.set("serviceTime",param.getServiceTime());
        }
        if (!StringUtils.isEmpty(param.getEmail())) {
            update.set("email",param.getEmail());
        }
        if (!StringUtils.isEmpty(param.getWebsite())) {
            update.set("website",param.getWebsite());
        }
        if (!StringUtils.isEmpty(param.getWelChat())) {
            update.set("wx",param.getWelChat());
        }
        if (!StringUtils.isEmpty(param.getPayType())) {
            update.set("paymentType",param.getPayType());
        }
        if (!StringUtils.isEmpty(param.getAveragePrice())) {
            update.set("averagePrice",String.valueOf(param.getAveragePrice()));
        }
        if (!StringUtils.isEmpty(param.getWholeScore())) {
            update.set("wholeScore",param.getWholeScore());
        }
        if (!StringUtils.isEmpty(param.getEnvScore())) {
            update.set("envScore",param.getEnvScore());
        }
        if (!StringUtils.isEmpty(param.getFlavorScore())) {
            update.set("flavorScore",param.getFlavorScore());
        }
        if (!StringUtils.isEmpty(param.getServiceScore())) {
            update.set("serviceScore",param.getServiceScore());
        }
        if (!StringUtils.isEmpty(param.getHot())) {
            update.set("hot",param.getHot());
        }
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
    public RegisterShopParam findByIdForClient(String shopId,String userId) {
        LogUtil.printLog(log,"findByIdForClient",shopId);
        Query query = Query.query(Criteria.where("_id").is(shopId));
        RegisterShopParam result = merchantToParam(mongoTemplate.findOne(query, Merchant.class),null);
        result.setIsCollection(false);
        if (!CollectionUtils.isEmpty(result.getCollectUsers()) && !StringUtils.isEmpty(userId) && result.getCollectUsers().contains(Integer.valueOf(userId))) {
            result.setIsCollection(true);
        }
        query = Query.query(Criteria.where("shopId").is(shopId));
        query.addCriteria(Criteria.where("applyStatus").is(2));
        List<CommonProduct> productList = mongoTemplate.find(query, CommonProduct.class);
        for (CommonProduct product : productList) {
            product.setIsCollection(false);
            if (!CollectionUtils.isEmpty(product.getCollectUsers()) && !StringUtils.isEmpty(userId)) {
                product.setIsCollection(product.getCollectUsers().contains(Integer.valueOf(userId)));
            }
            if (!CollectionUtils.isEmpty(product.getLikeUsers())) {
                if (!StringUtils.isEmpty(userId)) {
                    product.setIsLike(product.getLikeUsers().contains(Integer.valueOf(userId)));
                }
                product.setLikeNum(product.getLikeUsers().size());
            }
            query = Query.query(Criteria.where("productId").is(product.getId()));
            List<CommonAppraise> appraiseList = mongoTemplate.find(query, CommonAppraise.class);
            for (CommonAppraise appraise : appraiseList) {
                if (!CollectionUtils.isEmpty(appraise.getLikeUsers())) {
                    if (!StringUtils.isEmpty(userId)) {
                        appraise.setIsLike(appraise.getLikeUsers().contains(Integer.valueOf(userId)));
                    }
                    appraise.setLikeNum(appraise.getLikeUsers().size());
                }
            }
            product.setAppraiseList(appraiseList);
        }
        result.setClientProductList(productList);
        result.setCollectUsers(null);
        query = Query.query(Criteria.where("shopId").is(shopId));
        List<CommonAppraise> shopAppraiseList = mongoTemplate.find(query, CommonAppraise.class);
        if (!CollectionUtils.isEmpty(shopAppraiseList)) {
            for (CommonAppraise shopAppraise : shopAppraiseList) {
                if (!CollectionUtils.isEmpty(shopAppraise.getLikeUsers())) {
                    shopAppraise.setLikeNum(shopAppraise.getLikeUsers().size());
                    if (!StringUtils.isEmpty(userId)) {
                        shopAppraise.setIsLike(shopAppraise.getLikeUsers().contains(Integer.valueOf(userId)));
                    }
                }
            }
            result.setShopAppraiseList(shopAppraiseList);
        }
        result.setAppraiseNum(getShopAppraiseNum(result.getId()));
        enrichShopScore(result);
        return result;
    }

    private void enrichShopScore (RegisterShopParam shopParam) {
        Query query = new Query();
        query.addCriteria(Criteria.where("shopId").is(shopParam.getId()));
        List<CommonAppraise> shopAppraiseList = mongoTemplate.find(query, CommonAppraise.class);
        if (CollectionUtils.isEmpty(shopAppraiseList)) {
            return;
        }
        int size = shopAppraiseList.size();
        BigDecimal bigDecimalEnv = new BigDecimal(0.00);
        BigDecimal bigDecimalFlavor = new BigDecimal(0.00);
        BigDecimal bigDecimalService = new BigDecimal(0.00);
        for (CommonAppraise appraise : shopAppraiseList) {
            if (!StringUtils.isEmpty(appraise.getEnvScore())) {
                bigDecimalEnv = bigDecimalEnv.add(BigDecimal.valueOf(Double.parseDouble(appraise.getEnvScore())));
            }
            if (!StringUtils.isEmpty(appraise.getFlavorScore())) {
                bigDecimalFlavor = bigDecimalFlavor.add(BigDecimal.valueOf(Double.parseDouble(appraise.getFlavorScore())));
            }
            if (!StringUtils.isEmpty(appraise.getServiceScore())) {
                bigDecimalService = bigDecimalService.add(BigDecimal.valueOf(Double.parseDouble(appraise.getServiceScore())));
            }
        }
        if (bigDecimalEnv.doubleValue() > 0) {
            double bigDecimalEnvAver = bigDecimalEnv.doubleValue()/size;
            shopParam.setEnvScore(String.valueOf(bigDecimalEnvAver));
        }
        if (bigDecimalFlavor.doubleValue() > 0) {
            double bigDecimalFlavorAver = bigDecimalFlavor.doubleValue()/size;
            shopParam.setFlavorScore(String.valueOf(bigDecimalFlavorAver));
        }
        if (bigDecimalService.doubleValue() > 0) {
            double bigDecimalServiceAver = bigDecimalService.doubleValue()/size;
            shopParam.setServiceScore(String.valueOf(bigDecimalServiceAver));
        }
    }

    @Override
    public List<RegisterShopParam> findShopList() {
        List<Merchant> list = mongoTemplate.findAll(Merchant.class);
        return list.stream().map(item -> {
            return merchantToParam(item,null);
        }).collect(Collectors.toList());
    }

    @Override
    public List<RegisterShopParam> getCollectShopListByUserId (final String userId) {
        return this.findShopList().stream().filter(item -> !CollectionUtils.isEmpty(item.getCollectUsers()) && item.getCollectUsers().contains(Integer.valueOf(userId))).collect(Collectors.toList());
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
        }
        if (!StringUtils.isEmpty(registerShopParam.getLocation()) && registerShopParam.getLocation().contains(";")) {
            String [] locationArray = registerShopParam.getLocation().split(";");
            Point point = new Point(Double.parseDouble(locationArray[0]),Double.parseDouble(locationArray[1]));
            query.addCriteria(Criteria.where("location").nearSphere(point).maxDistance(5000.00));
        }
        merchantList = mongoTemplate.find(query, Merchant.class);
        if (!CollectionUtils.isEmpty(merchantList)) {
            for (Merchant merchant : merchantList) {
                RegisterShopParam item = merchantToParam(merchant,registerShopParam.getUserLocation());
                item.setAppraiseNum(getShopAppraiseNum(merchant.getId()));
                result.add(item);
            }
        }
        return result;
    }

    private Integer getShopAppraiseNum (String shopId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("shopId").is(shopId));
        query.addCriteria(Criteria.where("type").is("1"));
        List<CommonAppraise> shopAppraiseList = mongoTemplate.find(query, CommonAppraise.class);
        return CollectionUtils.isEmpty(shopAppraiseList)?0:shopAppraiseList.size();
    }

    @Override
    public List<RegisterShopParam> chooseShop (MerchantQueryParam merchantQueryParam) {
        LogUtil.printLog(log,"chooseShop",merchantQueryParam);
        Query query = buildQueryCondition(merchantQueryParam);
        List<Merchant> merchantList = mongoTemplate.find(query, Merchant.class);
        List<RegisterShopParam> result = new ArrayList<>(merchantList.size());
        if (!CollectionUtils.isEmpty(merchantList)) {
            for (Merchant merchant : merchantList) {
                //result.add(merchantToParam(merchant,merchantQueryParam.getUserLocation()));
                RegisterShopParam item = merchantToParam(merchant,merchantQueryParam.getUserLocation());
                item.setAppraiseNum(getShopAppraiseNum(merchant.getId()));
                result.add(item);
            }
        }
        return result;
    }

    private Query buildQueryCondition (MerchantQueryParam merchantQueryParam) {
        Query query = new Query();
        if (!StringUtils.isEmpty(merchantQueryParam.getCategory1()) && !"0".equals(merchantQueryParam.getCategory1())) {
            query.addCriteria(Criteria.where("category1").is(merchantQueryParam.getCategory1()));
        }
        if (!StringUtils.isEmpty(merchantQueryParam.getCategory2()) && !"0".equals(merchantQueryParam.getCategory2())) {
            query.addCriteria(Criteria.where("category2").is(merchantQueryParam.getCategory2()));
        }
        if (!StringUtils.isEmpty(merchantQueryParam.getAreaCode()) && !"0".equals(merchantQueryParam.getAreaCode())) {
            query.addCriteria(Criteria.where("address.$.area").is(merchantQueryParam.getAreaCode()));
        }
        if (merchantQueryParam.getIntelligentType() > 0) {
            if (1 == merchantQueryParam.getIntelligentType() && null != merchantQueryParam.getLocation()
                    && merchantQueryParam.getLocation().contains(";")) {
                String [] locationArray = merchantQueryParam.getLocation().split(";");
                Point point = new Point(Double.parseDouble(locationArray[0]),Double.parseDouble(locationArray[1]));
                query.addCriteria(Criteria.where("location").nearSphere(point).maxDistance(5000.00));
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
        return query;
    }

    private List<SearchResultDto> searchShop (SearchParam searchParam) {
        Query query = buildQueryCondition(searchParam);
        query.addCriteria(Criteria.where("name").regex(".*?\\"+searchParam.getName()+".*"));
        List<Merchant> merchantList = mongoTemplate.find(query, Merchant.class);
        if (CollectionUtils.isEmpty(merchantList)) {
            return new ArrayList<>();
        }
        List<Category> categoryList = mongoTemplate.findAll(Category.class);
        List<SearchResultDto> result = new ArrayList<>(merchantList.size());
        SearchResultDto searchResultDto;
        for (Merchant merchant : merchantList) {
            searchResultDto = new SearchResultDto();
            searchResultDto.setId(merchant.getId());
            searchResultDto.setName(merchant.getName());
            searchResultDto.setWholeScore(null == merchant.getWholeScore()?0.00:merchant.getWholeScore());
            searchResultDto.setAveragePrice(String.valueOf(merchant.getAveragePrice()));
            if (null != merchant.getAddress()) {
                searchResultDto.setArea(AreaEnum.getNameByCode(merchant.getAddress().getArea()));
            }
            if (null != merchant.getApplyInfo()) {
                searchResultDto.setPicture(merchant.getApplyInfo().getIDUrl());
            }
            searchResultDto.setServiceTime(merchant.getServiceTime());
            if (!CollectionUtils.isEmpty(categoryList)) {
                for (Category category : categoryList) {
                    if (null == category || null == category.getId()) {
                        continue;
                    }
                    if (category.getId().equals(merchant.getCategory2())) {
                        searchResultDto.setCategoryName(category.getName());
                    }
                }
            }
            result.add(searchResultDto);
        }
        return result;
    }

    private List<SearchResultDto> searchProduct (SearchParam searchParam) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productName").regex(".*?\\"+searchParam.getName()+".*"));
        if (searchParam.getIntelligentType() > 0) {
            if (3 == searchParam.getIntelligentType()) {
                query.with(Sort.by(Sort.Order.asc("price")));
            }
            if (4 == searchParam.getIntelligentType()) {
                query.with(Sort.by(Sort.Order.desc("price")));
            }
        }
        if (searchParam.getPrice() > 0) {
            switch (searchParam.getPrice()) {
                case 1:
                    query.addCriteria(Criteria.where("price").lt(10));
                    break;
                case 2:
                    query.addCriteria(Criteria.where("price").gte(10).lt(30));
                    break;
                case 3:
                    query.addCriteria(Criteria.where("price").gte(30).lt(60));
                    break;
                case 4:
                    query.addCriteria(Criteria.where("price").gte(60));
                    break;
            }
        }
        List<CommonProduct> productList = mongoTemplate.find(query, CommonProduct.class);
        if (CollectionUtils.isEmpty(productList)) {
            return new ArrayList<>();
        }
        List<Category> categoryList = mongoTemplate.findAll(Category.class);
        List<SearchResultDto> result = new ArrayList<>(productList.size());
        SearchResultDto searchResultDto;
        Merchant merchant;
        for (CommonProduct product : productList) {
            searchResultDto = new SearchResultDto();
            searchResultDto.setId(product.getId());
            searchResultDto.setName(product.getProductName());
            searchResultDto.setAveragePrice(String.valueOf(product.getPrice()));
            searchResultDto.setPicture(product.getFacePicture());
            merchant = mongoTemplate.findById(product.getShopId(),Merchant.class);
            if (!CollectionUtils.isEmpty(categoryList) && null != merchant) {
                if (null != merchant.getAddress()) {
                    searchResultDto.setArea(AreaEnum.getNameByCode(merchant.getAddress().getArea()));
                }
                searchResultDto.setShopName(merchant.getName());
                for (Category category : categoryList) {
                    if (null == category || null == category.getId()) {
                        continue;
                    }
                    if (category.getId().equals(merchant.getCategory2())) {
                        searchResultDto.setCategoryName(category.getName());
                    }
                }
            }
            result.add(searchResultDto);
        }
        return result;
    }

    public List<SearchResultDto> search (SearchParam searchParam) {
        LogUtil.printLog(log,"searchShop",searchParam);
        if (1 == searchParam.getType()) {
            return searchShop(searchParam);
        }
        return searchProduct(searchParam);
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
