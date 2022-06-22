package ua.tqs.delivera.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.exceptions.NoRidersAvailable;
import ua.tqs.delivera.models.*;
import ua.tqs.delivera.repositories.LocationRepository;
import ua.tqs.delivera.repositories.OrderProfitRepository;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.repositories.StoreRepository;
import ua.tqs.delivera.services.OrderService;
import ua.tqs.delivera.services.RiderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  
  @Mock
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
    orderProfit.setOrderPrice( distance( store.getAddress(), new Location( orderDTO1.getClientLat(),
      orderDTO.getClientLon() ) ) * 5 / 100 * 2 * .15 * orderDTO.getOrderPrice() );
    
    orderProfit1.setOrder( order1 );
    orderProfit1.setOrderPrice(
      distance( store.getAddress(), new Location( orderDTO1.getClientLat(), orderDTO.getClientLon() ) ) *
        orderDTO1.getClientLon() * 5 / 100 * 2 * .15 * orderDTO.getOrderPrice() );
    
    
    orderProfitSaved.setOrder( order );
    orderProfitSaved.setRider( sameRiderButUnavailable );
    orderProfitSaved.setOrderPrice( orderProfit.getOrderPrice() );
    orderProfit.setOrderPrice( distance( store.getAddress(), new Location( orderDTO1.getClientLat(),
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
  
  
  public double distance( Location l1, Location l2 ) {
    
    // The math module contains a function
    // named toRadians which converts from
    // degrees to radians.
    double lon1 = Math.toRadians( l1.getLongitude() );
    double lon2 = Math.toRadians( l2.getLongitude() );
    double lat1 = Math.toRadians( l1.getLatitude() );
    double lat2 = Math.toRadians( l2.getLatitude() );
    
    // Haversine formula
    double dLon = lon2 - lon1;
    double dLat = lat2 - lat1;
    double a = Math.pow( Math.sin( dLat / 2 ), 2 )
      + Math.cos( lat1 ) * Math.cos( lat2 )
      * Math.pow( Math.sin( dLon / 2 ), 2 );
    
    double c = 2 * Math.asin( Math.sqrt( a ) );
    
    // Radius of earth in kilometers. Use 3956
    // for miles
    double r = 6371;
    
    // calculate the result
    return ( c * r );
  }
  
  @ParameterizedTest
  @MethodSource("distancesGen")
  void assertDistances( Location l1, Location l2, double dist ) {
    assertThat( orderService.distance( l1, l2 ) ).isEqualTo( dist );
  }
  
  
  public static Stream<Arguments> distancesGen() {
    return Stream.of(
      Arguments.arguments( new Location( 53.32, 53.31 ),
        new Location( - 1.72, - 1.69 ), 7943.096602114035 ),
      Arguments.arguments( new Location( 13, 14 ),
        new Location( 4, 14 ), 1000.7543398010286 ),
      Arguments.arguments( new Location( 14, 0 ),
        new Location( 10, 14 ), 1585.845077250938 )
    
    );
  }
}
