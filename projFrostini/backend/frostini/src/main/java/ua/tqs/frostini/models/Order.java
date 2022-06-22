package ua.tqs.frostini.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;


import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "frostini_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  long id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "total_price")
  Double totalPrice;

  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @OneToMany(mappedBy = "order")
  List<OrderedProduct> orderedProductList;

  @Column(name = "order_state")
  String orderState = "ordered";
  
  @Column(name = "order_made_timestamp")
  Long orderMadeTimeStamp;
  
  @Override public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;
    Order order = (Order) o;
    return id == order.id && user.equals( order.user ) && totalPrice.equals( order.totalPrice ) &&
      orderState.equals( order.orderState ) && orderMadeTimeStamp.equals( order.orderMadeTimeStamp );
  }
  
  @Override public int hashCode() {
    return Objects.hash( id, user, totalPrice, orderState, orderMadeTimeStamp );
  }
}
