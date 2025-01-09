package org.example.utils;

import lombok.Data;
import org.example.pojo.PageResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


public class PagingUtils {

    public static <T> PageResult<T> paginate(MongoTemplate mongoTemplate, String collectionName, int page, int size, Class<T> clazz) {
        Pageable pageable = PageRequest.of(page, size); // 创建分页请求
        Query query = new Query();
        query.with(pageable); // 设置分页信息

        // 查询数据
        List<T> content = mongoTemplate.find(query, clazz, collectionName);

        // 获取总记录数
        long total = mongoTemplate.count(query, clazz, collectionName);

        return new PageResult<>(content, total, pageable.getPageNumber(), pageable.getPageSize());
    }
}
