package ua.tqs.delivera.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.tqs.delivera.controllers.OrdersController;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.services.OrderService;

@WebMvcTest(OrdersController.class)
public class OrdersControllerTest {
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
        order.setOrderState("delivered"); 
    }
}
