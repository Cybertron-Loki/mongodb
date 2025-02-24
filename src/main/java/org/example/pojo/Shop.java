package org.example.pojo;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * store in mysql
 */
@Data
public class Shop {
    /**
     * 商家id
     */
    private Long id;
    /**
     * 商家名/品牌名
     */
    private String name;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 商家位置
     */
    private String location;

    @Field("images")
    private List<File> images;
    /**
     * 关联品牌(一个shop可以有多个brand)
     */
    @Field("brandId")
    private List<Long> brandId;
    /**
     * shop的所属范畴
     */
    private int categoryId;
    /**
     * 商店描述
     */
    private String description;
}
