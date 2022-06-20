package ua.tqs.frostini.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.frostini.datamodels.AddressDTO;
import ua.tqs.frostini.models.Address;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.repositories.AddressRepository;
import ua.tqs.frostini.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private AddressRepository addressRepository;
  
  @InjectMocks
  private AddressService addressService;
  
  User u;
  Address a;
  AddressDTO aDto;
  
  @BeforeEach
  void setUp() {
    u = createUser( 1 );
    a = createAddress( 1 );
    aDto = createAddressDto( 1 );
  }
  
  @Test
  void whenPostNewAddressIfUserDoesNotExistThenReturnNull() {
    when( userRepository.findById( any() ) ).thenReturn( Optional.empty() );
    assertNull( addressService.getAddress( aDto ) );
    verify( userRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void whenGetAddressWithValidUserThatHasNoAddressThenCreateAndSaveNewAddress() {
    u.setAddress( null );
    assertNull( u.getAddress() );
    when( userRepository.findById( any() ) ).thenReturn( Optional.of( u ) ); // return a user with null address
    when( addressRepository.save( any() ) ).thenReturn( a );
    
    
    Address address = addressService.getAddress( aDto );
    
    assertThat( address.getLatitude(), equalTo( a.getLatitude() ) );
    assertThat( address.getLongitude(), equalTo( a.getLongitude() ) );
    
    verify( addressRepository, times( 1 ) ).save( any() );
    verify( userRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void whenGetAddressWithExistentUserThatHasAUnsavedAddressCreateANewOneSaveAndReturn() {
    a.setId( 1L );
    u.setAddress( a );
    assertNotNull( u.getAddress() );
    when( userRepository.findById( any() ) ).thenReturn( Optional.of( u ) ); // return a user with null address
    when( addressRepository.findById( 1L ) ).thenReturn( Optional.empty() );
    when( addressRepository.save( any() ) ).thenReturn( a );
    
    
    Address address = addressService.getAddress( aDto );
    
    assertThat( address.getLatitude(), equalTo( a.getLatitude() ) );
    assertThat( address.getLongitude(), equalTo( a.getLongitude() ) );
    
    verify( addressRepository, times( 1 ) ).save( any() );
    verify( addressRepository, times( 1 ) ).findById( 1L );
    verify( addressRepository, times( 1 ) ).save( any() );
  }
  
  
  @Test
  void whenGetAddressWithExistentUserThatHasASavedAddressReturn() {
    a.setId( 1L );
    u.setAddress( a );
    when( userRepository.findById( any() ) ).thenReturn( Optional.of( u ) ); // return a user with null address
    when( addressRepository.findById( 1L ) ).thenReturn( Optional.of( a ) );
    
    Address address = addressService.getAddress( aDto );
    
    assertThat( address.getLatitude(), equalTo( a.getLatitude() ) );
    assertThat( address.getLongitude(), equalTo( a.getLongitude() ) );
    
    verify( addressRepository, times( 1 ) ).findById( 1L );
    verify( addressRepository, times( 0 ) ).save( any() );
  }
  
  
  
  /* helpers */
  private User createUser( int i ) {
    User u = new User();
    u.setName( "Pedro" );
    u.setPassword( "safepassword" );
    u.setEmail( "pdfl" + i + "@ua.pt" );
    return u;
  }
  
  private Address createAddress( int i ) {
    return new Address( i, null, 40.640506, - 8.653754 );
  }
  
  private AddressDTO createAddressDto( int i ) {
    return new AddressDTO( u.getId(), 40.640506, - 8.653754 );
  }
}
