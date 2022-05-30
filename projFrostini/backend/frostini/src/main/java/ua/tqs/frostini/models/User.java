package ua.tqs.frostini.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private long id;
  
  private String name;
  
  @Size(min=8)
  private String pwd;
  
  @Email
  @Column(unique = true)
  @EqualsAndHashCode.Include
  private String email;
  
  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
  @ToString.Exclude
  private List<Order> order;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  @ToString.Exclude
  private List<Address> addresses;
}
