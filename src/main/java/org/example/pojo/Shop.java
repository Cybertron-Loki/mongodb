package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
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
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    private LocalDateTime updatetime;
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
