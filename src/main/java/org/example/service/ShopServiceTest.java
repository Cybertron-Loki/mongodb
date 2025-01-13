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

/**
 * 视图（view）是基于某个集合的查询结果，
 * 创建视图时必须指定一个聚合管道（pipeline），
 * 该管道定义了视图的数据是如何从基础集合中生成的。
 */
@Service
public class ShopServiceTest {
    @Resource
    private MongoTemplate mongoTemplate;
    //视图
    public Object createView() {
        String viewName = "view";
        String collectionName = "view";
        List<Bson> pipeline = Arrays.asList();
        pipeline.add(Document.parse("{ \"$match\":{ \"shopName\":\"cycle\",\"city\": \"New York\"} }"));
        //Bson matchStage = Aggregation.match(Criteria
        // .where("shopName").is("cycle").and("city").is("New York"));
        //pipeline.add(matchStage);
//        Bson matchStage = Aggregates.match(Filters.and(
        //    Filters.eq("shopName", "cycle"),
        //    Filters.eq("city", "New York")
        //));
        //pipeline.add(matchStage);
        mongoTemplate.getDb().createView(viewName, collectionName, pipeline);
//       返回的是一个 com.mongodb.client.MongoDatabase 对象
        return mongoTemplate.collectionExists(collectionName) ? collectionName : "no success";
    }
//`Document.parse(String json)` 是 MongoDB Java 驱动中的一个静态方法，将 JSON 字符串转换为 `Document` 对象---->BSON（Binary JSON）文档的类，它允许以结构化的方式构建和操作数据。
//- **JSON 转换**: `Document.parse` 允许使用字符串格式的 JSON 来创建 `Document`，构建聚合管道时方便。
//- **灵活性**: 它使得从外部数据源（如 API）快速构建 MongoDB 文档变得简单。
//- **支持嵌套结构**: 可以使用嵌套的 JSON 结构创建复杂的文档。


    //删除视图
    public Object dropView(String viewName) {
        if (!mongoTemplate.collectionExists(viewName)) {
            mongoTemplate.dropCollection(viewName);
            return "success";
        }
        return !mongoTemplate.collectionExists(viewName) ? "success" : "no success";
    }

    /**
     * 关于filter：
     * Filters 是 MongoDB Java 驱动的底层工具，用于构建查询条件。
     */

    /**
     * 聚合（aggregation）:数据处理和计算,多步骤处理的查询,大量数据.
     * 普通查询（find）:从集合中查找具体的文档，且不是进行复杂的计算或数据处理,简单的条件、精确查找,普通查询通常比聚合查询更快
     * 滤器（filters）:构造查询条件的工具，适合用于动态生成复杂查询条件
     * 视图（view）：只读：视图是只读的，不能直接插入、更新或删除；实时性：视图会实时更新，反映基础集合中的变化。
     */
}