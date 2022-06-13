package ua.tqs.delivera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Rider;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
