package org.example.MongoDBOperations;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.validation.Validator;

public class add {
    @Resource
    private MongoTemplate mongoTemplate;
    final static  private String collectionName="Shop";
    //create collection
    public Object createShopCollection(){
        mongoTemplate.createCollection(collectionName);
        return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
    }

//`save` 或 `insert` 方法来执行插入操作，但这些操作都是在底层实现中调用插入功能。
//  `insertOne` 和 `insertMany` 方法用于插入操作。
//Spring Data MongoDB**: 使用 `insert` 方法或 `save` 方法来执行插入操作。
    //### 3. 使用存储库接口
    //
    //如果您使用 Spring Data 的存储库接口（Repository），也可以通过 `save` 方法执行插入操作：
    //
    //```java
    //public interface YourEntityRepository extends MongoRepository<YourEntity, String> {
    //    // 其他方法
    //}
    //

    /// / 在服务中使用
    //@Autowired
    //private YourEntityRepository yourEntityRepository;
    //
    //YourEntity entity = new YourEntity();
    //entity.setId("1");
    //entity.setName("Alice");
    //yourEntityRepository.save(entity); // 插入或更新
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


    //### 2. 使用 `MongoTemplate`
    //
    //如果您使用 `MongoTemplate` 进行操作，通常需要提供集合名称，例如在 `insert` 或 `remove` 方法中。
    //
    //#### 示例
    //
    //```java
    //mongoTemplate.insert(entity, "custom_collection_name");
    //```
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

}
