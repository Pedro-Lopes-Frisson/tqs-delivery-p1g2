package ua.tqs.delivera.services;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.repositories.OrderRepository;


@Service
public class OrderService {
  
  @Autowired
  OrderRepository orderRepository;

  public boolean updateOrderStatus(long id) throws NonExistentResource {
    Optional<Order> res = orderRepository.findById(id);
    if (res.isEmpty())
      throw new NonExistentResource("Order not existent");
    return true;
  }

  public Order createOrder(OrderDTO order) {
    return new Order();
  }
  
}
