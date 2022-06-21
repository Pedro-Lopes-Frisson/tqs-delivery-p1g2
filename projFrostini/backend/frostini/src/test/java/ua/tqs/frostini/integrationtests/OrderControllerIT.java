package ua.tqs.frostini.integrationtests;

import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;
import ua.tqs.frostini.datamodels.OrderDTO;
import ua.tqs.frostini.datamodels.OrderedProductDTO;
import ua.tqs.frostini.models.Address;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.models.OrderedProduct;
import ua.tqs.frostini.models.Product;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.repositories.AddressRepository;
import ua.tqs.frostini.repositories.OrderRepository;
import ua.tqs.frostini.repositories.UserRepository;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIT {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    User u = createUser( 1 );
    List<Order> userOrders;

    OrderDTO order0DTO;
    OrderDTO order1DTO;
    OrderDTO order2DTO;

    Order o1 = createOrder(1);

    @Container
    static PostgreSQLContainer container = new PostgreSQLContainer( "postgres:11.12" )
        .withUsername( "demo" )
        .withPassword( "demopw" )
        .withDatabaseName( "shop" );


    @DynamicPropertySource
    static void properties( DynamicPropertyRegistry registry ) {
        registry.add( "spring.datasource.url", container::getJdbcUrl );
        registry.add( "spring.datasource.password", container::getPassword );
        registry.add( "spring.datasource.username", container::getUsername );
    }

    @BeforeEach
    void setUp() {
        //userRepository.saveAndFlush( u );
        orderRepository.saveAndFlush( o1 );
    }

    @AfterEach
    void resetDb() {
    
        userRepository.deleteAll();
        userRepository.flush();
        orderRepository.deleteAll();
        orderRepository.flush();
    }

    @Test
    void testUpdateOrderStateInvalidOrder_ThenReturnBadRequest() {

        RestAssured.given()
            .contentType( "application/json" )
            .put( createURL() + "/{orderId}", -1l )
            .then()
            .statusCode( 400 );


    }

    @Test
    void testUpdateOrderStateOrderedToInTransit_ThenReturnOk() {
        System.out.println("ORDER ID: " + o1.getId());
        RestAssured.given()
            .contentType( "application/json" )
            .put( createURL() + "/{orderId}", o1.getId() )
            .then()
            .statusCode( 200 )
            .body("orderState", equalTo("in transit"));
        /* userOrders.get( 0 ).setOrderState("in transit");

        given().contentType( ContentType.JSON ).put( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
            .then().log().body().assertThat()
            .status( HttpStatus.OK ).and()
            .body("orderState", is("in transit"));

 */
    }

    @Test
    void testUpdateOrderStateInTransitToDelivered_ThenReturnOk() {
        // u.getOrder().iterator().next().setOrderState("in transit");
        /* userOrders.get( 0 ).setOrderState("delivered");

        given().contentType( ContentType.JSON ).put( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
            .then().log().body().assertThat()
            .status( HttpStatus.OK ).and()
            .body("orderState", is("delivered"));
 */

    }

    @Test
    void testUpdateOrderStateInvalidState_ThenReturnBadRequest() {

        /* given().contentType( ContentType.JSON ).put( "/api/v1/order/{orderId}", userOrders.get( 0 ).getId() )
            .then().log().body().assertThat()
            .status( HttpStatus.BAD_REQUEST );

 */
    }

    /* helpers */

    String createURL() {
        return "http://localhost:" + randomServerPort + "/api/v1/order";
    }

    private User createUser( int i ) {
        User u = new User();
        u.setName( "Pedro" );
        u.setPassword( "safepassword" );
        u.setEmail( "pdfl" + i + "@ua.pt" );
        return u;
    }

    private Product createProduct( int i ) {
        Product p = new Product();
        p.setName( "Gelado " + i );
        p.setDescription( "This is a long description" );
        p.setPrice( 12.3 );
        p.setStockQuantity( i * 10 );
        return p;
    }

    private OrderedProduct createOrderedProduct( int i, Order order, Product p ) {
        OrderedProduct oP = new OrderedProduct();

        oP.setOrder( order );
        oP.setProduct( p );
        oP.setPrice( 12 );
        oP.setQuantity( 12 );

        return oP;
    }

    private Address createAddress( int i, User u ) {
        Address address = new Address();
        address.setLatitude( 40.640506 + i );
        address.setLongitude( -8.653754 + i );
        return address;
    }


    private Order createOrder( int i ) {


        Address address = createAddress( i, u );
        addressRepository.save( address );


        List<Product> products = new ArrayList<>();
        products.add( createProduct( 1 + i ) );
        products.add( createProduct( 2 + i ) );


        Order order = new Order();
        List<OrderedProduct> orderedProductList = new ArrayList<>();

        orderedProductList.add( createOrderedProduct( i, order, products.get( 0 ) ) );
        orderedProductList.add( createOrderedProduct( i, order, products.get( 1 ) ) );

        order.setOrderedProductList( orderedProductList );

        order.setAddress( address );
        order.setUser( u );
        order.setTotalPrice(
        orderedProductList.stream().mapToDouble( ( OrderedProduct oo ) -> oo.getPrice() * oo.getQuantity() ).sum() );

        return order;
    }

    private OrderDTO createOrderDTO( int i ) {


        OrderDTO order = new OrderDTO();
        List<OrderedProductDTO> orderedProductList = new ArrayList<>();

        // create orderedProducts with the matchin ids of userOrders
        orderedProductList.add( new OrderedProductDTO( 1,
        userOrders.get( i ).getOrderedProductList().get( 0 ).getProduct().getId() ) );

        orderedProductList.add( new OrderedProductDTO( 1,
        userOrders.get( i ).getOrderedProductList().get( 0 ).getProduct().getId() ) );

        order.setOrderedProductsList( orderedProductList );
        order.setAddressId( 1l );


        return order;
    }

}
