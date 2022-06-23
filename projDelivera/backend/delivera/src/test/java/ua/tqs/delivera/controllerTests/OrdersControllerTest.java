package ua.tqs.delivera.controllerTests;

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
import ua.tqs.delivera.controllers.OrdersController;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.datamodels.ReviewDTO;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.exceptions.OrderDoesnotExistException;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Store;
import ua.tqs.delivera.services.OrderService;

import static org.mockito.Mockito.*;

@WebMvcTest(OrdersController.class)
public class OrdersControllerTest {
  @Autowired
  private MockMvc mvnForTests;
  
  @MockBean
  private OrderService orderService;
  
  private Order order;
  private OrderDTO orderDto;
  private Store store;
  private Location location;
  
  @BeforeEach
  public void setUp() {
    location = new Location( 40.85, 25.9999 );
    
    store = new Store();
    store.setName( "Frostini" );
    store.setAddress( location );
    
    Long orderMadeTimestamp = System.currentTimeMillis();
    
    orderDto = new OrderDTO();
    orderDto.setOrderPrice( 12.2D );
    orderDto.setStoreLat( 12 );
    orderDto.setStoreLon( 12 );
    orderDto.setStoreName( "Frostini" );
    orderDto.setClientLat( 40.9800 );
    orderDto.setClientLon( - 8.2345 );
    orderDto.setOrderStoreId( 2L );
    
    order = new Order();
    order.setClientLocation( "40.9800,-8.2345" );
    order.setExternalId( 2l );
    order.setId( 1L );
    order.setOrderMadeTimestamp( orderMadeTimestamp );
    order.setStore( store );
    //order.setOrderState("delivered");
  }
  
  @Test
  public void whenPostOrder_thenCreateOrder() throws Exception {
    when( orderService.assignOrder( Mockito.any() ) ).thenReturn( order );
    
    mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/order" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( orderDto ) ) )
               .andExpect( MockMvcResultMatchers.status().isCreated() )
               .andExpect( MockMvcResultMatchers.jsonPath( "$.orderMadeTimestamp",
                 Matchers.is( order.getOrderMadeTimestamp() ) ) );
    verify( orderService, times( 1 ) ).assignOrder( Mockito.any() );
  }
  
  @Test
  public void whenPostOrderWithInvalidStore_thenReturnBadRequest() throws Exception {
    when( orderService.assignOrder( Mockito.any() ) ).thenReturn( null );
    
    mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/order" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( orderDto ) ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest() );
    verify( orderService, times( 1 ) ).assignOrder( Mockito.any() );
  }
  
  @Test
  public void whenReviewOrderWithEverythingOkay_ThenReturn200()
    throws Exception {
    when( orderService.reviewOrder( Mockito.anyLong(), Mockito.any() ) ).thenReturn( true );
    
    mvnForTests.perform( MockMvcRequestBuilders.put( "/api/v1/order/1/review" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( new ReviewDTO( 4D ) ) ) )
               .andExpect( MockMvcResultMatchers.status().isOk() );
    
    
    verify( orderService, times( 1 ) ).reviewOrder( Mockito.anyLong(), Mockito.any() );
  }
  
  
  @Test
  public void whenReviewOrderWithInvalidOrderId_ThenReturnBadRequest()
    throws Exception {
    when( orderService.reviewOrder( Mockito.anyLong(), Mockito.any() ) ).thenThrow( OrderDoesnotExistException.class );
    
    mvnForTests.perform( MockMvcRequestBuilders.put( "/api/v1/order/-1/review" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( new ReviewDTO( 4D ) ) ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest() );
    
    
    verify( orderService, times( 1 ) ).reviewOrder( Mockito.anyLong(), Mockito.any() );
  }
  
  @Test
  public void whenReviewOrderWithRiderIDHasProblems_ThenReturnBadRequest()
    throws Exception {
    when( orderService.reviewOrder( Mockito.anyLong(), Mockito.any() ) ).thenThrow( NonExistentResource.class );
    
    mvnForTests.perform( MockMvcRequestBuilders.put( "/api/v1/order/-1/review" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( new ReviewDTO( 4D ) ) ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest() );
    
    
    verify( orderService, times( 1 ) ).reviewOrder( Mockito.anyLong(), Mockito.any() );
  }
  
  
}
