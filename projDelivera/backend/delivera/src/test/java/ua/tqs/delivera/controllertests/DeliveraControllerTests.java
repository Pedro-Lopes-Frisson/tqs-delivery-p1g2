package ua.tqs.delivera.controllertests;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.tqs.delivera.controllers.DeliveraController;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.datamodels.LocationDTO;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.datamodels.RiderDTO;
import ua.tqs.delivera.services.RiderService;

@WebMvcTest(DeliveraController.class)
class DeliveraControllerTests {
  
  @Autowired
  private MockMvc mvnForTests;
  
  @MockBean
  private RiderService riderService;
  
  private RiderDTO riderDTO;
  private Rider rider;
  private LocationDTO locationDTO;
  private Location location;
  
  @BeforeEach
  public void setUp() {
    locationDTO = new LocationDTO( 40.85, 25.9999 );
    location = new Location( locationDTO );
    location.setId( 1L );
    riderDTO = new RiderDTO( "ma@gmail.com", "Manuel Antunes", "migant", true, location, 0, 0 );
    rider = new Rider( riderDTO );
    rider.setRiderId( 1L );
  }
  
  @Test
  void whenPostRider_thenCreateRider() throws Exception {
    
    when( riderService.saveRider( Mockito.any() ) ).thenReturn( rider );
    
    mvnForTests.perform( MockMvcRequestBuilders.post( "/api/delivera/rider" )
                                               .contentType( MediaType.APPLICATION_JSON )
                                               .content( ua.tqs.delivera.JSONUtil.toJson( rider ) ) )
               .andExpect( MockMvcResultMatchers.status().isCreated() )
               .andExpect( MockMvcResultMatchers.jsonPath( "$.name", Matchers.is( rider.getName() ) ) )
               .andExpect( MockMvcResultMatchers.jsonPath( "$.riderId", Matchers.is( rider.getRiderId().intValue() ) ) )
               .andExpect( MockMvcResultMatchers.jsonPath( "$.email", Matchers.is( rider.getEmail() ) ) );
    verify( riderService, times( 1 ) ).saveRider( Mockito.any() );
  }
  
}
