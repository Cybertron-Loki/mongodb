package org.example.service;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.pojo.Shop;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class shopServiceTest {
  @Resource
    private MongoTemplate mongoTemplate;
    final static  private String collectionName="shop";
  //todo:增
  public Object createShopCollection(){
      String collectionName = "shop";
      mongoTemplate.createCollection(collectionName);
      return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
  }

  public Object createCollectionFixedSize(){
      String collectionName = "fixedShops";
      long size=1024L;
      long max=5L;
//      可以配置参数 `size` 限制文档大小，可以配置参数 `max` 限制集合文档数量。
   CollectionOptions shops= CollectionOptions.empty()
           // 创建固定集合。固定集合是指有着固定大小的集合，当达到最大值时，它会自动覆盖最早的文档
           .capped()
           .size(size)
      .maxDocuments(max);
   mongoTemplate.createCollection(collectionName,shops);
   return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";

  }

  //todo:增改
    /*
     * 创建【验证文档数据】的集合
     *
     * 创建集合并在文档"插入"与"更新"时进行数据效验，如果符合创建集合设置的条件就进允许更新与插入，否则则按照设置的设置的策略进行处理。
     *
     * * 效验级别：
     *   - off：关闭数据校验。
     *   - strict：(默认值) 对所有的文档"插入"与"更新"操作有效。
     *   - moderate：仅对"插入"和满足校验规则的"文档"做"更新"操作有效。对已存在的不符合校验规则的"文档"无效。
     * * 执行策略：
     *   - error：(默认值) 文档必须满足校验规则，才能被写入。
     *   - warn：对于"文档"不符合校验规则的 MongoDB 允许写入，但会记录一条告警到 mongod.log 中去。日志内容记录报错信息以及该"文档"的完整记录。
     *
     * @return 创建集合结果
     */
    public Object createCollectionValidation(){
        String collectionName = "validation";
        CriteriaDefinition criteriaDefinition= Criteria.where("location").is("hongkong");
        //设置验证条件，只允许位置在hk的信息插入
        CollectionOptions validCollection = CollectionOptions.empty()
                .validator(Validator.criteria(criteriaDefinition))
                //设置验证级别
                .strictValidation()
                //设置校验不通过后执行的动作
                .failOnValidationError();
        mongoTemplate.createCollection(collectionName,validCollection);
        return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
    }


    //todo:查
    public Object getCollectionNames(){
        return mongoTemplate.getCollectionNames();
        //获取集合名称列表
    }
    public boolean collectionExists(String collectionName){
        return mongoTemplate.collectionExists(collectionName);
    }

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



    //todo:视图
   public Object createView(){
        String viewName="view";
        String collectionName="view";
       List<Bson> pipeline = Arrays.asList();
       pipeline.add(Document.parse("{ \"$match\":{ \"shopName\":\"cycle\"} }"));
       mongoTemplate.getDb().createView(viewName,collectionName,pipeline);
//       返回的是一个 com.mongodb.client.MongoDatabase 对象
       return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
   }
//   ### `Document.parse` 详解
//
//`Document.parse(String json)` 是 MongoDB Java 驱动中的一个静态方法，通常用于将 JSON 字符串转换为 `Document` 对象。`Document` 是 MongoDB 中用于表示 BSON（Binary JSON）文档的类，它允许您以结构化的方式构建和操作数据。
//
//#### 主要特性
//
//- **JSON 转换**: `Document.parse` 允许您使用字符串格式的 JSON 来创建 `Document`，这在构建聚合管道时非常方便。
//- **灵活性**: 它使得从外部数据源（如 API）快速构建 MongoDB 文档变得简单。
//- **支持嵌套结构**: 您可以使用嵌套的 JSON 结构创建复杂的文档。
//
//#### 示例
//
//```java
//import org.bson.Document;
//
//String jsonString = "{ \"name\": \"John\", \"age\": 30, \"city\": \"New York\" }";
//Document doc = Document.parse(jsonString);
//
//// 访问文档中的数据
//String name = doc.getString("name");
//int age = doc.getInteger("age");
//```
//
//### MongoDB 聚合管道
//
//聚合管道是 MongoDB 中用于处理数据的强大工具。它允许您按照一系列阶段（stages）处理和转换数据。每个阶段都可以对输入文档进行过滤、分组、排序等操作。
//
//#### 常见的聚合操作
//
//1. **`$match`**: 过滤文档，只有符合条件的文档才会被传递到管道的下一个阶段。
//
//    ```json
//    { "$match": { "shopName": "cycle" } }
//    ```
//
//2. **`$group`**: 将文档分组并对每组进行聚合操作（如计数、求和等）。
//
//    ```json
//    { "$group": { "_id": "$shopName", "total": { "$sum": 1 } } }
//    ```
//
//3. **`$sort`**: 对文档进行排序。
//
//    ```json
//    { "$sort": { "age": 1 } }  // 按年龄升序排序
//    ```
//
//4. **`$project`**: 重塑文档的结构，可以添加新字段或排除现有字段。
//
//    ```json
//    { "$project": { "name": 1, "age": 1 } }
//    ```
//
//5. **`$limit`**: 限制返回的文档数量。
//
//    ```json
//    { "$limit": 5 }
//    ```
//
//6. **`$skip`**: 跳过指定数量的文档。
//
//    ```json
//    { "$skip": 10 }
//    ```
//
//#### 使用聚合管道的示例
//
//以下是一个完整的聚合管道示例，使用 Spring Data MongoDB 的 `Aggregation` 类构建：
//
//```java
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//import org.springframework.data.mongodb.core.aggregation.AggregationResults;
//
//Aggregation aggregation = Aggregation.newAggregation(
//    Aggregation.match(Criteria.where("shopName").is("cycle")),
//    Aggregation.group("shopName").count().as("total")
//);
//
//AggregationResults<YourResultClass> results = mongoTemplate.aggregate(aggregation, "yourCollectionName", YourResultClass.class);
//```
//
//### 总结
//
//- `Document.parse` 是将 JSON 字符串转换为 `Document` 对象的便捷方法。
//- MongoDB 的聚合管道允许您使用一系列操作对数据进行复杂的处理和分析。每个阶段都可以执行不同的操作，组合在一起可以实现强大的数据处理能力。



    //todo：删除视图
    public Object dropView(String viewName){
        if(!mongoTemplate.collectionExists(viewName)){
            mongoTemplate.dropCollection(viewName);
            return "success";
        }
        return !mongoTemplate.collectionExists(viewName)?"success":"no success";
    }

    //todo:文档增删改查
    public Object documentInsert() {
        String collectionName = "shop";
        Shop shop = new Shop()
                .setId(10L)
                .setName("halo")
                .setLocation("HONGKONG")
                .setCategoryId(12)
                .setBrandId(12L);
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
                    .setBrandId(12L);
            Shop shop2=new Shop()
                    .setBrandId(13L)
                    .setLocation("HONGKONG")
                    .setName("tt")
                    .setId(14L);

            List<Shop> shopList=new ArrayList<Shop>();
            shopList.add(shop1);
            shopList.add(shop2);
            Collection<Shop> addShops = mongoTemplate.insert(shopList, insertCollectionName);
           for(Shop shop3: addShops){
               System.out.println(shop3);
           }
           return addShops;
        }
    //文档更新
    public Object update(){
        String insertCollectionName="shop";
        Shop shop1=new Shop()
                .setBrandId(12L)
                .setCategoryId(15)
                .setId(14L)
                .setLocation("HONGKONG");
        Shop save = mongoTemplate.save(shop1, insertCollectionName);
        //如果存在就更新,存储数据
        System.out.println(save);
        return save;
    }
    //查询：all
    public Object checkAll(){
        List<Shop> all = mongoTemplate.findAll(Shop.class, collectionName);
        for(Shop shop: all){
            System.out.println(shop);
        }
         return all;
    }

    //query:By Id
    public Object checkById(){
        Long id= 12L;
        Shop byId = mongoTemplate.findById(id, Shop.class, collectionName);
        System.out.println(byId);
        return byId;
    }
//    根据【条件】查询集合中【符合条件】的文档，只取【第一条】数据
    public Object findOne(){
        Long brandId=12L;
        Criteria criteria = Criteria.where("brandId").is(brandId);
        //构造条件对象
        Query query = Query.query(criteria);
        //构造查询对象
        Shop shop = mongoTemplate.findOne(query, Shop.class, collectionName);
        System.out.println(shop);
        return shop;
    }
//### 1. 使用 `Criteria` 和 `Query`
//
//这是最常见的方式，适合于复杂查询。您可以使用 `Criteria` 类来构造查询条件，然后通过 `Query` 类将其组合在一起。

//// 使用 mongoTemplate 进行查询
//List<YourEntity> results = mongoTemplate.find(query, YourEntity.class);
//```
//
//### 2. 使用 `Query` 的静态方法
//
//您可以直接使用 `Query` 的静态方法来创建查询。这在某些情况下可以让代码更简洁。
//
//```java
//Query query = new Query(Criteria.where("brandId").is(brandId));
//List<YourEntity> results = mongoTemplate.find(query, YourEntity.class);
//```
//
//### 3. 简单查询
//
//对于简单的查询，您可以直接使用 `mongoTemplate` 的方法，而不需要使用 `Criteria` 和 `Query`。例如：
//
//```java
//YourEntity result = mongoTemplate.findById(brandId, YourEntity.class);
//```
//
//### 4. 使用 `Example` 查询
//
//Spring Data 还支持使用 `Example` 查询，这种方式适合于根据现有对象的属性查找匹配的文档。
//
//```java
//Example<YourEntity> example = Example.of(new YourEntity(brandId));
//List<YourEntity> results = mongoTemplate.find(example, YourEntity.class);
//```
//
//### 5. 使用方法名查询
//
//如果您的存储库接口（Repository）继承自 `MongoRepository`，您可以直接通过方法名来进行简单查询。这种方式是最简洁的。
//
//```java
//List<YourEntity> findByBrandId(String brandId);
//```
//
//### 总结
//
//- 对于复杂查询，使用 `Criteria` 和 `Query` 是推荐的方式。
//- 对于简单查询，可以直接使用 `mongoTemplate` 提供的方法或使用方法名查询。
//- `Example` 查询适合根据对象的属性进行匹配。
//
//因此，并不是所有查询都需要使用 `Criteria` 和 `Query`，可以根据具体情况选择最合适的方式。如果您有其他问题或需要更详细的解释，请随时问我！


//find 文档list
    public Object findByCondition(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for (Shop shop: Shops) {
            System.out.println(shop);
        }
        return Shops;
    }

    public Object findByConditionAndSort(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria).with(Sort.by("id"));
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
       for(Shop shop: Shops){
           System.out.println(shop);
       }
       return Shops;
    }

