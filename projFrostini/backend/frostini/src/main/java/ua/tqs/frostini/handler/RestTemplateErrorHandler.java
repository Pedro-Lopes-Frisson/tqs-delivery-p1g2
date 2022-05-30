package ua.tqs.frostini.handler;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

public class RestTemplateErrorHandler implements ResponseErrorHandler {
  
  @Override
  public boolean hasError(ClientHttpResponse httpResponse)
    throws IOException {
    
    return (
      httpResponse.getStatusCode().series() == CLIENT_ERROR
        || httpResponse.getStatusCode().series() == SERVER_ERROR);
  }
  
  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {
  
  }
}
