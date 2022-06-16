package ua.tqs.delivera.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.tqs.delivera.models.Admin;
import ua.tqs.delivera.services.AdminService;

import org.springframework.web.bind.annotation.CrossOrigin;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
@Validated
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
