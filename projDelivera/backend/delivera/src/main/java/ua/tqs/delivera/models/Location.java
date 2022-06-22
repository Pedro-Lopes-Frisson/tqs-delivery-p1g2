package ua.tqs.delivera.models;

import javax.persistence.*;

import lombok.*;
import ua.tqs.delivera.datamodels.LocationDTO;

import java.util.Objects;

// @Data
@Entity
@Table(name = "location")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private double latitude;
    @Column
    private double longitude;
    
    
    public Location(){}
    
    public Location(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public Location( LocationDTO dto){
        this.latitude=dto.getLatitude();
        this.longitude=dto.getLongitude();
    }

    public long getId(){
        return this.id;
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

    public void setId(Long id){
        this.id=id;
    }
    
    @Override public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Location location = (Location) o;
        return Double.compare( location.latitude, latitude ) == 0 &&
          Double.compare( location.longitude, longitude ) == 0;
    }
    
    @Override public int hashCode() {
        return Objects.hash( latitude, longitude );
    }
}
