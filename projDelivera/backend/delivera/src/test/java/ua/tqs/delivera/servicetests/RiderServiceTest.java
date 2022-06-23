package ua.tqs.delivera.servicetests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.delivera.datamodels.RiderDTO;
import ua.tqs.delivera.exceptions.NoRidersAvailable;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.exceptions.RiderLoginWrongPasswordException;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.OrderProfitRepository;
import ua.tqs.delivera.repositories.RiderRepository;
import ua.tqs.delivera.services.RiderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RiderServiceTest {
  @Mock(lenient = true)
  private RiderRepository riderRepo;
  
  @Mock(lenient = true)
  private OrderProfitRepository orderProfitRepo;
  
  @InjectMocks
  private RiderService riderService;
  
  private Rider rider;
  private Rider riderSameEmail;
  private Location location;
  
  private Order order;
  private Order order1;
  
  private OrderProfit orderProfit;
  private OrderProfit orderProfit1;
  RiderDTO riderDTO;
  
  List<Order> orderList;
  List<OrderProfit> orderProfitList;
  
  @BeforeEach
  public void setUp() {
    location = new Location( 40.85, 25.9999 );
    rider = new Rider( "mal@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0 );
    rider.setLocation( 89.5566, 5.333 );
    riderSameEmail = new Rider( "mal@gmail.com", "Maria Alberta", "migant", true, location, 0, 0 );
    
    rider.setRiderId( 1L );
    orderProfitList = new ArrayList<>();
    orderList = new ArrayList<>();
    
    
    riderDTO = new RiderDTO( "ma@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0 );
    order = new Order();
    order.setClientLocation( "Aveiro, Rua da Pega" );
    order.setExternalId( 2L );
    order.setId( 1L );
    
    
    order1 = new Order();
    order1.setClientLocation( "Aveiro, Rua da Pega" );
    order1.setExternalId( 3L );
    order1.setId( 2L );
    order1.setOrderState( "delivered" );
    
    
    orderProfit = new OrderProfit();
    orderProfit.setOrder( order );
    orderProfit.setOrderPrice( 12.1 );
    
    orderProfit.setRider( rider );
    
    
    orderProfit1 = new OrderProfit();
    orderProfit1.setOrder( order1 );
    orderProfit1.setOrderPrice( 122.1 );
    
    orderProfit1.setRider( rider );
    
    orderList.add( order );
    orderList.add( order1 );
    orderProfitList.add( orderProfit );
    orderProfitList.add( orderProfit1 );
    
    
    rider.setOrderProfits( orderProfitList.subList( 0, 1 ) );
    
    when( riderRepo.save( rider ) ).thenReturn( rider );
  }
  
  
  @Test
  void whenListingAllOrdersForAnExistentRider_ThenReturnCorrectListOfOrders() throws NonExistentResource {
    when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.of( rider ) );
    
    List<Order> actualList = riderService.getAllOrdersForRider( (long) rider.getRiderId() );
    
    assertThat( actualList ).isEqualTo( orderList.subList( 0, 1 ) );
    
    verify( riderRepo, times( 1 ) ).findById( anyLong() );
  }
  
  @Test
  void whenListingAllOrdersForAnNonExistentRider_ThenThrowException() throws NonExistentResource {
    when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.empty() );
    
    
    assertThrows( NonExistentResource.class, () -> {
      riderService.getAllOrdersForRider( (long) - 1 );
    } );
    
    verify( riderRepo, times( 1 ) ).findById( anyLong() );
  }
  
  @Test
  void whenFindByInvalidRiderId_ThenThrowException() {
    when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.empty() );
    
    assertThrows( NonExistentResource.class, () -> riderService.findById( - 1 ) );
    
    verify( riderRepo, times( 1 ) ).findById( anyLong() );
  }
  
  
  @Test
  void whenFindByValidRiderId_ThenReturnRider() throws NonExistentResource {
    when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.ofNullable( rider ) );
    
    assertThat( riderService.findById( 1 ) ).isEqualTo( rider );
    
    verify( riderRepo, times( 1 ) ).findById( anyLong() );
  }
  
  @Test
  void whenValidOrderIDAndValidRiderIDThenReturnOrder() throws NonExistentResource {
    when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.ofNullable( rider ) );
    
    Order orderActual = riderService.getOrder( rider.getRiderId(), order.getId() );
    assertThat( orderActual ).isEqualTo( order );
    
    verify( riderRepo, times( 1 ) ).findById( anyLong() );
  }
  
  @Test
  void whenInvalidOrderIDAndValidRiderIDThenThrowNonExistentResource() throws NonExistentResource {
    when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.ofNullable( rider ) );
    
    assertThrows( NonExistentResource.class, () -> riderService.getOrder( rider.getRiderId(), - 1L ) );
    
    verify( riderRepo, times( 1 ) ).findById( anyLong() );
  }
  
  
  @Test
  void whenInvalidRiderIDThenThrowNonExistentResource() throws NonExistentResource {
    when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.empty() );
    
    
    assertThrows( NonExistentResource.class, () -> riderService.getOrder( rider.getRiderId(), - 1L ) );
    
    verify( riderRepo, times( 1 ) ).findById( anyLong() );
  }
  
  @Test
  void whenGetRiderStatsWithZeroReviews_thenReturnMap() throws NonExistentResource {
    when( riderRepo.findById( any() ) ).thenReturn( Optional.of( rider ) );
    when( orderProfitRepo.findByRider( any() ) )
      .thenReturn( Optional.of( Arrays.asList( orderProfit, orderProfit1 ) ) );
    rider.setRiderId( 1l );
    riderService.saveRider( rider );
    
    Map<String, Object> stats = riderService.getRiderStatistics( rider.getRiderId() );
    Map<String, Object> expected = new HashMap<String, Object>();
    expected.put( "averageReviewValue", 0.0 );
    expected.put( "totalRiderOrders", 2 );
    expected.put( "totalNumberOfOrdersDelivered", 1 );
    
    assertThat( stats ).isEqualTo( expected );
    verify( riderRepo, VerificationModeFactory.times( 1 ) ).findById( any() );
    verify( orderProfitRepo, VerificationModeFactory.times( 1 ) ).findByRider( any() );
  }
  
  @Test
  void whenGetRiderStats_thenReturnMap() throws NonExistentResource {
    when( riderRepo.findById( any() ) ).thenReturn( Optional.of( rider ) );
    when( orderProfitRepo.findByRider( any() ) )
      .thenReturn( Optional.of( Arrays.asList( orderProfit, orderProfit1 ) ) );
    rider.setRiderId( 1l );
    rider.setNumberOfReviews( 10 );
    rider.setSumOfReviews( 35 );
    riderService.saveRider( rider );
    
    Map<String, Object> stats = riderService.getRiderStatistics( rider.getRiderId() );
    Map<String, Object> expected = new HashMap<String, Object>();
    expected.put( "averageReviewValue", (double) rider.getSumOfReviews() / rider.getNumberOfReviews() );
    expected.put( "totalRiderOrders", 2 );
    expected.put( "totalNumberOfOrdersDelivered", 1 );
    
    assertThat( stats ).isEqualTo( expected );
    verify( riderRepo, VerificationModeFactory.times( 1 ) ).findById( any() );
    verify( orderProfitRepo, VerificationModeFactory.times( 1 ) ).findByRider( any() );
  }
  
  @Test
  void whenGetRiderWithoutOrders_thenReturnMap() throws NonExistentResource {
    Rider rider1 = new Rider();
    when( riderRepo.findById( any() ) ).thenReturn( Optional.of( rider1 ) );
    when( orderProfitRepo.findByRider( any() ) ).thenReturn( Optional.of( Arrays.asList() ) );
    rider1.setRiderId( 2l );
    rider1.setNumberOfReviews( 10 );
    rider1.setSumOfReviews( 35 );
    riderService.saveRider( rider1 );
    
    Map<String, Object> stats = riderService.getRiderStatistics( rider1.getRiderId() );
    Map<String, Object> expected = new HashMap<String, Object>();
    expected.put( "averageReviewValue", (double) rider1.getSumOfReviews() / rider1.getNumberOfReviews() );
    expected.put( "totalRiderOrders", 0 );
    expected.put( "totalNumberOfOrdersDelivered", 0 );
    
    assertThat( stats ).isEqualTo( expected );
    verify( riderRepo, VerificationModeFactory.times( 1 ) ).findById( any() );
    verify( orderProfitRepo, VerificationModeFactory.times( 1 ) ).findByRider( any() );
  }
  
  @Test
  void whenGetRiderStatsWithInvalidRider_thenReturnNull() {
    when( riderRepo.findById( any() ) ).thenReturn( Optional.empty() );
    assertThrows( NonExistentResource.class, () -> {
      riderService.getRiderStatistics( 10l );
    } );
    
    verify( riderRepo, VerificationModeFactory.times( 1 ) ).findById( any() );
  }
  
  @Test
  void testMakeRiderUnavailableSetsRiderAsUnavailable() {
    Rider sameRiderButUnavailable = new Rider( "mal@gmail.com", "Manuel Antunes", "migant", false, location, 0, 0 );
    
    when( riderRepo.save( rider ) ).thenReturn( sameRiderButUnavailable );
    
    Rider riderRet = riderService.makeRiderUnavailable( rider );
    assertThat( riderRet.isAvailable() ).isFalse();
    
    verify( riderRepo, times( 1 ) ).save( rider );
  }
  
  @Test
  void testMakeRiderAvailableSetsRiderAsAvailable() {
    Rider sameRiderButUnavailable = new Rider( "mal@gmail.com", "Manuel Antunes", "migant", false, location, 0, 0 );
    
    when( riderRepo.save( sameRiderButUnavailable ) ).thenReturn( rider );
    
    Rider riderRet = riderService.makeRiderAvailable( sameRiderButUnavailable );
    assertThat( riderRet.isAvailable() ).isTrue();
    
    verify( riderRepo, times( 1 ) ).save( sameRiderButUnavailable );
  }
  
  @Test
  void testWhenAddProfitToRiderThenAddItAndUpdateRider() {
    
    riderService.addProfitToRider( rider, orderProfit1 );
    assertThat( rider.getOrderProfits() ).isEqualTo( List.of( orderProfit, orderProfit1 ) );
    verify( riderRepo, times( 1 ) ).save( any() );
  }
  
  // test only one rider is available then return rider
  @Test
  void testWhenOneRidersAvailableReturnThatRider() throws NoRidersAvailable {
    
    Rider riderAvailable1 = new Rider( "mal@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0 );
    when( riderRepo.findAll() ).thenReturn( List.of( riderAvailable1 ) );
    assertThat( riderService.findClosestRider( new Location( 12, 11 ) ) ).isEqualTo( riderAvailable1 );
  }
  
  // test more than one rider is available return the closest
  @Test
  void testWhenMultipleRidersAvailableReturnClosestOne() throws NoRidersAvailable {
    
    Rider riderAvailable1 = new Rider( "mal@gmail.com", "Manuel Antunes", "migant", true, new Location( 11, 0 ), 0, 0 );
    Rider riderAvailable2 =
      new Rider( "mal@gmail.com", "Manuel Antunes 1", "migant", true, new Location( 11.999, 10.999 )
        , 0, 0 );
    when( riderRepo.findAll() ).thenReturn( List.of( riderAvailable1, riderAvailable2 ) );
    
    assertThat( riderService.findClosestRider( new Location( 12, 11 ) ) ).isEqualTo( riderAvailable2 );
  }
  
  // test no riders throw exc
  @Test
  void testWhenNoRidersAvailableThrowException() throws NoRidersAvailable {
    
    Rider riderUnavailable1 = new Rider( "mal@gmail.com", "Manuel Antunes", "migant", false, location, 0, 0 );
    Rider riderUnavailable2 = new Rider( "mal@gmail.com", "Manuel Antunes 1", "migant", false, location, 0, 0 );
    when( riderRepo.findAll() ).thenReturn( List.of( riderUnavailable1, riderUnavailable2 ) );
    assertThrows( NoRidersAvailable.class, () -> riderService.findClosestRider( new Location( 12, 11 ) ) );
  }
  
  
  @Test
  @DisplayName("Login: rider correct credentials")
  void whenLoginRiderCorrectCredentials_thenReturnRider() throws RiderLoginWrongPasswordException, NonExistentResource {
    Mockito.when( riderRepo.findByEmail( riderDTO.getEmaildto() ) ).thenReturn( Optional.of( rider ) );
    
    
    Rider foundRider = riderService.loginRider( riderDTO );
    assertThat( foundRider ).isEqualTo( rider );
    assertThat( foundRider.getPassword() ).isEqualTo( riderDTO.getPassworddto() );
    
  }
  
  
  @Test
  @DisplayName("Login: rider wrong credentials")
  void whenLoginRiderWrongCredentials_thenReturnNull() throws RiderLoginWrongPasswordException, NonExistentResource {
    Mockito.when( riderRepo.findByEmail( riderDTO.getEmaildto() ) ).thenReturn( Optional.of( rider ) );
    riderDTO.setPassworddto( "password" );
    
    // System.out.println(riderDTO.getEmaildto());
    // System.out.println(riderDTO.getPassworddto());
    
    Rider foundRider = riderService.loginRider( riderDTO );
    assertThat( foundRider.getPassword() ).isEqualTo( "wrong credentials" );
    
  }
  
  
  @Test
  @DisplayName("Get all riders")
  void whenGetAllRiders_thenReturnList() {
    Rider rider2 = new Rider( "mf@gmail.com", "Manuel Ferreira", "migferr", true, location, 10, 29 );
    List<Rider> allRiders = Arrays.asList( rider, rider2 );
    Mockito.when( riderRepo.findAll() ).thenReturn( allRiders );
    
    List<Rider> res = riderService.getAllRiders();
    assertThat( res ).isEqualTo( allRiders );
    
  }
  
  
  @Test
  @DisplayName("Register: valid rider")
  void whenCreateValidRider_thenReturnRider() {
    
    Rider foundRider = riderService.saveRider( rider );
    assertThat( foundRider ).isEqualTo( rider );
    verifySaveRiderIsCalledOnce();
    
  }
  
  @Test
  @DisplayName("Register: email provided already used")
  void whenCreateRiderWithAlreadyUsedEmail_thenReturnRider() {
    Mockito.when( riderRepo.save( riderSameEmail ) ).thenReturn( rider );
    
    // assertThrows(expectedThrowable, runnable)
    Rider foundRider = riderService.saveRider( rider );
    assertThat( foundRider ).isEqualTo( rider );
    verifySaveRiderIsCalledOnce();
    
  }
  
  
  @Test
  @DisplayName("Login: non existing rider")
  void whenLoginNonExistingRider_thenReturnNull() throws RiderLoginWrongPasswordException, NonExistentResource {
    riderDTO.setEmail( "invalid email" );
    Mockito.when( riderRepo.findByEmail( riderDTO.getEmaildto() ) ).thenReturn( null );
    
    Rider foundRider = riderService.loginRider( riderDTO );
    System.out.println( "hello\t" + foundRider );
    assertThat( foundRider ).isNull();
    
  }
  
  void verifySaveRiderIsCalledOnce() {
    verify( riderRepo, VerificationModeFactory.times( 1 ) ).save( any() );
  }
  
  
}
