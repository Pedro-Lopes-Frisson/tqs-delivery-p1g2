package ua.tqs.frostini.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.tqs.frostini.datamodels.UserDTO;
import ua.tqs.frostini.exceptions.DuplicatedUserException;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
@Validated
public class UserController {
  @Autowired UserService userService;
  
  @PostMapping("/user")
  public ResponseEntity<User> register( @Valid @RequestBody UserDTO user ) {
    User uSaved;
    try {
      uSaved = userService.register( user );
    } catch (DuplicatedUserException e) {
      return ResponseEntity.status( HttpStatus.CONFLICT ).body( null );
    }
    return ResponseEntity.status( HttpStatus.OK ).body( uSaved );
  }
}
