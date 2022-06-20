package ua.tqs.frostini.controller;

import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.exceptions.PossibleConstraintViolation;
import ua.tqs.frostini.exceptions.ResourceAlreadyCreated;
import ua.tqs.frostini.exceptions.ResourceNotFoundException;
import ua.tqs.frostini.models.Product;
import ua.tqs.frostini.service.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@WebMvcTest(value = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
  private String API_PRODUCTS_ENDPOINT = "api/v1/products";
  
  ProductDTO productDTO;
  Product product;
  Product unavailableProduct;
  List<Product> allproducts;
  
  @Autowired
  private MockMvc mvc;
  
  @MockBean
  private ProductService productService;
  
  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc( mvc );
    allproducts = new ArrayList();
    productDTO = createProductDTO();
    product = createProduct();
    unavailableProduct = createProduct();
    unavailableProduct.setStockQuantity( 0 );
    unavailableProduct.setId( 2L );
    
    allproducts.add( product );
    allproducts.add( unavailableProduct );
  }
  
  //add product
  @Test
  void testWhenCreateValidProduct_thenReturnCreatedProduct() throws ResourceAlreadyCreated {
    when( productService.createProduct( productDTO ) ).thenReturn( product );
    
    given()
      .contentType( ContentType.JSON ).body( productDTO ).post( API_PRODUCTS_ENDPOINT )
      .then().assertThat()
      .contentType( ContentType.JSON ).and()
      .status( HttpStatus.CREATED ).and()
      .body( "size()", is( 6 ) ).and()        // 1 Object with of attributes
      .body( "price", is( product.getPrice().floatValue() ) ).and()
    ;
    
    verify( productService, times( 1 ) ).createProduct( any() );
  }
  
  @Test
  void testWhenCreateAlreadyExistentResource_thenConflict() throws ResourceAlreadyCreated {
    when( productService.createProduct( productDTO ) ).thenThrow( ResourceAlreadyCreated.class );
    
    given()
      .contentType( ContentType.JSON ).body( productDTO ).post( API_PRODUCTS_ENDPOINT )
      .then().assertThat()
      .status( HttpStatus.CONFLICT )
    ;
    
    verify( productService, times( 1 ) ).createProduct( any() );
  }
  
  
  @Test
  void testWhenUpdatingResourceWithInvalidIdOrName_thenBadRequest()
    throws PossibleConstraintViolation {
    productDTO.setName( "RandomName" );
    when( productService.editProduct( 1L, productDTO ) ).thenThrow( PossibleConstraintViolation.class );
    given()
      .contentType( ContentType.JSON ).body( productDTO ).put( API_PRODUCTS_ENDPOINT + "/1" )
      .then().assertThat()
      .status( HttpStatus.BAD_REQUEST )
    ;
    
    verify( productService, times( 1 ) ).editProduct( 1L, productDTO );
  }
  
  @Test
  void testWhenCreateInvalidProduct_thenReturnBadRequest() throws ResourceAlreadyCreated {
    when( productService.createProduct( any() ) ).thenReturn( null );
    
    given()
      .contentType( ContentType.JSON ).body( "InvalidProduct" ).post( API_PRODUCTS_ENDPOINT )
      .then().assertThat()
      .status( HttpStatus.BAD_REQUEST );
    
    verify( productService, times( 0 ) ).createProduct( any() );
  }
    
    /*
    // //remove product
    @Test
    void testWhenDeleteValidProduct_thenReturnNoContent(){
        when(productService.deleteProduct(product.getId())).thenReturn(true);

        given()
            .delete(API_PRODUCTS_ENDPOINT+"/{productId}",product.getId())
        .then().log().body().assertThat()
            .status(HttpStatus.NO_CONTENT);

        verify(productService, times(1)).deleteProduct(product.getId());
    }

    @Test
    void testWhenDeleteInvalidProduct_thenReturnBadRequest(){
        when(productService.deleteProduct(0L)).thenReturn(false);

        given()
            .delete(API_PRODUCTS_ENDPOINT+"/{productId}", 0L)
        .then().log().body().assertThat()
            .status(HttpStatus.BAD_REQUEST);

        verify(productService, times(1)).deleteProduct(0L);
    }
    */
  
  //update product
  @Test
  void testWhenUpdateProductWithValidIdAndBody_thenReturnUpdatedProduct() throws PossibleConstraintViolation {
    product.setStockQuantity( 8 );
    productDTO.setStockQuantity( 8 );
    when( productService.editProduct( product.getId(), productDTO ) ).thenReturn( product );
    given()
      .contentType( ContentType.JSON ).body( productDTO ).put( API_PRODUCTS_ENDPOINT + "/{productId}", product.getId() )
      .then().log().body().assertThat()
      .status( HttpStatus.OK ).and()
      .contentType( ContentType.JSON ).and()
      .body( "size()", is( 6 ) ).and()
      .body( "stockQuantity", is( product.getStockQuantity() ) );
    
    verify( productService, times( 1 ) ).editProduct( product.getId(), productDTO );
  }
  
  @Test
  void testWhenUpdateProductWithInvalidId_thenReturnBadRequest() throws PossibleConstraintViolation {
    productDTO.setStockQuantity( 8 );
    when( productService.editProduct( 0L, productDTO ) ).thenReturn( null );
    given()
      .contentType( ContentType.JSON ).body( productDTO ).put( API_PRODUCTS_ENDPOINT + "/{productId}", 0L )
      .then().log().body().assertThat()
      .status( HttpStatus.BAD_REQUEST );
    
    verify( productService, times( 1 ) ).editProduct( 0L, productDTO );
  }
  
  // @Test
  // void testWhenUpdateProductWithInvalidBody_thenReturnBadRequest(){
  //     when(productService.editProduct(product.getId(), any())).thenReturn(null);
  //     given()
  //         .contentType(ContentType.JSON).body("invalidBody").put(API_PRODUCTS_ENDPOINT+"/{productId}",product
  //         .getId())
  //     .then().log().body().assertThat()
  //         .status(HttpStatus.BAD_REQUEST);
  
  //     verify(productService, times(0)).editProduct(product.getId(), any());
  // }
  
  //list all products
  @Test
  void testWhenGetAllProducts_thenReturnListOfProducts() {
    when( productService.getAllProducts() ).thenReturn( allproducts );
    System.out.println( allproducts );
    given()
      .get( API_PRODUCTS_ENDPOINT )
      .then().log().body().assertThat()
      .status( HttpStatus.OK ).and()
      .contentType( ContentType.JSON ).and()
      .body( "size()", is( allproducts.size() ) );
    
    verify( productService, times( 1 ) ).getAllProducts();
  }
  
  @Test
  void testWhenGetAllAvailableProducts_thenReturnListOfAvailableProducts() {
    allproducts.removeIf( s -> s.getStockQuantity() == 0 );
    List<Product> availableProducts = allproducts;
    
    when( productService.getAllAvailableProducts() ).thenReturn( availableProducts );
    given()
      .get( API_PRODUCTS_ENDPOINT + "/available" )
      .then().log().body().assertThat()
      .status( HttpStatus.OK ).and()
      .contentType( ContentType.JSON ).and()
      .body( "size()", is( availableProducts.size() ) );
    
    verify( productService, times( 1 ) ).getAllAvailableProducts();
  }
  
  @Test
  void testWhenGetAllUnavailableProducts_thenReturnListOfUnavailableProducts() {
    Predicate<Product> unavailable = product -> product.getStockQuantity() == 0;
    List<Product> unavailableProducts = allproducts.stream().filter( unavailable ).collect( Collectors.toList() );
    
    System.out.println( "unavailable products!" );
    System.out.println( unavailableProducts );
    
    when( productService.getAllUnavailableProducts() ).thenReturn( unavailableProducts );
    given()
      .get( API_PRODUCTS_ENDPOINT + "/unavailable" )
      .then().log().body().assertThat()
      .status( HttpStatus.OK ).and()
      .contentType( ContentType.JSON ).and()
      .body( "size()", is( unavailableProducts.size() ) );
    
    verify( productService, times( 1 ) ).getAllUnavailableProducts();
  }
  
  // list products according to a substring
  @Test
  void testWhenGetAllProductsWithFilteringBySubstring_thenReturnListOfProducts() {
    String substring = "kinder";
    Predicate<Product> bySubstring = product -> product.getName().toLowerCase().contains( substring.toLowerCase() );
    List<Product> filteredProducts = allproducts.stream().filter( bySubstring ).collect( Collectors.toList() );
    
    when( productService.getProductsBySubstring( substring ) ).thenReturn( filteredProducts );
    given()
      .get( API_PRODUCTS_ENDPOINT + "?substring=" + substring )
      .then().log().body().assertThat()
      .status( HttpStatus.OK ).and()
      .contentType( ContentType.JSON ).and()
      .body( "size()", is( filteredProducts.size() ) );
    
    verify( productService, times( 1 ) ).getProductsBySubstring( substring );
  }
  
  //get product by id
  @Test
  void testWhenGetProductsByValidId_thenReturnProduct() throws ResourceNotFoundException {
    when( productService.getProductById( product.getId() ) ).thenReturn( product );
    given()
      .get( API_PRODUCTS_ENDPOINT + "/{productId}", product.getId() )
      .then().log().body().assertThat()
      .status( HttpStatus.OK ).and()
      .contentType( ContentType.JSON ).and()
      .body( "size()", is( 6 ) ).and()
      .body( "name", is( product.getName() ) );
    
    verify( productService, times( 1 ) ).getProductById( product.getId() );
  }
  
  @Test
  void testWhenGetProductsByInvalidId_thenReturnBadRequest() throws ResourceNotFoundException {
    when( productService.getProductById( product.getId() ) ).thenThrow( ResourceNotFoundException.class );
    given()
      .get( API_PRODUCTS_ENDPOINT + "/{productId}", product.getId() )
      .then().log().body().assertThat()
      .status( HttpStatus.BAD_REQUEST );
    
    verify( productService, times( 1 ) ).getProductById( product.getId() );
  }
  
  private ProductDTO createProductDTO() {
    ProductDTO product = new ProductDTO();
    product.setPrice( 9.99 );
    product.setName( "Kinder Bueno Ice Cream" );
    product.setStockQuantity( 10 );
    product.setDescription( "Sou gostoso" );
    return product;
  }
  
  private Product createProduct() {
    Product product = new Product();
    product.setPrice( 9.99 );
    product.setName( "Kinder Bueno Ice Cream" );
    product.setStockQuantity( 10 );
    product.setDescription( "Sou gostoso" );
    product.setId( 1L );
    return product;
  }
}
