package ua.tqs.delivera.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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
  
  @Column(name = "name", unique = true)
  String name;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  Location address;
  
  @JsonIgnore
  @OneToMany(mappedBy = "store")
  @ToString.Exclude
  List<Order> orders;
  
  @Override public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;
    Store store = (Store) o;
    return name.equals( store.name ) && address.equals( store.address ) &&
      Objects.equals( orders, store.orders );
  }
  
  @Override public int hashCode() {
    return Objects.hash( name, address, orders );
  }
}
