package ua.tqs.frostini.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs.frostini.datamodels.OrderDTO;
import ua.tqs.frostini.datamodels.OrderedProductDTO;
import ua.tqs.frostini.datamodels.ReviewDTO;
import ua.tqs.frostini.exceptions.IncompleteOrderPlacement;
import ua.tqs.frostini.exceptions.IncompleteOrderReviewException;
import ua.tqs.frostini.exceptions.ResourceNotFoundException;
import ua.tqs.frostini.models.*;
import ua.tqs.frostini.service.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(value = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {
  
  User u = createUser( 1 );
  List<Order> userOrders;
  
  OrderDTO order0DTO;
  OrderDTO order1DTO;
  OrderDTO order2DTO;
  
  @Autowired
  private MockMvc mvc;
  
  @MockBean
  private OrderService orderService;
  
  
  @BeforeEach
  void setUp() throws IOException {
    RestAssuredMockMvc.mockMvc( mvc );
    userOrders = new ArrayList<>();
    userOrders.add( createOrder( 1 ) );
    userOrders.add( createOrder( 2 ) );
    userOrders.add( createOrder( 3 ) );
    userOrders.add( createOrder( 4 ) );
    
    order0DTO = createOrderDTO( 1 );
    order1DTO = createOrderDTO( 2 );
    order2DTO = createOrderDTO( 3 );
    
  }
  
  @Test
  void testWhenReviewingOrderIfSucceedReturn200() throws IncompleteOrderReviewException, ResourceNotFoundException {
    when(orderService.reviewOrder( anyLong(),any() )).thenReturn(200);
    given().body( new ReviewDTO( 5.0 ) ).contentType( ContentType.JSON ).put( "/api/v1/order/review/{orderId}", 1 )
           .then().log().body().assertThat()
           .status( HttpStatus.OK );
    verify( orderService, times( 1 ) ).reviewOrder( anyLong(), any() );
  }
  
  @Test
  void testWhenReviewingOrderIfOrderIdWasNotFoundTReturnBad_Request() throws IncompleteOrderReviewException,
    ResourceNotFoundException {
    doThrow( ResourceNotFoundException.class ).when( orderService ).reviewOrder( anyLong(), any() );
    
    given().body( new ReviewDTO( 5.0 ) ).contentType( ContentType.JSON ).put( "/api/v1/order/review/{orderId}", 1 )
           .then().log().body().assertThat()
           .status( HttpStatus.BAD_REQUEST );
    verify( orderService, times( 1 ) ).reviewOrder( anyLong(), any() );
  }
  
  @Test
  void testWhenReviewingOrderServerFailedThenReturnPreConditionFailed() throws IncompleteOrderReviewException,
    ResourceNotFoundException {
    doThrow( IncompleteOrderReviewException.class ).when( orderService ).reviewOrder( anyLong(), any() );
    
    given().body( new ReviewDTO( 5.0 ) ).contentType( ContentType.JSON ).put( "/api/v1/order/review/{orderId}", 1 )
           .then().log().body().assertThat()
           .status( HttpStatus.EXPECTATION_FAILED );
    verify( orderService, times( 1 ) ).reviewOrder( anyLong(), any() );
  }
  
  @Test
  void testGetAllOrdersByValidUser_ThenReturnAListOfOrders() {
    when( orderService.getAllOrdersByUser( anyLong() ) ).thenReturn( userOrders );
    
    given().get( "/api/v1/order/user/{userId}", u.getId() )
           .then().log().body().assertThat()
           .contentType( ContentType.JSON ).and()
           .body( "size()", is( 4 ) )
           .body( "[0].totalPrice", is( userOrders.get( 0 ).getTotalPrice().floatValue() ) );
    ;
    
    verify( orderService, times( 1 ) ).getAllOrdersByUser( anyLong() );
  }
  
  @Test
  void testGetAllOrdersByInvalidUser_ThenReturnResourceNotFound() {
    when( orderService.getAllOrdersByUser( 2000 ) ).thenReturn( new ArrayList<Order>() );
    given().get( "/api/v1/order/user/{userId}", 2000 )
           .then().log().body().assertThat().status( HttpStatus.BAD_REQUEST );
    
    verify( orderService, times( 1 ) ).getAllOrdersByUser( 2000 );
  }
  
  @Test
  void testGetOrderByValidId_ThenReturnCorrectOrder() {
    when( orderService.trackOrder( anyLong() ) ).thenReturn( userOrders.get( 0 ) );
    
    given().get( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
           .then().log().body().assertThat()
           .contentType( ContentType.JSON ).and()
           .status( HttpStatus.OK ).and()
           .body( "orderedProductList.size()", is( 2 ) ).and()
           .body( "totalPrice", is( userOrders.get( 0 ).getTotalPrice().floatValue() ) )
    ;
    
    verify( orderService, times( 1 ) ).trackOrder( anyLong() );
  }
  
  
  @Test
  void testGetOrderByInvalidId_ThenReturnBadRequest() {
    when( orderService.trackOrder( anyLong() ) ).thenReturn( null );
    
    given().get( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
           .then().log().body().assertThat()
           .status( HttpStatus.BAD_REQUEST );
    
    verify( orderService, times( 1 ) ).trackOrder( anyLong() );
  }
  
  
  @Test
  void testMakeOrderWithValidRequestBody_ThenReturnCreatedAndTheOrderThatHasBeenPlaced()
    throws IncompleteOrderPlacement {
    when( orderService.placeOrder( any() ) ).thenReturn( userOrders.get( 0 ) );
    
    System.out.println( order0DTO );
    given().contentType( ContentType.JSON ).body( order0DTO ).post( "/api/v1/order" )
           .then().log().body().assertThat()
           .contentType( ContentType.JSON ).and()
           .status( HttpStatus.CREATED ).and()
           .body( "orderedProductList.size()", is( 2 ) ).and()
           .body( "totalPrice", is( userOrders.get( 0 ).getTotalPrice().floatValue() ) )
    ;
    verify( orderService, times( 1 ) ).placeOrder( any() );
  }
  
  
  @Test
  void testMakeOrderWithInvalidRequestBody_ThenReturnBADREQUEST() throws IncompleteOrderPlacement {
    when( orderService.placeOrder( any() ) ).thenReturn( null );
    
    given().contentType( ContentType.JSON ).body( "IncorrectBody" ).post( "/api/v1/order" )
           .then().log().body().assertThat()
           .status( HttpStatus.BAD_REQUEST );
    
    
    verify( orderService, times( 0 ) ).placeOrder( any() );
  }
  
  @Test
  void testUpdateOrderStateInvalidOrder_ThenReturnBadRequest() {
    when( orderService.updateOrderState( anyLong() ) ).thenReturn( null );
    
    given().contentType( ContentType.JSON ).put( "/api/v1/order/{orderId}", - 1l )
           .then().log().body().assertThat()
           .status( HttpStatus.BAD_REQUEST );
    
    
    verify( orderService, times( 1 ) ).updateOrderState( anyLong() );
  }
  
  @Test
  void testUpdateOrderStateOrderedToInTransit_ThenReturnOk() {
    userOrders.get( 0 ).setOrderState( "in transit" );
    when( orderService.updateOrderState( anyLong() ) ).thenReturn( userOrders.get( 0 ) );
    
    given().contentType( ContentType.JSON ).put( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
           .then().log().body().assertThat()
           .status( HttpStatus.OK ).and()
           .body( "orderState", is( "in transit" ) );
    
    
    verify( orderService, times( 1 ) ).updateOrderState( anyLong() );
  }
  
  @Test
  void testUpdateOrderStateInTransitToDelivered_ThenReturnOk() {
    userOrders.get( 0 ).setOrderState( "delivered" );
    when( orderService.updateOrderState( anyLong() ) ).thenReturn( userOrders.get( 0 ) );
    
    given().contentType( ContentType.JSON ).put( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
           .then().log().body().assertThat()
           .status( HttpStatus.OK ).and()
           .body( "orderState", is( "delivered" ) );
    
    
    verify( orderService, times( 1 ) ).updateOrderState( anyLong() );
  }
  
  @Test
  void testUpdateOrderStateInvalidState_ThenReturnBadRequest() {
    when( orderService.updateOrderState( anyLong() ) ).thenReturn( null );
    
    given().contentType( ContentType.JSON ).put( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
           .then().log().body().assertThat()
           .status( HttpStatus.BAD_REQUEST );
    
    
    verify( orderService, times( 1 ) ).updateOrderState( anyLong() );
  }
  
  /* helpers */
  
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
  
  private Address createAddress( int i, User u ) {
    Address address = new Address();
    address.setLatitude( 40.640506 + i );
    address.setLongitude( - 8.653754 + i );
    return address;
  }
  
  
  private Order createOrder( int i ) {
    
    
    Address address = createAddress( i, u );
    
    
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
