package ua.tqs.delivera.serviceTests;

import java.util.HashMap;
import java.util.Map;
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

    @Test
    void whenGetRiderStatsWithZeroReviews_thenReturnMap() {
        Mockito.when(riderRepo.findById(Mockito.any())).thenReturn(Optional.of(rider));
        rider.setRiderId(1l);
        riderService.saveRider(rider);

        Map<String, Object> stats = riderService.getRiderStatistics(rider.getRiderId());
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("averageReviewValue", 0.0);

        assertThat(stats).isEqualTo(expected);
    }

    @Test
    void whenGetRiderStats_thenReturnMap() {
        Mockito.when(riderRepo.findById(Mockito.any())).thenReturn(Optional.of(rider));
        rider.setRiderId(1l);
        rider.setNumberOfReviews(10);
        rider.setSumOfReviews(35);
        riderService.saveRider(rider);

        Map<String, Object> stats = riderService.getRiderStatistics(rider.getRiderId());
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("averageReviewValue", rider.getSumOfReviews()/rider.getNumberOfReviews()*1.0);

        assertThat(stats).isEqualTo(expected);
    }

    @Test
    void whenGetRiderStatsWithInvalidRider_thenReturnNull() {
        Map<String, Object> stats = riderService.getRiderStatistics(10l);

        assertThat(stats).isNull();
    }

    private void verifySaveRiderIsCalledOnce() {
        Mockito.verify(riderRepo, VerificationModeFactory.times(1)).save(any());
    }
    
}
