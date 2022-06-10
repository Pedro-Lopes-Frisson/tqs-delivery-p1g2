package ua.tqs.delivera.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private Long id;
  
  
  @ManyToOne
  @JoinColumn(name = "rider_rider_id")
  private Rider rider;
  
  
  @OneToOne
  @JoinColumn(name = "order_id")
  private Order order;
  
  @Column(name = "order_price")
  private long orderPrice;
  
}