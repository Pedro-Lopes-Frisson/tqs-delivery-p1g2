package ua.tqs.delivera.controllerTests;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.tqs.delivera.controllers.DeliveraController;
import ua.tqs.delivera.datamodels.LocationDTO;
import ua.tqs.delivera.datamodels.RiderDTO;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.services.OrderService;
import ua.tqs.delivera.services.RiderService;

@WebMvcTest(DeliveraController.class)
class DeliveraControllerTests {
  
  @Autowired
  private MockMvc mvnForTests;
  
  @MockBean
  private RiderService riderService;

  @MockBean 
  private OrderService orderService;
  
  private RiderDTO riderDTO;
  private Rider rider;
  private LocationDTO locationDTO;
  private Location location;

  private Order order;
  private Order order1;
  
  private OrderProfit orderProfit;
  private OrderProfit orderProfit1;
  
  List<Order> orderList;
  List<OrderProfit> orderProfitList;
  
  @BeforeEach
  public void setUp() {
    locationDTO = new LocationDTO( 40.85, 25.9999 );
    location = new Location( locationDTO );
    location.setId( 1L );
    riderDTO = new RiderDTO( "ma@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0 );
    rider = new Rider( riderDTO );
    rider.setRiderId( 1L );

    orderProfitList = new ArrayList<>();
    orderList = new ArrayList<>();
    
    
    order = new Order();
    order.setClientLocation( "Aveiro, Rua da Pega" );
    order.setExternalId( 2l );
    order.setId( 1L );
    order.setOrderState("delivered"); 
    
    
    order1 = new Order();
    order1.setClientLocation( "Aveiro, Rua da Pega" );
    order1.setExternalId( 3l );
    
    
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
    rider.setOrderProfits( orderProfitList );
  }
  
  @Test
  void whenPostRider_thenCreateRider() throws Exception {
    
    when( riderService.saveRider( Mockito.any() ) ).thenReturn( rider );
    
    mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/rider" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( rider ) ) )
               .andExpect( MockMvcResultMatchers.status().isCreated() )
               .andExpect( MockMvcResultMatchers.jsonPath( "$.name", Matchers.is( rider.getName() ) ) )
               .andExpect( MockMvcResultMatchers.jsonPath( "$.riderId", Matchers.is( rider.getRiderId().intValue() ) ) )
               .andExpect( MockMvcResultMatchers.jsonPath( "$.email", Matchers.is( rider.getEmail() ) ) );
    verify( riderService, times( 1 ) ).saveRider( Mockito.any() );
  }

  // @Test
  // void whenPostLoginCorrectRider_thenReturnRider() throws Exception {
    
  //   when( riderService.loginRider( riderDTO ) ).thenReturn( rider );
    
  //   mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/rider/login" )
  //                                              .contentType( MediaType.APPLICATION_JSON )
  //                                              .content( ua.tqs.delivera.JSONUtil.toJson( riderDTO ) ) )
  //              .andExpect( MockMvcResultMatchers.status().isOk() )
  //              .andExpect( MockMvcResultMatchers.jsonPath( "$.name", Matchers.is( rider.getName() ) ) )
  //              .andExpect( MockMvcResultMatchers.jsonPath( "$.riderId", Matchers.is( rider.getRiderId().intValue() ) ) )
  //              .andExpect( MockMvcResultMatchers.jsonPath( "$.email", Matchers.is( rider.getEmail() ) ) );
  //   verify( riderService, times( 1 ) ).loginRider( Mockito.any() );
  // }

  // @Test
  // void whenPostLoginWrongCredentials_thenReturnUnauthorized() throws Exception {
  //   riderDTO.setPassworddto("password");
    
  //   when( riderService.loginRider( riderDTO ) ).thenReturn( null );
    
  //   mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/rider/login" )
  //                                              .contentType( MediaType.APPLICATION_JSON )
  //                                              .content( ua.tqs.delivera.JSONUtil.toJson( riderDTO ) ) )
  //              .andExpect( MockMvcResultMatchers.status().isUnauthorized() );
  //   verify( riderService, times( 1 ) ).loginRider( Mockito.any() );
  // }

  // @Test
  // void whenPostLoginNonExistingRider_thenReturnNotFound() throws Exception {
    
  //   when( riderService.loginRider( Mockito.any() ) ).thenReturn( null );
    
  //   mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/rider/login" )
  //                                              .contentType( MediaType.APPLICATION_JSON )
  //                                              .content( ua.tqs.delivera.JSONUtil.toJson( "invalid rider" ) ) )
  //              .andExpect( MockMvcResultMatchers.status().isNotFound() );
  //   verify( riderService, times( 1 ) ).loginRider( Mockito.any() );
  // }

  @Test
  void whenGetRiderStatsWithNoReviews_thenReceiveMap() throws Exception {

    Map<String, Object> expected = new HashMap<String, Object>();
    expected.put("averageReviewValue", 0.0);
    expected.put("totalRiderOrders", 2);
    expected.put("totalNumberOfOrdersDelivered", 1);

    when( riderService.getRiderStatistics( Mockito.anyLong() ) ).thenReturn( expected );

    mvnForTests.perform( MockMvcRequestBuilders.get( "/api/v1/stats/rider/" + rider.getRiderId() )
                                              .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( jsonPath("averageReviewValue", is(0.0) ) );
  
    verify( riderService, times( 1 ) ).getRiderStatistics( Mockito.anyLong() );
  
  }

  @Test
  void whenGetRiderStats_thenReceiveMap() throws Exception {

    Map<String, Object> expected = new HashMap<String, Object>();
    rider.setNumberOfReviews(10);
    rider.setSumOfReviews(30);
    expected.put("averageReviewValue", 30/10.0);
    expected.put("totalRiderOrders", 2);
    expected.put("totalNumberOfOrdersDelivered", 1);

    when( riderService.getRiderStatistics( Mockito.anyLong() ) ).thenReturn( expected );

    mvnForTests.perform( MockMvcRequestBuilders.get( "/api/v1/stats/rider/" + rider.getRiderId() )
                                              .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( jsonPath( "averageReviewValue", is(30/10.0) ) )
                .andExpect( jsonPath( "totalRiderOrders", is(2) ) )
                .andExpect( jsonPath( "totalNumberOfOrdersDelivered", is(1) ) );

    verify( riderService, times( 1 ) ).getRiderStatistics( Mockito.anyLong() );
  }

  @Test
  void whenGetRiderStatsWIthInvalidRider_thenReceiveBadRequest() throws Exception {

    when( riderService.getRiderStatistics( Mockito.anyLong() ) ).thenThrow( new NonExistentResource( "This rider does not exist!" ) );

    mvnForTests.perform( MockMvcRequestBuilders.get( "/api/v1/stats/rider/" + rider.getRiderId() )
                                              .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( MockMvcResultMatchers.status().isBadRequest() );

    verify( riderService, times( 1 ) ).getRiderStatistics( Mockito.anyLong() );
  }
  
  @Test 
  void whenGetAllOrdersForRider_ThenReturnAListOfOrders() throws Exception {
    when( riderService.getAllOrdersForRider( rider.getRiderId() ) ).thenReturn( orderList );
    
    
    mvnForTests.perform( MockMvcRequestBuilders.get( "/api/v1/rider/" + rider.getRiderId() + "/orders" ) )
               .andExpect( MockMvcResultMatchers.status().isOk() )
               .andExpect( MockMvcResultMatchers.jsonPath( "$", Matchers.hasSize( 2 ) ) ).andExpect(
                 MockMvcResultMatchers.jsonPath( "$[0].clientLocation", Matchers.is( order.getClientLocation() ) ) ).andExpect(
                 MockMvcResultMatchers.jsonPath( "$[1].clientLocation", Matchers.is( order1.getClientLocation() ) ) ).andExpect(
                 MockMvcResultMatchers.jsonPath( "$[0].externalId", Matchers.is( order.getExternalId().intValue() ) ) ).andExpect(
                 MockMvcResultMatchers.jsonPath( "$[1].externalId",
                   Matchers.is( order1.getExternalId().intValue() ) ) );
    
    verify( riderService, times( 1 ) ).getAllOrdersForRider( rider.getRiderId() );
  }
  
  
  @Test 
  void whenGetAllOrdersForNonExistentRider_ThenReturnBAD_REQUEST() throws Exception {
    when( riderService.getAllOrdersForRider( anyLong() ) ).thenThrow(
      new NonExistentResource( "This rider " + "does not exist at the moment!" ) );
    
    
    mvnForTests.perform( MockMvcRequestBuilders.get( "/api/v1/rider/" + - 1 + "/orders" ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest() );
    
    verify( riderService, times( 1 ) ).getAllOrdersForRider( anyLong() );
  }
  
  @Test 
  void whenGetOrderForValidArguments_ThenReturnOrder() throws Exception {
    when( riderService.getOrder( rider.getRiderId(), order.getId() ) ).thenReturn( order );
    
    
    mvnForTests.perform(
                 MockMvcRequestBuilders.get( "/api/v1/rider/" + rider.getRiderId() + "/orders/" + order.getId() ) )
               .andExpect( MockMvcResultMatchers.status().isOk() );
    
    verify( riderService, times( 1 ) ).getOrder( rider.getRiderId(), order.getId() );
  }
  
  @Test 
  void whenGetOrderForNonExistentRider_ThenReturnBAD_REQUEST() throws Exception {
    when( riderService.getOrder( anyLong(), anyLong() ) ).thenThrow(
      new NonExistentResource( "This rider " + "does not exist at the moment!" ) );
    
    
    mvnForTests.perform( MockMvcRequestBuilders.get( "/api/v1/rider/" + - 1 + "/orders/-1" ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest() );
    
    verify( riderService, times( 1 ) ).getOrder( anyLong(), anyLong() );
  }
  
  @Test 
  void whenGetOrderForNonExistentOrder_ThenReturnBAD_REQUEST() throws Exception {
    when( riderService.getOrder( anyLong(), anyLong() ) ).thenThrow(
      new NonExistentResource( "This rider " + "does not exist at the moment!" ) );
    
    
    mvnForTests.perform( MockMvcRequestBuilders.get( "/api/v1/rider/" + - 1 + "/orders/-1" ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest() );
    
    verify( riderService, times( 1 ) ).getOrder( anyLong(), anyLong() );
  }
  
}
