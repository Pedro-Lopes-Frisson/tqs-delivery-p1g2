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
  @OneToMany(mappedBy = "address")
  private List<Order> order;
  
  @Column(name = "latitude")
  double latitude;

  @Column(name = "longitude")
  double longitude;
}
