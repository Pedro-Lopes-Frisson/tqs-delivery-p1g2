package ua.tqs.delivera.integrationtests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.datamodels.ReviewDTO;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Store;
import ua.tqs.delivera.repositories.LocationRepository;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.repositories.RiderRepository;
import ua.tqs.delivera.repositories.StoreRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) @Testcontainers
@DirtiesContext
public class OrderControllerIT {
  
  @Container public static PostgreSQLContainer container =
    new PostgreSQLContainer( "postgres:12" ).withUsername( "postgres" ).withPassword( "secret" )
                                            .withDatabaseName( "delivera" );
  
  @DynamicPropertySource static void properties( DynamicPropertyRegistry registry ) {
    registry.add( "spring.datasource.url", container::getJdbcUrl );
    registry.add( "spring.datasource.password", container::getPassword );
    registry.add( "spring.datasource.username", container::getUsername );
  }
  
  @LocalServerPort int randomServerPort;
  
  @Autowired private RiderRepository riderRepo;
  @Autowired private OrderRepository orderRepository;
  @Autowired private StoreRepository storeRepository;
  
  @Autowired private LocationRepository locationRepo;
  
  OrderDTO orderDto;
  
  @BeforeEach public void setUp() {
    orderDto = new OrderDTO();
    orderDto.setOrderPrice( 12.2D );
    orderDto.setStoreLat( 12 );
    orderDto.setStoreLon( 12 );
    orderDto.setStoreName( "Frostini" );
    orderDto.setClientLat( 40.9800 );
    orderDto.setClientLon( - 8.2345 );
    orderDto.setOrderStoreId( 2L );
  }
  
  @Test
  void whenReviewOrderAndEverythingIsOkay_ThenReturn200() {
    Order o = orderRepository.findAll().get( 0 );
    System.out.println( o );
    System.out.println( o.getOrderProfit() );
    System.out.println( o.getOrderProfit().getRider() );
    RestAssured.given()
               .contentType( "application/json" )
               .when().body( new ReviewDTO( 4D ) )
               .put( createURL() + "/{id}/review", o.getId() )
               .then().assertThat()
               .log().body()
               .statusCode( 200 );
  }
  
  @Test
  void whenReviewOrderButOrderDoesNotExist() {
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .put( createURL() + "/{id}/review", - 1L )
               .then().assertThat()
               .log().body()
               .contentType( ContentType.JSON ).and().statusCode( HttpStatus.BAD_REQUEST.value() );
  }
  
  @Test
  void whenMakeOrderWithEverythingOkayThenReturnOrder200() {
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .body( orderDto )
               .post( createURL() ).then().assertThat().statusCode( 201 )
               .and().log().body()
               .body( "externalId", is( 2 ) )
               .and().body( "clientLocation", is( "40.98,-8.2345" ) )
               .and().body( "store.id", is( 1 ) );
  }
  
  @Test
  void whenMakeOrderWithEverythingOkayAndANewStoreThenReturnOrder200() {
    orderDto.setStoreName( "Frostini 2" );
    orderDto.setOrderStoreId( 3L );
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .body( orderDto )
               .post( createURL() ).then().assertThat().statusCode( 201 )
               .and().log().body()
               .body( "externalId", is( 3 ) )
               .and().body( "clientLocation", is( "40.98,-8.2345" ) )
               .and().body( "store.id", is( 2 ) );
  }
  
  String createURL() {
    return "http://localhost:" + randomServerPort + "/api/v1/order";
  }
  
}
