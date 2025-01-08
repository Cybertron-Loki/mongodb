package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
        private static final long serialVersionUID = 1L;
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
