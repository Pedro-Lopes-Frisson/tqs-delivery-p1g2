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
        Optional<Address> addressOptional = addressRepository.findByUserAndStreetAndCityAndZipCode(user, addressDto.getStreet(), addressDto.getCity(), addressDto.getZipCode());
        if ( addressOptional.isEmpty() ) {
            Address a = new Address();
            a.setUser(user);
            a.setStreet(addressDto.getStreet());
            a.setCity(addressDto.getCity());
            a.setZipCode(addressDto.getZipCode());

            addressRepository.save(a);
            return a;
        }

        return addressOptional.get();
    }

    // check if user exist, if not, return null
    // check if address exists, if yes, return the id
    // if not, create and return id
    
}
