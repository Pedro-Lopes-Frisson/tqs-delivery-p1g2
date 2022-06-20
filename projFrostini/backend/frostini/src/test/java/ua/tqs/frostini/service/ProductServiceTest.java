package ua.tqs.frostini.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.exceptions.PossibleConstraintViolation;
import ua.tqs.frostini.exceptions.ResourceAlreadyCreated;
import ua.tqs.frostini.exceptions.ResourceNotFoundException;
import ua.tqs.frostini.models.Product;
import ua.tqs.frostini.repositories.ProductRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
  @Mock(lenient = true) ProductRepository productRepository;
  @InjectMocks ProductService productService;
  
  Product p;
  Product p1;
  ProductDTO productDTO;
  ProductDTO productDTO1;
  
  @BeforeEach
  void setUp() {
    p = createProduct( 2 );
    p1 = createProduct( 20 );
    
    productDTO = createProductDTO( 2 );
    productDTO1 = createProductDTO( 20 );
  }
  
  @Test
  void testWhenCreateProductIsCalled_thenReturnProduct() throws ResourceAlreadyCreated {
    when( productRepository.save( any() ) ).thenReturn( p );
    when( productRepository.findByName( productDTO.getName() ) ).thenReturn( Optional.empty() );
    
    Product product = productService.createProduct( productDTO );
    assertThat( product.getName() ).isEqualTo( productDTO.getName() );
    assertThat( product.getPrice() ).isEqualTo( productDTO.getPrice() );
    assertThat( product.getDescription() ).isEqualTo( productDTO.getDescription() );
    assertThat( product.getStockQuantity() ).isEqualTo( productDTO.getStockQuantity() );
    
    verify( productRepository, times( 1 ) ).save( any() );
    verify( productRepository, times( 1 ) ).findByName( productDTO.getName() );
  }
  
  @Test
  void testWhenCreateProductIsCalledWithAlreadyCreatedProduct_thenThrowException() throws ResourceAlreadyCreated {
    when( productRepository.findByName( productDTO.getName() ) ).thenReturn( Optional.of( p ) );
    
    assertThrows( ResourceAlreadyCreated.class, () -> productService.createProduct( productDTO ) );
    
    verify( productRepository, times( 1 ) ).findByName( productDTO.getName() );
  }
  
  @Test
  void testWhenUpdatingProductIdIfNameMatchesWithProductIdThenUpdateProduct() throws PossibleConstraintViolation {
    
    productDTO.setPrice( 10D ); //change price
    productDTO.setDescription( "Gacha Master 3000 Does React Native Raw Style" ); //change description
    
    // Create after update product
    Product productAfterUpdate = new Product();
    
    productAfterUpdate.setName( productDTO.getName() );
    productAfterUpdate.setDescription( productDTO.getDescription() );
    productAfterUpdate.setPrice( productDTO.getPrice() );
    productAfterUpdate.setStockQuantity( productDTO.getStockQuantity() );
    
    // Mock repo calls
    when( productRepository.findByIdAndName( p.getId(), productDTO.getName() ) ).thenReturn( Optional.of( p ) );
    when( productRepository.save( any() ) ).thenReturn( p );
    
    
    // Make sure they are different now
    assertThat( productDTO.getPrice() ).isNotEqualTo( p.getPrice() );
    assertThat( productDTO.getDescription() ).isNotEqualTo( p.getDescription() );
    
    // Make sure name is the same
    assertThat( productDTO.getName() ).isEqualTo( p.getName() );
    
    Product product = productService.editProduct( p.getId(), productDTO );
    
    assertThat( product.getName() ).isEqualTo( productAfterUpdate.getName() );
    assertThat( product.getPrice() ).isEqualTo( productAfterUpdate.getPrice() );
    assertThat( product.getDescription() ).isEqualTo( productAfterUpdate.getDescription() );
    assertThat( product.getStockQuantity() ).isEqualTo( productAfterUpdate.getStockQuantity() );
    
    
    verify( productRepository, times( 1 ) ).findByIdAndName( p.getId(), productDTO.getName() );
    verify( productRepository, times( 1 ) ).save( any() );
  }
  
  @Test
  void testWhenUpdatingProductIdIfNameDoesNotMatchThrowException() throws PossibleConstraintViolation {
    
    productDTO.setName( "asdf" ); //change price
    productDTO.setPrice( 10D ); //change price
    productDTO.setDescription( "Gacha Master 3000 Does React Native Raw Style" ); //change description
    
    // Create after update product
    Product productAfterUpdate = new Product();
    
    productAfterUpdate.setName( productDTO.getName() );
    productAfterUpdate.setDescription( productDTO.getDescription() );
    productAfterUpdate.setPrice( productDTO.getPrice() );
    productAfterUpdate.setStockQuantity( productDTO.getStockQuantity() );
    
    // Mock repo calls
    when( productRepository.findByIdAndName( p.getId(), productDTO.getName() ) ).thenReturn( Optional.empty() );
    when( productRepository.save( any() ) ).thenReturn( null );
    
    assertThrows( PossibleConstraintViolation.class, () -> {
      productService.editProduct( p.getId(), productDTO );
    } );
    
    
    verify( productRepository, times( 1 ) ).findByIdAndName( p.getId(), productDTO.getName() );
    verify( productRepository, times( 0 ) ).save( any() );
  }
  
  @Test
  void testWhenGetAllProductsThenReturnListOfProducts() {
    when( productRepository.findAll() ).thenReturn( Arrays.asList( p, p1 ) );
    
    List<Product> productList = productService.getAllProducts();
    List<Product> productListExpected = Arrays.asList( p, p1 );
    
    assertThat( productList.stream().map( Product::getName ).collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getName ).collect(
        Collectors.toList() ) );
    
    assertThat( productList.stream().map( Product::getPrice ).collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getPrice ).collect(
        Collectors.toList() ) );
    
    verify( productRepository, times( 1 ) ).findAll();
  }
  
  
  @Test
  void testWhenGetAllAvailableProductsThenReturnProductsWithMoreThan0StockQuantity() {
    p.setStockQuantity( 0 );
    
    when( productRepository.findAll() ).thenReturn( Arrays.asList( p, p1 ) );
    
    List<Product> productList = productService.getAllAvailableProducts();
    List<Product> productListExpected = List.of( p1 );
    
    assertThat( productList.stream().filter( ( Product p ) -> p.getStockQuantity() > 0 ).map( Product::getName )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getName ).collect(
        Collectors.toList() ) );
    
    assertThat( productList.stream().filter( ( Product p ) -> p.getStockQuantity() > 0 ).map( Product::getPrice )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getPrice ).collect(
        Collectors.toList() ) );
    
    assertThat( productList.stream().map( Product::getStockQuantity ).filter( stockQuantity -> stockQuantity > 0 )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getStockQuantity ).collect(
        Collectors.toList() ) );
    
    verify( productRepository, times( 1 ) ).findAll();
  }
  
  @Test
  void testWhenGetAllUnavailableProductsThenReturnProductsWithStockQuantityOf0() {
    p.setStockQuantity( 0 );
    
    when( productRepository.findAll() ).thenReturn( Arrays.asList( p, p1 ) );
    
    List<Product> productList = productService.getAllUnavailableProducts();
    List<Product> productListExpected = List.of( p );
    
    assertThat( productList.stream().filter( ( Product p ) -> p.getStockQuantity() <= 0 ).map( Product::getName )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getName ).collect(
        Collectors.toList() ) );
    
    assertThat( productList.stream().filter( ( Product p ) -> p.getStockQuantity() <= 0 ).map( Product::getPrice )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getPrice ).collect(
        Collectors.toList() ) );
    
    assertThat( productList.stream().map( Product::getStockQuantity ).filter( stockQuantity -> stockQuantity <= 0 )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getStockQuantity ).collect(
        Collectors.toList() ) );
    verify( productRepository, times( 1 ) ).findAll();
  }
  
  @Test
  void whenGetProductsBySubStringThenReturnOnlyProductsThatContainThatSubString() {
    
    p.setName( "SubString" );
    
    when( productRepository.findAll() ).thenReturn( Arrays.asList( p, p1 ) );
    
    List<Product> productList = productService.getProductsBySubstring( "SubStr" );
    List<Product> productListExpected = List.of( p );
    
    assertThat( productList.stream().map( Product::getName ).filter( name -> name.contains( "SubStr" ) )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getName ).collect(
        Collectors.toList() ) );
    
    assertThat( productList.stream().filter( ( Product p ) -> p.getName().toLowerCase( Locale.ROOT ).contains(
                             "SubStr".toLowerCase() ) ).map( Product::getPrice )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getPrice ).collect(
        Collectors.toList() ) );
    
    assertThat( productList.stream().filter( ( Product p ) -> p.getName().toLowerCase( Locale.ROOT ).contains(
                             "SubStr".toLowerCase() ) ).map( Product::getStockQuantity )
                           .collect( Collectors.toList() ) ).isEqualTo(
      productListExpected.stream().map( Product::getStockQuantity ).collect(
        Collectors.toList() ) );
    
    
    verify( productRepository, times( 1 ) ).findAll();
    
  }
  
  @Test
  void testWhenGetProductByIdWithValidIdThenReturnProduct() throws ResourceNotFoundException {
    when( productRepository.findById( p.getId() ) ).thenReturn( Optional.of( p ) );
    
    Product product = productService.getProductById( p.getId() );
    Assertions.assertEquals( product, p );
    
    verify( productRepository, times( 1 ) ).findById( p.getId() );
  }
  
  
  @Test
  void testWhenGetProductByIdWithInvalidIdThenThrowResourceNotFound() throws ResourceNotFoundException {
    when( productRepository.findById( p.getId() ) ).thenReturn( Optional.empty() );
    
    assertThrows( ResourceNotFoundException.class, () -> productService.getProductById( p.getId() ) );
    
    verify( productRepository, times( 1 ) ).findById( p.getId() );
  }
  
  private ProductDTO createProductDTO( int someValue ) {
    ProductDTO product = new ProductDTO();
    product.setPrice( 9.99 + 2 * someValue );
    product.setName( "Kinder Bueno Ice Cream " + someValue );
    product.setStockQuantity( 10 + someValue * 10 );
    product.setDescription( "Sou gostoso" );
    return product;
  }
  
  private Product createProduct( int someValue ) {
    Product product = new Product();
    product.setPrice( 9.99 + 2 * someValue );
    product.setName( "Kinder Bueno Ice Cream " + someValue );
    product.setStockQuantity( 10 + someValue * 10 );
    product.setDescription( "Sou gostoso" );
    product.setId( (long) someValue );
    return product;
  }
}
