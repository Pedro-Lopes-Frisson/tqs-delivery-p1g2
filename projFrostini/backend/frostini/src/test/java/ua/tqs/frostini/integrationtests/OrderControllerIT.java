package ua.tqs.frostini.integrationtests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import ua.tqs.frostini.datamodels.OrderDTO;
import ua.tqs.frostini.datamodels.OrderedProductDTO;
import ua.tqs.frostini.models.*;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.repositories.*;
import ua.tqs.frostini.service.DeliverySystemService;
import ua.tqs.frostini.service.OrderService;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIT {
  
  @LocalServerPort
  int randomServerPort;
  
  
  @Autowired
  private OrderService orderService;
  @Autowired
  AddressRepository addressRepository;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  UserRepository userRepository;
  
  @Autowired
  ProductRepository productRepository;
  @Autowired
  OrderedProductRepository orderedProductRepository;
  
  Address address;
  Address address1;
  Address address2;
  Address address3;
  
  User u = createUser( 1 );
  List<Order> userOrders;
  
  OrderDTO order0DTO;
  OrderDTO order1DTO;
  OrderDTO order2DTO;
  
  private Product product;
  private Product product1;
  private Product product2;
  Order o1;
  
  public static MockWebServer mockBackEnd;
  
  @BeforeAll
  static void allSetUp() throws IOException {
    mockBackEnd = new MockWebServer();
    mockBackEnd.start();
  }
  
  @AfterAll
  static void allCleanUp() throws IOException {
    mockBackEnd.shutdown();
  }
  
  @Container
  static PostgreSQLContainer container = new PostgreSQLContainer( "postgres:11.12" )
    .withUsername( "demo" )
    .withPassword( "demopw" )
    .withDatabaseName( "shop" );
  
  
  @DynamicPropertySource
  static void properties( DynamicPropertyRegistry registry ) {
    registry.add( "spring.datasource.url", container::getJdbcUrl );
    registry.add( "spring.datasource.password", container::getPassword );
    registry.add( "spring.datasource.username", container::getUsername );
  }
  
  @BeforeEach
  void setUp() {
    userOrders = new ArrayList<>();
    address = addressRepository.save( createAddress( 1 ) );
    address1 = addressRepository.save( createAddress( 2 ) );
    address2 = addressRepository.save( createAddress( 3 ) );
    address3 = addressRepository.save( createAddress( 4 ) );
    o1 = createOrder( 1, address );
    userOrders.add( o1 );
    userRepository.saveAndFlush( u );
    orderRepository.saveAndFlush( o1 );
    u.setOrder( Set.of( o1 ) );
    userRepository.saveAndFlush( u );
    
    
    product = new Product();
    product1 = new Product();
    product2 = new Product();
    
    product.setName( "Gelado de Nozes" );
    product.setPrice( 12.2D );
    product.setDescription( "Gelado de Nozes Pecã" );
    product.setStockQuantity( 12 );
    product.setPhotoUrl( "SomeUrl" );
    
    product1.setName( "Gelado de Nozes 1" );
    product1.setPrice( 22.2D );
    product1.setDescription( "Gelado de Nozes Pecã 1" );
    product1.setStockQuantity( 120 );
    product1.setPhotoUrl( "SomeUrl" );
    
    product2.setName( "Gelado de Nozes 3" );
    product2.setPrice( 32.2D );
    product2.setDescription( "Gelado de Nozes Pecã 3" );
    product2.setStockQuantity( 1200 );
    product2.setPhotoUrl( "SomeUrl" );
    
    product = productRepository.save( product );
    product1 = productRepository.save( product1 );
    product2 = productRepository.save( product2 );
    
    order0DTO = new OrderDTO();
    order0DTO.setAddressId( address.getId() );
    order0DTO.setUserId( u.getId() );
    order0DTO.setOrderedProductsList( List.of( new OrderedProductDTO( 11, product.getId() ),
      new OrderedProductDTO( 10, product1.getId() ) ) );
    
    DeliverySystemService.setHost( new StringBuilder( "http://localhost:" ).append( mockBackEnd.getPort() ) );
  }
  
  @AfterEach
  void resetDb() {
    
    orderedProductRepository.deleteAll();
    orderedProductRepository.flush();
    
    
    orderRepository.deleteAll();
    orderRepository.flush();
    
    productRepository.deleteAll();
    productRepository.flush();
    
    userRepository.deleteAll();
    userRepository.flush();
    
    addressRepository.deleteAll();
    addressRepository.flush();
    
  }
  
  @Test
  void testMakeAnOrderWithAValidDTOAndDeliverySystemIsOk_ThenReturnOrder() throws JsonProcessingException {
    OrderDelivera orderDelivera = new OrderDelivera();
    orderDelivera.setId( 10L );
    
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString( orderDelivera );
    
    mockBackEnd.enqueue(
      new MockResponse().setBody( json ).setResponseCode( 201 ).addHeader( "Content-Type",
        ContentType.JSON )
    );
    RestAssured.given()
               .contentType( "application/json" )
               .body( order0DTO )
               .post( createURL() + "/api/v1/order" )
               .then().statusCode( 201 ).log().body()
               .body( "totalPrice",
                 equalTo( ( Double.valueOf( 11 * product.getPrice() + 10 * product1.getPrice() ) ).floatValue() ) )
               .body( "orderState", equalTo( "ordered" ) )
               .body( "externalId", equalTo( 10 ) );
  }
  
  
  @Test
  void testMakeAnOrderWithAValidDTOAndDeliverySystemIsNotOk_ThenReturnNull(){
    mockBackEnd.enqueue(
      new MockResponse().setResponseCode( 400 ).addHeader( "Content-Type",
        ContentType.JSON )
    );
    RestAssured.given()
               .contentType( "application/json" )
               .body( order0DTO )
               .post( createURL() + "/api/v1/order" )
               .then().statusCode( HttpStatus.BAD_REQUEST.value() ).log();
  }
  
  @Test
  void testUpdateOrderStateInvalidOrder_ThenReturnBadRequest() {
    
    RestAssured.given()
               .contentType( "application/json" )
               .put( createURL() + "/api/v1/order/{orderId}", - 1L )
               .then()
               .statusCode( 400 );
    
    
  }
  
  @Test
  void testUpdateOrderStateOrderedToInTransit_ThenReturnOk() {
    System.out.println( "ORDER ID: " + o1.getId() );
    RestAssured.given()
               .contentType( "application/json" )
               .put( createURL() + "/api/v1/order/{orderId}", o1.getId() )
               .then()
               .statusCode( 200 )
               .body( "orderState", equalTo( "in transit" ) );
    userOrders.get( 0 ).setOrderState( "in transit" );
    
    RestAssured.given().contentType( ContentType.JSON ).put( createURL() + "/api/v1/order/{orderId}",
                 userOrders.get( 0 ).getId() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK.value() ).and()
               .body( "orderState", is( "in transit" ) );
    
  }
  
  @Test
  void testUpdateOrderStateInTransitToDelivered_ThenReturnOk() {
    u.getOrder().iterator().next().setOrderState( "in transit" );
    orderRepository.save( u.getOrder().iterator().next() );
    orderRepository.flush();
    RestAssured.given().contentType( ContentType.JSON ).put( createURL() + "/api/v1/order/{orderId}",
                 userOrders.get( 0 ).getId() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK.value() ).and()
               .body( "orderState", is( "delivered" ) );
    
    
  }
  
  @Test
  void testUpdateOrderStateInvalidState_ThenReturnBadRequest() {
    u.getOrder().iterator().next().setOrderState( "asdf" );
    orderRepository.save( u.getOrder().iterator().next() );
    orderRepository.flush();
    System.out.println();
    RestAssured.given().contentType( ContentType.JSON ).put( createURL() + "/api/v1/order/{orderId}",
                 userOrders.get( 0 ).getId() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.BAD_REQUEST.value() );
    
  }
  
  /* helpers */
  
  String createURL() {
    return "http://localhost:" + randomServerPort;
  }
  
  private User createUser( int i ) {
    User u = new User();
    u.setName( "Pedro" );
    u.setPassword( "safepassword" );
    u.setEmail( "pdfl" + i + "@ua.pt" );
    return u;
  }
  
  private Product createProduct( int i ) {
    Product p = new Product();
    p.setName( "Gelado " + i );
    p.setDescription( "This is a long description" );
    p.setPrice( 12.3 );
    p.setStockQuantity( i * 10 );
    return p;
  }
  
  private OrderedProduct createOrderedProduct( int i, Order order, Product p ) {
    OrderedProduct oP = new OrderedProduct();
    
    oP.setOrder( order );
    oP.setProduct( p );
    oP.setPrice( 12 );
    oP.setQuantity( 12 );
    
    return oP;
  }
  
  private Address createAddress( int i ) {
    Address address = new Address();
    address.setLatitude( 40.640506 + i );
    address.setLongitude( - 8.653754 + i );
    return address;
  }
  
  
  private Order createOrder( int i, Address address ) {
    
    
    List<Product> products = new ArrayList<>();
    products.add( createProduct( 1 + i ) );
    products.add( createProduct( 2 + i ) );
    
    
    Order order = new Order();
    List<OrderedProduct> orderedProductList = new ArrayList<>();
    
    orderedProductList.add( createOrderedProduct( i, order, products.get( 0 ) ) );
    orderedProductList.add( createOrderedProduct( i, order, products.get( 1 ) ) );
    
    order.setOrderedProductList( orderedProductList );
    
    order.setAddress( address );
    order.setUser( u );
    order.setTotalPrice(
      orderedProductList.stream().mapToDouble( ( OrderedProduct oo ) -> oo.getPrice() * oo.getQuantity() ).sum() );
    
    return order;
  }
  
  private OrderDTO createOrderDTO( int i ) {
    
    
    OrderDTO order = new OrderDTO();
    List<OrderedProductDTO> orderedProductList = new ArrayList<>();
    
    // create orderedProducts with the matchin ids of userOrders
    orderedProductList.add( new OrderedProductDTO( 1,
      userOrders.get( i ).getOrderedProductList().get( 0 ).getProduct().getId() ) );
    
    orderedProductList.add( new OrderedProductDTO( 1,
      userOrders.get( i ).getOrderedProductList().get( 0 ).getProduct().getId() ) );
    
    order.setOrderedProductsList( orderedProductList );
    order.setAddressId( 1l );
    
    
    return order;
  }
  
}
