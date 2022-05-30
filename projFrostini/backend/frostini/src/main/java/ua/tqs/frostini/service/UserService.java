package ua.tqs.frostini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tqs.frostini.datamodels.UserDTO;
import ua.tqs.frostini.exceptions.DuplicatedUserException;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {
  
  @Autowired UserRepository userRepository;
  
  public User register( UserDTO userDTO ) throws DuplicatedUserException {
    Optional<User> optionalUser = userRepository.findByEmail( userDTO.getEmail() );
    
    if ( optionalUser.isPresent() ) {
      throw new DuplicatedUserException( "User Already Exists!" );
    }
    
    User userToSave = new User();
    userToSave.setEmail( userDTO.getEmail() );
    userToSave.setPassword( userDTO.getPwd() );
    userToSave.setName( userDTO.getName() );
    
    userRepository.save( userToSave );
    return userToSave;
    
  }
  
}
