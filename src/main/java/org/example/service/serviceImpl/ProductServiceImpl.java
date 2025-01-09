package org.example.service.serviceImpl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.example.VO.ShopProducts;
import org.example.pojo.Product;
import org.example.pojo.Result;
import org.example.service.ProductService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private MongoTemplate mongoTemplate;
    final static private String COLLECTION_NAME = "Product";//需要操作的集合名
    /**
     * check all the products info
     * @return products list
     */

    public Result checkAllProducts(){
        List<Product> productList = mongoTemplate.findAll(Product.class, COLLECTION_NAME);
        return Result.success(productList);
    }
    /**
     * page inquiry
     */

    /**
     * fussy check
     * @param name
     * @return products may match
     */
    @Override
    public Result checkByName(String name) {
        Criteria criteria = Criteria.where("name").regex(name,"i");
        Query query = new Query(criteria).with(Sort.by("id"));
        List<Product> productsList = mongoTemplate.find(query, Product.class, COLLECTION_NAME);
        return Result.success(productsList);
    }

    @Override
    public Result addProduct(Product product) {

        Product productInsert = mongoTemplate.insert(product, COLLECTION_NAME);
        if(productInsert != null){
            return Result.success(productInsert);
        }
        return Result.fail("something went wrong");
    }

    @Override
    public Result updateProduct(Product product) {
        //verify admin indentity
        Query query = new Query(Criteria.where("id").is(product.getId()));
        Update update = Update.update("description", product.getDescription())
                .set("location", product.getBrandId())
                .set("price", product.getPrice())
                .set("brandId", product.getBrandId())
                .set("shopId", product.getShopId())
                .set("categoryId",product.getCategory())
                .set("description", product.getDescription());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        //if here haven't match result ,then did nothing ,instead of create a new one
        if(updateResult.getMatchedCount() > 0){
            return Result.success(product);
        }
        return Result.fail("something went wrong");
    }

    @Override
    public Result deleteProductById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        DeleteResult removeResult = mongoTemplate.remove(query, COLLECTION_NAME);
        if (removeResult.getDeletedCount() > 0){
            return Result.success(id);
        }
        return Result.fail("something went wrong");
    }

    @Override
    public Result checkShopProducts(Long id) {
        Criteria criteria = Criteria.where("shopId").is(id);
        //id is shop's ID
        Query query = new Query(criteria).with(Sort.by("id"));
        //this id is products' ID in the document
        List<Product> productsList = mongoTemplate.find(query, Product.class, COLLECTION_NAME);
        return Result.success(productsList);
        //front end in charge of display products and shops together(shops display a part ahead)
    }


}
