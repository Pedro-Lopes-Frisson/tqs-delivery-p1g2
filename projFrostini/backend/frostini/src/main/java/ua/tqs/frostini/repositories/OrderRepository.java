package ua.tqs.frostini.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findByid( long id);
  List<Order> findAllByUser( User user, Pageable pageable);
  

}
