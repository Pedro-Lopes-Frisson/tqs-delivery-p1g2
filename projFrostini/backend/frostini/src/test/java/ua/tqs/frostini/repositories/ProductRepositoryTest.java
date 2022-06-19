package ua.tqs.frostini.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.tqs.frostini.models.Product;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTest {
  
  @Container
  static PostgreSQLContainer container = new PostgreSQLContainer( "postgres:11.12" )
    .withUsername( "demo" )
    .withPassword( "demopw" )
    .withDatabaseName( "shop" );
  
  
  @DynamicPropertySource
  static void properties( DynamicPropertyRegistry registry ) {
    System.out.println( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + container.getJdbcUrl() );
    registry.add( "spring.datasource.url", container::getJdbcUrl );
    registry.add( "spring.datasource.password", container::getPassword );
    registry.add( "spring.datasource.username", container::getUsername );
  }
  
  Product p;
  Product p1;
  
  @BeforeEach
  void setUp() {
    p = createProduct( 2 );
    p1 = createProduct( 20 );
    
    testEntityManager.persistAndFlush( p );
    testEntityManager.persistAndFlush( p1 );
  }
  
  @AfterEach
  void tearDown() {
    productRepository.deleteAll();
    testEntityManager.clear();
    testEntityManager.flush();
    productRepository.flush();
  }
  
  @Autowired
  private ProductRepository productRepository;
  
  
  @Autowired
  private TestEntityManager testEntityManager;
  
  @Test
  @Order(1)
  void WhenFindByValidIdThenReturnProduct() {
    Optional<Product> optionalProduct = productRepository.findById( 2L );
    assertThat( optionalProduct ).isPresent();
    
    Product product = optionalProduct.get();
    assertThat( product.getStockQuantity() ).isEqualTo( p1.getStockQuantity() );
    assertThat( product.getPrice() ).isEqualTo( p1.getPrice() );
    assertThat( product.getName() ).isEqualTo( p1.getName() );
    assertThat( product.getDescription() ).isEqualTo( p1.getDescription() );
  }
  
  
  @Test
  @Order(2)
  void WhenFindByValidNameThenReturnProduct() {
    Optional<Product> optionalProduct = productRepository.findByName( p.getName() );
    assertThat( optionalProduct ).isPresent();
    
    Product product = optionalProduct.get();
    assertThat( product.getStockQuantity() ).isEqualTo( p.getStockQuantity() );
    assertThat( product.getPrice() ).isEqualTo( p.getPrice() );
    assertThat( product.getName() ).isEqualTo( p.getName() );
    assertThat( product.getDescription() ).isEqualTo( p.getDescription() );
  }
  
  @Test
  @Order(3)
  void WhenFindByIdAndValidNameThenReturnProduct() {
    Optional<Product> optionalProduct = productRepository.findByIdAndName( 5L, p.getName() );
    assertThat( optionalProduct ).isPresent();
    
    Product product = optionalProduct.get();
    assertThat( product.getStockQuantity() ).isEqualTo( p.getStockQuantity() );
    assertThat( product.getPrice() ).isEqualTo( p.getPrice() );
    assertThat( product.getName() ).isEqualTo( p.getName() );
    assertThat( product.getDescription() ).isEqualTo( p.getDescription() );
  }
  
  @Test
  @Order(4)
  void testWhenFindByInvalidIdReturnOptionalEmpty() {
    Optional<Product> optionalProduct = productRepository.findById( 2L );
    assertThat( optionalProduct ).isEmpty();
  }
  
  
  @Test
  @Order(5)
  void testWhenFindByInvalidNameReturnOptionalEmpty() {
    Optional<Product> optionalProduct = productRepository.findByName( "Gertrudes" );
    assertThat( optionalProduct ).isEmpty();
  }
  
  @Test
  @Order(6)
  void testWhenFindByInvalidIdAndNameReturnOptionalEmpty() {
    Optional<Product> optionalProduct = productRepository.findByIdAndName( 1L, "Gertrudes" );
    assertThat( optionalProduct ).isEmpty();
  }
  
  
  private Product createProduct( int someValue ) {
    Product product = new Product();
    product.setPrice( 9.99 + 2 * someValue );
    product.setName( "Kinder Bueno Ice Cream " + someValue );
    product.setStockQuantity( 10 * someValue );
    product.setDescription( "Not the previous description damn" );
    return product;
  }
}
