package ua.tqs.delivera.models;

public class LocationDTO {
    private double latitude;
    private double longitude;
    public LocationDTO(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }
}
