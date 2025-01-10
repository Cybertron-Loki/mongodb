package org.example.MongoDBOperations;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Index {
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
}
