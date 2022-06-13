package ua.tqs.delivera.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import ua.tqs.delivera.exceptions.NonExistentResource;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.RiderRepository;

@Service
public class RiderService {
    @Autowired
    private RiderRepository riderRepo;
    //create rider
    public Rider saveRider(Rider rider){
        System.out.println(riderRepo.findByEmail(rider.getEmail()));
        if (!riderRepo.findByEmail(rider.getEmail()).isPresent())
            return riderRepo.save(rider);
        throw new DuplicateKeyException("Email already in use");
    }

    public Map<String, Object> getRiderStatistics(long riderId) throws NonExistentResource {
        // check if rider exists
        Optional<Rider> rider = riderRepo.findById(riderId);
        if (rider.isEmpty()) {
            throw new NonExistentResource( "This rider does not exist!" );
        }

        Map<String, Object> result = new HashMap<>();

        if(rider.get().getNumberOfReviews() != 0) {

            double average = (double) rider.get().getSumOfReviews()/rider.get().getNumberOfReviews();
            result.put("averageReviewValue", average);
        }

        

        // TODO: totalNumberOfOrdersDelivered

        return result;
    }
}
