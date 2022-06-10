package ua.tqs.frostini.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

    User u = createUser( 1 );
    Address a = createAddress( 1 );
    AddressDTO aDto = createAddressDto( 1 );

    @Test
    void whenPostNewAddress_ThenReturnAddress() {
        when( userRepository.findById( a.getUser().getId() ) ).thenReturn( Optional.of( u ) );

        Address address = addressService.getAddress( aDto );

        assertThat(address.getUser(), equalTo(u));
        assertThat(address.getLatitude(), equalTo(a.getLatitude()));
        assertThat(address.getLongitude(), equalTo(a.getLongitude()));

        verify( userRepository, times( 1 ) ).findById( a.getUser().getId() );
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
        return new Address( i, u, null, 40.640506, -8.653754 );
    }

    private AddressDTO createAddressDto( int i ) {
        return new AddressDTO( u.getId(), 40.640506, -8.653754 );
    }
}
