package ua.tqs.delivera.serviceTests;

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
import ua.tqs.delivera.exceptions.NonExistentResource;
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

@ExtendWith(MockitoExtension.class)
public class RiderServiceTest {
    @Mock( lenient = true)
    private RiderRepository riderRepo;

    @Mock( lenient = true)
    private OrderProfitRepository orderProfitRepo;

    @InjectMocks
    private RiderService riderService;

    private RiderDTO riderDTO;
    private Rider rider;
    private Rider riderSameEmail;
    private Location location;

    private Order order;
    private Order order1;
    
    private OrderProfit orderProfit;
    private OrderProfit orderProfit1;
    
    List<Order> orderList;
    List<OrderProfit> orderProfitList;

    @BeforeEach
    public void setUp(){
        location = new Location(40.85, 25.9999);
        rider = new Rider("mal@gmail.com","Manuel Antunes", "migant", true, location, 0, 0);
        rider.setLocation(89.5566, 5.333);
        riderSameEmail = new Rider("mal@gmail.com","Maria Alberta", "migant", true, location, 0, 0);
        riderDTO = new RiderDTO( "ma@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0 );
        
        rider.setRiderId( 1L );
        orderProfitList = new ArrayList<>();
        orderList = new ArrayList<>();
        
        
        order = new Order();
        order.setClientLocation( "Aveiro, Rua da Pega" );
        order.setExternalId( 2L );
        order.setId( 1L );
        
        
        order1 = new Order();
        order1.setClientLocation( "Aveiro, Rua da Pega" );
        order1.setExternalId( 3L );
        order1.setId( 2L );
        order1.setOrderState("delivered");
        
        
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
        
        
        rider.setOrderProfits( orderProfitList );
        
        Mockito.when( riderRepo.save( rider ) ).thenReturn( rider );
    }

    @Test
    @DisplayName("Register: valid rider")
    void whenCreateValidRider_thenReturnRider(){

        Rider foundRider = riderService.saveRider(rider);
        assertThat(foundRider).isEqualTo(rider);
        verifySaveRiderIsCalledOnce();

    }

    @Test
    @DisplayName("Register: email provided already used")
    void whenCreateRiderWithAlreadyUsedEmail_thenReturnRider(){
        Mockito.when(riderRepo.save(riderSameEmail)).thenReturn(rider);

        // assertThrows(expectedThrowable, runnable)
        Rider foundRider = riderService.saveRider(rider);
        assertThat(foundRider).isEqualTo(rider);
        verifySaveRiderIsCalledOnce();

    }

    @Test
    @DisplayName("Login: rider correct credentials")
    void whenLoginRiderCorrectCredentials_thenReturnRider(){
        Mockito.when(riderRepo.findByEmail(riderDTO.getEmaildto())).thenReturn(rider);

        Rider foundRider = riderService.loginRider(riderDTO);
        assertThat(foundRider).isEqualTo(rider);
        assertThat(foundRider.getPassword()).isEqualTo(riderDTO.getPassworddto());

    }

    @Test
    @DisplayName("Login: rider wrong credentials")
    void whenLoginRiderWrongCredentials_thenReturnNull(){
        Mockito.when(riderRepo.findByEmail(riderDTO.getEmaildto())).thenReturn(rider);
        riderDTO.setPassworddto("password");

        // System.out.println(riderDTO.getEmaildto());
        // System.out.println(riderDTO.getPassworddto());

        Rider foundRider = riderService.loginRider(riderDTO);
        assertThat(foundRider).isNull();

    }

    @Test
    @DisplayName("Login: non existing rider")
    void whenLoginNonExistingRider_thenReturnNull(){
        riderDTO.setEmail("invalid email");
        Mockito.when(riderRepo.findByEmail(riderDTO.getEmaildto())).thenReturn(null);

        Rider foundRider = riderService.loginRider(riderDTO);
        System.out.println("hello\t"+foundRider);
        assertThat(foundRider).isEqualTo(null);

    }

    @Test
    void whenListingAllOrdersForAnExistentRider_ThenReturnCorrectListOfOrders() throws NonExistentResource {
        Mockito.when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.of( rider ) );
        
        List<Order> actualList = riderService.getAllOrdersForRider( (long) rider.getRiderId() );
        
        assertThat( actualList ).isEqualTo( orderList );
        
        Mockito.verify( riderRepo, Mockito.times( 1 ) ).findById( anyLong() );
    }
    
    @Test
    void whenListingAllOrdersForAnNonExistentRider_ThenThrowException() throws NonExistentResource {
        Mockito.when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.empty() );
        
        
        assertThrows( NonExistentResource.class, () -> {
        riderService.getAllOrdersForRider( (long) - 1 );
        } );
        
