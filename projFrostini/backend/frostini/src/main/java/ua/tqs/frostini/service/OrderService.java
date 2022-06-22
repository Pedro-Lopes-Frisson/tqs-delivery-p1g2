package ua.tqs.frostini.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.tqs.frostini.datamodels.OrderDTO;
import ua.tqs.frostini.datamodels.OrderedProductDTO;
import ua.tqs.frostini.models.*;
import ua.tqs.frostini.models.emddedIds.OrderProductEmbeddedId;
import ua.tqs.frostini.repositories.*;

import java.util.*;

@Service
@Log4j2
public class OrderService {
  
  @Autowired OrderRepository orderRepository;
  @Autowired UserRepository userRepository;
  @Autowired ProductRepository productRepository;
  @Autowired AddressRepository addressRepository;
  @Autowired OrderedProductRepository orderedProductRepository;
  
  public Order placeOrder( OrderDTO orderDTO ) {
    // We need to first save the order than retrieve it's id and the we save the orderProductList
    Order order = new Order();
    // Get Address and make sure the address exists
    Optional<Address> addressOptional = addressRepository.findById( orderDTO.getAddressId() );
    if ( addressOptional.isEmpty() ) {
      return null;
    }
    order.setAddress( addressOptional.get() );
    
    
    // Get User and make sure the user exists
    Optional<User> userOptional = userRepository.findById( orderDTO.getUserId() );
    if ( userOptional.isEmpty() ) {
      return null;
    }
    order.setUser( userOptional.get() );
    
    // cache products
    Map<Long, Product> productMap = new HashMap<>();
    //Calculate total price
    double price = 0;
    for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProductsList()) {
      Optional<Product> productFromDb = productRepository.findById( orderedProductDTO.getProductId() );
      //Make sure that each product is actually an existent item in our db
      if ( productFromDb.isEmpty() ) {
        return null;
      }
      Product product = productFromDb.get();
      productMap.put( product.getId(), product );
      price += product.getPrice() * orderedProductDTO.getQuantity();
    }

    order.setOrderMadeTimeStamp( System.currentTimeMillis() );
    
    order.setTotalPrice( price );
    
    //Finally Save order
    Order savedOrder = orderRepository.save( order );
    
    List<OrderedProduct> orderedProductList = new ArrayList<>();
    for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProductsList()) {
      // Retrieve from cache
      Product product = productMap.get( orderedProductDTO.getProductId() );
      
      //save orderedProduct in db
      orderedProductList.add( orderedProductRepository.save( new OrderedProduct( orderedProductDTO.getQuantity(),
        product.getPrice(),
        new OrderProductEmbeddedId( savedOrder.getId(), product.getId() ), savedOrder, product ) ) );
    }
    savedOrder.setOrderedProductList( orderedProductList );
    savedOrder.setOrderMadeTimeStamp( System.currentTimeMillis() / 1000L );
    return orderRepository.save( savedOrder );
  }
  
  public Order trackOrder( long orderId ) {
    
    Optional<Order> orderFromDb = orderRepository.findById( orderId );
    return orderFromDb.isEmpty() ? null : orderFromDb.get();
    
  }
  
  public List<Order> getAllOrdersByUser( long id ) {
    Optional<User> userFromDb = userRepository.findById( id );
    if ( userFromDb.isEmpty() ) {
      return new ArrayList<>();
    }
    return orderRepository.findAllByUser( userFromDb.get(), Pageable.unpaged() );
  }

  public Order updateOrderState( long orderId ) {
    // se estado for ordered -> in transit
    // se for in transit -> delivered
    // any other case -> erro
    Optional<Order> orderFromDb = orderRepository.findById( orderId );
    if(orderFromDb.isEmpty()) {
      return null;
    }

    Order order = orderFromDb.get();

    switch(order.getOrderState()) {
      case "ordered":
        order.setOrderState("in transit");
        break;
      case "in transit":
        order.setOrderState("delivered");
        break;
      default:
        return null;
    }

    orderRepository.save(order);

    return order;
  }
}
