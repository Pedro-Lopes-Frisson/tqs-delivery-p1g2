package ua.tqs.delivera.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.services.OrderService;

@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin
@Log4j2
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<Order> createOrder( @RequestBody OrderDTO orderDTO){
        // verificar se store existe
        Order order = orderService.createOrder(orderDTO);
        if(order == null) {
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
        }
        return ResponseEntity.status( HttpStatus.CREATED ).body( order );
    }

    @PutMapping("/{id}")
    public ResponseEntity updateOrderStatus(@PathVariable Long id){
        try{
            orderService.updateOrderState(id);
        }catch(NonExistentResource e){
            log.error( "Error: Order not Found" );
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
        log.info( " Order status updated ");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
