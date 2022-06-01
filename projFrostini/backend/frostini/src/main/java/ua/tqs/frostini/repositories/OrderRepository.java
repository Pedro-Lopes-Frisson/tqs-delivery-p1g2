package ua.tqs.frostini.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.frostini.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
