package ua.tqs.frostini.service;

import ua.tqs.frostini.datamodels.UserDTO;
import ua.tqs.frostini.exceptions.DuplicatedUserException;
import ua.tqs.frostini.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tqs.frostini.repositories.UserRepository;
import ua.tqs.frostini.services.UserService;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;
  
  
  @InjectMocks
  private UserService userService;
  
  private User user;
  private UserDTO userDto;
  
  @BeforeEach
  void setUp() throws IOException {
    user = new User( 1l, "Fernando", "12345678", "fernando@ua.pt", null, null );
    userDto = new UserDTO("Fernando", "12345678", "fernando@ua.pt");
    
  }
  
  @Test
  public void testRegister_whenEmailAlreadyInUse_thenDuplicatedUserException() {
    when( userRepository.findByEmail( user.getEmail() ) ).thenReturn( Optional.of( user ) );
    
    assertThrows( DuplicatedUserException.class, () -> {
      userService.register( userDto );
    } );
    
    verify( userRepository, times( 0 ) ).save( any() );
  }
  
  @Test
  @DisplayName("Register: everything is ok, then return")
  public void testRegister_whenEverythingIsOK_thenReturn() throws DuplicatedUserException {
    when( userRepository.findByEmail( user.getEmail() ) ).thenReturn( Optional.empty() );
    
    User userResp = userService.register( userDto );
    
    assertThat( userResp.getEmail(), equalTo( user.getEmail() ) );
    assertThat( userResp.getName(), equalTo( user.getName() ) );
    
    verify( userRepository, times( 1 ) ).save( any() );
  }
  
  
}
