package org.example.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * store in mongoDB ,complex
 */
@Data
@Document(collation = "Blog")
public class Blog {
    /**
     * 个人发表的避雷/种草/旅游经验帖子 ID
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * blog类型
     */
    private String categoryId;

    /**
     * blog内容
     */
    private String content;
    /**
     * 标题
     */
    private String title;
    /**
     * 关联商家/品牌/产品 id（可不填）
     */
    private Long brandId;

    private Long shopId;

    private Long productId;

    /**
     * 探店的照片，最多9张，多张以","隔开
     */
    private List<File> images;
    /**
     * 评论数量
     */
    private Integer commentCount;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 隐私：先定为公开可见或者仅群内可见或者仅自己可见
     */
    private String visible;
    private Long groupId;

    private Integer like;

    /**
     * 当前用户是否点赞过
     */
    @Field("isLiked")
    private boolean isLiked;

}
