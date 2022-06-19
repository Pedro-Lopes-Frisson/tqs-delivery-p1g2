package ua.tqs.delivera.repositoryTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.repositories.LocationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
//To use the same datasource as the regular application
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LocationRepository locationRepo;

    private Location location;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:12")
		.withUsername("postgres")
		.withPassword("secret")
		.withDatabaseName("delivera");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeEach
    void setup(){
        location = new Location(40.85, 25.9999);
    }

    @Order(1)
    @Test
    void whenSaveValidLocation_thenReturnLocation(){
        Location location_saved = entityManager.persistAndFlush(location);
        assertThat(location_saved).isEqualTo(location);
    }

    @Order(2)
    @Test
    void whenFindRiderByExistingId_thenReturnRider(){
        entityManager.persistAndFlush(location);

        Location foundLocation = locationRepo.findById(location.getId()).get();
        assertThat(foundLocation).isEqualTo(location);
    }

    @Order(3)
    @Test
    void whenInvalidId_thenReturnNull(){
        Location location_saved = locationRepo.findById(-1L).orElse(null);
        assertThat(location_saved).isNull();
    }
}