//    根据【单个条件】查询集合中的文档数据，并【按指定字段进行排序】与【限制指定数目】
    public Object findByConditionAndSortLimit(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria).with(Sort.by("id")).limit(1);
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }

    public Object findByConditionAndSortSkip(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria).with(Sort.by("id")).skip(1);
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }
//     查询【存在指定字段名称】的文档数据
    public Object findByExistField(){
        Criteria criteria = Criteria.where("categoryId").exists(true);
        Query query = Query.query(criteria);
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }

    //关联查询多个条件，and
    public Object findByAndCondition(){
        Criteria criteria1 = Criteria.where("categoryId").is(12);
        Criteria criteria2 = Criteria.where("brandId").is(12);
        Criteria criteria=new Criteria().andOperator(criteria2,criteria1);
//        Criteria criteria12=Criteria.where("categoryId").is(12)
//                .and("brandId").lt(12);
        Query query = Query.query(criteria);
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }
//   在 Spring Data MongoDB 中，`Query` 类的构造方法和静态方法都可以用来创建查询对象，它们在功能上是等价的，但在使用上有所不同。让我们详细比较这两种方式，并探讨 `Criteria` 的相关方法。
//
//### 1. `Query` 的两种创建方式
//
//#### 使用构造函数
//
//```java
//Query query = new Query(criteria);
//```
//
//- **说明**: 直接使用 `Query` 类的构造函数来创建查询对象。这种方式简单明了，适合于直接创建 `Query` 实例时使用。
//
//#### 使用静态方法
//
//```java
//Query query = Query.query(criteria);
//```
//
//- **说明**: 使用 `Query` 类的静态方法 `query()` 创建查询对象。这种方式在某些情况下可能更加语义化，尤其是当你想强调这是一个查询时。
//
//### 区别
//
//- **功能**: 两者在功能上没有区别，都是创建一个新的 `Query` 实例，内部实现相同。
//- **可读性**: 使用静态方法 `Query.query(criteria)` 在某些情况下可能更具可读性，特别是当查询逻辑较复杂时，语义上更清晰。
//
//### 2. `Criteria` 的方法
//
//`Criteria` 类提供了多种构建查询条件的方法，但没有与 `Query` 类相同的静态方法来创建 `Criteria` 实例。您通常使用 `Criteria.where(...)` 方法来创建查询条件。例如：
//
//```java
//Criteria criteria = Criteria.where("fieldName").is(value);
//```
//
//### 其他 `Criteria` 方法示例
//
//- **使用 `and`**: 添加多个条件。
//
//  ```java
//  Criteria criteria = Criteria.where("field1").is(value1)
//                               .and("field2").lt(value2);
//  ```
//
//- **使用 `or`**: 使用 `orOperator` 来组合多个条件。
//
//  ```java
//  Criteria criteria = new Criteria().orOperator(
//      Criteria.where("field1").is(value1),
//      Criteria.where("field2").is(value2)
//  );
//  ```
//
//- **使用范围查询**: 进行范围查询。
//
//  ```java
//  Criteria criteria = Criteria.where("field").gte(minValue).lte(maxValue);
//  ```
//
//### 总结
//
//- `Query` 的两种创建方式在功能上是等价的，选择哪种方式更多是个人喜好和代码可读性。
//- `Criteria` 类没有类似于 `Query` 的静态方法，但提供了多种方法来构建复杂的查询条件。




