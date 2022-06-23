package ua.tqs.delivera.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

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
import ua.tqs.delivera.repositories.StoreRepository;
import ua.tqs.delivera.services.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {

    @Mock( lenient = true)
    private StoreRepository storeRepo;

    @Mock( lenient = true)
    private OrderRepository orderRepo;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDTO orderDto;
    private Store store;
    private Location location;
    private Long orderMadeTimestamp;

    @BeforeEach
    public void setUp() {
        location = new Location(40.85, 25.9999);

        store = new Store();
        store.setName("Frostini");
        store.setAddress(location);

        orderMadeTimestamp = System.currentTimeMillis();

        orderDto = new OrderDTO(2L, "40.9800,-8.2345", store, orderMadeTimestamp);
        order = new Order();
        order.setClientLocation( "40.9800,-8.2345" );
        order.setExternalId( 2l );
        order.setId( 1l );
        order.setOrderMadeTimestamp(orderMadeTimestamp);
        order.setStore(store);
    }

    @Test
    public void whenCreateOrder_thenReturnOrder() throws Exception {
        Mockito.when( storeRepo.findById( any() )).thenReturn( Optional.of(store) );
        Mockito.when( orderRepo.save( any() ) ).thenReturn( order );

        Order foundOrder = orderService.createOrder(orderDto);
        assertThat(foundOrder).isEqualTo(order);

        Mockito.verify(orderRepo, VerificationModeFactory.times(1)).save(any());
        Mockito.verify(storeRepo, VerificationModeFactory.times(1)).findById(any());
    }

    @Test
    public void whenCreateOrderWithInvalidStore_thenReturnNull() throws Exception {
        Mockito.when( storeRepo.findById( any() )).thenReturn( Optional.empty() );
        Mockito.when( orderRepo.save( order ) ).thenReturn( null );

        Order foundOrder = orderService.createOrder(orderDto);
        assertThat(foundOrder).isNull();

        Mockito.verify(orderRepo, VerificationModeFactory.times(0)).save(any());
        Mockito.verify(storeRepo, VerificationModeFactory.times(1)).findById(any());
    }
}
