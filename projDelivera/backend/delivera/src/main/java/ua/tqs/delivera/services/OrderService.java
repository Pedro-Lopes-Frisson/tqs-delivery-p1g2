package ua.tqs.delivera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.models.Store;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.repositories.StoreRepository;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class OrderService {

  @Autowired
  StoreRepository storeRepository;
  
  @Autowired
  OrderRepository orderRepository;

  public Order createOrder(OrderDTO orderDto) {
    
    Optional<Store> orderStore = storeRepository.findById(orderDto.getStore().getId());
    if(orderStore.isEmpty()) {
      return null;
    }

    Order order = new Order(orderDto);
    return orderRepository.save(order);
  }
  
}
