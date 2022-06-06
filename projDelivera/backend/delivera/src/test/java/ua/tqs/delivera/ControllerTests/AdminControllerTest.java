package ua.tqs.delivera.ControllerTests;


import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs.delivera.controllers.AdminController;
import ua.tqs.delivera.models.Admin;
import ua.tqs.delivera.services.AdminService;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(value = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {
  Admin admin;
  
  @Autowired
  private MockMvc mvc;
  
  @MockBean
  private AdminService adminService;
  
  @BeforeEach
  void setUp() {
    admin =
      Admin.builder().email( "admin.Alfredo@delivera.pt" ).name( "Alfredo Ferreira" ).password( "safepassword" )
           .build();
  }
  
  @Test
  public void testWhenLoginEndpointIsCalledWithCorrectEmail_ThenReturnAdminAndOkStatus() {
    when( adminService.login( admin.getEmail() ) ).thenReturn( admin );
    
    RestAssuredMockMvc.given()
                      .contentType( ContentType.JSON )
                      .get( "api/v1/admin/{email}", admin.getEmail() )
                      .then()
                      .contentType( ContentType.JSON )
                      .and().status( HttpStatus.OK )
                      .and().body( "name", is( admin.getName() ) )
                      .and().body( "email", is( admin.getEmail() ) )
                      .and().body( "password", is( admin.getPassword() ) );
    
    verify( adminService, times( 1 ) ).login( admin.getEmail() );
  }
  
  @Test
  public void testWhenLoginEndpointIsCalledWithInvalidEmail_ThenReturnBadRequeust() {
    
    when( adminService.login( any() ) ).thenReturn( null );
    
    RestAssuredMockMvc.given()
                      .contentType( ContentType.JSON )
                      .get( "api/v1/admin/{email}", "somerandom@email.pt" )
                      .then()
                      .contentType( ContentType.JSON )
                      .and().status( HttpStatus.BAD_REQUEST );
    
    verify( adminService, times( 0 ) ).login( any() );
  }
  
  @Test
  public void testWhenLoginEndpointIsCalledWithUnusedEmail_ThenReturnBadRequeust() {
    
    when( adminService.login( any() ) ).thenReturn( null );
    
    RestAssuredMockMvc.given()
                      .contentType( ContentType.JSON )
                      .get( "api/v1/admin/{email}", "somerandom@email.pt" )
                      .then()
                      .contentType( ContentType.JSON )
                      .and().status( HttpStatus.BAD_REQUEST );
    
    verify( adminService, times( 0 ) ).login( any() );
  }
  
}
