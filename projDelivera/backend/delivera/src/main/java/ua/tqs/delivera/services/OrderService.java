package ua.tqs.delivera.services;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.models.Store;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.repositories.StoreRepository;

import java.util.List;

@Service
@Log4j2
public class OrderService {

  @Autowired
  StoreRepository storeRepository;
  
  @Autowired
  OrderRepository orderRepository;

  private static final String ORDERED = "ordered";
  private static final String IN_TRANSIT = "in transit";
  private static final String DELIVERED = "delivered";

  public boolean updateOrderState(long id) throws NonExistentResource {
    Optional<Order> res = orderRepository.findById(id);
    if (res.isEmpty())
      throw new NonExistentResource("Order not existent");
    Order order = res.get();
    try{
      order.setOrderState(changeState(order.getOrderState()));
    }
    catch(Exception e){
      log.info(e.getMessage());
      return false;
    }
    return true;
  }

  public Order createOrder(OrderDTO orderDto) {
    
    Optional<Store> orderStore = storeRepository.findById(orderDto.getStore().getId());
    if(orderStore.isEmpty()) {
      return null;
    }

    Order order = new Order(orderDto);
    return orderRepository.save(order);
  }
  // ordered > in transit > delived

  public String changeState(String state) throws Exception{
    String newState;
    if (state==null)
      newState = ORDERED;
    else if (state.equals(ORDERED))
      newState = IN_TRANSIT;
    else if (state.equals(IN_TRANSIT))
      newState = DELIVERED;
    else if (state.equals(DELIVERED))
      return state;
    else
      throw new Exception("Can't assign a state.");
    return newState;
  }
  
}
