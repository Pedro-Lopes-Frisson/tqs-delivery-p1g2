package ua.tqs.frostini.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.exceptions.PossibleConstraintViolation;
import ua.tqs.frostini.exceptions.ResourceAlreadyCreated;
import ua.tqs.frostini.exceptions.ResourceNotFoundException;
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
  public ResponseEntity<Product> createProduct( @Valid @RequestBody ProductDTO productDTO ) {
    Product saved = null;
    try {
      saved = productService.createProduct( productDTO );
    } catch (ResourceAlreadyCreated e) {
      return ResponseEntity.status( HttpStatus.CONFLICT ).body( null );
    }
    return ResponseEntity.status( HttpStatus.CREATED ).body( saved );
  }
  
  /*
  @DeleteMapping("/{productId}")
  public ResponseEntity<Product> removeProduct(@Valid @PathVariable long productId){
      boolean deleted = productService.deleteProduct(productId);
      if (!deleted){
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
   */
  @PutMapping("/{productId}")
  public ResponseEntity<Product> editProduct( @Valid @PathVariable long productId,
                                              @RequestBody ProductDTO updatedProductDTO ) {
    Product edited = null;
    try {
      edited = productService.editProduct( productId, updatedProductDTO );
    } catch (PossibleConstraintViolation e) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }
    if ( edited == null ) {return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );}
    return ResponseEntity.status( HttpStatus.OK ).body( edited );
  }
  
  @GetMapping("")
  public ResponseEntity<List<Product>> getAllProducts( @RequestParam(required = false) String substring ) {
    if ( substring != null ) {
      List<Product> filteredProducts = productService.getProductsBySubstring( substring );
      return ResponseEntity.status( HttpStatus.OK ).body( filteredProducts );
    }
    List<Product> allProducts = productService.getAllProducts();
    return ResponseEntity.status( HttpStatus.OK ).body( allProducts );
  }
  
  @GetMapping("/available")
  public ResponseEntity<List<Product>> getAllAvailableProducts() {
    List<Product> allAvailableProducts = productService.getAllAvailableProducts();
    return ResponseEntity.status( HttpStatus.OK ).body( allAvailableProducts );
  }
  
  @GetMapping("/unavailable")
  public ResponseEntity<List<Product>> getAllUnavailableProducts() {
    List<Product> allUnavailableProducts = productService.getAllUnavailableProducts();
    return ResponseEntity.status( HttpStatus.OK ).body( allUnavailableProducts );
  }
  
  @GetMapping("/{productId}")
  public ResponseEntity<Product> getProduct( @PathVariable long productId ) {
    Product found = null;
    try {
      found = productService.getProductById( productId );
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }
    return ResponseEntity.status( HttpStatus.OK ).body( found );
  }
//azure to upload product image

}
