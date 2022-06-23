package ua.tqs.delivera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.OrderRepository;

import java.util.List;

@Service
public class OrderService {
  
  @Autowired
  OrderRepository orderRepository;

  public Order createOrder(Order order) {
    return null;
  }
  
}
