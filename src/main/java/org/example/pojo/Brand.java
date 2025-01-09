package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * store in mysql ,not change frequently
 */
@Data
public class Brand {
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
         * 品牌位置(headquarter)
         */
        private String location;
        @Field("images")
        private List<File> images;

        private int categoryId;
        /**
         * brand描述
         */
        private String description;
}
