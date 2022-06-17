package ua.tqs.delivera.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ua.tqs.delivera.datamodels.RiderDTO;

import java.util.ArrayList;
import java.util.List;

// @Data
@Entity
@Table(name = "rider")
@Getter
@Setter
public class Rider {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long riderId;
  
  @Column
  private String name;
  
  @Column
  private String email;
  
  @Column
  private String password;
  
  @Column
  private boolean available;
  
  @Column
  private int numberOfReviews;
  
  @Column
  private int sumOfReviews;
  
  
  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "location_id")
  private Location currentLocation;
  
  @JsonIgnore
  @OneToMany(mappedBy = "rider", fetch = FetchType.EAGER)
  private List<OrderProfit> orderProfits = new ArrayList<>();
  
  
  // ------------------------------------ GETTERS AND SETTERS ------------------------------------
  public List<OrderProfit> getOrderProfits() {return orderProfits;}
  
  public void setOrderProfits( List<OrderProfit> orderProfits ) {this.orderProfits = orderProfits;}
  
  public Rider() {}
  
  public Rider( String email, String name, String password, boolean available, Location currentLocation,
                int numberOfReviews, int sumOfReviews ) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.available = available;
    this.currentLocation = currentLocation;
    this.numberOfReviews = numberOfReviews;
    this.sumOfReviews = sumOfReviews;
  }
  
  public Rider( RiderDTO dto ) {
    this.email = dto.getEmaildto();
    this.name = dto.getNamedto();
    this.password = dto.getPassworddto();
    this.available = dto.isAvailabledto();
    this.currentLocation = dto.getLocationdto();
    this.numberOfReviews = dto.getNumberOfReviewsdto();
    this.sumOfReviews = dto.getSumOfReviewsdto();
  }
  
  public Long getRiderId() {
    return this.riderId;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public boolean isAvailable() {
    return this.available;
  }
  
  public Location getlLocation() {
    return this.currentLocation;
  }
  
  public int getNumberOfReviews() {
    return this.numberOfReviews;
  }
  
  public int getSumOfReviews() {
    return this.sumOfReviews;
  }
  
  public void setLocation( double lat, double lon ) {
    this.currentLocation.setLatitude( lat );
    this.currentLocation.setLongitude( lon );
  }
  
  public void setRiderId( Long id ) {
    this.riderId = id;
  }
  
  public void setName( String name ) {
    this.name = name;
  }
  
  public void setEmail( String email ) {
    this.email = email;
  }
  
  public void setPassword( String password ) {
    this.password = password;
  }
  
  public void setAvailable( boolean available ) {
    this.available = available;
  }
  
  public void setNumberOfReviews( int numberOfReviews ) {
    this.numberOfReviews = numberOfReviews;
  }
  
  public void setSumOfReviews( int sumOfReviews ) {
    this.sumOfReviews = sumOfReviews;
  }
  
  public Location getCurrentLocation() {
    return currentLocation;
  }
  
  public void setCurrentLocation( Location currentLocation ) {
    this.currentLocation = currentLocation;
  }
}