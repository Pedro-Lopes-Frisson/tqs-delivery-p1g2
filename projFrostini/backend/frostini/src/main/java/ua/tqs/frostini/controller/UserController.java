package ua.tqs.frostini.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.tqs.frostini.datamodels.UserDTO;
import ua.tqs.frostini.exceptions.DuplicatedUserException;
import ua.tqs.frostini.models.User;
import ua.tqs.frostini.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("api/v1")
@Validated

@CrossOrigin("http://localhost:3000")
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
    return ResponseEntity.status( HttpStatus.CREATED ).body( uSaved );
  }

  @GetMapping("/user/{email}")
  public ResponseEntity<User> login( @PathVariable @Valid @Email String email ) {
    User user = userService.login( email );

    if ( user == null ) {
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }

    return ResponseEntity.status( HttpStatus.OK ).body( user );
  }

}
