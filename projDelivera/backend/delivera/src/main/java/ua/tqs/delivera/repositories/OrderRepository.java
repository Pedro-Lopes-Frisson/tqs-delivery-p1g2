package ua.tqs.delivera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.delivera.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
