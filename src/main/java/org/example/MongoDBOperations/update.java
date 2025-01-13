package org.example.MongoDBOperations;

import com.mongodb.client.result.UpdateResult;
import org.example.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.example.MongoDBOperations.Constant.collectionName;

/**Spring Data MongoDB:
 * 使用 `insert`(`insertOne` 和 `insertMany`) 方法或 `save` 方法来执行插入操作。(都是在底层实现中调用插入功能。)
 *Spring Data 的存储库接口（Repository）， `save` 方法执行插入操作.
 */
@Component
public class update {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Object documentInsert() {
        Shop shop = new Shop()
                .setName("halo")
                .setId(10L)
                .setLocation("HONGKONG")
                .setCategoryId(12)
                .setBrandId(Arrays.asList(13L));
//               no .setCreatetime(new LocalDateTime().setCreateTime(12,11,111));
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
        //更新集合中匹配查询到的第一条文档数据，如果没有找到就新建并插入一个新文档
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
//      更新集合中匹配查询到的文档数据集合中的第一条数据
    }

/**`update1` 方法
*  - 如果查询条件匹配到的文档存在，则更新该文档的 `brandId` 字段。
*  - 如果没有匹配到任何文档，则创建并插入一个新文档，字段 `brandId` 设置为 12，且其他字段将根据类的定义（如果没有指定则为默认值）进行插入。
*  -返回结果**: `upsert.getMatchedCount()` 返回匹配的文档数量，如果存在匹配的文档，则返回 1；如果没有匹配的文档则返回 0。
*  -更新或插入文档。适用于需要确保文档存在的情况。
*### 2. `updateFirst` 方法
*  - 仅更新第一个匹配的文档的 `brandId` 和 `categoryId` 字段。
*  - 如果没有匹配的文档，则什么都不会做，不会创建新文档。
*  - 返回结果: `updateResult.getMatchedCount()` 返回匹配到的文档数量，`updateResult.getModifiedCount()` 返回实际被更新的文档数量。如果匹配到但没有实质性变化，`getModifiedCount()` 可能返回 0。
*  - 仅更新第一个匹配的文档。如果没有匹配，则不进行任何操作。
 */


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
