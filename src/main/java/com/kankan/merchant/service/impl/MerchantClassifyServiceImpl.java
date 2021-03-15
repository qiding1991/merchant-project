package com.kankan.merchant.service.impl;

import com.kankan.merchant.module.classify.model.ItemClassify;
import com.kankan.merchant.module.classify.model.MerchantClassify;
import com.kankan.merchant.service.ClassifyService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MerchantClassifyServiceImpl implements ClassifyService {

  private final MongoTemplate mongoTemplate;

  public MerchantClassifyServiceImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }


  public MerchantClassify addMerchantClassify(MerchantClassify merchantClassify) {
    MerchantClassify addResult = mongoTemplate.insert(merchantClassify);
    return addResult;
  }

  public void addMerchantItemClassify(String classifyId, List<ItemClassify> itemClassify) {
    Query query = Query.query(Criteria.where("_id").is(classifyId));
    Update update = new Update().addToSet("itemTypeList").each(itemClassify);
    UpdateResult result = mongoTemplate.updateMulti(query, update, MerchantClassify.class);
    return;
  }


  public List<MerchantClassify> findAllClassify() {
    return mongoTemplate.findAll(MerchantClassify.class);
  }

  @Override
  public void updateMerchantClassify(MerchantClassify merchantClassify) {
    Query query = Query.query(Criteria.where("_id").is(merchantClassify.getId()));
    Update update = new Update();
    if (!StringUtils.isEmpty(merchantClassify.getName())) {
      update.set("name", merchantClassify.getName());
    }
    if (!StringUtils.isEmpty(merchantClassify.getPicture())) {
      update.set("picture", merchantClassify.getPicture());
    }
    mongoTemplate.upsert(query, update, MerchantClassify.class);
  }

  @Override
  public void updateMerchantClassifyItem(ItemClassify itemClassify) {
    Query query = Query.query(Criteria.where("itemTypeList.id").is(itemClassify.getId()));
    MerchantClassify merchantClassify = mongoTemplate.findOne(query, MerchantClassify.class);
    ItemClassify itemClassify1 = merchantClassify.getItemTypeList().stream().filter((item) -> item.getId()
      .equalsIgnoreCase(itemClassify.getId())).findFirst().get();
    itemClassify1.setName(itemClassify.getName());
    mongoTemplate.save(merchantClassify);
  }

  @Override
  public void delMerchantClassify(String classifyId) {
    Query query = Query.query(Criteria.where("_id").is(classifyId));
    mongoTemplate.remove(query,MerchantClassify.class);
  }

  @Override
  public void delMerchantClassifyItem(String classifyItemId) {
    Query query = Query.query(Criteria.where("itemTypeList.id").is(classifyItemId));
    MerchantClassify merchantClassify = mongoTemplate.findOne(query, MerchantClassify.class);

    ItemClassify itemClassify=new ItemClassify();
    itemClassify.setId(classifyItemId);
    merchantClassify.getItemTypeList().remove(itemClassify);
    mongoTemplate.save(merchantClassify);

  }

}
