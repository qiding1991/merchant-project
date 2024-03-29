package com.kankan.merchant.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kankan.merchant.common.AreaEnum;
import com.kankan.merchant.module.classify.model.Category;
import com.kankan.merchant.module.merchant.Appraise;
import com.kankan.merchant.module.merchant.Merchant;
import com.kankan.merchant.module.merchant.common.CommonAppraise;
import com.kankan.merchant.module.merchant.common.CommonProduct;
import com.kankan.merchant.module.merchant.dto.ProductResultDto;
import com.kankan.merchant.module.merchant.dto.SearchResultDto;
import com.kankan.merchant.module.param.CollectLikeParam;
import com.kankan.merchant.module.param.ProductUpdateParam;
import com.kankan.merchant.utils.DateUtils;
import com.kankan.merchant.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
        if (!StringUtils.isEmpty(product.getProductPictures())) {
            update.set("productPictures",product.getProductPictures());
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

    @Override
    public List<ProductResultDto> findAllProduct () {
        List<CommonProduct> list = mongoTemplate.findAll(CommonProduct.class);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<ProductResultDto> result = new ArrayList<>(list.size());
        ProductResultDto productResultDto;
        for (CommonProduct product : list) {
            productResultDto = new ProductResultDto();
            Merchant shop = mongoTemplate.findById(product.getShopId(), Merchant.class);
            BeanUtils.copyProperties(product,productResultDto);
            if (null != shop) {
                productResultDto.setShopName(shop.getName());
            }
            result.add(productResultDto);
        }
        return result;
    }

    public ProductResultDto findDetailForClient (String productId,String userId) {
        ProductResultDto productResultDto = new ProductResultDto();
        CommonProduct product = mongoTemplate.findById(productId,CommonProduct.class);
        if (null == product) {
            return productResultDto;
        }
        BeanUtils.copyProperties(product,productResultDto);
        Merchant shop = mongoTemplate.findById(product.getShopId(), Merchant.class);
        if (null != shop) {
            productResultDto.setShopId(shop.getId());
            productResultDto.setShopName(shop.getName());
        }
        if (!StringUtils.isEmpty(userId)) {
            productResultDto.setIsCollection(false);
            if (!CollectionUtils.isEmpty(product.getCollectUsers()) && product.getCollectUsers().contains(userId)) {
                productResultDto.setIsCollection(true);
            }
            productResultDto.setIsLike(false);
            if (!CollectionUtils.isEmpty(product.getLikeUsers()) && product.getLikeUsers().contains(userId)) {
                productResultDto.setIsLike(true);
            }
        }
        Query query = Query.query(Criteria.where("productId").is(product.getId()));
        List<CommonAppraise> appraiseList = mongoTemplate.find(query, CommonAppraise.class);
        for (CommonAppraise appraise : appraiseList) {
            if (!CollectionUtils.isEmpty(appraise.getLikeUsers())) {
                if (!StringUtils.isEmpty(userId)) {
                    appraise.setIsLike(appraise.getLikeUsers().contains(userId));
                }
                appraise.setLikeNum(appraise.getLikeUsers().size());
            }
        }
        productResultDto.setAppraiseList(appraiseList);
        productResultDto.setApplyStatus(null);
        return productResultDto;
    }

    @Override
    public List<SearchResultDto> getCollectProductListByUserId (final String userId) {
        List<CommonProduct> result = mongoTemplate.findAll(CommonProduct.class);
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList<>();
        }
        List<CommonProduct> list = result.stream().filter(item -> !CollectionUtils.isEmpty(item.getCollectUsers()) && item.getCollectUsers().contains(Integer.valueOf(userId))).collect(Collectors.toList());
        List<Merchant> shopList = mongoTemplate.findAll(Merchant.class);
        List<Category> categoryList = mongoTemplate.findAll(Category.class);
        SearchResultDto resultDto = null;
        List<SearchResultDto> resultList = new ArrayList<SearchResultDto>(list.size());
        for (CommonProduct product : list) {
            if (StringUtils.isEmpty(product.getShopId())) {
                continue;
            }
            resultDto = new SearchResultDto();
            resultDto.setId(product.getId());
            resultDto.setName(product.getProductName());
            resultDto.setAveragePrice(String.valueOf(product.getPrice()));
            resultDto.setPicture(product.getFacePicture());
            for (Merchant shop : shopList) {
                if (product.getShopId().equals(shop.getId())) {
                    resultDto.setShopName(shop.getName());
                    if (null != shop.getAddress()) {
                        resultDto.setArea(AreaEnum.getNameByCode(shop.getAddress().getArea()));
                    }
                }
                for (Category category : categoryList) {
                    if (null != category && null != category.getId() && shop.getCategory2().equals(category.getId())) {
                        resultDto.setCategoryName(category.getName());
                    }
                }
            }
            resultList.add(resultDto);
        }
        return resultList;
    }

    @Override
    public void productCollect(CollectLikeParam param) {
        LogUtil.printLog(logger,"productCollect",param);
        Query query = Query.query(Criteria.where("_id").is(param.getTargetId()));
        CommonProduct product = mongoTemplate.findOne(query,CommonProduct.class);
        if (null == product) {
            return;
        }
        List<String> collectUsers = product.getCollectUsers();
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
        List<String> markLikeUsers = product.getLikeUsers();
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
