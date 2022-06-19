package ua.tqs.delivera.repositoryTests;

import org.junit.jupiter.api.AfterEach;
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
import ua.tqs.delivera.models.Admin;
import ua.tqs.delivera.repositories.AdminRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
//To use the same datasource as the regular application
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminRepositoryTest {
  
  @Autowired
  private TestEntityManager entityManager;
  
  @Autowired
  private AdminRepository adminRepository;
  
  private Admin admin;
  
  @Container
  public static PostgreSQLContainer container = new PostgreSQLContainer( "postgres:11.12" )
    .withUsername( "postgres" )
    .withPassword( "secret" )
    .withDatabaseName( "delivera" );
  
  @DynamicPropertySource
  static void properties( DynamicPropertyRegistry registry ) {
    registry.add( "spring.datasource.url", container::getJdbcUrl );
    registry.add( "spring.datasource.password", container::getPassword );
    registry.add( "spring.datasource.username", container::getUsername );
  }
  
  @BeforeEach
  void setUp() {
    admin =
      Admin.builder().email( "admin.Alfredo@delivera.pt" ).name( "Alfredo Ferreira" ).password( "safepassword" )
           .build();
  }
  
  @AfterEach
  void tearDown() {
    adminRepository.deleteAll();
    entityManager.flush();
  }
  
  @Test
  void testWhenFindByValidAndUsedEmail_ThenReturnValidAdmin() {
    entityManager.persistAndFlush( admin );
    
    Optional<Admin> adminFromDb = adminRepository.findByEmail( admin.getEmail() );
    assertThat( adminFromDb ).isPresent();
    Admin adminDB = adminFromDb.get();
    assertThat( adminDB.getEmail() ).isEqualTo( admin.getEmail() );
    assertThat( adminDB.getName() ).isEqualTo( admin.getName() );
    assertThat( adminDB.getPassword() ).isEqualTo( admin.getPassword() );
  }
  @Test
  void testWhenFindByUnusedEmail_ThenReturnEmptyOptionalObject(){
    Optional<Admin>  adminOptional = adminRepository.findByEmail( admin.getEmail() );
    assertThat( adminOptional ).isEmpty();
  }
  
  
}
