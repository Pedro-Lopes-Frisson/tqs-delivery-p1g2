package ua.tqs.frostini.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.exceptions.PossibleConstraintViolation;
import ua.tqs.frostini.exceptions.ResourceAlreadyCreated;
import ua.tqs.frostini.models.Product;
import ua.tqs.frostini.repositories.ProductRepository;
import ua.tqs.frostini.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
  @Autowired ProductRepository productRepository;
  
  //add product
  public Product createProduct( ProductDTO productDTO ) throws ResourceAlreadyCreated {
    Optional<Product> optionalProduct = productRepository.findByName( productDTO.getName() );
    if ( optionalProduct.isPresent() ) {
      throw new ResourceAlreadyCreated( "This Product was already created." );
    }
    Product p = new Product();
    p.setName( productDTO.getName() );
    p.setDescription( productDTO.getDescription() );
    p.setPrice( productDTO.getPrice() );
    p.setStockQuantity( productDTO.getStockQuantity() );
    return productRepository.save( p );
  }
  
  //remove product
  //public boolean deleteProduct( long productId ) {
  //return false;
  //}
  
  //update product
  public Product editProduct( long productId, ProductDTO updatedProductDTO ) throws PossibleConstraintViolation {
    // look for product with that id
    // look for product with that name
    Optional<Product> optionalProduct = productRepository.findByIdAndName( productId, updatedProductDTO.getName() );
    if ( optionalProduct.isEmpty() ) {
      throw new PossibleConstraintViolation( "This project might exist with another id or with another name" );
    }
    
    Product p = optionalProduct.get();
    p.setName( updatedProductDTO.getName() );
    p.setDescription( updatedProductDTO.getDescription() );
    p.setPrice( updatedProductDTO.getPrice() );
    p.setStockQuantity( updatedProductDTO.getStockQuantity() );
    return productRepository.save( p );
  }
  
  //list all products
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }
  
  public List<Product> getAllAvailableProducts() {
    return productRepository.findAll().stream().filter( p -> p.getStockQuantity() > 0 ).collect( Collectors.toList() );
  }
  
  public List<Product> getAllUnavailableProducts() {
    return productRepository.findAll().stream().filter( p -> p.getStockQuantity() <= 0 ).collect( Collectors.toList() );
  }
  
  //list products according to a substring
  public List<Product> getProductsBySubstring( String subString ) {
    return productRepository.findAll().stream().filter(
                              p -> p.getName().toLowerCase( Locale.ROOT ).contains( subString.toLowerCase( Locale.ROOT ) ) )
                            .collect( Collectors.toList() );
  }
  
  //get product by id
  public Product getProductById( long productId ) throws ResourceNotFoundException {
    Optional<Product> optionalProduct = productRepository.findById( productId );
    if(optionalProduct.isEmpty()){
      throw new ResourceNotFoundException("This Product does not exist");
    }
    return optionalProduct.get();
  }
  
  
}