//    在 Spring Data MongoDB 中，`Criteria` 类并没有提供 `Criteria.or(...)` 方法。要构建多个条件的“或”关系，您使用的确实是 `Criteria.orOperator(...)` 方法。下面是一些关于 `orOperator` 的具体用法示例以及说明。
//
//### 使用 `orOperator` 方法
//
//`orOperator` 方法用于将多个 `Criteria` 条件组合成一个“或”条件。以下是一个示例：
//
//```java
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//
//// 创建多个 Criteria 条件
//Criteria criteria1 = Criteria.where("field1").is(value1);
//Criteria criteria2 = Criteria.where("field2").is(value2);
//
//// 使用 orOperator 组合条件
//Criteria orCriteria = new Criteria().orOperator(criteria1, criteria2);
//
//// 创建查询对象
//Query query = new Query(orCriteria);
//```
//
//### 关键点
//
//- **orOperator 方法**: 这是唯一的方法用于创建多个条件的“或”关系。您可以将多个 `Criteria` 对象传递给 `orOperator` 方法。
//- **无 Criteria.or 方法**: `Criteria` 类本身并没有提供 `or` 这样的静态方法，所有的“或”组合都需要通过 `orOperator` 来实现。
//
//### 其他组合条件的方法
//
//除了 `orOperator`，`Criteria` 还提供了以下组合条件的方法：
//
//- **andOperator**: 用于将多个条件组合成“与”关系。
//
//  ```java
//  Criteria andCriteria = Criteria.where("field1").is(value1)
//                                   .and("field2").lt(value2);
//  ```
//
//### 总结
//
//- 使用 `Criteria.orOperator(...)` 来构建“或”条件的组合。
//- `Criteria` 类没有 `Criteria.or(...)` 方法，所有组合条件需要使用 `andOperator` 或 `orOperator`。

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
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, shop.class, collectionName);
        System.out.println(updateResult.getMatchedCount() + updateResult.getModifiedCount());
        return updateResult;
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

    //聚合表达式
    public Object aggregationGroupCount(){
//        $group 进行分组，然后统计各个组的文档数量
        GroupOperation group = Aggregation.group("categoryId").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(group);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for(Map map: results.getMappedResults()) {
            System.out.println(map);
        }
     return results.getMappedResults();
  }

  public Object aggregationGroupMax(){
      GroupOperation groupOperation = Aggregation.group("categoryId").max("brandId").as("maxGroupCount");
      Aggregation aggregation = Aggregation.newAggregation(groupOperation);
      AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
      for(Map map: results.getMappedResults()) {
          System.out.println(map);
      }
      return results.getMappedResults();
  }
    public Object aggregationGroupCountSum(){
//      使用管道操作符 $group 进行分组，然后统计各个组文档某字段值合计  avg
        GroupOperation groupOperation = Aggregation.group("categoryId").sum("brandId").as("maxGroupCount");
        Aggregation aggregation = Aggregation.newAggregation(groupOperation);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for(Map map: results.getMappedResults()) {
            System.out.println(map);
        }
        return results.getMappedResults();
    }

    public Object aggregationGroupFirst(){
//        先对数据进行排序，然后使用管道操作符 $group 进行分组，最后统计各个组文档某字段值第一个值  last
        AggregationOperation sort = Aggregation.sort(Sort.by("shopId").descending());
        GroupOperation group = Aggregation.group("categoryId")
                .first("brandId").as("brandFirst")
                .sum("brandId").as("brandTotal");
        //统计每个范畴的第一个值与总和
        //要是求brandid和？
         Aggregation aggregation = Aggregation.newAggregation(group, sort);
//         public static Aggregation newAggregation(AggregationOperation... operations) {
//		return new Aggregation(operations);
//	}
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for(Map map: result.getMappedResults()) {
            System.out.println(map);
        }
        return result.getMappedResults();
    }

    public Object aggregationGroupPush(){
//      使用管道操作符 $group 结合表达式操作符 $push 获取某字段列表
        GroupOperation push = Aggregation.group("categoryId").push("brandId").as("brandList");
        Aggregation aggregation = Aggregation.newAggregation(push);
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for(Map map: result.getMappedResults()) {
            System.out.println(map);
        }
  return result.getMappedResults();}
