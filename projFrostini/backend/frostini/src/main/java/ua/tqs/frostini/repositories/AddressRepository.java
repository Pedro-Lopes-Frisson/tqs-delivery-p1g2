package ua.tqs.frostini.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tqs.frostini.models.Address;
import ua.tqs.frostini.models.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByUserAndStreetAndCityAndZipCode(User user, String street, String city, String zipCode);
}
