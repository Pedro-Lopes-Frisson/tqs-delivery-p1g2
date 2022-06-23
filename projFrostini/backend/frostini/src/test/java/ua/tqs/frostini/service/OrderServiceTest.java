package ua.tqs.frostini.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ua.tqs.frostini.datamodels.OrderDTO;

import static org.hamcrest.MatcherAssert.assertThat;

import ua.tqs.frostini.datamodels.OrderedProductDTO;
import ua.tqs.frostini.datamodels.ReviewDTO;
import ua.tqs.frostini.exceptions.*;
import ua.tqs.frostini.models.*;
import ua.tqs.frostini.repositories.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private OrderRepository orderRepository;
  
  @Mock
  private ProductRepository productRepository;
  
  @Mock
  private OrderedProductRepository orderedProductRepository;
  
  @Mock(lenient = true)
  private AddressRepository addressRepository;
  
  @Mock
  private DeliverySystemService deliveryService;
  @InjectMocks
  private OrderService orderService;
  
  
  User u = createUser( 1 );
  List<Order> userOrders;
  OrderDTO order0DTO;
  OrderDTO order1DTO;
  Product p1;
  Product p2;
  Product p3;
  Product p4;
  Address address;
  OrderDelivera orderDelivera;
  
  Order order;
  Order order1;
  
  @BeforeEach void setUp() {
    p1 = createProduct( 1 );
    p2 = createProduct( 2 );
    p3 = createProduct( 3 );
    p4 = createProduct( 4 );
    
    order = createOrder( 1, Arrays.asList( p1, p2 ) );
    order1 = createOrder( 2, Arrays.asList( p3, p4 ) );
    order.setExternalId( 1L );
    userOrders = new ArrayList<>();
    userOrders.add( order );
    userOrders.add( order1 );
    
    userOrders.get( 0 ).getOrderedProductList().stream().map( OrderedProduct::getProduct ).map( Product::getName )
              .forEach( System.out::println );
    order0DTO = createOrderDTO( 1, Arrays.asList( p1, p2 ) );
    order1DTO = createOrderDTO( 2, Arrays.asList( p3, p4 ) );
    address = createAddress( 1, u );
    
    orderDelivera = new OrderDelivera();
    orderDelivera.setOrderMadeTimestamp( System.currentTimeMillis() );
    orderDelivera.setClientLocation(
      address.getLatitude() + "," + address.getLongitude() );
    orderDelivera.setId( 1L );
    
    when( addressRepository.findById( anyLong() ) ).thenReturn( Optional.of( address ) );
  }
  
  @Test
  void whenValidOrderDTOObjectThen_OrderShouldBePlaced_AndANewOrderShouldBeReturned()
    throws FailedToPlaceOrderException, IncompleteOrderPlacement {
    when( userRepository.findById( order0DTO.getUserId() ) ).thenReturn( Optional.of( u ) );
    
    when( productRepository.findById( p1.getId() ) ).thenReturn( Optional.of( p1 ) );
    when( productRepository.findById( p2.getId() ) ).thenReturn( Optional.of( p2 ) );
    
    when( orderRepository.save( any() ) ).thenReturn( order );
    when( deliveryService.newOrder( any() ) ).thenReturn( orderDelivera );
    
    
    assertNotNull( order.getOrderedProductList() );
    Order orderPlaced = orderService.placeOrder( order0DTO );
    assertThat( orderPlaced.getExternalId(), equalTo( orderDelivera.getId() ) );
    
    assertThat( orderPlaced.getTotalPrice(),
      equalTo( 12 * 12.3 + 12 * 12.3 ) );
    
    
    verify( userRepository, times( 1 ) ).findById( order0DTO.getUserId() );
    verify( addressRepository, times( 1 ) ).findById( any() );
    
    verify( productRepository, times( 1 ) ).findById( p1.getId() );
    verify( productRepository, times( 1 ) ).findById( p2.getId() );
    verify( deliveryService, times( 1 ) ).newOrder( any() );
    
    
    verify( orderRepository, times( 2 ) ).save( any() );
  }
  
  
  @Test
  void whenValidOrderDTOObjectButDeliverySystemFailsThenReturnIncompleteOrder()
    throws FailedToPlaceOrderException, IncompleteOrderPlacement {
    when( userRepository.findById( order0DTO.getUserId() ) ).thenReturn( Optional.of( u ) );
    
    when( productRepository.findById( p1.getId() ) ).thenReturn( Optional.of( p1 ) );
    when( productRepository.findById( p2.getId() ) ).thenReturn( Optional.of( p2 ) );
    
    when( orderRepository.save( any() ) ).thenReturn( userOrders.get( 0 ) );
    when( deliveryService.newOrder( any() ) ).thenThrow( FailedToPlaceOrderException.class );
    
    assertThrows( IncompleteOrderPlacement.class, () -> orderService.placeOrder( order0DTO ) );
    
    verify( userRepository, times( 1 ) ).findById( order0DTO.getUserId() );
    verify( addressRepository, times( 1 ) ).findById( any() );
    
    verify( productRepository, times( 1 ) ).findById( p1.getId() );
    verify( productRepository, times( 1 ) ).findById( p2.getId() );
    verify( deliveryService, times( 1 ) ).newOrder( any() );
    
    
    verify( orderRepository, times( 1 ) ).save( any() );
  }
  
  @Test
  void whenTrackOrderOfAValidOrder_ThenReturnThatOrder() {
    when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.of( userOrders.get( 0 ) ) );
    
    Order orderPlaced = orderService.trackOrder( userOrders.get( 0 ).getId() );
    assertThat( orderPlaced.getOrderedProductList(), equalTo( userOrders.get( 0 ).getOrderedProductList() ) );
    assertThat( orderPlaced.getTotalPrice(), equalTo( userOrders.get( 0 ).getTotalPrice() ) );
    
    verify( orderRepository, times( 1 ) ).findById( anyLong() );
  }
  
  
  @Test
  void whenTrackOrderOfAInvalidOrder_ThenReturnNull() {
    when( orderRepository.findById( any() ) ).thenReturn( Optional.empty() );
    
    Order orderPlaced = orderService.trackOrder( 2012104124L );
    assertNull( orderPlaced );
    
    verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void whenGetAllOrdersByAnExistentUserId_ThenReturnAllOrdersOfThatUser() {
    
    when( userRepository.findById( u.getId() ) ).thenReturn( Optional.of( u ) );
    when( orderRepository.findAllByUser( any(), any() ) ).thenReturn( userOrders );
    
    List<Order> ordersPlacedByUser = orderService.getAllOrdersByUser( u.getId() );
    assertThat( ordersPlacedByUser.stream().map( Order::getTotalPrice ).collect( Collectors.toList() ),
      equalTo( userOrders.stream().map( Order::getTotalPrice ).collect( Collectors.toList() ) ) );
    
    verify( userRepository, times( 1 ) ).findById( u.getId() );
    verify( orderRepository, times( 1 ) ).findAllByUser( any(), any() );
  }
  
  
  @Test
  void whenGetAllOrdersByAnNonExistentUserId_ThenReturnAllOrdersOfThatUser() {
    
    when( userRepository.findById( u.getId() ) ).thenReturn( Optional.empty() );
    
    List<Order> ordersPlacedByUser = orderService.getAllOrdersByUser( u.getId() );
    assertThat( ordersPlacedByUser.size(), equalTo( 0 ) );
    
    verify( userRepository, times( 1 ) ).findById( u.getId() );
  }
  
  @Test
  void testUpdateOrderStateInvalidOrder_ThenReturnNull() {
    when( orderRepository.findById( any() ) ).thenReturn( Optional.empty() );
    
    Order orderUpdated = orderService.updateOrderState( 2012104124L );
    assertNull( orderUpdated );
    
    verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void testUpdateOrderStateOrderedToInTransit_ThenReturnOrderWithUpdatedState() {
    when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.of( userOrders.get( 0 ) ) );
    
    Order orderUpdated = orderService.updateOrderState( userOrders.get( 0 ).getId() );
    assertThat( orderUpdated.getOrderState(), equalTo( "in transit" ) );
    
    verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void testUpdateOrderStateInTransitToDelivered_ThenReturnOrderWithUpdatedState() {
    userOrders.get( 0 ).setOrderState( "in transit" );
    when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.of( userOrders.get( 0 ) ) );
    
    Order orderUpdated = orderService.updateOrderState( userOrders.get( 0 ).getId() );
    assertThat( orderUpdated.getOrderState(), equalTo( "delivered" ) );
    
    verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void testUpdateOrderStateInvalidState_ThenReturnNull() {
    userOrders.get( 0 ).setOrderState( "state" );
    when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.of( userOrders.get( 0 ) ) );
    
    Order orderUpdated = orderService.updateOrderState( userOrders.get( 0 ).getId() );
    assertNull( orderUpdated );
    
    verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void testReviewOrderReturns200IfDeliverySystemReturns200()
    throws FailedToReviewOrder, IncompleteOrderReviewException, ResourceNotFoundException {
    when( deliveryService.reviewOrder( anyLong(), any() ) ).thenReturn( 200 );
    when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.of( userOrders.get( 0 ) ) );
    int status = orderService.reviewOrder( 1, new ReviewDTO( 2D ) );
    assertThat( status, equalTo( 200 ) );
    
    verify( deliveryService, times( 1 ) ).reviewOrder( anyLong(), any() );
    verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void testReviewOrderServerFailsThrowException()
    throws FailedToReviewOrder, IncompleteOrderReviewException, ResourceNotFoundException {
    when( deliveryService.reviewOrder( anyLong(), any() ) ).thenThrow( FailedToReviewOrder.class );
    when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.of( userOrders.get( 0 ) ) );
    
    assertThrows( IncompleteOrderReviewException.class, () -> orderService.reviewOrder( 1, new ReviewDTO( 1D ) ) );
    verify( deliveryService, times( 1 ) ).reviewOrder( anyLong(), any() );
    verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void testReviewOrderDoesNotExistThrow()
    throws FailedToReviewOrder, IncompleteOrderReviewException, ResourceNotFoundException {
    when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.empty() );
    assertThrows( ResourceNotFoundException.class, () -> orderService.reviewOrder( 1L, new ReviewDTO(1D) ) );
    verify( orderRepository, times( 1 ) ).findById( any() );
    verify( deliveryService, times( 0 ) ).reviewOrder( anyLong(), any() );
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
    p.setId( i );
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
    oP.setPrice( p.getPrice() );
    oP.setQuantity( 12 );
    
    return oP;
  }
  
  private Address createAddress( int i, User u ) {
    Address address = new Address();
    address.setLatitude( 40.640506 + i );
    address.setLongitude( - 8.653754 + i );
    return address;
  }
  
  
  private Order createOrder( int i, List<Product> products ) {
    
    
    Order order = new Order();
    List<OrderedProduct> orderedProductList = new ArrayList<>();
    
    orderedProductList.add( createOrderedProduct( i, order, products.get( 0 ) ) );
    orderedProductList.add( createOrderedProduct( i, order, products.get( 1 ) ) );
    
    order.setOrderedProductList( orderedProductList );
    orderedProductList.stream().map( OrderedProduct::getProduct ).forEach( System.out::println );
    
    order.setAddress( address );
    order.setUser( u );
    order.setTotalPrice(
      orderedProductList.stream().mapToDouble( ( OrderedProduct oo ) -> oo.getPrice() * oo.getQuantity() ).sum() );
    
    return order;
  }
  
  private OrderDTO createOrderDTO( int i, List<Product> productList ) {
    
    
    OrderDTO order = new OrderDTO();
    List<OrderedProductDTO> orderedProductList = new ArrayList<>();
    
    // create orderedProducts with the matchin ids of userOrders
    orderedProductList.add( new OrderedProductDTO( 1, productList.get( 0 ).getId() ) );
    orderedProductList.add( new OrderedProductDTO( 1, productList.get( 1 ).getId() ) );
    
    
    order.setOrderedProductsList( orderedProductList );
    order.setAddressId( 1l );
    
    
    return order;
  }
}
