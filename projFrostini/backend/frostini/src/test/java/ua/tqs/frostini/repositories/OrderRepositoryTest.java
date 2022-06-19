package ua.tqs.frostini.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.tqs.frostini.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest @Testcontainers @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {
  
  Order order1;
  Order order2;
  Order order3;
  Order order4;
  
  User u = createUser( 1 );
  @Container static PostgreSQLContainer container =
    new PostgreSQLContainer( "postgres:11.12" ).withUsername( "demo" ).withPassword( "demopw" )
                                               .withDatabaseName( "shop" );
  
  
  @DynamicPropertySource static void properties( DynamicPropertyRegistry registry ) {
    System.out.println( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + container.getJdbcUrl() );
    registry.add( "spring.datasource.url", container::getJdbcUrl );
    registry.add( "spring.datasource.password", container::getPassword );
    registry.add( "spring.datasource.username", container::getUsername );
  }
  
  
  @AfterEach void tearDown() {
    orderRepository.deleteAll();
    productRepository.deleteAll();
    entityManager.clear();
  }
  
  
  @BeforeEach void setUp() {
    order1 = createAndSaveOrder( 1 );
    order2 = createAndSaveOrder( 2 );
    order3 = createAndSaveOrder( 3 );
    order4 = createAndSaveOrder( 4 );
  }
  
  @Autowired private OrderRepository orderRepository;
  @Autowired private ProductRepository productRepository;
  
  
  @Autowired private TestEntityManager entityManager;
  
  @Test void whenFindByValidID_ThenReturnCorrectOrder() {
    Optional<Order> optionalOrder = orderRepository.findByid( order1.getId() );
    assertThat( optionalOrder ).isPresent();
    assertThat( optionalOrder.get().getAddress() ).isEqualTo( order1.getAddress() );
    assertThat( optionalOrder.get().getUser() ).isEqualTo( order1.getUser() );
    assertThat( optionalOrder.get().getTotalPrice() ).isEqualTo( order1.getTotalPrice() );
    
  }
  
  
  @Test void whenFindByInvalidID_ThenReturnEmptyOptionalObject() {
    Optional<Order> optionalOrder = orderRepository.findByid( 500000 );
    assertThat( optionalOrder ).isEmpty();
  }
  
  
  @Test void whenFindAllOrderByUser_thenReturnAllOrdersMadeByThatUser() {
    List<Order> orders = new ArrayList<>();
    orders.add( order1 );
    orders.add( order2 );
    orders.add( order3 );
    orders.add( order4 );
    
    List<Order> fromDb = orderRepository.findAllByUser( u, Pageable.ofSize( 2 ).first() );
    
    assertThat( fromDb.size() ).isEqualTo( 2 );
    assertThat( fromDb ).isEqualTo( orders.subList( 0, 2 ) );
    
  }
  
  
  
  /* helpers */
  
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
    address.setUser( u );
    address.setStreet( "Some Real Street " + i );
    address.setZipCode( "0000-" + i + "23" );
    return address;
  }
  
  
  private Order createAndSaveOrder( int i ) {
    
    entityManager.persist( u );
    
    Address address = createAddress( i, u );
    
    entityManager.persist( address );
    
    
    List<Product> products = new ArrayList<>();
    products.add( createProduct( 1 + i ) );
    products.add( createProduct( 2 + i ) );
    
    
    Order order = new Order();
    for (Product prod : products) {
      entityManager.persist( prod );
    }
    List<OrderedProduct> orderedProductList = new ArrayList<>();
    orderedProductList.add( createOrderedProduct( i, order, products.get( 0 ) ) );
    orderedProductList.add( createOrderedProduct( i, order, products.get( 1 ) ) );
    
    order.setAddress( address );
    order.setUser( u );
    order.setTotalPrice(
      orderedProductList.stream().mapToDouble( ( OrderedProduct oo ) -> oo.getPrice() * oo.getQuantity() ).sum() );
    
    entityManager.persistAndFlush( order );
    return order;
  }
}
