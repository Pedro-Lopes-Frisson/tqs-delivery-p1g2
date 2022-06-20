package ua.tqs.delivera;

import org.aspectj.weaver.ast.Or;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.tqs.delivera.models.*;
import ua.tqs.delivera.repositories.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration public class LoadDatabase {
  @Bean CommandLineRunner initDatabase( AdminRepository adminRepository, RiderRepository riderRepository,
                                        OrderRepository orderRepository, LocationRepository locationRepository,
                                        StoreRepository storeRepository, OrderProfitRepository orderProfitRepository ) {
    return args -> {
      Admin admin = Admin.builder().email( "email@ua.pt" ).password( "password" ).name( "Admin1" ).build();
      
      adminRepository.save( admin );
      // save Admin
      
      
      Location location = createTestLocation( 12, 12 );
      Location location1 = createTestLocation( 15, 12 );
      Location location2 = createTestLocation( 22, 22 );
      Location location3 = createTestLocation( 42, 32 );
      Location frostiniLoc = new Location( 11, 1 );
      List<Location> locationList = Arrays.asList( location, location1, location2, location3 ); // save
      
      locationRepository.saveAll( Arrays.asList( location, location1, location2, location3, frostiniLoc ) ); // save
      // all Locations
      
      Store frostini = Store.builder().name( "Frostini" ).address( frostiniLoc ).build();
      frostini = storeRepository.save( frostini ); // save store
      
      List<OrderProfit> orderProfitList1 = createOrderProfitList( 1 );
      List<OrderProfit> orderProfitList2 = createOrderProfitList( 2 );
      List<OrderProfit> orderProfitList3 = createOrderProfitList( 3 );
      List<OrderProfit> orderProfitList4 = createOrderProfitList( 4 );
      
      List<Order> orderList1 = createOrder();
      List<Order> orderList2 = createOrder();
      List<Order> orderList3 = createOrder();
      List<Order> orderList4 = createOrder();
      
      List<List<OrderProfit>> listOfListOrderProfit = Arrays.asList(
        orderProfitList1,
        orderProfitList2,
        orderProfitList3,
        orderProfitList4
      );
      
      List<List<Order>> listOfListOrder = Arrays.asList(
        orderList1,
        orderList2,
        orderList3,
        orderList4
      );
      
      listOfListOrder.forEach( orderRepository::saveAllAndFlush ); //save All orders
      listOfListOrderProfit.forEach( orderProfitRepository::saveAllAndFlush );  //save All orders_Profits
      
      
      for (int i = 0 ; i < listOfListOrderProfit.size() ; i++) {
        for (int j = 0 ; j < orderList1.size() ; j++) {
          listOfListOrder.get( i ).get( j ).setOrderProfit( listOfListOrderProfit.get( i ).get( j ) );
          listOfListOrder.get( i ).get( j ).setCurrentLocation( locationList.get( i ) );
          listOfListOrder.get( i ).get( j ).setStore( frostini );
        }
      } // set Order_profit to Order
      
      
      listOfListOrder.forEach( orderRepository::saveAllAndFlush ); // update list of orders having in account the
      // OrderProfit
      
      
      Rider rider1 =
        createTestRider( "pdfl@ua.pt", "Pedro", "pedro", true, location, 10, 10, 1 );
      Rider rider2 =
        createTestRider( "pdfl2@ua.pt", "Pedro2", "pedro", true, location1, 10, 10, 2 );
      Rider rider3 =
        createTestRider( "pdfl3@ua.pt", "Pedro3", "pedro", true, location2, 10, 10, 3 );
      Rider rider4 =
        createTestRider( "pdfl4@ua.pt", "Pedro4", "pedro", true, location3, 10, 10, 4 );
      
      
      List<Rider> riderList = Arrays.asList( rider1, rider2, rider3, rider4 );
      
      for (int i = 0 ; i < listOfListOrderProfit.size() ; i++) {
        riderList.get( i ).setOrderProfits( listOfListOrderProfit.get( i ) );
      }
      
      
      riderRepository.saveAll( riderList );
      //save all Riders
      
      for (int i = 0 ; i < listOfListOrderProfit.size() ; i++) {
        for (int j = 0 ; j < orderList1.size() ; j++) {
          listOfListOrderProfit.get( i ).get( j ).setOrder( listOfListOrder.get( i ).get( j ) );
          listOfListOrderProfit.get( i ).get( j ).setRider( riderList.get( i ) );
        }
      }
      // setOrder To OrderProfit
      listOfListOrderProfit.forEach( orderProfitRepository::saveAllAndFlush ); // update list of ordersProfits having
      // in account the Order
      
      
    }
      ;
  }
  
  
  private Location createTestLocation( double lat, double lon ) {
    return new Location( lat, lon );
  }
  
  private Rider createTestRider( String email, String name, String password, boolean available,
                                 Location currentLocation, int numberOfReviews, int sumOfReviews, long someValue ) {
    return new Rider( email, name, password, available, currentLocation, numberOfReviews, sumOfReviews );
  }
  
  private List<OrderProfit> createOrderProfitList( long someValue ) {
    OrderProfit orderProfit = new OrderProfit();
    orderProfit.setOrderPrice( 200.12 * someValue );
    
    OrderProfit orderProfit1 = new OrderProfit();
    orderProfit1.setOrderPrice( 100.12 * someValue );
    
    return Arrays.asList( orderProfit, orderProfit1 );
  }
  
  private List<Order> createOrder() {
    Order o = new Order();
    o.setClientLocation( "16,19" );
    o.setExternalId( 1L );
    
    Order o1 = new Order();
    o1.setClientLocation( "12,19" );
    o1.setExternalId( 1L );
    return Arrays.asList( o, o1 );
  }
  
  Store createStore( String name ) {
    return Store.builder().name( "Frostini" ).address( createTestLocation( 12, 10 ) ).build();
  }
}
