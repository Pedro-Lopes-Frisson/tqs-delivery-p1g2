package ua.tqs.frostini.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@Validated
@CrossOrigin
public class AddressController {

    /* @GetMapping("/address")
    public ResponseEntity<Object> getAddressId(@Valid @RequestBody AddressDTO addressDTO) {
        return null;
    } */

    // get address <user id, street, city, zip code>
    // post <user id, street, city, zip code>
    
}
