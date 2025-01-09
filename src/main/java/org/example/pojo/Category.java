package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * store in redis ,check frequently ,
 * use id to connect with other entity (include blog/shop/product/brand),
 * convent to manage
 */
@Data
public class Category {
    private int id; //范畴id
    private String name; //范畴名字
    private String description;//范畴说明
    private int kind;//哪种范畴（shop还是brand还是product还是blog ）
}
