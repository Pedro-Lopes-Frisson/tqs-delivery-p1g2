package ua.tqs.frostini.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.frostini.models.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Override Optional<Product> findById( Long aLong );
  Optional<Product> findByName( String name );
  
  Optional<Product> findByIdAndName( long productId, String name );
}
