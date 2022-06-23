package ua.tqs.delivera.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "order_profit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderProfit {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  Long id;
  
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "rider_rider_id")
  Rider rider;
  
  @OneToOne
  @JsonIgnore
  @JoinColumn(name = "order_id")
  Order order;
  
  @Column(name = "order_price")
  Double orderPrice;
  
  @Override public String toString() {
    return "OrderProfit{" +
      "id=" + id +
      ", rider=" + rider +
      ", orderPrice=" + orderPrice +
      '}';
  }
}