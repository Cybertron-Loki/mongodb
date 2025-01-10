package org.example.MongoDBOperations;

import org.example.pojo.Shop;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class check {

    //todo:查
    public Object getCollectionNames(){
        return mongoTemplate.getCollectionNames();
        //获取集合名称列表
    }
    public boolean collectionExists(String collectionName){
        return mongoTemplate.collectionExists(collectionName);
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

}
