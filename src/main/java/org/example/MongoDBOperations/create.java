package org.example.MongoDBOperations;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.validation.Validator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.example.MongoDBOperations.Constant.collectionName;

/**
 * spring data 操作mongodb，以shop为例
 * 创建collection，创建固定长度collection，创建验证数据的文档
 */
@Component
public class create {
    @Resource
    private MongoTemplate mongoTemplate;
    //create collection
    public Object createShopCollection(){
        mongoTemplate.createCollection(collectionName);
        return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
    }



 /**
 * 创建固定长度的collection
 */
    public Object createCollectionFixedSize(){
        long size=1024L;
        long max=5L;
//  `size` 限制文档大小， `max` 限制集合文档数量。
        CollectionOptions shops= CollectionOptions.empty()
                // 创建固定集合。固定集合是指有着固定大小的集合，当达到最大值时，它会自动覆盖最早的文档
                .capped()
                .size(size)
                .maxDocuments(max);
        mongoTemplate.createCollection(collectionName,shops);
        return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";

    }


    /**
     * 创建验证文档数据的集合
     * 创建集合并在文档"插入"与"更新"时进行数据效验，如果符合创建集合设置的条件就进允许更新与插入，否则则按照设置的设置的策略进行处理。
     * * 效验级别：
     *   - off：关闭数据校验。
     *   - strict：(默认值) 对所有的文档"插入"与"更新"操作有效。
     *   - moderate：仅对"插入"和满足校验规则的"文档"做"更新"操作有效。对已存在的不符合校验规则的"文档"无效。
     * * 执行策略：
     *   - error：(默认值) 文档必须满足校验规则，才能被写入。
     *   - warn：对于"文档"不符合校验规则的 MongoDB 允许写入，但会记录一条告警到 mongod.log 中去。日志内容记录报错信息以及该"文档"的完整记录。
     * @return 创建集合结果
     */
    public Object createCollectionValidation(){
        CriteriaDefinition criteriaDefinition= Criteria.where("location").is("hongkong");
        //设置验证条件，只允许位置在hk的信息插入
        CollectionOptions validCollection = CollectionOptions.empty()
                .validator(Validator.criteria(criteriaDefinition))
                //验证级别
                .strictValidation()
                //校验不通过后执行的动作
                .failOnValidationError();
        mongoTemplate.createCollection(collectionName,validCollection);
        return mongoTemplate.collectionExists(collectionName)?collectionName:"no success";
    }

}
