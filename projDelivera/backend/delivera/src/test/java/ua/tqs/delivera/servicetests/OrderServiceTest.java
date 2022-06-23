package ua.tqs.delivera.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.datamodels.ReviewDTO;
import ua.tqs.delivera.exceptions.NoRidersAvailable;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.exceptions.OrderDoesnotExistException;
import ua.tqs.delivera.models.*;
import ua.tqs.delivera.repositories.LocationRepository;
import ua.tqs.delivera.repositories.OrderProfitRepository;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.repositories.StoreRepository;
import ua.tqs.delivera.services.OrderService;
import ua.tqs.delivera.services.RiderService;
import ua.tqs.delivera.utils.DistanceCalculator;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  
  
  private static final String ORDERED = "ordered";
  private static final String IN_TRANSIT = "in transit";
  private static final String DELIVERED = "delivered";
  @Mock(lenient = true)
  OrderRepository orderRepository; // create and manage Orders
  
  @Mock
  RiderService riderService;  // manageRiders
  
  @Mock
  StoreRepository storeRepository; // create and look for stores
  
  @Mock
  LocationRepository locationRepository; // create update find
  
  @Mock
  OrderProfitRepository orderProfitRepository;
  @InjectMocks
  OrderService orderService;
  
  OrderDTO orderDTO;
  OrderDTO orderDTO1;
  Order order;
  Order order1;
  OrderProfit orderProfit;
  OrderProfit orderProfitSaved;
  Order orderToSave;
  Order order1ToSave;
  OrderProfit orderProfit1;
  OrderProfit orderProfit1Saved;
  Store store;
  Rider rider;
  Rider sameRiderButUnavailable;
  private OrderDTO orderDto;
  private Location location;
  
  @BeforeEach
  void setUp() {
    orderDTO = new OrderDTO();
    orderDTO1 = new OrderDTO();
    order = new Order();
    order1 = new Order();
    orderToSave = new Order();
    order1ToSave = new Order();
    rider = new Rider( "pdfl@ua.pt", "Pedro", "pedro", true, new Location( 12, 33.11111 ), 1, 5 );
    sameRiderButUnavailable = new Rider( "pdfl@ua.pt", "Pedro", "pedro", false,
      new Location( 12, 33.11111 ), 1, 5 );
    orderProfit = new OrderProfit();
    orderProfit1 = new OrderProfit();
    orderProfitSaved = new OrderProfit();
    orderProfit1Saved = new OrderProfit();
    
    
    orderDTO.setOrderPrice( 192.199D );
    orderDTO.setClientLat( 11 );
    orderDTO.setClientLon( 22 );
    orderDTO.setStoreLat( 12 );
    orderDTO.setStoreLon( 33 );
    orderDTO.setStoreName( "Frostini" );
    orderDTO.setOrderStoreId( 2L );
    
    orderDTO1.setOrderPrice( 192.199D );
    orderDTO1.setClientLat( 21 );
    orderDTO1.setClientLon( 22 );
    orderDTO1.setStoreLat( 12 );
    orderDTO1.setStoreLon( 33 );
    orderDTO1.setStoreName( "Frostini" );
    orderDTO1.setOrderStoreId( 3L );
    
    store = new Store();
    store.setName( "Frostini" );
    store.setAddress( new Location( 12, 33 ) );
    
    order.setExternalId( 2L );
    order.setClientLocation( "11,22" );
    order.setStore( store );
    
    order1.setExternalId( 3L );
    order1.setClientLocation( "21,22" );
    order1.setStore( store );
    
    
    orderProfit.setOrder( order );
    orderProfit.setOrderPrice(
      DistanceCalculator.distanceBetweenPointsOnEarth( store.getAddress(), new Location( orderDTO1.getClientLat(),
        orderDTO.getClientLon() ) ) * 5 / 100 * 2 * .15 * orderDTO.getOrderPrice() );
    
    orderProfit1.setOrder( order1 );
    orderProfit1.setOrderPrice(
      DistanceCalculator.distanceBetweenPointsOnEarth( store.getAddress(),
        new Location( orderDTO1.getClientLat(), orderDTO.getClientLon() ) ) *
        orderDTO1.getClientLon() * 5 / 100 * 2 * .15 * orderDTO.getOrderPrice() );
    
    
    orderProfitSaved.setOrder( order );
    orderProfitSaved.setRider( sameRiderButUnavailable );
    orderProfitSaved.setOrderPrice( orderProfit.getOrderPrice() );
    orderProfit.setOrderPrice(
      DistanceCalculator.distanceBetweenPointsOnEarth( store.getAddress(), new Location( orderDTO1.getClientLat(),
        orderDTO.getClientLon() ) ) * 5 / 100 * 2 * .15 * orderDTO.getOrderPrice() );
    
    
    orderToSave.setExternalId( 2L );
    orderToSave.setClientLocation( "11,22" );
    orderToSave.setStore( store );
    orderToSave.setOrderProfit( orderProfitSaved );
    orderToSave.setClientLocation( "11,22" );
    
    order1ToSave.setExternalId( 3L );
    order1ToSave.setClientLocation( "21,22" );
    order1ToSave.setStore( store );
    order1ToSave.setOrderProfit( orderProfit1 );
    order1ToSave.setClientLocation( "11,22" );
    
    location = new Location( 40.85, 25.9999 );
    
    store = new Store();
    store.setName( "Frostini" );
    store.setAddress( location );
    
    Long orderMadeTimestamp = System.currentTimeMillis();
    
    orderDto = new OrderDTO();
    orderDTO.setOrderStoreId( 2L );
    orderDTO.setClientLat( 40.98 );
    orderDTO.setClientLon( - 8.2345 );
    orderDTO.setStoreName( store.getName() );
    orderDTO.setOrderPrice( 143D );
    order = new Order();
    order.setClientLocation( "40.9800,-8.2345" );
    order.setExternalId( 2L );
    order.setId( 1L );
    order.setOrderMadeTimestamp( orderMadeTimestamp );
    order.setStore( store );
    //order.setOrderState("delivered");
    
    store.setOrders( List.of( order, order1 ) );
  }
  
  @Test
  void testStoreIsFoundWithCorrectAddressAndRiderIsAvailableThenReturnCreatedOrder() throws NoRidersAvailable {
    
    when( storeRepository.findByName( "Frostini" ) ).thenReturn( Optional.of( store ) );
    when( riderService.findClosestRider( store.getAddress() ) ).thenReturn( rider );
    when( riderService.makeRiderUnavailable( rider ) ).thenReturn( sameRiderButUnavailable );
    when( orderRepository.save( any() ) ).thenReturn( orderToSave );
    when(
      orderProfitRepository.save( any() )
    ).thenReturn( orderProfitSaved );
    
    Order finalOrder = orderService.assignOrder( orderDTO );
    assertThat( finalOrder.getOrderProfit() ).isEqualTo( orderProfitSaved );
    assertThat( finalOrder.getStore() ).isEqualTo( store );
    assertThat( finalOrder.getOrderProfit().getRider() ).isEqualTo( sameRiderButUnavailable );
    assertThat( finalOrder.getExternalId() ).isEqualTo( orderDTO.getOrderStoreId() );
    
    
    verify( storeRepository, times( 1 ) ).findByName( "Frostini" );
    verify( riderService, times( 1 ) ).findClosestRider( store.getAddress() );
    verify( riderService, times( 1 ) ).makeRiderUnavailable( rider );
    verify( orderRepository, times( 1 ) ).save( any() );
    verify( orderProfitRepository, times( 1 ) ).save( any() );
  }
  
  /*
   * test store findByName but address is incorrect
   */
  
  @Test
  void testStoreIsFoundButAddressIsIncorrectAndRiderIsAvailableThenReturnCreatedOrder() throws NoRidersAvailable {
    Location tempLocation = store.getAddress();
    when( storeRepository.findByName( "Frostini" ) ).thenReturn( Optional.of( store ) );
    when( riderService.findClosestRider( store.getAddress() ) ).thenReturn( rider );
    when( riderService.makeRiderUnavailable( rider ) ).thenReturn( sameRiderButUnavailable );
    when( orderRepository.save( any() ) ).thenReturn( orderToSave );
    when(
      orderProfitRepository.save( any() )
    ).thenReturn( orderProfitSaved );
    
    orderDTO.setStoreLat( tempLocation.getLatitude() );
    orderDTO.setStoreLon( tempLocation.getLongitude() );
    
    store.setAddress( new Location( 11, 22 ) );
    
    Order finalOrder = orderService.assignOrder( orderDTO );
    assertThat( finalOrder.getOrderProfit() ).isEqualTo( orderProfitSaved );
    assertThat( finalOrder.getStore() ).isEqualTo( store );
    assertThat( finalOrder.getOrderProfit().getRider() ).isEqualTo( sameRiderButUnavailable );
    assertThat( finalOrder.getExternalId() ).isEqualTo( orderDTO.getOrderStoreId() );
    
    
    verify( storeRepository, times( 1 ) ).findByName( "Frostini" );
    verify( riderService, times( 1 ) ).findClosestRider( store.getAddress() );
    verify( riderService, times( 1 ) ).makeRiderUnavailable( rider );
    verify( orderRepository, times( 1 ) ).save( any() );
    verify( orderProfitRepository, times( 1 ) ).save( any() );
  }
  
  /*
   * test store findByName with empty
   */
  
  @Test
  void testStoreIsNotFoundAndRiderIsAvailableThenReturnCreatedOrder() throws NoRidersAvailable {
    when( storeRepository.findByName( "Frostini" ) ).thenReturn( Optional.empty() );
    when( storeRepository.save( any() ) ).thenReturn( store );
    when( riderService.findClosestRider( store.getAddress() ) ).thenReturn( rider );
    when( riderService.makeRiderUnavailable( rider ) ).thenReturn( sameRiderButUnavailable );
    when( orderRepository.save( any() ) ).thenReturn( orderToSave );
    when(
      orderProfitRepository.save( any() )
    ).thenReturn( orderProfitSaved );
    
    Order finalOrder = orderService.assignOrder( orderDTO );
    assertThat( finalOrder.getOrderProfit() ).isEqualTo( orderProfitSaved );
    assertThat( finalOrder.getStore() ).isEqualTo( store );
    assertThat( finalOrder.getOrderProfit().getRider() ).isEqualTo( sameRiderButUnavailable );
    assertThat( finalOrder.getExternalId() ).isEqualTo( orderDTO.getOrderStoreId() );
    
    
    verify( storeRepository, times( 1 ) ).findByName( "Frostini" );
    verify( riderService, times( 1 ) ).findClosestRider( store.getAddress() );
    verify( storeRepository, times( 1 ) ).save( any() );
    verify( riderService, times( 1 ) ).makeRiderUnavailable( rider );
    verify( orderRepository, times( 1 ) ).save( any() );
    verify( orderProfitRepository, times( 1 ) ).save( any() );
  }
  
  /*
   * test No available Riders
   */
  
  @Test
  void testNoRidersAvailable() throws NoRidersAvailable {
    when( storeRepository.findByName( "Frostini" ) ).thenReturn( Optional.of( store ) );
    when( riderService.findClosestRider( store.getAddress() ) ).thenThrow( NoRidersAvailable.class );
    
    Order finalOrder = orderService.assignOrder( orderDTO );
    assertNull( finalOrder );
    
    
    verify( storeRepository, times( 1 ) ).findByName( "Frostini" );
    verify( riderService, times( 1 ) ).findClosestRider( store.getAddress() );
  }
  
  @Test
  void whenReviewOrderAndEverythingOkay_ThenReturnTrue() throws NonExistentResource, OrderDoesnotExistException {
    Mockito.when( orderRepository.findById( Mockito.anyLong() ) ).thenReturn( Optional.ofNullable( order ) );
    Mockito.when( riderService.reviewRider( Mockito.anyLong(), Mockito.anyDouble() ) ).thenReturn( rider );
    
    assertTrue( orderService.reviewOrder( order.getId(), new ReviewDTO( 1.0 ) ) );
    
    Mockito.verify( riderService, Mockito.times( 1 ) ).reviewRider( Mockito.anyLong(), Mockito.anyDouble() );
    Mockito.verify( orderRepository, Mockito.times( 1 ) ).findById( Mockito.anyLong() );
  }
  
  @Test
  void whenReviewOrderAndRiderDoesNoExist_ThenPropagateException() throws NonExistentResource {
    Mockito.when( orderRepository.findById( Mockito.anyLong() ) ).thenReturn( Optional.ofNullable( order ) );
    Mockito.when( riderService.reviewRider( Mockito.anyLong(), Mockito.anyDouble() ) )
           .thenThrow( NonExistentResource.class );
    
    assertThrows( NonExistentResource.class, () -> orderService.reviewOrder( order.getId(), new ReviewDTO( 3D ) ) );
    
    Mockito.verify( riderService, Mockito.times( 1 ) ).reviewRider( Mockito.anyLong(), Mockito.anyDouble() );
    Mockito.verify( orderRepository, Mockito.times( 1 ) ).findById( Mockito.anyLong() );
  }
  
  @Test
  void whenReviewOrderAndOrderDoesNoExist_ThenThrowExeption() throws OrderDoesnotExistException, NonExistentResource {
    Mockito.when( orderRepository.findById( Mockito.anyLong() ) ).thenReturn( Optional.empty() );
    
    assertThrows( OrderDoesnotExistException.class,
      () -> orderService.reviewOrder( order.getId(), new ReviewDTO( 3D ) ) );
    
    Mockito.verify( orderRepository, Mockito.times( 1 ) ).findById( Mockito.anyLong() );
  }
  
  @Test
  void whenUpdateOrderStateWithExistentId_ThenReturnTrue() throws NonExistentResource {
    Mockito.when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.of( order ) );
    
    boolean statusUpdated = orderService.updateOrderState( order.getId() );
    assertTrue( statusUpdated );
    verifyFindByIdIsCalledOnce();
  }
  
  @Test
  void whenUpdateOrderStateWithInvalidIdOrInexistenId_ThenReturnTrue() throws NonExistentResource {
    Mockito.when( orderRepository.findById( anyLong() ) ).thenReturn( Optional.empty() );
    
    assertThrows( NonExistentResource.class, () ->
      orderService.updateOrderState( - 1L )
    );
    
    verifyFindByIdIsCalledOnce();
  }
  
  @Test
  void testChangeState() throws Exception {
    assertEquals( ORDERED, orderService.changeState( null ) );
    assertEquals( IN_TRANSIT, orderService.changeState( ORDERED ) );
    assertEquals( DELIVERED, orderService.changeState( IN_TRANSIT ) );
    assertEquals( DELIVERED, orderService.changeState( DELIVERED ) );
    assertEquals( ORDERED, orderService.changeState( null ) );
    assertThrows( Exception.class, () -> orderService.changeState( "anyString" ) );
  }
  
  private void verifyFindByIdIsCalledOnce() {
    Mockito.verify( orderRepository, times( 1 ) ).findById( any() );
  }
  
  
}
