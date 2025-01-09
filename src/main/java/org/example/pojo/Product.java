package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * store in mongoDB ,complex data structure
 */
@Data
@Document("Product")
public class Product {
    @MongoId
    private Long    id ;//    '产品id',
    @Field("brandId")
    private Long   brandId ;   //'关联品牌',
    @Field("shopId")
    private Long   shopId ;   //'关联商家',
    @Field("images")
    private List<File> images ;// '照骗',
    private String description;//'产品描述'
    private String category ;//'产品所属分类'
    @JsonFormat(pattern = "")
    private LocalDateTime createTime; //'创建时间',
    private LocalDateTime   updateTime; // '更新时间',
    @Field("price")
    private Double   price; //'价格'
}
