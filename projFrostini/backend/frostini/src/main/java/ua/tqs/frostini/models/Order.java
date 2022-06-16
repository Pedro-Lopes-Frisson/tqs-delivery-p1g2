package ua.tqs.frostini.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;


import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
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

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @OneToMany(mappedBy = "order")
  List<OrderedProduct> orderedProductList;

}
