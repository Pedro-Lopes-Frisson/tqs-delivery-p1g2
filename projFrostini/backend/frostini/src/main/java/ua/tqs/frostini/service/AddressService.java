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
  
  public Address getAddress( AddressDTO addressDto ) {
    // Get User and make sure the user exists
    Optional<User> userOptional = userRepository.findById( addressDto.getUserId() );
    if ( userOptional.isEmpty() ) {
      return null;
    }
    User user = userOptional.get();
    // Get address and check if exists
    if ( user.getAddress() == null ) {
      Address a = new Address();
      a.setLatitude( addressDto.getLatitude() );
      a.setLongitude( addressDto.getLongitude() );
      
      addressRepository.save( a ); // create entity
      
      user.setAddress( a );
      userRepository.save( user ); // update User to include Address
      
      return a;
    }
    
    Optional<Address> addressOptional = addressRepository.findById( user.getAddress().getId() );
    if ( addressOptional.isEmpty() ) {
      Address a = new Address();
      a.setLatitude( addressDto.getLatitude() );
      a.setLongitude( addressDto.getLongitude() );
      
      addressRepository.save( a );
      return a;
    }
    
    return addressOptional.get();
  }
  
}
