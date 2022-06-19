package ua.tqs.delivera.controllers;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import ua.tqs.delivera.datamodels.RiderDTO;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.services.RiderService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
@Log4j2
public class DeliveraController {
  @Autowired
  private RiderService riderService;


  @PostMapping("/rider")
  public ResponseEntity<Rider> createRider( @RequestBody RiderDTO riderDTO ) {
    Rider rider = new Rider( riderDTO );
    Rider saved = riderService.saveRider( rider );
    return new ResponseEntity<>( saved, HttpStatus.CREATED );
  }

  // @PostMapping("/rider/login")
  // public ResponseEntity<Rider> loginRider(@RequestBody RiderDTO riderDTO){
  //   Rider rider = new Rider( riderDTO );
  //   Rider saved = riderService.saveRider( rider );
  //   return new ResponseEntity<>( saved, HttpStatus.OK );
  // }

  // @GetMapping("/riders")
  // public ResponseEntity<List<Rider>> getAllRiders(){
  //   List<Rider> riders = riderService.getAllRiders();
  //   return new ResponseEntity<>(riders, HttpStatus.OK);
  // }

  @GetMapping("/rider/{id}/orders")
  public ResponseEntity<List<Order>> getAllOrdersForRider( @PathVariable Long id ) {
    // get rider with Rider service then user order service
    List<Order> orderList = null;
    try {
      orderList = riderService.getAllOrdersForRider( id );
    } catch (NonExistentResource e) {
      log.error( "Error: Rider not Found" );
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }

    log.info( " Response method returned -> {} ", orderList );
    return ResponseEntity.status( HttpStatus.OK ).body( orderList );
  }


  @GetMapping("/rider/{id}/orders/{orderId}")
  public ResponseEntity<Order> getAllOrdersForRider( @PathVariable Long id, @PathVariable Long orderId ) {
    // get rider with Rider service then user order service
    Order order = null;
    try {
      order = riderService.getOrder( id, orderId );
    } catch (NonExistentResource e) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }

    log.info( " Response method returned -> {} ", order );
    log.info( " Response method returned -> {} ", order.getStore() );
    return ResponseEntity.status( HttpStatus.OK ).body( order );
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
