package org.example.MongoDBOperations;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.example.MongoDBOperations.Constant.collectionName;

/**
 *
 * 聚合管道是 MongoDB 中用于处理数据的强大工具,允许在一个管道中执行多个操作，并返回经过处理的结果。
聚合管道操作符:
$addFields： 可以在文档中添加新字段，并对其进行计算。
$bucket： 可以将文档分组，并对每个组进行不同的操作。
$bucketAuto： 可以根据指定的条件自动分组文档。
$count： 可以统计文档的数量。
$facet： 可以对多个聚合管道进行并行处理。
$geoNear： 可以根据地理位置对文档进行排序。
$graphLookup： 可以在文档中查找其他集合中的文档，并在结果文档中添加引用。
$lookup： 可以在文档中查找其他集合中的文档。
$out： 可以将聚合结果输出到一个新的集合中。
$replaceRoot： 可以替换文档的根节点。
$sample： 可以随机取样文档。
$set： 可以设置文档中的字段值。
$setWindowFields： 可以在文档中添加窗口函数计算的字段。
$project： 可以从文档中选择想要的字段，和不想要的字段（指定的字段可以是来自输入文档或新计算字段的现有字段 ，也可以通过管道表达式进行一些复杂的操作，例如数学操作，日期操作，字符串操作，逻辑操作。
$match： 用于过滤数据，只输出符合条件的文档。$match使用MongoDB的标准查询操作。
$limit： 用来限制MongoDB聚合管道返回的文档数。
$skip： 在聚合管道中跳过指定数量的文档，并返回余下的文档。
$unwind： 将文档中的某一个数组类型字段拆分成多条，每条包含数组中的一个值。
$group： 将集合中的文档分组，可用于统计结果。
$sort： 将输入文档排序后输出。
 /**
 1.`$match`: 过滤文档，只有符合条件的文档才会被传递到管道的下一个阶段。
 { "$match": { "shopName": "cycle" } }

 2.`$group`: 将文档分组并对每组进行聚合操作（如计数、求和等）。
 { "$group": { "_id": "$shopName", "total": { "$sum": 1 } } }

 3. `$sort`: 对文档进行排序。
 { "$sort": { "age": 1 } }  // 按年龄升序排序

 4. `$project`: 重塑文档的结构，可以添加新字段或排除现有字段。
 { "$project": { "name": 1, "age": 1 } }

 5. `$limit`: 限制返回的文档数量。
 { "$limit": 5 }

 6. `$skip`: 跳过指定数量的文档。
 { "$skip": 10 }

 */
@Component
public class aggregation {
@Resource
private MongoTemplate mongoTemplate;

