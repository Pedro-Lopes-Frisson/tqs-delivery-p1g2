package ua.tqs.delivera.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.tqs.delivera.models.Admin;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController("api/v1")
public class AdminController {
  @GetMapping("admin/{email}")
  public ResponseEntity<Admin> login( @PathVariable @Email @Valid String email ){
    return null;
  }
}
