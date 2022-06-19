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
        entityManager.persistAndFlush(location);
        rider = new Rider("ma@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0);
    }

    @Order(1)
    @Test
    void whenSaveRider_thenReturnRider(){
        Rider rider_saved = entityManager.persistAndFlush(rider);
        assertThat(rider_saved).isEqualTo(rider);
    }

    @Order(2)
    @Test
    void whenFindRiderByExistingId_thenReturnRider(){
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

    @Order(5)
    @Test
    void whenGetAverageRidersReview_thenReturnDouble() {
        Rider rider1 = new Rider("mf@gmail.com", "Manuel Ferreira", "migferr", true, location, 10, 29);
        entityManager.persistAndFlush(rider1);

        Rider rider2 = new Rider("jt@gmail.com", "Joaquim Tereso", "password", true, location, 2, 6);
        entityManager.persistAndFlush(rider2); 

        Double average = riderRepo.getAverageRiderRating();
        assertThat(average).isEqualTo((double) (29/10.0 + 6/2.0)/2);
    }

    @Order(4)
    @Test
    void whenGetAverageRidersReviewWithNoReviews_thenReturnNull() {
        Double average = riderRepo.getAverageRiderRating();
        assertThat(average).isNull();
    }

    @Order(6)
    @Test
    void whenFindByExistingEmail_thenReturnRider(){
        entityManager.persistAndFlush(rider);
        Rider riderByEmail = riderRepo.findByEmail(rider.getEmail());
        assertThat(riderByEmail).isEqualTo(rider);
    }

    @Order(7)
    @Test
    void whenFindByWrongEmail_thenReturnNull(){
        Rider riderByEmail = riderRepo.findByEmail("invalid email");
        assertThat(riderByEmail).isNull();
    }
}
