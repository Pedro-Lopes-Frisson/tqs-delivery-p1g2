package ua.tqs.delivera.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity @Table(name = "`store`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;
  
  @Column(name = "name")
  String name;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  Location address;
  
  @JsonIgnore
  @OneToMany(mappedBy = "store")
  @ToString.Exclude
  List<Order> orders;
  
}
