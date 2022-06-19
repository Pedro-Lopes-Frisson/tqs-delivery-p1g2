package ua.tqs.frostini.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
  void whenPostNewAddress_ThenReturnAddress() {
    u.setAddress( a );
    when( userRepository.findById( any() ) ).thenReturn( Optional.of( u ) );
    
    Address address = addressService.getAddress( aDto );
    
    assertThat( address.getLatitude(), equalTo( a.getLatitude() ) );
    assertThat( address.getLongitude(), equalTo( a.getLongitude() ) );
    
    verify( userRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void whenPostNewAddress_ThenNull() {
    when( userRepository.findById( any() ) ).thenReturn( Optional.empty() );
    
    Address address = addressService.getAddress( aDto );
    
    assertNull( address );
    
    verify( userRepository, times( 1 ) ).findById( any() );
  }
  
  @Test
  void whenPostNewAddressInvalidUser_ThenReturnSaveAndReturnAddress() {
    when( userRepository.findById( any() ) ).thenReturn( Optional.of( u ) );
    when( addressRepository.save( any() ) ).thenReturn( a );
    
    Address address = addressService.getAddress( aDto );
    
    assertThat( address.getLatitude(), equalTo( a.getLatitude() ) );
    assertThat( address.getLongitude(), equalTo( a.getLongitude() ) );
    
    verify( userRepository, times( 1 ) ).findById( any() );
    verify( addressRepository, times( 1 ) ).save( any() );
  }
  
  @Test
  void whenPostNewAddressButPreviousAddressNotFound_ThenSaveNewAddressReturnAddress() {
    u.setAddress( a );
    when( userRepository.findById( any() ) ).thenReturn( Optional.of( u ) );
    when( addressRepository.findById( any() ) ).thenReturn( Optional.empty() );
    when( addressRepository.save( any() ) ).thenReturn( a );
    
    Address address = addressService.getAddress( aDto );
    
    assertThat( address.getLatitude(), equalTo( a.getLatitude() ) );
    assertThat( address.getLongitude(), equalTo( a.getLongitude() ) );
    
    verify( userRepository, times( 1 ) ).findById( any() );
    verify( addressRepository, times( 1 ) ).findById( any() );
    verify( addressRepository, times( 1 ) ).save( any() );
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
