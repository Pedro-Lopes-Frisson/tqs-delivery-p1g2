package ua.tqs.delivera.IntegrationTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.tqs.delivera.models.Admin;
import ua.tqs.delivera.repositories.AdminRepository;

import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AdminIT {
  
  
  @LocalServerPort
  int randomServerPort;
  
  @Autowired
  private AdminRepository adminRepository;
  
  private String token;
  private Admin admin;
  
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
  public void setUp() {
    admin = new Admin();
    admin.setName( "Joaquim" );
    admin.setPassword( "safepassword" );
    admin.setEmail( "joaquim@ua.pt" );
    
    admin = adminRepository.saveAndFlush( admin );
  }
  
  @AfterEach
  public void resetDb() {
    adminRepository.deleteAll();
    adminRepository.flush();
  }
  
  @Test
  void testWhenValidEmailIsRequestedThen_SendCorrectAdminObject() {
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .get( createURL() + "/{email}", "joaquim@ua.pt" ).then().assertThat()
               .log().body()
               .body( "name", equalTo( admin.getName() ) ).and()
               .body( "email", equalTo( admin.getEmail() ) ).and()
               .body( "password", equalTo( admin.getPassword() ) ).and()
               .contentType( ContentType.JSON ).and().statusCode( 200 );
  }
  
  
  @Test
  void testWhenInvalidEmailIsRequestedThen_SendBadRequest() {
    RestAssured.given()
               .contentType( "application/json" )
               .when()
               .get( createURL() + "/{email}", "iamnotaadmin@ua.pt" ).then().assertThat()
               .log().body()
               .and().statusCode( 400 );
  }
  
  String createURL() {
    return "http://localhost:" + randomServerPort + "/api/v1/admin";
  }
}
