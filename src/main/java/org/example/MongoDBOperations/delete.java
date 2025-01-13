package org.example.MongoDBOperations;

import com.mongodb.client.result.DeleteResult;
import org.example.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.MongoDBOperations.Constant.collectionName;

@Component
public class delete {
  @Autowired
  private MongoTemplate mongoTemplate;
    public Object dropCollection(String collectionName){
         mongoTemplate.dropCollection(collectionName);
/** `mongoTemplate.dropCollection(collectionName);``mongoTemplate.getCollection(collectionName).drop();`区别：
* mongoTemplate.dropCollection(collectionName): Spring Data MongoDB 提供的高层 API。
* 该方法会处理一些额外的逻辑，例如清理数据库状态、更新元数据等,一般用这个;会提供更清晰的异常处理和错误消息，符合 Spring 的异常处理机制。
**`mongoTemplate.getCollection(collectionName).drop();直接操作 MongoDB 的底层 API
* `getCollection(collectionName)` 返回一个 `MongoCollection` 对象，然后调用其 `drop()` 方法。
* 这个方法更接近 MongoDB 的原生操作，可能会略微提升性能，但缺乏一些高层 API 提供的便利性。会抛出原生 MongoDB 异常，需要手动处理这些异常。
*/
       //mongoTemplate.getCollection(collectionName).drop();
        return !mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
    }

    //    删除集合中符合条件的一个或多个文档
    public Object remove(){
        Criteria criteria = Criteria.where("categoryId").is(12).and("brandId").is(12);
        Query query = Query.query(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, Shop.class, collectionName);
        System.out.println(deleteResult.getDeletedCount());
        return deleteResult;
    }

    //删除符合条件的单个文档，并返回删除的文档。
    public Object findAndRemove(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        Shop result = mongoTemplate.findAndRemove(query, Shop.class, collectionName);
        System.out.println(result);
        return result;
    }
    //   删除符合条件的全部文档，并返回删除的文档
    public Object findAllAndRemove(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        List<Shop> result= mongoTemplate.findAllAndRemove(query, Shop.class, collectionName);
        System.out.println(result);
        return result;
    }



}
