package ua.tqs.frostini.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "frostini_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Address {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  long id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  
  @JsonIgnore
  @OneToMany(mappedBy = "address")
  private List<Order> order;
  
  @Column(name = "street")
  String street;

  @Column(name = "city")
  String city;
  
  @Column(name = "zip_code")
  String zipCode;
}
