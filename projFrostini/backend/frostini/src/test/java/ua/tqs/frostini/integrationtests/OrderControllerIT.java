package ua.tqs.frostini.integrationtests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
import ua.tqs.frostini.models.Address;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.models.OrderedProduct;
import ua.tqs.frostini.models.Product;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.repositories.AddressRepository;
import ua.tqs.frostini.repositories.OrderRepository;
import ua.tqs.frostini.repositories.UserRepository;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIT {
  
  @LocalServerPort
  int randomServerPort;
  
  
  @Autowired
  AddressRepository addressRepository;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  UserRepository userRepository;
  
  Address address;
  Address address1;
  Address address2;
  Address address3;
  
  User u = createUser( 1 );
  List<Order> userOrders;
  
  OrderDTO order0DTO;
  OrderDTO order1DTO;
  OrderDTO order2DTO;
  
  Order o1;
  
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
  }
  
  @AfterEach
  void resetDb() {
    userRepository.deleteAll();
    userRepository.flush();
    orderRepository.deleteAll();
    orderRepository.flush();
    addressRepository.deleteAll();
    addressRepository.flush();
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
    
    RestAssured.given().contentType( ContentType.JSON ).put( createURL()+ "/api/v1/order/{orderId}",
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
    RestAssured.given().contentType( ContentType.JSON ).put(createURL() + "/api/v1/order/{orderId}",
                 userOrders.get( 0 ).getId() )
               .then().log().body().assertThat()
               .statusCode( HttpStatus.OK.value() ).and()
               .body( "orderState", is( "delivered" ) );
    
    
  }
  
  @Test
  void testUpdateOrderStateInvalidState_ThenReturnBadRequest() {
    u.getOrder().iterator().next().setOrderState("asdf");
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
    return "http://localhost:" + randomServerPort ;
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
