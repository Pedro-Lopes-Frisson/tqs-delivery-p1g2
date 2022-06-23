package ua.tqs.delivera.services;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.datamodels.OrderDTO;
import ua.tqs.delivera.datamodels.ReviewDTO;
import ua.tqs.delivera.exceptions.NoRidersAvailable;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.exceptions.OrderDoesnotExistException;
import ua.tqs.delivera.models.*;
import ua.tqs.delivera.repositories.LocationRepository;
import ua.tqs.delivera.repositories.OrderProfitRepository;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.repositories.StoreRepository;
import ua.tqs.delivera.utils.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class OrderService {
  
  
  @Autowired
  OrderRepository orderRepository;
  
  @Autowired
  RiderService riderService;  // manageRiders
  
  @Autowired
  StoreRepository storeRepository; // create and look for stores
  
  @Autowired
  LocationRepository locationRepository; // create update find
  
  @Autowired
  OrderProfitRepository orderProfitRepository;
  
  ArrayList<Order> notAttributedOrders = new ArrayList<>();
  
  
  @Transactional
  public Order assignOrder( OrderDTO orderDTO ) {
    // get store
    String storeName = orderDTO.getStoreName();
    
    // create Store Location of the order
    Location storeLocation = new Location( orderDTO.getStoreLat(), orderDTO.getStoreLon() );
  
    storeLocation = locationRepository.save( storeLocation );
    // create Client Location of the order
    Location clientLocation = new Location( orderDTO.getClientLat(), orderDTO.getClientLon() );
    clientLocation = locationRepository.save( clientLocation );
    
    Optional<Store> storeOPT = storeRepository.findByName( storeName );
    Store store = null;
    
    if ( storeOPT.isEmpty() ) {
      Store storeToSave = new Store();
  
      storeToSave.setName( storeName );
      storeToSave.setAddress( storeLocation );
      
      store = storeRepository.save( storeToSave );
    }
    
    // Store exists
    if ( store == null ) {
      store = storeOPT.get();
    }
    // confirm store Location
    if ( ! storeLocation.equals( store.getAddress() ) ) {
      // update store Location
      store.setAddress( storeLocation );
    }
    
    // calculate distance between the 2 locations
    double distance = DistanceCalculator.distanceBetweenPointsOnEarth( clientLocation, storeLocation );
    
    // create the OrderProfit
    OrderProfit orderProfit = new OrderProfit();
    orderProfit = orderProfitRepository.save( orderProfit );
    
    // distance * avg_motorcycle fuel consumption spent in 100 km / 100 km *
    // price of the gas * a percentage of the price of the order so that it is lucrative for the rider
    orderProfit.setOrderPrice( distance * 5 / 100 * 2 * .15 * orderDTO.getOrderPrice() );
    
    Order order = new Order();
    
    Rider riderToAssign = null;
    // get free riders and determine who is closer of the store
    try {
      riderToAssign = riderService.findClosestRider( storeLocation );
    } catch (NoRidersAvailable e) {
      //queue Order
      notAttributedOrders.add( order );
      return null;
    }
  
    order.setClientLocation( clientLocation.getLatitude() + "," + clientLocation.getLongitude() );
    order.setExternalId( orderDTO.getOrderStoreId() );
    order = orderRepository.save( order );
    order.setOrderProfit( orderProfit );
    order.setStore( store );
    
    orderProfit.setOrder( order );
    orderProfit.setRider( riderToAssign );
    
    riderToAssign = riderService.makeRiderUnavailable( riderToAssign );
    riderService.addProfitToRider( riderToAssign, orderProfit );
    orderProfit = orderProfitRepository.save( orderProfit );
    
    order.setOrderProfit( orderProfit );
    // set all fields and save new order
    // change Rider availability
    return orderRepository.save( order );
  }
  
  
  public boolean reviewOrder( long orderId, ReviewDTO reviewDTO ) throws OrderDoesnotExistException,
    NonExistentResource {
    // get order
    Optional<Order> orderOptional = orderRepository.findById( orderId );
    if ( orderOptional.isEmpty() ) {
      log.error( "Order Does not exist" );
      throw new OrderDoesnotExistException( "Order Does not exist" );
    }
    // get riderId
    log.info( "Order -> {}", orderOptional.get() );
    riderService.reviewRider( orderOptional.get().getOrderProfit().getRider().getRiderId(), 4D );
    // call riderService
    // return True if a rider is returned false otherwise
    return true;
    
  }
  
  // ordered > in transit > delived
  private static final String ORDERED = "ordered";
  private static final String IN_TRANSIT = "in transit";
  private static final String DELIVERED = "delivered";
  
  public String changeState( String state ) throws Exception {
    String newState;
    if ( state == null ) {newState = ORDERED;}
    else if ( state.equals( ORDERED ) ) {newState = IN_TRANSIT;}
    else if ( state.equals( IN_TRANSIT ) ) {newState = DELIVERED;}
    else if ( state.equals( DELIVERED ) ) {return state;}
    else {throw new Exception( "Can't assign a state." );}
    return newState;
  }
  
  
  public boolean updateOrderState( long id ) throws NonExistentResource {
    Optional<Order> res = orderRepository.findById( id );
    if ( res.isEmpty() ) {throw new NonExistentResource( "Order not existent" );}
    Order order = res.get();
    try {
      order.setOrderState( changeState( order.getOrderState() ) );
    } catch (Exception e) {
      log.info( e.getMessage() );
      return false;
    }
    return true;
  }
  
}
