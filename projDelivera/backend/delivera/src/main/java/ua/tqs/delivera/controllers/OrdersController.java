package ua.tqs.delivera.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import ua.tqs.delivera.models.Order;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
@Log4j2
public class OrdersController {
    @PostMapping
    public ResponseEntity<Order> createOrder( @RequestBody Order orderDTO){
        return null;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id){
        return null;
    }
}
