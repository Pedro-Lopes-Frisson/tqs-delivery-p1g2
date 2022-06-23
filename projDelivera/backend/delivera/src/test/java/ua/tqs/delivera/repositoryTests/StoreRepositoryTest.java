package ua.tqs.delivera.repositoryTests;

import static org.assertj.core.api.Assertions.assertThat;

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

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Store;
import ua.tqs.delivera.repositories.StoreRepository;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StoreRepository storeRepo;

    private Store store;
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

        store = new Store();
        store.setName("Frostini");
        store.setAddress(location);
    }

    @Test
    void whenSaveStore_thenReturnStore() {
        Store storeSaved = entityManager.persistAndFlush(store);
        assertThat(storeSaved).isEqualTo(store);
    }

    @Test
    void whenFindStoreByExistingId_thenReturnStore() {
        entityManager.persistAndFlush(store);

        Store foundStore = storeRepo.findById(store.getId()).get();
        assertThat(foundStore).isEqualTo(store);
    }

    @Test
    void whenFindInvalidStoreId_thenReturnNull() {
        Store foundStore = storeRepo.findById(store.getId()).orElse(null);
        assertThat(foundStore).isNull();
    }
}
