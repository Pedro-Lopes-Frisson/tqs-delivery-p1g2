package ua.tqs.delivera.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.services.RiderService;

@RestController
@RequestMapping("/api/delivera")
public class DeliveraController {
    @Autowired
    private RiderService riderService;
    
    @PostMapping("/rider")
    public ResponseEntity<Rider> createRider(@RequestBody Rider rider){
        Rider saved = riderService.saveRider(rider);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
}
