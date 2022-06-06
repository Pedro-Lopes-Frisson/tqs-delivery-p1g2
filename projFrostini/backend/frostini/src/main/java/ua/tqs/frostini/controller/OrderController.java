package ua.tqs.frostini.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.tqs.frostini.datamodels.OrderDTO;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.service.OrderService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1")
@Validated
@CrossOrigin("http://localhost:3000")
public class OrderController {

  @Autowired OrderService orderService;

  @GetMapping("/order/user/{userId}")
  public ResponseEntity<List<Order>> getAllOrdersByUser( @PathVariable long userId ) {

    ArrayList<Order> orderArrayList = (ArrayList<Order>) orderService.getAllOrdersByUser( userId );

    if ( orderArrayList.isEmpty() ) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }

    return ResponseEntity.status( HttpStatus.OK ).body( orderArrayList );
  }


  @GetMapping("/order/{id}")
  public ResponseEntity<Object> getOrder( @PathVariable long id ) {
    Order order = orderService.trackOrder( id );
    if (order == null){
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body(  null);
    }

    return ResponseEntity.status( HttpStatus.OK ).body( order );
  }


  @PostMapping("/order")
  public ResponseEntity<Order> makeOrder( @Valid @RequestBody OrderDTO orderDTO ) {
    Order orderPlaced = orderService.placeOrder( orderDTO );
    return ResponseEntity.status( HttpStatus.CREATED ).body( orderPlaced );
  }

}
