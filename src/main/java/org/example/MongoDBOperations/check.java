package org.example.MongoDBOperations;

import org.example.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.MongoDBOperations.Constant.collectionName;


/**
 * @Bean 通常用于方法上，而不是类上。
 * 使用 @Component 或 @Service 注解来标记这个类为 Spring 管理的 Bean。
 * 查询操作
 */
@Component
public class check {
  @Autowired
  private MongoTemplate mongoTemplate;

    public Object getCollectionNames(){
        return mongoTemplate.getCollectionNames();
        //获取所有集合名称列表
    }
    public boolean collectionExists(String collectionName){
        //查询是否存在该集合
        return mongoTemplate.collectionExists(collectionName);
    }

    public Object checkAll(){
        List<Shop> all = mongoTemplate.findAll(Shop.class, collectionName);
        //返回名为 collectionName 的集合中的所有 Shop 文档
        for(Shop shop: all){
            System.out.println(shop);
        }
        return all;
    }


    public Object checkById(){
        Long id= 12L;
        Shop byId = mongoTemplate.findById(id, Shop.class, collectionName);
        //query:By Id
        System.out.println(byId);
        return byId;
    }

    public Object findOne(){
        Long brandId=12L;
        Criteria criteria = Criteria.where("brandId").is(brandId);
        //构造条件对象
        Query query = Query.query(criteria);
        //构造查询对象
        Shop shop = mongoTemplate.findOne(query, Shop.class, collectionName);
        //根据条件查询集合中符合条件的文档的第一条数据
        System.out.println(shop);
        return shop;
    }

/**关于`Criteria` 和 `Query`，以及几种查询方式
 *适合于复杂查询： `Criteria` 构造查询条件，然后通过 `Query` 类将其组合在一起。
 *  Criteria 没有与 `Query` 类相同的静态方法来创建 `Criteria` 实例。
 * `Criteria.where(...)` and or  范围查询： gte lte
 * Criteria criteria = Criteria.where("field1").is(value1)
 *                              .and("field2").lt(value2);
 * `Criteria` 类并没有提供 `Criteria.or(...)` 方法
 * `Criteria.orOperator(...)`
 *  `Query` 的两种创建方式(功能上是等价的，但在使用上有所不同)
 *  *使用构造函数
 *  Query query = new Query(criteria);
 *`Query` 的静态方法（简洁版）:
 * Query query = new Query(Criteria.where("brandId").is(brandId));
 * 强调这是一个查询时。
 *List<YourEntity> results = mongoTemplate.find(query, YourEntity.class);
 *简单查询：
 *YourEntity result = mongoTemplate.findById(brandId, YourEntity.class);
 *使用 `Example` 查询 ，这种方式适合于根据现有对象的属性查找匹配的文档:
 *Example<YourEntity> example = Example.of(new YourEntity(brandId));
 *List<YourEntity> results = mongoTemplate.find(example, YourEntity.class);
 *存储库接口（Repository）使用方法名查询:
 *List<YourEntity> findByBrandId(String brandId);
*/


    public Object findByCondition(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria);
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        //find 文档list
        for (Shop shop: Shops) {
            System.out.println(shop);
        }
        return Shops;
    }

    public Object findByConditionAndSort(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria).with(Sort.by("id"));
        //根据指定字段排序
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }


    public Object findByConditionAndSortLimit(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria).with(Sort.by("id")).limit(1);
        // 限制指定数目
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }

    public Object findByConditionAndSortSkip(){
        Criteria criteria = Criteria.where("categoryId").is(12);
        Query query = Query.query(criteria).with(Sort.by("id")).skip(1);
        //跳过第一个查询到的文档
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }

    public Object findByExistField(){
        Criteria criteria = Criteria.where("categoryId").exists(true);
        Query query = Query.query(criteria);
        // 查询存在指定字段(categoryID)名称的文档数据
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }

    public Object findByAndCondition(){
        Criteria criteria1 = Criteria.where("categoryId").is(12);
        Criteria criteria2 = Criteria.where("brandId").is(12);
        Criteria criteria=new Criteria().andOperator(criteria2,criteria1);
        //关联查询多个条件，and
//        Criteria criteria12=Criteria.where("categoryId").is(12)
//                .and("brandId").lt(12);
// 或：
// Criteria criteria1 = Criteria.where("field1").is(value1);
//Criteria criteria2 = Criteria.where("field2").is(value2);
//Criteria orCriteria = new Criteria().orOperator(criteria1, criteria2);
        Query query = Query.query(criteria);
        List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
        for(Shop shop: Shops){
            System.out.println(shop);
        }
        return Shops;
    }

    /**
     * 出现了大量重复的 Criteria criteria = Criteria.
     * Query query = Query.query(criteria)语句，
     * 是否有像mybatis的工具可以简化代码，或者怎样简化?
     */
     //  1.Specification 或 QueryDSL
     public class ShopSpecifications {
         public static Query buildQuery(String name, String location) {
             Criteria criteria = new Criteria();
             if (name != null) {
                 criteria.and("name").is(name);
             }
             if (location != null) {
                 criteria.and("location").is(location);
             }
             return Query.query(criteria);
         }
     }

     public Object test1(){
         Query query = ShopSpecifications.buildQuery("Shop", collectionName);
         List<Shop> Shops = mongoTemplate.find(query, Shop.class, collectionName);
         // 添加额外的条件
         query.addCriteria(Criteria.where("categoryId").is(12));
         //find 文档list
         for (Shop shop: Shops) {
             System.out.println(shop);
         }
         return Shops;
     }


     /** 2. 查询逻辑封装到一个方法中*/
      public Query createQuery(String name, String location) {
          Criteria criteria = new Criteria();

          if (name != null) {
              criteria.and("name").is(name);
          }
          if (location != null) {
              criteria.and("location").is(location);
          }

          return Query.query(criteria);
      }

      Query query = createQuery("ShopName", "ShopLocation").with(Sort.by("id")).skip(1);

     /**  3. 使用 MongoTemplate 的 `find` 方法
     *
     * 可以将 `MongoTemplate` 的查询封装成一个通用方法，直接返回结果。
     * public List<Shop> findShops(String name, String location) {
     *     Query query = createQuery(name, location);
     *     return mongoTemplate.find(query, Shop.class);
     *  4. 使用流式 API
     * public Query createQuery(String... criteria) {
     *     Criteria criteriaQuery = Stream.of(criteria)
     *             .filter(Objects::nonNull)
     *             .map(criterion -> Criteria.where("field").is(criterion)) // 根据实际字段构建
     *             .reduce(Criteria::and)
     *             .orElse(new Criteria());
     *
     *     return Query.query(criteriaQuery);
     */
}
