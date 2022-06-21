package ua.tqs.frostini.repositories;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

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

import ua.tqs.frostini.models.Address;
import ua.tqs.frostini.models.User;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryTest {

   @Container
   static PostgreSQLContainer container = new PostgreSQLContainer("postgres:11.12")
    .withUsername("demo")
    .withPassword("demopw")
    .withDatabaseName("shop");

   @DynamicPropertySource
   static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.password", container::getPassword);
    registry.add("spring.datasource.username", container::getUsername);
  }

  @Autowired
  private AddressRepository addressRepository;


  @Autowired
  private TestEntityManager testEntityManager;

  User u1;
  User u2;

  Address address1;

  @AfterEach
  void tearDown() {
    addressRepository.deleteAll();
    testEntityManager.clear();
  }

  @BeforeEach void setUp() {
    u1 = createUser( 1 );
    u2 = createUser( 2 );
    address1 = createAndSaveAddress( 1, u1 );
  }

  @Test
  void whenFindByValidUserAndAddress_ThenReturnAddress() {
    Optional<Address> optionalAddress = addressRepository.findById(1L);
    assertThat( optionalAddress ).isPresent();
    assertThat( optionalAddress.get().getLatitude() ).isEqualTo( address1.getLatitude() );
    assertThat( optionalAddress.get().getLongitude() ).isEqualTo( address1.getLongitude() );
  }

  @Test
  void whenFindByInvalidUserAndAddress_ThenReturnAddress() {
    testEntityManager.persist( u2 );
    Optional<Address> optionalAddress = addressRepository.findById(-1L);
    assertThat( optionalAddress ).isEmpty();
  }
   /* helpers */

   private User createUser( int i ) {
    User u = new User();
    u.setName( "Pedro" );
    u.setPassword( "safepassword" );
    u.setEmail( "pdfl" + i + "@ua.pt" );
    return u;
  }

  private Address createAndSaveAddress( int i, User u ) {
    testEntityManager.persist( u );

    Address address = new Address();
    address.setLatitude( 40.640506 );
    address.setLongitude( -8.653754 );
    testEntityManager.persistAndFlush( address );

    return address;
  }
}
