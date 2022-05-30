package ua.tqs.delivera.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.RiderRepository;

@Service
public class RiderService {
    @Autowired
    private RiderRepository riderRepo;
    //create rider
    public Rider saveRider(Rider rider){
        return riderRepo.save(rider);
    }
}
