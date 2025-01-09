package org.example.controller;


import org.example.pojo.Product;
import org.example.pojo.Result;
import org.example.service.ProductService;
import org.example.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopService shopService;

    /**
     * check all the shops in mongodb
     * @return shop list
     */
    @GetMapping("/checkAllShops")
    public Result checkAllProducts(){
        return productService.checkAllProducts();
    }

    /**
     * check by fuzzy query
     */
    @GetMapping("/checkByName")
    public Result checkByName(String name){
        return productService.checkByName(name);
    }

    /**
     * add shop one by one
     * @param product
     * @return
     */
    @PostMapping("/addShop")
    public Result addProduct(Product product){
        return productService.addProduct(product);
    }

    /**
     * admin can update the data about shop
     */
    @PostMapping("/updateShop")
    public Result updateProduct(Product product){
        return productService.updateProduct(product);
    }

    /**
     * delete shop by ID (just admin function)
     */
    @DeleteMapping("/delete")
    public Result deleteProductById(Long id){
        return productService.deleteProductById(id);
    }
    /**
     * select by shop Id
     *
     */
    @GetMapping("/checkShopProduct")
    public Result checkShopProductById(Long id){
        return productService.checkShopProducts(id);
    }

}
