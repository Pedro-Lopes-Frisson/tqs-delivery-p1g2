package ua.tqs.delivera.utils;


import ua.tqs.delivera.models.Location;

public class DistanceCalculator {
  public static Double distanceBetweenPointsOnEarth( Location l1, Location l2 ){
  
  
    // The math module contains a function
    // named toRadians which converts from
    // degrees to radians.
    double lon1 = Math.toRadians( l1.getLongitude() );
    double lon2 = Math.toRadians( l2.getLongitude() );
    double lat1 = Math.toRadians( l1.getLatitude() );
    double lat2 = Math.toRadians( l2.getLatitude() );
  
    // Haversine formula
    double dLon = lon2 - lon1;
    double dLat = lat2 - lat1;
    double a = Math.pow( Math.sin( dLat / 2 ), 2 )
      + Math.cos( lat1 ) * Math.cos( lat2 )
      * Math.pow( Math.sin( dLon / 2 ), 2 );
  
    double c = 2 * Math.asin( Math.sqrt( a ) );
  
    // Radius of earth in kilometers. Use 3956
    // for miles
    double r = 6371;
  
    // calculate the result
    return ( c * r );
    
  }
}
