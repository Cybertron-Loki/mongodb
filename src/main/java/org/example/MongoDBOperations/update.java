package org.example.MongoDBOperations;

import com.mongodb.client.result.UpdateResult;
import org.example.pojo.Shop;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class update {
    //todo:文档增删改查
    public Object documentInsert() {
        String collectionName = "shop";
        Shop shop = new Shop()
                .setId(10L)
                .setName("halo")
                .setLocation("HONGKONG")
                .setCategoryId(12)
                .setBrandId(Arrays.asList(13L));
//                .setCreatetime(new LocalDateTime().setCreateTime(12,11,111));
        Shop shopInsert = mongoTemplate.insert(shop, collectionName);
        System.out.println(shopInsert);
        return shopInsert;
    }
    //批插入
    public Object insertShops(){
        String insertCollectionName="shop";
        Shop shop1=new Shop()
                .setName("ff")
                .setId(11L)
                .setLocation("HONGKONG")
                .setBrandId(Arrays.asList(13L));
        Shop shop2=new Shop()
                .setBrandId(Arrays.asList(13L))
                .setLocation("HONGKONG")
                .setName("tt")
                .setId(14L);

        List<Shop> shopList=new ArrayList<Shop>();
        shopList.add(shop1);
        shopList.add(shop2);
        Collection<Shop> addShops = mongoTemplate.insert(shopList, insertCollectionName);
        //// 插入多个文档
        //List<YourEntity> entities = Arrays.asList(new YourEntity("2", "Bob"), new YourEntity("3", "Charlie"));
        //mongoTemplate.insert(entities, "collectionName");
        for(Shop shop3: addShops){
            System.out.println(shop3);
        }
        return addShops;
    }
    //文档更新
    public Object update(){
        String insertCollectionName="shop";
        Shop shop1=new Shop()
                .setBrandId(Arrays.asList(13L))
                .setCategoryId(15)
                .setId(14L)
                .setLocation("HONGKONG");
        Shop save = mongoTemplate.save(shop1, insertCollectionName);
        //如果存在就更新,存储数据
        System.out.println(save);
        return save;
    }

    //改
    public Object update1(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        Update update1 = Update.update("brandId",12);
        UpdateResult upsert = mongoTemplate.upsert(query, update1, Shop.class, collectionName);
        System.out.println(upsert.getMatchedCount());
        //更改第一条数据
//      更新集合中【匹配】查询到的第一条文档数据，如果没有找到就【创建并插入一个新文档】
        return upsert;
    }

    public Object updateFirst() {
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        Update update = Update.update("brandId", 12).set("categoryId", 12);
//      ==  Update update = new Update().set("age", 33).set("name", "zhangsansan");
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Shop.class, collectionName);
        System.out.println(updateResult.getMatchedCount() + updateResult.getModifiedCount());
        return updateResult;
//      更新集合中【匹配】查询到的【文档数据集合】中的【第一条数据】
    }
//  在您提供的两个方法 `update1` 和 `updateFirst` 中，主要区别在于它们执行的操作和所使用的方法。下面是这两者的详细对比：
//
//### 1. `update1` 方法
//
//```java
//public Object update1() {
//    Criteria criteria = Criteria.where("categoryId").is(12);
//    Query query = Query.query(criteria);
//    Update update1 = Update.update("brandId", 12);
//    UpdateResult upsert = mongoTemplate.upsert(query, update1, shop.class, collectionName);
//    System.out.println(upsert.getMatchedCount());
//    return upsert;
//}
//```
//
//#### 特点
//- **操作类型**: 使用 `upsert` 方法。
//- **功能**:
//  - 如果查询条件匹配到的文档存在，则更新该文档的 `brandId` 字段。
//  - 如果没有匹配到任何文档，则创建并插入一个新文档，字段 `brandId` 设置为 12，且其他字段将根据类的定义（如果没有指定则为默认值）进行插入。
//- **返回结果**: `upsert.getMatchedCount()` 返回匹配的文档数量，如果存在匹配的文档，则返回 1；如果没有匹配的文档则返回 0。
//
//### 2. `updateFirst` 方法
//
//```java
//public Object updateFirst() {
//    Criteria criteria = Criteria.where("categoryId").is(12);
//    Query query = Query.query(criteria);
//    Update update = Update.update("brandId", 12).set("categoryId", 12);
//    UpdateResult updateResult = mongoTemplate.updateFirst(query, update, shop.class, collectionName);
//    System.out.println(updateResult.getMatchedCount() + updateResult.getModifiedCount());
//    return updateResult;
//}
//```
//
//#### 特点
//- **操作类型**: 使用 `updateFirst` 方法。
//- **功能**:
//  - 仅更新第一个匹配的文档的 `brandId` 和 `categoryId` 字段。
//  - 如果没有匹配的文档，则什么都不会做，不会创建新文档。
//- **返回结果**: `updateResult.getMatchedCount()` 返回匹配到的文档数量，`updateResult.getModifiedCount()` 返回实际被更新的文档数量。如果匹配到但没有实质性变化，`getModifiedCount()` 可能返回 0。
//
//### 主要区别总结
//
//1. **操作类型**:
//   - **`upsert`**: 更新或插入文档。适用于需要确保文档存在的情况。
//   - **`updateFirst`**: 仅更新第一个匹配的文档。如果没有匹配，则不进行任何操作。
//
//2. **行为**:
//   - **`upsert`**: 如果没有匹配的文档，会创建新文档。
//   - **`updateFirst`**: 如果没有匹配的文档，不会创建新文档。
//
//3. **返回值**:
//   - **`upsert`**: 主要关注于文档的匹配情况。
//   - **`updateFirst`**: 关注于匹配和修改的文档数量。
//
//### 适用场景
//
//- **使用 `upsert`**: 当您希望确保某个关键字段存在时，或者希望在没有匹配的情况下自动创建新文档。
//- **使用 `updateFirst`**: 当您只想更新已有文档，不希望创建新文档时。

    //更新所有
    public Object updateMulti() {
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        Update update = Update.update("categoryId", 12).set("brandId", 12);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Shop.class, collectionName);
        System.out.println(updateResult.getMatchedCount() + updateResult.getModifiedCount());
        return updateResult;
    }
}
