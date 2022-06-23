package ua.tqs.delivera.utilstest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.tqs.delivera.models.Location;
import ua.tqs.delivera.utils.DistanceCalculator;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DistanceCalculatorTest {
  
  @ParameterizedTest
  @MethodSource("distancesGen")
  void assertDistances( Location l1, Location l2, double dist ) {
    assertThat( DistanceCalculator.distanceBetweenPointsOnEarth( l1, l2 ) ).isEqualTo( dist );
  }
  
  
  public static Stream<Arguments> distancesGen() {
    return Stream.of(
      Arguments.arguments( new Location( 53.32, 53.31 ),
        new Location( - 1.72, - 1.69 ), 7943.096602114035 ),
      Arguments.arguments( new Location( 13, 14 ),
        new Location( 4, 14 ), 1000.7543398010286 ),
      Arguments.arguments( new Location( 14, 0 ),
        new Location( 10, 14 ), 1585.845077250938 )
    
    );
  }
}
