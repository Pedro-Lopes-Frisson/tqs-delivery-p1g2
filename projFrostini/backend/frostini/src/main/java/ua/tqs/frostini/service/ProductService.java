package ua.tqs.frostini.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.models.Product;

public class ProductService {
     //add product
    public Product createProduct(ProductDTO productDTO){
         return null;
    }
 
     //remove product
     public boolean deleteProduct(long productId) {
        return false;
    }
 
     //update product
    public Product editProduct(long productId, ProductDTO updatedProductDTO){
        return null;
    }
 
     //list all products
    public List<Product> getAllProducts(){
        return null;
    }
 
    public List<Product> getAllAvailableProducts(){
        return null;
    }

    public List<Product> getAllUnavailableProducts(){
        return null;
    }
     //list products according to a substring
     public List<Product> getProductsBySubstring(String subString){
        return null;
    }
 
     //get product by id
     public Product getProductById(long productId){
        return null;
    }

    
 
}
