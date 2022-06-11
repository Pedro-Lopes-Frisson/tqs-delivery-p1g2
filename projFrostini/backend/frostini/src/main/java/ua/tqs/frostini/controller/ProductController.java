package ua.tqs.frostini.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.models.Product;
import ua.tqs.frostini.service.ProductService;

@RestController
@RequestMapping("api/v1/products")
@Validated
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    //add product
    //remove product
    //update product
    //list all products
    //list products according to a substring
    //get product by id

    @PostMapping("")
    public ResponseEntity<List<Product>> createProduct(@Valid @RequestBody ProductDTO productDTO){
        return null;
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<List<Product>> removeProduct(@Valid @RequestBody long productId){
        return null;
    }
    @PutMapping("/{productId}")
    public ResponseEntity<List<Product>> editProduct(@Valid @RequestBody long productId){
        return null;
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String substring){
        return null;
    }

    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAllAvailableProducts(){
        return null;
    }

    @GetMapping("/unavailable")
    public ResponseEntity<List<Product>> getAllUnavailableProducts(){
        return null;
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<List<Product>> getProduct(@PathVariable long productId){
        return null;
    }
//azure to upload product image

}
