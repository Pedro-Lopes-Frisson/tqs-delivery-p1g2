package ua.tqs.frostini.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.tqs.frostini.datamodels.ProductDTO;
import ua.tqs.frostini.models.Product;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    p = createProduct( 2 ) ;
    p = createProduct( 20 ) ;
  
  }
  
  @AfterEach
  void tearDown() {
    productRepository.deleteAll();
    testEntityManager.clear();
    
  }
  
  @Autowired
  private ProductRepository productRepository;
  
  
  @Autowired
  private TestEntityManager testEntityManager;
  
  
  private Product createProduct( int someValue ) {
    Product product = new Product();
    product.setPrice( 9.99 + 2 * someValue );
    product.setName( "Kinder Bueno Ice Cream " + someValue );
    product.setStockQuantity( 10 * someValue );
    product.setDescription( "Not the previous description damn" );
    return product;
  }
}
