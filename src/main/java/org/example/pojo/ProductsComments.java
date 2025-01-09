//package org.example.pojo;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//import org.springframework.data.mongodb.core.mapping.MongoId;
//
//import java.io.File;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Data
//@Document("productsComments")
//public class ProductsComments {
//    @MongoId
//    private Long    id ;//    '产品评论id',
//    private Long   productId ;   //'关联产品',
//    @Field("brandId")
//    private Long   brandId ;   //'关联品牌',
//    @Field("shopId")
//    private Long   shopId ;   //'关联商家',
//    @Field("images")
//    private List<File> images ;// '照骗',
//    private String description;//'产品评论'
//    private String category ;//'产品所属分类'
//    @JsonFormat(pattern = "")
//    private LocalDateTime createTime; //'创建时间',
//    private LocalDateTime   updateTime; // '更新时间',
//    @Field("price")
//    private Double price; //'价格'
//
//}
