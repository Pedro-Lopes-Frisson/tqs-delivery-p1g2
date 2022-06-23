package ua.tqs.delivera.serviceTests;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Store;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.services.OrderService;
import ua.tqs.delivera.exceptions.NonExistentResource;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock( lenient = true)
    private OrderRepository orderRepo;

    @InjectMocks
    private OrderService orderService;

    private static final String ORDERED = "ordered";
    private static final String IN_TRANSIT = "in transit";
    private static final String DELIVERED = "delivered";

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
        order.setId( 1L );
        order.setOrderMadeTimestamp(orderMadeTimestamp);
        order.setStore(store);
        //order.setOrderState("delivered"); 
    }

    @Test
    void whenUpdateOrderStateWithExistentId_ThenReturnTrue() throws NonExistentResource{
        Mockito.when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));

        boolean statusUpdated = orderService.updateOrderState(order.getId());
        assertTrue( statusUpdated);
        verifyFindByIdIsCalledOnce();
    }

    @Test
    void whenUpdateOrderStateWithInvalidIdOrInexistenId_ThenReturnTrue() throws NonExistentResource{
        Mockito.when(orderRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NonExistentResource.class, () ->
            orderService.updateOrderState(-1L)
        );

        verifyFindByIdIsCalledOnce();
    }

    @Test
    void testChangeState() throws Exception{
        assertEquals(ORDERED, orderService.changeState(null));
        assertEquals(IN_TRANSIT, orderService.changeState(ORDERED));
        assertEquals(DELIVERED, orderService.changeState(IN_TRANSIT));
        assertEquals(DELIVERED, orderService.changeState(DELIVERED));
        assertEquals(ORDERED, orderService.changeState(null));
        assertThrows(Exception.class, ()-> orderService.changeState("anyString"));
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(orderRepo, VerificationModeFactory.times(1)).findById(any());
    }


}
