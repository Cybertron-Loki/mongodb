package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class brand {
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
         * 商家名/品牌位置
         */
        private String location;
        @Field("images")
        private String images;

        private int categoryId;
}