        Mockito.verify( riderRepo, Mockito.times( 1 ) ).findById( anyLong() );
    }
    
    @Test
    void whenFindByInvalidRiderId_ThenThrowException() {
        Mockito.when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.empty() );
        
        assertThrows( NonExistentResource.class, () -> riderService.findById( - 1 ) );
        
        Mockito.verify( riderRepo, Mockito.times( 1 ) ).findById( anyLong() );
    }
    
    
    @Test
    void whenFindByValidRiderId_ThenReturnRider() throws NonExistentResource {
        Mockito.when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.ofNullable( rider ) );
        
        assertThat( riderService.findById( 1 ) ).isEqualTo( rider );
        
        Mockito.verify( riderRepo, Mockito.times( 1 ) ).findById( anyLong() );
    }
    
    @Test
    void whenValidOrderIDAndValidRiderIDThenReturnOrder() throws NonExistentResource {
        Mockito.when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.ofNullable( rider ) );
        
        Order orderActual = riderService.getOrder( rider.getRiderId(), order.getId() );
        assertThat( orderActual ).isEqualTo( order );
        
        Mockito.verify( riderRepo, Mockito.times( 1 ) ).findById( anyLong() );
    }
    
    @Test
    void whenInvalidOrderIDAndValidRiderIDThenThrowNonExistentResource() throws NonExistentResource {
        Mockito.when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.ofNullable( rider ) );
        
        assertThrows( NonExistentResource.class, () -> riderService.getOrder( rider.getRiderId(), - 1L ) );
        
        Mockito.verify( riderRepo, Mockito.times( 1 ) ).findById( anyLong() );
    }
    
    
    @Test
    void whenInvalidRiderIDThenThrowNonExistentResource() throws NonExistentResource {
        Mockito.when( riderRepo.findById( anyLong() ) ).thenReturn( Optional.empty() );
        
        
        assertThrows( NonExistentResource.class, () -> riderService.getOrder( rider.getRiderId(), - 1L ) );
        
        Mockito.verify( riderRepo, Mockito.times( 1 ) ).findById( anyLong() );
    }

    @Test
    void whenGetRiderStatsWithZeroReviews_thenReturnMap() throws NonExistentResource {
        Mockito.when(riderRepo.findById(Mockito.any())).thenReturn(Optional.of(rider));
        Mockito.when(orderProfitRepo.findByRider(Mockito.any())).thenReturn(Optional.of(Arrays.asList(orderProfit, orderProfit1)));
        rider.setRiderId(1l);
        riderService.saveRider(rider);

        Map<String, Object> stats = riderService.getRiderStatistics(rider.getRiderId());
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("averageReviewValue", 0.0);
        expected.put("totalRiderOrders", 2);
        expected.put("totalNumberOfOrdersDelivered", 1);

        assertThat(stats).isEqualTo(expected);
        Mockito.verify(riderRepo, VerificationModeFactory.times(1)).findById(any());
        Mockito.verify(orderProfitRepo, VerificationModeFactory.times(1)).findByRider(any());
    }

    @Test
    void whenGetRiderStats_thenReturnMap() throws NonExistentResource {
        Mockito.when(riderRepo.findById(Mockito.any())).thenReturn(Optional.of(rider));
        Mockito.when(orderProfitRepo.findByRider(Mockito.any())).thenReturn(Optional.of(Arrays.asList(orderProfit, orderProfit1)));
        rider.setRiderId(1l);
        rider.setNumberOfReviews(10);
        rider.setSumOfReviews(35);
        riderService.saveRider(rider);

        Map<String, Object> stats = riderService.getRiderStatistics(rider.getRiderId());
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("averageReviewValue", (double) rider.getSumOfReviews()/rider.getNumberOfReviews());
        expected.put("totalRiderOrders", 2);
        expected.put("totalNumberOfOrdersDelivered", 1);

        assertThat(stats).isEqualTo(expected);
        Mockito.verify(riderRepo, VerificationModeFactory.times(1)).findById(any());
        Mockito.verify(orderProfitRepo, VerificationModeFactory.times(1)).findByRider(any());
    }

    @Test
    void whenGetRiderWithoutOrders_thenReturnMap() throws NonExistentResource {
        Rider rider1 = new Rider();
        Mockito.when(riderRepo.findById(Mockito.any())).thenReturn(Optional.of(rider1));
        Mockito.when(orderProfitRepo.findByRider(Mockito.any())).thenReturn(Optional.of(Arrays.asList()));
        rider1.setRiderId(2l);
        rider1.setNumberOfReviews(10);
        rider1.setSumOfReviews(35);
        riderService.saveRider(rider1);

        Map<String, Object> stats = riderService.getRiderStatistics(rider1.getRiderId());
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("averageReviewValue", (double) rider1.getSumOfReviews()/rider1.getNumberOfReviews());
        expected.put("totalRiderOrders", 0);
        expected.put("totalNumberOfOrdersDelivered", 0);

        assertThat(stats).isEqualTo(expected);
        Mockito.verify(riderRepo, VerificationModeFactory.times(1)).findById(any());
        Mockito.verify(orderProfitRepo, VerificationModeFactory.times(1)).findByRider(any());
    }

    @Test
    void whenGetRiderStatsWithInvalidRider_thenReturnNull() {
        Mockito.when(riderRepo.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows( NonExistentResource.class, () -> {
          riderService.getRiderStatistics(10l);
        } );

        Mockito.verify(riderRepo, VerificationModeFactory.times(1)).findById(any());
    }

    private void verifySaveRiderIsCalledOnce() {
        Mockito.verify(riderRepo, VerificationModeFactory.times(1)).save(any());
    }
    
}
