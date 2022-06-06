package ua.tqs.delivera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tqs.delivera.models.Admin;
import ua.tqs.delivera.repositories.AdminRepository;

@Service
public class AdminService {
  @Autowired AdminRepository adminRepository;
  
  public Admin login( String email ) {
    return null;
  }
}