//聚合管道操作符:
//
//$project： 可以从文档中选择想要的字段，和不想要的字段（指定的字段可以是来自输入文档或新计算字段的现有字段 ，也可以通过管道表达式进行一些复杂的操作，例如数学操作，日期操作，字符串操作，逻辑操作。
//$match： 用于过滤数据，只输出符合条件的文档。$match使用MongoDB的标准查询操作。
//$limit： 用来限制MongoDB聚合管道返回的文档数。
//$skip： 在聚合管道中跳过指定数量的文档，并返回余下的文档。
//$unwind： 将文档中的某一个数组类型字段拆分成多条，每条包含数组中的一个值。
//$group： 将集合中的文档分组，可用于统计结果。
//$sort： 将输入文档排序后输出。

    //聚合管道操作符
    public Object aggregationGroupMatch(){
        AggregationOperation match = Aggregation.match(Criteria.where("categoryId").is(12));
        AggregationOperation group = Aggregation.group("location").max("shopId").as("shopMG");
        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for(Map map: result.getMappedResults()) {
            System.out.println(map);
        }
    return result.getMappedResults();
  }

    /**
     * 使用 $group 和 $limit 聚合,先使用 $group 进行分组，然后再使用 $limit 限制一定数目文档
     *
     * @return 聚合结果
     */
    public Object aggregateGroupLimit() {
        // 设置聚合条件，先按岁数分组，然后求每组用户的工资总数、最大值、最小值、平均值，限制只能显示五条
        AggregationOperation group = Aggregation.group("age")
                .sum("salary").as("sumSalary")
                .max("salary").as("maxSalary")
                .min("salary").as("minSalary")
                .avg("salary").as("avgSalary");
        AggregationOperation limit = Aggregation.limit(5L);
        // 将操作加入到聚合对象中
        Aggregation aggregation = Aggregation.newAggregation(group, limit);
        // 执行聚合查询
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map result : results.getMappedResults()) {
            System.out.println(result);
        }
        return results.getMappedResults();
    }

    /**
     * 使用 $group 和 $skip 聚合,先使用 $group 进行分组，然后再使用 $skip 跳过一定数目文档
     *
     * @return 聚合结果
     */
    public Object aggregateGroupSkip() {
        // 设置聚合条件，先按岁数分组，然后求每组用户的工资总数、最大值、最小值、平均值，跳过前 2 条
        AggregationOperation group = Aggregation.group("age")
                .sum("salary").as("sumSalary")
                .max("salary").as("maxSalary")
                .min("salary").as("minSalary")
                .avg("salary").as("avgSalary");
        AggregationOperation limit = Aggregation.skip(2L);
        // 将操作加入到聚合对象中
        Aggregation aggregation = Aggregation.newAggregation(group, limit);
        // 执行聚合查询
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map result : results.getMappedResults()) {
            System.out.println(result);
        }
        return results.getMappedResults();
    }

    /**
     * 使用 $group 和 $project 聚合,先使用 $group 进行分组，然后再使用 $project 限制显示的字段
     *
     * @return 聚合结果
     */
    public Object aggregateGroupProject() {
        // 设置聚合条件,按岁数分组，然后求每组用户工资最大值、最小值，然后使用 $project 限制值显示 salaryMax 字段
        AggregationOperation group = Aggregation.group("age")
                .max("salary").as("maxSalary")
                .min("salary").as("minSalary");
        AggregationOperation project = Aggregation.project("maxSalary");
        // 将操作加入到聚合对象中
        Aggregation aggregation = Aggregation.newAggregation(group, project);
        // 执行聚合查询
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map result : results.getMappedResults()) {
            System.out.println(result);
        }
        return results.getMappedResults();
    }

    /**
     * 使用 $group 和 $unwind 聚合,先使用 $project 进行分组，然后再使用 $unwind 拆分文档中的数组为一条新文档记录
     *
     * @return 聚合结果
     */
    public Object aggregateProjectUnwind() {
        // 设置聚合条件，设置显示`name`、`age`、`title`字段，然后将结果中的多条文档按 title 字段进行拆分
        AggregationOperation project = Aggregation.project("name", "age", "title");
        AggregationOperation unwind = Aggregation.unwind("title");
        // 将操作加入到聚合对象中
        Aggregation aggregation = Aggregation.newAggregation(project, unwind);
        // 执行聚合查询
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map result : results.getMappedResults()) {
            System.out.println(result);
        }
        return results.getMappedResults();
    }

    //mongodb索引
    //创建索引
    public Object createIndex(){
        String field1="shopId";
        String field2="categoryId";
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(field1,field2));
    }
    //创建文字索引
    public Object createTextIndex(){
        String field="categoryId";
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.text(field));
    }
    //创建哈希索引
    public Object createHashIndex(){
        String field="categoryId";
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.hashed(field));
    }
    //创建升序唯一索引
    public Object createUniqueIndex(){
        String field="categoryId";
        IndexOptions options=new IndexOptions();
        options.unique(true);
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(field),options);
    }
   //创建局部索引
    public Object createPartialIndex(){
        String field="categoryId";
        IndexOptions options=new IndexOptions();
        options.partialFilterExpression(Filters.exists("brandId",true));
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(field),options);
    }


    //查询索引
//    获取当前【集合】对应的【所有索引】的【名称列表】
    public Object getAllIndex(){
        ListIndexesIterable<Document> indexLists = mongoTemplate.getCollection(collectionName).listIndexes();
        List<Document> list=new ArrayList<>();
        for(Document index: indexLists){
            list.add(index);
        }
        return list;
    }

    //删除索引
    public void removeIndex(){
        String field="categoryId";
        mongoTemplate.getCollection(collectionName).dropIndex(Indexes.ascending(field));
//        mongoTemplate.getCollection(collectionName).dropIndex(field);
    }
    public void removeAllIndex(){
        mongoTemplate.getCollection(collectionName).dropIndexes();
    }

//    单节点 mongodb 不支持事务，需要搭建 MongoDB 复制集
}
