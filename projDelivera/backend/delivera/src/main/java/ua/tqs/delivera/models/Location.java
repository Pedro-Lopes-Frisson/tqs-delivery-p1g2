package ua.tqs.delivera.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @Column
    private long riderId;
    @Column
    private double latitude;
    @Column
    private double longitude;
    
    public Location(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }
    public double getLongitude(){
        return this.longitude;
    }
    
    public void setLatitude(double lat){
        this.latitude = lat;
    }

    public void setLongitude(double lon){
        this.longitude = lon;
    }

}
