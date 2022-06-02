package ua.tqs.delivera.ServiceTests;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.models.Rider;
import ua.tqs.delivera.repositories.RiderRepository;
import ua.tqs.delivera.services.RiderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RiderServiceTest {
    @Mock( lenient = true)
    private RiderRepository riderRepo;

    @InjectMocks
    private RiderService riderService;

    private Rider rider;
    private Rider riderSameEmail;
    private Location location;

    @BeforeEach
    public void setUp(){
        location = new Location(40.85, 25.9999);
        rider = new Rider("mal@gmail.com","Manuel Antunes", "migant", true, location, 0, 0);
        rider.setLocation(89.5566, 5.333);
        riderSameEmail = new Rider("mal@gmail.com","Maria Alberta", "migant", true, location, 0, 0);
        Mockito.when(riderRepo.save(rider)).thenReturn(rider);
    }

    @Test
    @DisplayName("Register: valid rider")
    void whenCreateValidRider_thenReturnRider(){

        Rider foundRider = riderService.saveRider(rider);
        assertThat(foundRider).isEqualTo(rider);
        verifySaveRiderIsCalledOnce();

    }

    @Test
    @DisplayName("Register: email provided already used")
    void whenCreateRiderWithAlreadyUsedEmail_thenReturnRider(){
        Mockito.when(riderRepo.save(riderSameEmail)).thenReturn(rider);

        // assertThrows(expectedThrowable, runnable)
        Rider foundRider = riderService.saveRider(rider);
        assertThat(foundRider).isEqualTo(rider);
        verifySaveRiderIsCalledOnce();

    }

    private void verifySaveRiderIsCalledOnce() {
        Mockito.verify(riderRepo, VerificationModeFactory.times(1)).save(any());
    }
    
}
