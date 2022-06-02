package ua.tqs.delivera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.tqs.delivera.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{
    
}