    //聚合表达式
    public Object aggregationGroupCount() {
      // $group 进行分组，然后统计各个组的文档数量
        GroupOperation group = Aggregation.group("categoryId").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(group);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map map : results.getMappedResults()) {
            System.out.println(map);
        }
        return results.getMappedResults();
    }

    public Object aggregationGroupMax() {
        GroupOperation groupOperation = Aggregation.group("categoryId").max("brandId").as("maxGroupCount");
        //计算 brandId 字段的最大值,将计算出的最大值命名为 maxGroupCount。
        Aggregation aggregation = Aggregation.newAggregation(groupOperation);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map map : results.getMappedResults()) {
            System.out.println(map);
        }
        return results.getMappedResults();
    }

    public Object aggregationGroupCountSum() {
        //使用管道操作符 $group 进行分组，然后统计各个组文档某字段值合计  avg
        GroupOperation groupOperation = Aggregation.group("categoryId").sum("brandId").as("maxGroupCount");
        Aggregation aggregation = Aggregation.newAggregation(groupOperation);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map map : results.getMappedResults()) {
            System.out.println(map);
        }
        return results.getMappedResults();
    }

    /**
     * 求brandId的和
     * 统计 brandId 列表中元素的个数，应该使用 $size 操作符，
     * 但在 Spring Data MongoDB 的 GroupOperation 中，没有直接支持 $size 的方法。
     * @return
     */
    public Object aggregationGroupCountSum1() {
        // 使用 $project 阶段计算 brandId 列表的大小
        ProjectionOperation projectionOperation = Aggregation.project()
                .and(ArrayOperators.Size.lengthOfArray("brandId")).as("brandIdCount");

        // 按照 categoryId 分组，并对 brandIdCount 求和
        GroupOperation groupOperation = Aggregation.group("categoryId")
                .sum("brandIdCount").as("totalBrandIdCount");

        // 构建聚合管道
        Aggregation aggregation = Aggregation.newAggregation(projectionOperation, groupOperation);

        // 执行聚合操作
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);

        // 输出结果
        for (Map map : results.getMappedResults()) {
            System.out.println(map);
        }

        return results.getMappedResults();
    }

    public Object aggregationGroupFirst() {
//      先对数据进行排序，然后使用管道操作符 $group 进行分组，最后统计各个组文档某字段值第一个值  last
        AggregationOperation sort = Aggregation.sort(Sort.by("shopId").descending());
        GroupOperation group = Aggregation.group("categoryId")
                .first("brandId").as("brandFirst")
                .sum("brandId").as("brandTotal");
        //统计每个范畴的第一个值与总和
        Aggregation aggregation = Aggregation.newAggregation(group, sort);
//         public static Aggregation newAggregation(AggregationOperation... operations) {
//		return new Aggregation(operations);
//	}
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map map : result.getMappedResults()) {
            System.out.println(map);
        }
        return result.getMappedResults();
    }

    public Object aggregationGroupPush() {
//      使用管道操作符 $group 结合表达式操作符 $push 获取某字段列表
        GroupOperation push = Aggregation.group("categoryId").push("brandId").as("brandList");
        Aggregation aggregation = Aggregation.newAggregation(push);
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map map : result.getMappedResults()) {
            System.out.println(map);
        }
        return result.getMappedResults();
    }


    //聚合管道操作符
    public Object aggregationGroupMatch() {
        AggregationOperation match = Aggregation.match(Criteria.where("categoryId").is(12));
        AggregationOperation group = Aggregation.group("location").max("shopId").as("shopMG");
        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map map : result.getMappedResults()) {
            System.out.println(map);
        }
        return result.getMappedResults();
    }

    /**
     * 使用 $group 和 $limit 聚合,先使用 $group 进行分组，然后再使用 $limit 限制一定数目文档
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
//        AggregationOperation limit = Aggregation.skip(2L);
//        // 将操作加入到聚合对象中
//        Aggregation aggregation = Aggregation.newAggregation(group, limit);
        // 执行聚合查询
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Map result : results.getMappedResults()) {
            System.out.println(result);
        }
        return results.getMappedResults();
    }


    /**
     * 使用 $group 和 $project 聚合,先使用 $group 进行分组，然后再使用 $project 限制显示的字段
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
    /**
     * 想要取出某一个或者几个列，用聚合group.push还是视图view
     * 1. 使用 group 和 push
     * 需要对数据进行分组并计算聚合值，例如求和、计数、最大值、最小值等。
     * 希望在结果中包含某些元数据，比如说在分组中将一些字段的值聚合到一个数组中。
     * GroupOperation groupOperation = Aggregation.group("categoryId")
     *         .push("brandId").as("brandIds");
     * groupOperation每个 categoryId 分组都有一个 包含该组内所有的 brandId 值的brandIds 数组。
     * 2. 使用视图（view）
     * 视图通常用于将复杂查询封装起来，以便于后续的数据访问。
     * 需要从数据库中提取特定的列，且能够实时更新。
     * db.createView("myView", "collectionName", [
     *     { $project: { categoryId: 1, brandId: 1 } } // 只取出 categoryId 和 brandId
     * ]);
     * 复杂的聚合操作（如计数、求和、或其他统计），建议使用 group 和 push 操作符。
     * 如果要提取某些字段并希望这些字段是实时可用的，则使用视图
     */

}
