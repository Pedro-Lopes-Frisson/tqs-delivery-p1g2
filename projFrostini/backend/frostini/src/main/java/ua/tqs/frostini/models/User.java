package ua.tqs.frostini.models;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;
  
  @Column(name = "name")
  String name;
  
  @Column(name = "password")
  String password;
  
  @Column(name = "admin")
  boolean isAdmin;
  
  @OneToMany(mappedBy = "user")
  Set<Address> address;
}
