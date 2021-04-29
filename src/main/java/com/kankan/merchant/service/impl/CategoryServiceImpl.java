package com.kankan.merchant.service.impl;

import com.kankan.merchant.module.classify.model.ItemClassify;
import com.kankan.merchant.module.classify.model.Category;
import com.kankan.merchant.module.classify.param.CategoryParam;
import com.kankan.merchant.service.CategoryService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final MongoTemplate mongoTemplate;

  public CategoryServiceImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Category addCategory(Category category) {
    if (StringUtils.isEmpty(category.getId())) {
      category.setId(UUID.randomUUID().toString());
    }
    return mongoTemplate.insert(category);
  }

  @Override
  public List<Category> findAllCategory(String parentId) {
    if (null != parentId) {
      Query query = Query.query(Criteria.where("parentId").is(parentId));
      return mongoTemplate.find(query,Category.class);
    }
    Query query = Query.query(Criteria.where("parentId").is(null));
    return mongoTemplate.find(query,Category.class);
  }

  @Override
  public Map<Category,Object> queryCategoryForTree() {
    Query query = Query.query(Criteria.where("parentId").is(null));
    List<Category> category1List = mongoTemplate.find(query,Category.class);
    if (CollectionUtils.isEmpty(category1List)) {
      return null;
    }
    Map<Category,Object> categoryTree = new HashMap<>(category1List.size());
    for (Category category : category1List) {
      query = Query.query(Criteria.where("parentId").is(category.getId()));
      List<Category> category2List = mongoTemplate.find(query,Category.class);
      categoryTree.put(category,category2List);
    }
    return categoryTree;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delCategory(String categoryId) {
    Query query = Query.query(Criteria.where("_id").is(categoryId));
    Category category = mongoTemplate.findOne(query,Category.class);
    if (null != category && null != category.getParentId()) {
      mongoTemplate.remove(query, Category.class);
      return;
    }
    mongoTemplate.remove(query, Category.class);
    query = Query.query(Criteria.where("parentId").is(categoryId));
    mongoTemplate.remove(query, Category.class);
  }

  @Override
  public void updateCategory(CategoryParam categoryParam) {
      /*Query query = Query.query(Criteria.where("_id").is(categoryParam.getId()));
      Update update = new Update();
      if (!StringUtils.isEmpty(categoryParam.getName())) {
        update.set("name", categoryParam.getName());
      }
      if (!StringUtils.isEmpty(categoryParam.getName())) {
        update.set("icon", categoryParam.getIcon());
      }*/
    Category category = new Category();
    BeanUtils.copyProperties(categoryParam,category);
      mongoTemplate.save(category);
  }

  public void addMerchantItemClassify(String classifyId, List<ItemClassify> itemClassify) {
    Query query = Query.query(Criteria.where("_id").is(classifyId));
    Update update = new Update().addToSet("itemTypeList").each(itemClassify);
    UpdateResult result = mongoTemplate.updateMulti(query, update, Category.class);
    return;
  }

  @Override
  public void updateMerchantClassifyItem(ItemClassify itemClassify) {
    /*Query query = Query.query(Criteria.where("itemTypeList.id").is(itemClassify.getId()));
    Category merchantClassify = mongoTemplate.findOne(query, Category.class);
    ItemClassify itemClassify1 = merchantClassify.getItemTypeList().stream().filter((item) -> item.getId()
      .equalsIgnoreCase(itemClassify.getId())).findFirst().get();
    itemClassify1.setName(itemClassify.getName());
    mongoTemplate.save(merchantClassify);*/
  }

  @Override
  public void delMerchantClassifyItem(String classifyItemId) {
    Query query = Query.query(Criteria.where("itemTypeList.id").is(classifyItemId));
    Category merchantClassify = mongoTemplate.findOne(query, Category.class);

    ItemClassify itemClassify=new ItemClassify();
    itemClassify.setId(classifyItemId);
    //merchantClassify.getItemTypeList().remove(itemClassify);
    mongoTemplate.save(merchantClassify);

  }

}
