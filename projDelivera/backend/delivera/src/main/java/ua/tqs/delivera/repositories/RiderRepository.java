package ua.tqs.delivera.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.tqs.delivera.models.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long>{

    Optional<Rider> findByEmail(String email);
    
    
}
