package ua.tqs.delivera.integrationtests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.LocationRepository;
import ua.tqs.delivera.repositories.RiderRepository;

@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class IntegrationTest {

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

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RiderRepository riderRepo;

    @Autowired
    private LocationRepository locationRepo;

    @AfterEach
    public void resetDb() {
        riderRepo.deleteAll();
    }

    @Test
     void whenValidInput_thenCreateCar() {
        Rider rider = createTestRider("ma@gmail.com","Manuel Antunes", "migant", true, createTestLocation(40.85, 25.9999), 0, 0);;
        ResponseEntity<Rider> entity = restTemplate.postForEntity("/api/delivera/rider", rider, Rider.class);

        List<Rider> found = riderRepo.findAll();
        assertThat(found).extracting(Rider::getEmail).containsOnly(rider.getEmail());
    }

    private Location createTestLocation(double lat, double lon){
        Location location = new Location(lat, lon);
        locationRepo.saveAndFlush(location);
        return location;
    }

    private Rider createTestRider(String email, String name, String password, boolean available, Location currentLocation, int numberOfReviews, int sumOfReviews) {
        Rider rider = new Rider(email, name, password, available, currentLocation, numberOfReviews, sumOfReviews);
        riderRepo.saveAndFlush(rider);
        return rider;
    }
    
}
