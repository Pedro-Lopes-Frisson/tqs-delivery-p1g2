package ua.tqs.frostini.integrationtests;

import io.restassured.RestAssured;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.models.Product;
import ua.tqs.frostini.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;


@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {
  
  @LocalServerPort
  int randomServerPort;
  
  @Autowired
  private ProductRepository productRepository;
  
  
  private Product product;
  private Product product1;
  private Product product2;
  private ProductDTO productDTO;
  
  
  @Container
  static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer( "postgres:11.12" )
    .withUsername( "demo" )
    .withPassword( "demopw" )
    .withDatabaseName( "shop" ).withReuse( true );
  
  
  @DynamicPropertySource
  static void properties( DynamicPropertyRegistry registry ) {
    registry.add( "spring.datasource.url", container::getJdbcUrl );
    registry.add( "spring.datasource.password", container::getPassword );
    registry.add( "spring.datasource.username", container::getUsername );
  }
  
  @BeforeEach
  void setUp() {
    product = new Product();
    product1 = new Product();
    product2 = new Product();
    productDTO = new ProductDTO();
    product.setName( "Gelado de Nozes" );
    product.setPrice( 12.2D );
    product.setDescription( "Gelado de Nozes Pecã" );
    product.setStockQuantity( 12 );
    
    productDTO.setName( "Gelado de Nozes" );
    productDTO.setPrice( 12.2D );
    productDTO.setDescription( "Gelado de Nozes Pecã" );
    productDTO.setStockQuantity( 12 );
    
    
    product1.setName( "Gelado de Nozes 1" );
    product1.setPrice( 22.2D );
    product1.setDescription( "Gelado de Nozes Pecã 1" );
    product1.setStockQuantity( 120 );
    
    product2.setName( "Gelado de Nozes 3" );
    product2.setPrice( 32.2D );
    product2.setDescription( "Gelado de Nozes Pecã 3" );
    product2.setStockQuantity( 1200 );
  }
  
  @AfterEach
  void resetDb() {
    productRepository.deleteAll();
    productRepository.flush();
  }
  
  @Test
  void testWhenPostProductDtoThenCreateProductAndReturnIt() {
    
    RestAssured.given()
               .contentType( "application/json" )
               .body( productDTO )
               .when()
               .post( createURL() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.CREATED_201 )
               .and().body( "name", equalTo( product.getName() ) )
               .and().body( "description", equalTo( product.getDescription() ) )
               .and().body( "price", equalTo( product.getPrice().floatValue() ) )
               .and().body( "stockQuantity", equalTo( product.getStockQuantity() ) );
    
  }
  
  @Test
  void testWhenPostExistentResourceReceiveConflict() {
    productRepository.save( product );
    RestAssured.given()
               .contentType( "application/json" )
               .body( productDTO )
               .when()
               .post( createURL() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.CONFLICT_409 );
    
  }
  
  @Test
  void testWhenEditingValidProdcutThenReturnUpdatedProduct() {
    Product p = productRepository.save( product );
    productDTO.setStockQuantity( 2000 );
    RestAssured.given()
               .contentType( "application/json" )
               .body( productDTO )
               .when()
               .put( createURL() + "/" + p.getId() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK_200 )
               .and().body( "name", equalTo( product.getName() ) )
               .and().body( "description", equalTo( product.getDescription() ) )
               .and().body( "price", equalTo( product.getPrice().floatValue() ) )
               .and().body( "stockQuantity", not( equalTo( product.getStockQuantity() ) ) )
               .and().body( "stockQuantity", equalTo( productDTO.getStockQuantity() ) );
    
  }
  
  
  @Test
  void testWhenEditingValidProductWithInvalidIdAsPathParameterThenReturnBadRequest() {
    productRepository.save( product );
    productDTO.setStockQuantity( 2000 );
    RestAssured.given()
               .contentType( "application/json" )
               .body( productDTO )
               .when()
               .put( createURL() + "/-1" )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.BAD_REQUEST_400 );
    
  }
  
  @Test
  void testWhenEditingValidProdcutWithInvalidProductDTO_thenReturnError() {
    Product p = productRepository.save( product );
    productDTO.setStockQuantity( - 100 );
    productDTO.setPrice( - 100 );
    RestAssured.given()
               .contentType( "application/json" )
               .body( productDTO )
               .when()
               .put( createURL() + "/" + p.getId() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.BAD_REQUEST_400 );
    
  }
  
  @Test
  void testWhenGetAllProductsThenReturnAListOfProducts() {
    
    Product p = productRepository.save( product );
    Product p1 = productRepository.save( product1 );
    Product p2 = productRepository.save( product2 );
    List<Product> products = Arrays.asList( p, p1, p2 );
    
    
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .get( createURL() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK_200 )
               .and()
               .body( "[0].name", in( products.stream().map( Product::getName ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].name", in( products.stream().map( Product::getName ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[2].name", in( products.stream().map( Product::getName ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[0].price",
                 in( products.stream().map( pp -> pp.getPrice().floatValue() ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].price",
                 in( products.stream().map( pp -> pp.getPrice().floatValue() ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[2].price",
                 in( products.stream().map( pp -> pp.getPrice().floatValue() ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[0].description",
                 in( products.stream().map( Product::getDescription ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].description",
                 in( products.stream().map( Product::getDescription ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[2].description",
                 in( products.stream().map( Product::getDescription ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[0].stockQuantity",
                 in( products.stream().map( Product::getStockQuantity ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].stockQuantity",
                 in( products.stream().map( Product::getStockQuantity ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[2].stockQuantity",
                 in( products.stream().map( Product::getStockQuantity ).collect( Collectors.toList() ) ) );
  }
  
  @Test
  void testWhenGetAllAvailableProductsThenReturnAListOfProducts() {
    product.setStockQuantity( 0 );
    product1.setStockQuantity( 0 );
    
    productRepository.save( product );
    productRepository.save( product1 );
    productRepository.save( product2 );
    
    
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .get( createURL() + "/available" )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK_200 )
               .and()
               .body( "[0].name", equalTo( product2.getName() ) )
               .and()
               .body( "[0].price", equalTo( product2.getPrice().floatValue() ) )
               .and()
               .body( "[0].description", equalTo( product2.getDescription() ) )
               .and()
               .body( "[0].stockQuantity", equalTo( product2.getStockQuantity() ) );
    
  }
  
  @Test
  void testWhenGetAllUnavailableProductsThenReturnAListOfProducts() {
    product.setStockQuantity( 0 );
    product1.setStockQuantity( 0 );
    
    
    Product p = productRepository.save( product );
    Product p1 = productRepository.save( product1 );
    Product p2 = productRepository.save( product2 );
    List<Product> products = Arrays.asList( p, p1 );
    
    
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .get( createURL() + "/unavailable" )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK_200 )
               .and()
               .body( "[0].name", in( products.stream().map( Product::getName ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].name", in( products.stream().map( Product::getName ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[0].price",
                 in( products.stream().map( pp -> pp.getPrice().floatValue() ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].price",
                 in( products.stream().map( pp -> pp.getPrice().floatValue() ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[0].description",
                 in( products.stream().map( Product::getDescription ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].description",
                 in( products.stream().map( Product::getDescription ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[0].stockQuantity",
                 in( products.stream().map( Product::getStockQuantity ).collect( Collectors.toList() ) ) )
               .and()
               .body( "[1].stockQuantity",
                 in( products.stream().map( Product::getStockQuantity ).collect( Collectors.toList() ) ) );
  }
  
  @Test
  void testWhenGettingValidProductThenReturnProduct() {
    Product p = productRepository.save( product );
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .get( createURL() + "/" + p.getId() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK_200 )
               .and().body( "id", equalTo( (int) p.getId() ) )
               .and().body( "name", equalTo( product.getName() ) )
               .and().body( "description", equalTo( product.getDescription() ) )
               .and().body( "price", equalTo( product.getPrice().floatValue() ) )
               .and().body( "stockQuantity", equalTo( productDTO.getStockQuantity() ) );
    
  }
  
  @Test
  void testWhenGettingInvalidProductThenReturnProduct() {
    Product p = productRepository.save( product );
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .get( createURL() + "/-1")
               .then().log().body().assertThat()
               .statusCode( HttpStatus.BAD_REQUEST_400 );
    
  }
  
  String createURL() {
    return "http://localhost:" + randomServerPort + "/api/v1/products";
  }
}
