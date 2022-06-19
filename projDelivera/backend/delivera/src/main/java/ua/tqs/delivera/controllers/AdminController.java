package ua.tqs.delivera.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.tqs.delivera.models.Admin;
import ua.tqs.delivera.services.AdminService;

import org.springframework.web.bind.annotation.CrossOrigin;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
@Validated
@CrossOrigin
public class AdminController {
  @Autowired AdminService adminService;
  @GetMapping("admin/{email}")
  public ResponseEntity<Admin> login( @PathVariable @Email @Valid String email ){
    Admin adminToLogin = adminService.login( email ) ;
    if (adminToLogin == null){
      return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( null );
    }
    return ResponseEntity.status( HttpStatus.OK ).body( adminToLogin );
  }
}
