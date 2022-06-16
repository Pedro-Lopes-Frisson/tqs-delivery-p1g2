package ua.tqs.frostini.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.frostini.datamodels.OrderDTO;

import static org.hamcrest.MatcherAssert.assertThat;

import ua.tqs.frostini.datamodels.OrderedProductDTO;
import ua.tqs.frostini.models.*;
import ua.tqs.frostini.repositories.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertNull;
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

  @BeforeEach void setUp() {
    p1 = createProduct( 1 );
    p2 = createProduct( 2 );
    p3 = createProduct( 3 );
    p4 = createProduct( 4 );

    userOrders = new ArrayList<>();
    userOrders.add( createOrder( 1, Arrays.asList( p1, p2 ) ) );
    userOrders.add( createOrder( 2, Arrays.asList( p3, p4 ) ) );

    order0DTO = createOrderDTO( 1, Arrays.asList( p1, p2 ) );
    order1DTO = createOrderDTO( 2, Arrays.asList( p3, p4 ) );
    address = createAddress( 1, u );
    when( addressRepository.findById( anyLong() ) ).thenReturn( Optional.of( address ) );
  }

  @Test
  void whenValidOrderDTOObjectThen_OrderShouldBePlaced_AndANewOrderShouldBeReturned() {
    when( userRepository.findById( order0DTO.getUserId() ) ).thenReturn( Optional.of( u ) );

    when( productRepository.findById( p1.getId() ) ).thenReturn( Optional.of( p1 ) );
    when( productRepository.findById( p2.getId() ) ).thenReturn( Optional.of( p2 ) );

    when( orderRepository.save( any() ) ).thenReturn( userOrders.get( 0 ) );

    Order orderPlaced = orderService.placeOrder( order0DTO );

    assertThat( orderPlaced.getTotalPrice(),
      equalTo( 12 * 12.3 + 12 * 12.3 ) );

    assertThat( orderPlaced.getOrderedProductList().stream().map( ( o1 ) -> o1.getProduct().getName() ).collect(
        Collectors.toList() ),
      hasItems( p1.getName(), p2.getName() ) );

    verify( userRepository, times( 1 ) ).findById( order0DTO.getUserId() );
    verify( addressRepository, times( 1 ) ).findById( any() );

    verify( productRepository, times( 1 ) ).findById( p1.getId() );
    verify( productRepository, times( 1 ) ).findById( p2.getId() );


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
    address.setLongitude( -8.653754 + i );
    return address;
  }


  private Order createOrder( int i, List<Product> products ) {


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
