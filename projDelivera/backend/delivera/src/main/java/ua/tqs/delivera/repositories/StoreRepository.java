package ua.tqs.delivera.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.delivera.models.Store;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
  Optional<Store> findById( Long id );
  Optional<Store> findByName( String storeName );
}
