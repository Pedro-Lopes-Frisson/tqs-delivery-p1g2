package ua.tqs.delivera.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.datamodels.RiderDTO;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.services.RiderService;

@RestController
@RequestMapping("/api/delivera")
public class DeliveraController {
    @Autowired
    private RiderService riderService;
    
    @PostMapping("/rider")
    public ResponseEntity<Rider> createRider(@RequestBody RiderDTO riderDTO){
        Rider rider = new Rider(riderDTO);
        Rider saved = riderService.saveRider(rider);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/stats/rider/{id}")
    public ResponseEntity<Map<String, Object>> getRiderStats(@PathVariable Long id) {
        try {
            Map<String, Object> result = riderService.getRiderStatistics(id);
            return ResponseEntity.status( HttpStatus.OK ).body( result );
        } catch (NonExistentResource e) {
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
        }
    }
    
}
