package ua.tqs.delivera.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.tqs.delivera.models.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long>{

    Rider findByEmail(String email);

    @Query("SELECT AVG(1.0*r.sumOfReviews/r.numberOfReviews) FROM  Rider r WHERE r.numberOfReviews <> 0")
    Double getAverageRiderRating();
    
    
}
