package ua.tqs.delivera.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.delivera.datamodels.ReviewDTO;
import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.exceptions.OrderDoesnotExistException;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Order;
import ua.tqs.delivera.models.OrderProfit;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.OrderRepository;
import ua.tqs.delivera.services.OrderService;
import ua.tqs.delivera.services.RiderService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  @Mock RiderService riderService;
  @Mock OrderRepository orderRepository;
  @InjectMocks OrderService orderService;
  
  Rider rider;
  Order order;
  
  
  @BeforeEach
  public void setUp() {
    rider = new Rider( "mal@gmail.com", "Manuel Antunes", "migant", true, new Location( 11, 22 ), 1, 4 );
    rider.setRiderId( 1L );
    order = new Order();
    order.setId( 1L );
    OrderProfit op = new OrderProfit();
    op.setRider( rider );
    order.setOrderProfit( op );
    
  }
  
  @Test
  void whenReviewOrderAndEverythingOkay_ThenReturnTrue() throws NonExistentResource, OrderDoesnotExistException {
    Mockito.when( orderRepository.findById( Mockito.anyLong() ) ).thenReturn( Optional.ofNullable( order ) );
    Mockito.when( riderService.reviewRider( Mockito.anyLong(), Mockito.anyDouble() ) ).thenReturn( rider );
    
    assertTrue( orderService.reviewOrder( order.getId(), new ReviewDTO( 1.0 ) ) );
    
    Mockito.verify( riderService, Mockito.times( 1 ) ).reviewRider( Mockito.anyLong(), Mockito.anyDouble() );
    Mockito.verify( orderRepository, Mockito.times( 1 ) ).findById( Mockito.anyLong() );
  }
  
  @Test
  void whenReviewOrderAndRiderDoesNoExist_ThenPropagateException() throws NonExistentResource {
    Mockito.when( orderRepository.findById( Mockito.anyLong() ) ).thenReturn( Optional.ofNullable( order ) );
    Mockito.when( riderService.reviewRider( Mockito.anyLong(), Mockito.anyDouble() ) )
           .thenThrow( NonExistentResource.class );
    
    assertThrows( NonExistentResource.class, () -> orderService.reviewOrder( order.getId(), new ReviewDTO( 3D ) ) );
    
    Mockito.verify( riderService, Mockito.times( 1 ) ).reviewRider( Mockito.anyLong(), Mockito.anyDouble() );
    Mockito.verify( orderRepository, Mockito.times( 1 ) ).findById( Mockito.anyLong() );
  }
  
  @Test
  void whenReviewOrderAndOrderDoesNoExist_ThenThrowExeption() throws OrderDoesnotExistException, NonExistentResource {
    Mockito.when( orderRepository.findById( Mockito.anyLong() ) ).thenReturn( Optional.empty() );
    
    assertThrows( OrderDoesnotExistException.class,
      () -> orderService.reviewOrder( order.getId(), new ReviewDTO( 3D ) ) );
    
    Mockito.verify( orderRepository, Mockito.times( 1 ) ).findById( Mockito.anyLong() );
  }
  
}
