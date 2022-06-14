package ua.tqs.delivera.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.RiderRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class RiderService {
  @Autowired
  private RiderRepository riderRepo;

  //create rider
  public Rider saveRider( Rider rider ) {
    System.out.println( riderRepo.findByEmail( rider.getEmail() ) );
    if ( ! riderRepo.findByEmail( rider.getEmail() ).isPresent() ) {return riderRepo.save( rider );}
    throw new DuplicateKeyException( "Email already in use" );
  }

  public Rider findById( long id ) throws NonExistentResource {
    Optional<Rider> optionalRider = riderRepo.findById( id );
    if ( optionalRider.isEmpty() ) {
      throw new NonExistentResource( "This rider does not exist!" );
    }

    return optionalRider.get();
  }

  /*
   * Get All orders for rider with id = riderID
   * return a List of orders
   *
   */
  public List<Order> getAllOrdersForRider( Long riderID ) throws NonExistentResource {
    log.info( "Id rider : {}", riderID );
    Rider rider = findById( riderID );

    log.info( "Rider was found compiling a list of Orders. {}",
      rider.getOrderProfits().stream().map( OrderProfit::getOrder )
           .collect( Collectors.toList() ) );

    return rider.getOrderProfits().stream().map( OrderProfit::getOrder ).collect( Collectors.toList() );
  }

  public Order getOrder( Long id, Long orderId ) throws NonExistentResource {
    Rider rider = findById( id );
    log.info( "Rider was found compiling a list of Orders. {}",
      rider.getOrderProfits().stream().map( OrderProfit::getOrder ).map( Order::getId )
           .collect( Collectors.toList() ) );

    List<Order> orderList = rider.getOrderProfits().stream().map( OrderProfit::getOrder ).filter( ( o ) -> {
        return Objects.equals(
          o.getId(), orderId
        );
      }
    ).collect( Collectors.toList() );

    if ( orderList.size() > 1 ) {
      log.info( orderList );
      throw new RuntimeException( "Multiple Values detected" );
      // this should never happen because orderId is a primary key
    }

    if ( orderList.isEmpty() ) {
      throw new NonExistentResource( "This Order id is not of this rider" );
      // this only happens if an orderID is not of that rider or if it does not exist
    }

    return  orderList.get( 0 );

  }
}
