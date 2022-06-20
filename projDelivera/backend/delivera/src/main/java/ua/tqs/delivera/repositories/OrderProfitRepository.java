package ua.tqs.delivera.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;

public interface OrderProfitRepository extends JpaRepository<OrderProfit, Long> {
    Optional<List<OrderProfit>> findByRider(Rider rider);
}
