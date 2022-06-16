package ua.tqs.frostini.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tqs.frostini.datamodels.AddressDTO;
import ua.tqs.frostini.models.Address;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.repositories.AddressRepository;
import ua.tqs.frostini.repositories.UserRepository;

@Service
public class AddressService {

    @Autowired UserRepository userRepository;
    @Autowired AddressRepository addressRepository;

    public Address getAddress(AddressDTO addressDto) {
        // Get User and make sure the user exists
        Optional<User> userOptional = userRepository.findById( addressDto.getUserId() );
        if ( userOptional.isEmpty() ) {
            return null;
        }
        User user = userOptional.get();
        
        // Get address and check if exists
        Optional<Address> addressOptional = addressRepository.findByUserAndLatitudeAndLongitude(user, addressDto.getLatitude(), addressDto.getLongitude());
        if ( addressOptional.isEmpty() ) {
            Address a = new Address();
            a.setUser(user);
            a.setLatitude(addressDto.getLatitude());
            a.setLongitude(addressDto.getLongitude());

            addressRepository.save(a);
            return a;
        }

        return addressOptional.get();
    }
    
}
