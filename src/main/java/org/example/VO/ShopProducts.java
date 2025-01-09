package org.example.VO;

import lombok.Data;
import org.example.pojo.Product;
import org.example.pojo.Shop;

import java.util.List;

/**
 * this is used to show the products and its' products
 */
@Data
public class ShopProducts {
    private Shop shop;
    private List<Product> product;
}
