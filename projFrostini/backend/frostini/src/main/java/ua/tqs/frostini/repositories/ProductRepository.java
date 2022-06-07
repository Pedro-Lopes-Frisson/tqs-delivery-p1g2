package ua.tqs.frostini.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.frostini.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
