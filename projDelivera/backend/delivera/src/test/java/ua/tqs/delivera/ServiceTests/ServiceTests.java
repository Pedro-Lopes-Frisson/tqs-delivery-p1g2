package ua.tqs.delivera.ServiceTests;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.RiderRepository;
import ua.tqs.delivera.services.RiderService;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
    @Mock( lenient = true)
    private RiderRepository riderRepo;

    @InjectMocks
    private RiderService riderService;

    private Rider rider;

    @BeforeEach
    public void setUp(){
        //(String name, String password, boolean available, 
        // Location currentLocation, int numberOfReviews, 
        // int sumOfReviews)
        rider = new Rider("Manuel Antunes", "migant", true, new Location(40.85, 25.9999), 0, 0);

        // Mockito.when(riderRepo.save(rider)).thenReturn(Optional.of(rider));
    }

    @Test
    void whenCreateCar_thenCarShouldBeSaved(){

    }
    
}
