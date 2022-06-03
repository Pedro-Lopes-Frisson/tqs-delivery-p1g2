package ua.tqs.frostini.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.frostini.models.OrderedProduct;
import ua.tqs.frostini.models.emddedIds.OrderProductEmbeddedId;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, OrderProductEmbeddedId> {
}
