package ua.tqs.delivera.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.LocationRepository;
import ua.tqs.delivera.repositories.RiderRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) @Testcontainers
@DirtiesContext
public class IntegrationTest {
  
  @Container public static PostgreSQLContainer container =
    new PostgreSQLContainer( "postgres:12" ).withUsername( "postgres" ).withPassword( "secret" )
                                            .withDatabaseName( "delivera" );
  
  @DynamicPropertySource static void properties( DynamicPropertyRegistry registry ) {
    registry.add( "spring.datasource.url", container::getJdbcUrl );
    registry.add( "spring.datasource.password", container::getPassword );
    registry.add( "spring.datasource.username", container::getUsername );
  }
  
  @LocalServerPort int randomServerPort;
  
  @Autowired private TestRestTemplate restTemplate;
  
  @Autowired private RiderRepository riderRepo;
  
  @Autowired private LocationRepository locationRepo;
  
  private Order order;
  private Order order1;
  
  private OrderProfit orderProfit;
  private OrderProfit orderProfit1;
  
  List<Order> orderList;
  List<OrderProfit> orderProfitList;
  
  @BeforeEach public void setUp() {
    
    orderProfitList = new ArrayList<>();
    orderList = new ArrayList<>();
    
    
    order = new Order();
    order.setClientLocation( "Aveiro, Rua da Pega" );
    order.setExternalId( 2L );
    
    
    order1 = new Order();
    order1.setClientLocation( "Aveiro, Rua da Pega" );
    order1.setExternalId( 3L );
    
    
    orderProfit = new OrderProfit();
    orderProfit.setOrder( order );
    orderProfit.setOrderPrice( 12.1 );
    
    
    orderProfit1 = new OrderProfit();
    orderProfit1.setOrder( order1 );
    orderProfit1.setOrderPrice( 122.1 );
    
    
    orderList.add( order );
    orderList.add( order1 );
    
  }
  
  
  @Test void whenValidInput_thenCreateCar() {
    Rider rider =
      createTestRider( "ma@gmail.com", "Manuel Antunes", "migant", true, createTestLocation( 40.85, 25.9999 ), 0, 0 );
    ;
    ResponseEntity<Rider> entity = restTemplate.postForEntity( "/api/delivera/rider", rider, Rider.class );
    
    List<Rider> found = riderRepo.findAll();
    assertThat( found ).extracting( Rider::getEmail ).contains( rider.getEmail() );
  }
  
  @Test
  void whenValidRiderId_ThenReturnListOfOrders() {
    Optional<Rider> rider = riderRepo.findById( 1L );
    assertThat( rider ).isNotEmpty();
    
    ResponseEntity<List<Order>> response =
      restTemplate.exchange( "/api/delivera/rider/" + 1 + "/orders",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<>() {}
      );
    
    assertEquals( response.getStatusCode(), HttpStatus.OK );
    
    List<Order> orderListResp = null;
    if ( response != null && response.hasBody() ) {
      orderListResp = response.getBody();
    }
    System.out.println(orderListResp);
    
    assertThat( orderListResp ).isNotEmpty();
    assertThat( orderListResp ).hasSize( rider.get().getOrderProfits().size() );
    
    //assertThat( orderListResp.get( 0 ).getId() ).isEqualTo( orderList.get( 0 ).getId() );
  }
  
  
  
  
  
  
  //-------------------------------- HELPERS ----------------------------------------
  private Location createTestLocation( double lat, double lon ) {
    Location location = new Location( lat, lon );
    locationRepo.saveAndFlush( location );
    return location;
  }
  
  private Rider createTestRider( String email, String name, String password, boolean available,
                                 Location currentLocation, int numberOfReviews, int sumOfReviews ) {
    Rider rider = new Rider( email, name, password, available, currentLocation, numberOfReviews, sumOfReviews );
    
    orderProfit1.setRider( rider );
    orderProfit.setRider( rider );
    
    rider.setOrderProfits( orderProfitList );
    
    rider = riderRepo.saveAndFlush( rider );
    
    return rider;
  }
  
}
