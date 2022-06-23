package ua.tqs.delivera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.repositories.OrderRepository;


@Service
public class OrderService {
  
  @Autowired
  OrderRepository orderRepository;

  public boolean updateOrderStatus(long anyLong) throws NonExistentResource {

    //throw new NonExistentResource("Order not existent");
    return false;
  }
  
}
