package ua.tqs.delivera.integrationtests;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIT {

    @LocalServerPort
    int randomServerPort;
    
    
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
}
