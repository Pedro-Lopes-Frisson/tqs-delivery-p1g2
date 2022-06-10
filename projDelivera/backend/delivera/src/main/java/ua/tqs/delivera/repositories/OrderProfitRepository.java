package ua.tqs.delivera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.delivera.models.OrderProfit;

public interface OrderProfitRepository extends JpaRepository<OrderProfit, Long> {
}
