package ua.tqs.delivera;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.RiderRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RiderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RiderRepository riderRepo;

    private Rider rider;
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
        rider = new Rider("Manuel Antunes", "migant", true, location, 0, 0);
    }

    @Order(1)
    @Test
    void whenSaveRider_thenReturnRider(){
        entityManager.persistAndFlush(location);
        Rider rider_saved = entityManager.persistAndFlush(rider);
        assertThat(rider_saved).isEqualTo(rider);
    }

    @Order(2)
    @Test
    void whenFindRiderByExistingId_thenReturnRider(){
        entityManager.persistAndFlush(location);
        entityManager.persistAndFlush(rider);

        Rider foundRider = riderRepo.findById(rider.getRiderId()).get();
        assertThat(foundRider).isEqualTo(rider);
    }

    @Order(3)
    @Test
    void whenInvalidId_thenReturnNull(){
        Rider rider_saved = riderRepo.findById(-1L).orElse(null);
        assertThat(rider_saved).isNull();
    }
}
