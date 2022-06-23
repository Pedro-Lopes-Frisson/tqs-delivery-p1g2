package ua.tqs.delivera.services;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.datamodels.ReviewDTO;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.exceptions.OrderDoesnotExistException;
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
  
  @Autowired
  RiderService riderService;
  

  public Order createOrder(OrderDTO orderDto) {
    
    Optional<Store> orderStore = storeRepository.findById(orderDto.getStore().getId());
    if(orderStore.isEmpty()) {
      return null;
    }

    Order order = new Order(orderDto);
    return orderRepository.save(order);
    //return null;
  }
  
  public boolean reviewOrder( long orderId, ReviewDTO reviewDTO ) throws OrderDoesnotExistException,
    NonExistentResource {
    // get order
    Optional<Order> orderOptional = orderRepository.findById( orderId );
    if ( orderOptional.isEmpty() ) {
      log.error( "Order Does not exist" );
      throw new OrderDoesnotExistException( "Order Does not exist" );
    }
    // get riderId
    log.info( "Order -> {}", orderOptional.get() );
    riderService.reviewRider( orderOptional.get().getOrderProfit().getRider().getRiderId(), 4D );
    // call riderService
    // return True if a rider is returned false otherwise
    return true;
  }
  
}
