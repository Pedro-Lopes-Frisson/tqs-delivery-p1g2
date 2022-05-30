package ua.tqs.delivera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ua.tqs.delivera.repositories.RiderRepository;

@DataJpaTest
public class RiderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RiderRepository riderRepo;
}
