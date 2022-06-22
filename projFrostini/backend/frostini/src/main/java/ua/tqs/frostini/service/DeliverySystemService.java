package ua.tqs.frostini.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.tqs.frostini.datamodels.OrderDTODelivera;
import ua.tqs.frostini.datamodels.ReviewDTO;
import ua.tqs.frostini.exceptions.FailedToPlaceOrderException;
import ua.tqs.frostini.exceptions.FailedToReviewOrder;
import ua.tqs.frostini.handler.RestTemplateErrorHandler;
import ua.tqs.frostini.models.OrderDelivera;

import java.util.Objects;

@Component
@Log4j2
public class DeliverySystemService {
  private RestTemplate restTemplate =  new RestTemplateBuilder().errorHandler( new RestTemplateErrorHandler() ).build();;
  
  
  private static StringBuilder host = new StringBuilder( "http://deliveraSpring:8080/api/v1" ); // api base URL
  
  
  public static void setHost( StringBuilder host ) {
    DeliverySystemService.host = host;
  }
  
  public OrderDelivera newOrder( OrderDTODelivera purchase ) throws FailedToPlaceOrderException {
    
    StringBuilder url = host.append( "/order" );
    
    ResponseEntity<OrderDelivera> response = restTemplate.exchange(
      url.toString(), HttpMethod.POST, new HttpEntity<>( purchase ),
      OrderDelivera.class );
    
    try {
      return Objects.requireNonNull( response.getBody() );
      
    } catch (NullPointerException e) {
      
      log.error( "Delivery System : Null id " );
      throw new FailedToPlaceOrderException( "Null serverOrderID" );
      
    }
    
  }
  
  public HttpStatus reviewOrder( ReviewDTO review ) throws FailedToReviewOrder {
    // review order Delivery process
    
    String url = host.append( "/order/" )
                     .append( review.getRiderId() )
                     .append( "/review" ).toString();
    
    ResponseEntity<HttpStatus> response = restTemplate.exchange(
      url, HttpMethod.PUT, new HttpEntity<>( review ),
      HttpStatus.class );
    
    log.info( "Delivery System Returned: " + response.getStatusCode() );
    if ( response.getStatusCode().value() != 200 ) {
      throw new FailedToReviewOrder( "Impossible to review order" );
    }
    
    return response.getStatusCode();
  }
  
}
