package ua.tqs.frostini.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.tqs.frostini.datamodels.OrderDTO;
import ua.tqs.frostini.datamodels.ReviewDTO;
import ua.tqs.frostini.exceptions.FailedToReviewOrder;
import ua.tqs.frostini.exceptions.IncompleteOrderPlacement;
import ua.tqs.frostini.exceptions.IncompleteOrderReviewException;
import ua.tqs.frostini.exceptions.ResourceNotFoundException;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.service.OrderService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1")
@Validated
@CrossOrigin
@Log4j2
public class OrderController {
  
  @Autowired OrderService orderService;
  
  @GetMapping("/order/user/{userId}")
  public ResponseEntity<List<Order>> getAllOrdersByUser( @PathVariable long userId ) {
    
    ArrayList<Order> orderArrayList = (ArrayList<Order>) orderService.getAllOrdersByUser( userId );
    
    if ( orderArrayList.size() == 0 ) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }
    
    return ResponseEntity.status( HttpStatus.OK ).body( orderArrayList );
  }
  
  
  @GetMapping("/order/{id}")
  public ResponseEntity<Object> getOrder( @PathVariable long id ) {
    Order order = orderService.trackOrder( id );
    if ( order == null ) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }
    
    return ResponseEntity.status( HttpStatus.OK ).body( order );
  }
  
  @PutMapping("/order/{id}")
  public ResponseEntity<Object> updateOrderState( @PathVariable long id ) {
    Order order = orderService.updateOrderState( id );
    if ( order == null ) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }
    
    return ResponseEntity.status( HttpStatus.OK ).body( order );
  }
  
  
  @PostMapping("/order")
  public ResponseEntity<Order> makeOrder( @Valid @RequestBody OrderDTO orderDTO ) {
    Order orderPlaced = null;
    try {
      orderPlaced = orderService.placeOrder( orderDTO );
    } catch (IncompleteOrderPlacement e) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }
    return ResponseEntity.status( HttpStatus.CREATED ).body( orderPlaced );
  }
  
  
  @PutMapping("/order/review/{orderId}")
  public ResponseEntity<Object> reviewOrder( @Valid @RequestBody ReviewDTO reviewDTO, @PathVariable long orderId ) {
    long status;
    try {
      status = orderService.reviewOrder( orderId, reviewDTO );
    } catch (ResourceNotFoundException e) {
      log.info( "Resource Not Found" );
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    } catch (IncompleteOrderReviewException e) {
      log.info( "REQUEST Failed" );
      return ResponseEntity.status( HttpStatus.EXPECTATION_FAILED ).body( null );
    }
    log.info("Status Code: {}", status);
    return ResponseEntity.status( HttpStatus.OK ).body( null );
  }
  
}
