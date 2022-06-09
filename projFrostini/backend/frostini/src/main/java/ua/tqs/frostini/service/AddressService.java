package ua.tqs.frostini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tqs.frostini.datamodels.AddressDTO;
import ua.tqs.frostini.repositories.AddressRepository;

@Service
public class AddressService {

    @Autowired AddressRepository addressRepository;

    public Long getAddress(AddressDTO addressDto) {
        return null;
    }

    // check if user exist, if not, return null
    // check if address exists, if yes, return the id
    // if not, create and return id
    
}
