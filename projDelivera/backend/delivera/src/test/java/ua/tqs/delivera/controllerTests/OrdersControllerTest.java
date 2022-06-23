package ua.tqs.delivera.controllerTests;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.tqs.delivera.controllers.OrdersController;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.services.OrderService;

@WebMvcTest(OrdersController.class)
class OrdersControllerTest {
    @Autowired
    private MockMvc mvnForTests;


    @MockBean 
    private OrderService orderService;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
        order.setClientLocation( "Aveiro, Rua da Pega" );
        order.setExternalId( 2l );
        order.setId( 1L );
    }

    @Test
    void whenPutOrderStatusForValidId_ThenReturnStatusUpdate() throws Exception{
        when(orderService.updateOrderStatus(anyLong())).thenReturn(true);

        mvnForTests.perform(
            MockMvcRequestBuilders.put( "/api/v1/orders/" + order.getId() ) )
               .andExpect( MockMvcResultMatchers.status().isOk()
        );
        verify( orderService, times( 1 ) ).updateOrderStatus( anyLong() );
    }

    @Test
    void whenPutOrderStatusForInvalidId_ThenReturnBadRequest() throws Exception{
        when(orderService.updateOrderStatus(anyLong())).thenThrow(
            new NonExistentResource( "Order not existent" ) );

        mvnForTests.perform(
            MockMvcRequestBuilders.put( "/api/v1/orders/" + -1L ) )
               .andExpect( MockMvcResultMatchers.status().isBadRequest()
        );
        verify( orderService, times( 1 ) ).updateOrderStatus( anyLong() );
    }
}
