package ua.tqs.frostini.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.List;

@Entity
@Table(name = "`frostini_user`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "`user_id`")
  long id;
  
  @Column(name = "`name`")
  String name;
  
  @Column(name = "`password`")
  @Size(min = 8)
  String password;
  
  @Column(name = "`email`")
  @Email
  String email;
  
  @Column(name = "`admin`")
  boolean isAdmin;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  Address address;
  
  @JsonIgnore
  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
  Set<Order> order;
  
  @Override public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;
    User user = (User) o;
    return id == user.id && isAdmin == user.isAdmin && name.equals( user.name ) && password.equals( user.password ) &&
      email.equals( user.email ) && Objects.equals( address, user.address );
  }
  
  @Override public int hashCode() {
    return Objects.hash( id, name, password, email, isAdmin, address );
  }
}
