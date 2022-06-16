package ua.tqs.delivera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.delivera.models.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
