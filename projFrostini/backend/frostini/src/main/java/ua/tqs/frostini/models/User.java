package ua.tqs.frostini.models;


import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "frostini_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id")
  long id;
  
  @Column(name = "name")
  String name;
  
  @Column(name = "`password`")
  @Size(min=8)
  String password;
  
  @Column(name = "`email`")
  @Email
  String email;
  
  @Column(name = "`admin`")
  boolean isAdmin;
  
  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
  Set<Address> address;
  
  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
  Set<Order> order;
}
