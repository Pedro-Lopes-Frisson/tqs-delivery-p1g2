package ua.tqs.delivera.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.tqs.delivera.exceptions.NoRidersAvailable;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.OrderProfitRepository;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.repositories.RiderRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class RiderService {
  @Autowired
  private RiderRepository riderRepo;

  @Autowired
  private OrderProfitRepository orderProfitRepo;

  @Autowired
  private OrderRepository orderRepo;

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

  public Map<String, Object> getRiderStatistics(long riderId) throws NonExistentResource {
    // check if rider exists
    Optional<Rider> rider = riderRepo.findById(riderId);
    if (rider.isEmpty()) {
        throw new NonExistentResource( "This rider does not exist!" );
    }

    Rider currentRider = rider.get();

    Map<String, Object> result = new HashMap<>();

    if(currentRider.getNumberOfReviews() != 0) {

        double average = (double) currentRider.getSumOfReviews()/currentRider.getNumberOfReviews();
        result.put("averageReviewValue", average);

    } else {
      result.put("averageReviewValue", 0.0);
    }

    Optional<List<OrderProfit>> ordersProfit = orderProfitRepo.findByRider(currentRider);

    result.put("totalRiderOrders", ordersProfit.isPresent() ? ordersProfit.get().size() : 0);

    int totalNumberOfOrdersDelivered = 0;
    if(ordersProfit.isPresent()) {
      for (OrderProfit profit : ordersProfit.get()) {
        System.out.println("STATE: " + profit.getOrder().getOrderState());
        if (profit.getOrder().getOrderState().equals("delivered")) {
          totalNumberOfOrdersDelivered++;
        }
        //orderRepo.findByIdAndOrderState(profit.getOrder().getId(), "delivered");
      }
    }
    result.put("totalNumberOfOrdersDelivered", totalNumberOfOrdersDelivered);

    return result;
  }

  /*
   * Get All orders for rider with id = riderID
   * return a List of orders
   *
   */
  public List<Order> getAllOrdersForRider( Long riderID ) throws NonExistentResource {
    //log.info( "Id rider : {}", riderID );
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
      //log.info( orderList );
      throw new RuntimeException( "Multiple Values detected" );
      // this should never happen because orderId is a primary key
    }

    if ( orderList.isEmpty() ) {
      throw new NonExistentResource( "This Order id is not of this rider" );
      // this only happens if an orderID is not of that rider or if it does not exist
    }

    return orderList.get( 0 );

  }

  /**
   * Taken from <a href="https://www.geeksforgeeks.org/program-distance-two-points-earth/">Source of this code</a>
   * Changes were applied to reflect practices defined in our QA Manual
   *
   * @param l1 first point
   * @param l2 second point
   * @return distance between 2 points in kilometers
   */
  private double distance( Location l1, Location l2 ) {

    // The math module contains a function
    // named toRadians which converts from
    // degrees to radians.
    double lon1 = Math.toRadians( l1.getLongitude() );
    double lon2 = Math.toRadians( l2.getLongitude() );
    double lat1 = Math.toRadians( l1.getLatitude() );
    double lat2 = Math.toRadians( l2.getLatitude() );

    // Haversine formula
    double dLon = lon2 - lon1;
    double dLat = lat2 - lat1;
    double a = Math.pow( Math.sin( dLat / 2 ), 2 )
      + Math.cos( lat1 ) * Math.cos( lat2 )
      * Math.pow( Math.sin( dLon / 2 ), 2 );

    double c = 2 * Math.asin( Math.sqrt( a ) );

    // Radius of earth in kilometers. Use 3956
    // for miles
    double r = 6371;

    // calculate the result
    return ( c * r );
  }

  public Rider makeRiderUnavailable( Rider rider ) {
    rider.setAvailable( false );
    return riderRepo.save( rider );
  }

  public Rider makeRiderAvailable( Rider rider ) {
    rider.setAvailable( true );
    return riderRepo.save( rider );
  }

  public void addProfitToRider( Rider rider, OrderProfit oProfit ) {
    rider.getOrderProfits().add( oProfit );
    riderRepo.save( rider );
  }

  public Rider findClosestRider( Location storeLocation ) throws NoRidersAvailable {
    List<Rider> riders =
      riderRepo.findAll().stream().filter( Rider::isAvailable ).collect( Collectors.toList() );

    List<Double> riderDistances =
      riders.stream().filter( Rider::isAvailable ).map( r -> distance( r.getlLocation(),
              storeLocation ) )
            .collect( Collectors.toList() );

    Optional<Double> smallestDistOpt = riderDistances.stream().min( Comparator.naturalOrder() );
    if ( smallestDistOpt.isEmpty() ) {
      throw new NoRidersAvailable( "There are no riders Available at this point in time" );
    }
    Double smallestDist = smallestDistOpt.get();

    return riders.get( riderDistances.indexOf( smallestDist ) );
  }
}
