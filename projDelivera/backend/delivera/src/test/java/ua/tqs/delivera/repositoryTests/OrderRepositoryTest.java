package ua.tqs.delivera.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.OrderRepository;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepo;

    private Rider rider;
    private Location location;
    private Order order;
    private OrderProfit orderProfit;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:12")
		.withUsername("postgres")
		.withPassword("secret")
		.withDatabaseName("delivera");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeEach
    void setup(){
        location = new Location(40.85, 25.9999);
        entityManager.persistAndFlush(location);

        rider = new Rider("ma@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0);
        entityManager.persistAndFlush(rider);

        orderProfit = new OrderProfit();
        orderProfit.setOrderPrice( 12.1 );
        orderProfit.setRider( rider );
        entityManager.persistAndFlush(orderProfit); 

        order = new Order();
        order.setClientLocation( "40.85, 25.9999" );
        order.setExternalId( 2L );
        order.setOrderProfit(orderProfit);
        order.setOrderState("delivered"); 
        entityManager.persistAndFlush(order);
    }

    @Test
    void whenGetOrderDelivered_thenReturnOrder(){
        Optional<Order> orders = orderRepo.findByIdAndOrderState( order.getId() , "delivered" );
        assertThat(orders).contains(order);
        assertThat(orders.get().getOrderProfit()).isEqualTo(orderProfit);
    }

    @Test
    void whenGetInvalidOrderDelivered_thenReturnEmpty(){
        Optional<Order> orders = orderRepo.findByIdAndOrderState( -1l , "delivered" );
        assertThat(orders).isEmpty();
    }
}
