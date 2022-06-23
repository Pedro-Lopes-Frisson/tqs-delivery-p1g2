package ua.tqs.delivera.controllerTests;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.tqs.delivera.controllers.OrdersController;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Store;
import ua.tqs.delivera.services.OrderService;

@WebMvcTest(OrdersController.class)
class OrdersControllerTest {
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
        location = new Location(40.85, 25.9999);

        store = new Store();
        store.setName("Frostini");
        store.setAddress(location);

        Long orderMadeTimestamp = System.currentTimeMillis();

        orderDto = new OrderDTO(2L, "40.9800,-8.2345", store, orderMadeTimestamp);
        order = new Order();
        order.setClientLocation( "40.9800,-8.2345" );
        order.setExternalId( 2l );
        order.setId( 1l );
        order.setOrderMadeTimestamp(orderMadeTimestamp);
        order.setStore(store);
    }

    @Test
    void whenPutOrderStatusForValidId_ThenReturnStatusUpdate() throws Exception{
        when(orderService.updateOrderState(anyLong())).thenReturn(true);

        mvnForTests.perform(
            MockMvcRequestBuilders.put( "/api/v1/order/" + order.getId() ) )
               .andExpect( MockMvcResultMatchers.status().isOk()
        );
        verify( orderService, times( 1 ) ).updateOrderState( anyLong() );
    }

    @Test
    void whenPutOrderStatusForInvalidId_ThenReturnBadRequest() throws Exception{
        when(orderService.updateOrderState(anyLong())).thenThrow(
            new NonExistentResource( "Order not existent" ) );

        mvnForTests.perform(
            MockMvcRequestBuilders.put( "/api/v1/order/" + -1L ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest()
        );
        verify( orderService, times( 1 ) ).updateOrderState( anyLong() );
        
    }

    @Test
    public void whenPostOrder_thenCreateOrder() throws Exception {
        when( orderService.createOrder( Mockito.any() ) ).thenReturn( order );

        mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/order" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( orderDto ) ) )
               .andExpect( MockMvcResultMatchers.status().isCreated() )
               .andExpect( MockMvcResultMatchers.jsonPath("$.id", Matchers.is( order.getId().intValue() )) )
               .andExpect( MockMvcResultMatchers.jsonPath("$.externalId", Matchers.is( order.getExternalId().intValue() )) )
               .andExpect( MockMvcResultMatchers.jsonPath("$.currentLocation", Matchers.is( order.getCurrentLocation() )) )
               .andExpect( MockMvcResultMatchers.jsonPath("$.clientLocation", Matchers.is( order.getClientLocation() )) )
               .andExpect( MockMvcResultMatchers.jsonPath("$.orderMadeTimestamp", Matchers.is( order.getOrderMadeTimestamp() )) )
               .andExpect( MockMvcResultMatchers.jsonPath("$.orderState", Matchers.is( order.getOrderState() )) );
        verify( orderService, times( 1 ) ).createOrder( Mockito.any() );
    }

    @Test
    public void whenPostOrderWithInvalidStore_thenReturnBadRequest() throws Exception {
        when( orderService.createOrder( Mockito.any() ) ).thenReturn( null );

        mvnForTests.perform( MockMvcRequestBuilders.post( "/api/v1/order" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( orderDto ) ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest() );
        verify( orderService, times( 1 ) ).createOrder( Mockito.any() );
    }
}
