package ua.tqs.frostini.controller;

import java.io.IOException;
import java.util.Collections;

import org.apache.tomcat.jni.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import ua.tqs.frostini.datamodels.AddressDTO;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.service.AddressService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(value = AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {
    
  @Autowired
  private MockMvc mvc;

  @MockBean
  private AddressService addressService;

  private Address address = createAddress(1);

  AddressDTO addressDTO;

  @BeforeEach
  void setUp() throws IOException {
    RestAssuredMockMvc.mockMvc( mvc );
    /* userOrders = new ArrayList<>();
    userOrders.add( createOrder( 1 ) );
    System.out.println( userOrders.get( 0 ) );
    userOrders.add( createOrder( 2 ) );
    userOrders.add( createOrder( 3 ) );
    userOrders.add( createOrder( 4 ) ); */

    addressDTO = createAddressDTO( 1 );
    /* order1DTO = createOrderDTO( 2 );
    order2DTO = createOrderDTO( 3 ); */

  }

  @Test
  void test_Then() {
    when( addressService.getAddress( any() ) ).thenReturn( address.getId() );
  }

  /* helpers */

  private Address createAddress( int i ) {

    User u = new User( i, "Pedro", "safepassword", "pdfl" + i + "@ua.pt", false, null, null );

    Address a = new Address( i, u, null, "Street, " + i, "City", "234" );

    Address a1 = new Address( i, u, Collections.emptyList(), "Street, " + i, "City", "234" );

    Address a2 = new Address();
    a2.setUser(1);
    a2.setStreet("Street, " + i);

    return a;
  }

  private AddressDTO createAddressDTO( int i ) {


    AddressDTO addressDto = new AddressDTO();

    addressDto.setUserId(address.getUser());
    addressDto.setStreet("Street, " + i);
    addressDto.setCity("City");
    addressDto.setZipCode("234");

    return addressDto;
  }
}
