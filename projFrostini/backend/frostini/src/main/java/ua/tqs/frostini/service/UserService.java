package ua.tqs.frostini.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tqs.frostini.datamodels.UserDTO;
import ua.tqs.frostini.exceptions.DuplicatedUserException;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.repositories.UserRepository;

import java.util.Optional;

@Service
@Log4j2
public class UserService {
  
  @Autowired UserRepository userRepository;
  
  public User register( UserDTO userDTO ) throws DuplicatedUserException {
    Optional<User> optionalUser = userRepository.findByEmail( userDTO.getEmail() );
    
    if ( optionalUser.isPresent() ) {
      log.info( "User already registered with email {}.", userDTO.getEmail() );
      throw new DuplicatedUserException( "User Already Exists!" );
    }
    
    User userToSave = new User();
    userToSave.setEmail( userDTO.getEmail() );
    userToSave.setPassword( userDTO.getPwd() );
    userToSave.setName( userDTO.getName() );
  
    userRepository.save( userToSave );
    log.info( "User with email {} is now registered.", userDTO.getEmail() );
    return userToSave;
    
  }
  
  public User login( String email ) {
    
    Optional<User> optionalUser = userRepository.findByEmail( email );
    
    if ( optionalUser.isEmpty() ) {
      log.info( "No such email!" );
      return null;
    }
  
    log.info( "Got User: {} ", optionalUser.get() );
    return optionalUser.get();
  }
}
