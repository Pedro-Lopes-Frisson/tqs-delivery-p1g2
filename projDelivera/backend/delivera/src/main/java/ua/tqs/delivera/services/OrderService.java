package ua.tqs.delivera.services;

import org.springframework.beans.factory.annotation.Autowired;
import ua.tqs.delivera.repositories.OrderRepository;

public class OrderService {
  @Autowired
  OrderRepository orderRepository;
}
