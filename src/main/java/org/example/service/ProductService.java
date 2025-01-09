package org.example.service;


import org.example.pojo.Product;
import org.example.pojo.Result;


public interface ProductService {
     Result checkAllProducts();

    Result checkByName(String name);

    Result addProduct(Product product);

    Result updateProduct(Product product);

    Result deleteProductById(Long id);

    Result checkShopProducts(Long id);
}
