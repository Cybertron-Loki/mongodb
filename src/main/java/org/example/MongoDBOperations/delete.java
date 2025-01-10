package org.example.MongoDBOperations;

import com.mongodb.client.result.DeleteResult;
import org.example.pojo.Shop;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class delete {

    //todo；删
    public Object dropCollection(String collectionName){
        mongoTemplate.dropCollection(collectionName);
//        `mongoTemplate.dropCollection(collectionName);` 和 `mongoTemplate.getCollection(collectionName).drop();` 在功能上都可以用来删除 MongoDB 中的集合，但它们之间存在一些区别：
//
//### 1. 方法的来源和实现
//
//- **`mongoTemplate.dropCollection(collectionName);`**:
//  - 这是 Spring Data MongoDB 提供的高层 API。
//  - 该方法会处理一些额外的逻辑，例如清理数据库状态、更新元数据等。
//  - 适合于大多数常见情况，通常推荐使用。
//
//- **`mongoTemplate.getCollection(collectionName).drop();`**:
//  - 这是直接操作 MongoDB 的底层 API。
//  - `getCollection(collectionName)` 返回一个 `MongoCollection` 对象，然后调用其 `drop()` 方法。
//  - 这个方法更接近 MongoDB 的原生操作，可能会略微提升性能，但缺乏一些高层 API 提供的便利性。
//
//### 2. 错误处理
//
//- **`dropCollection`**:
//  - 该方法在执行过程中可能会提供更清晰的异常处理和错误消息，符合 Spring 的异常处理机制。
//
//- **`getCollection(...).drop()`**:
//  - 直接调用可能会抛出原生 MongoDB 异常，您需要手动处理这些异常。
//
//### 3. 可读性和可维护性
//
//- 使用 `dropCollection` 方法通常会使代码更具可读性，因为它明确表明了意图是删除集合。而使用 `getCollection(...).drop()` 可能需要读者了解 MongoDB 的底层操作。

//
//       mongoTemplate.getCollection(collectionName).drop();
        return !mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
    }

    //todo:delete

    //    删除集合中【符合条件】的【一个]或[多个】文档
    public Object remove(){
        Criteria criteria = Criteria.where("categoryId").is(12).and("brandId").is(12);
        Query query = Query.query(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, Shop.class, collectionName);
        System.out.println(deleteResult.getDeletedCount());
        return deleteResult;
    }

    //删除【符合条件】的【单个文档】，并返回删除的文档。
    public Object findAndRemove(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        Shop result = mongoTemplate.findAndRemove(query, Shop.class, collectionName);
        System.out.println(result);
        return result;
    }
    //    删除【符合条件】的【全部文档】，并返回删除的文档
    public Object findAllAndRemove(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        List<Shop> result= mongoTemplate.findAllAndRemove(query, Shop.class, collectionName);
        System.out.println(result);
        return result;
    }



}
