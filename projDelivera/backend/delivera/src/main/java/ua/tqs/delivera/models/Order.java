package ua.tqs.delivera.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @Table(name = "`order`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  
  
  @Column(name = "external_id", nullable = false)
  private Long externalId;
  
  @OneToOne
  @JoinColumn(name = "order_profit_id")
  private OrderProfit orderProfit;
  
  
  @OneToOne
  @JoinColumn(name = "location_id")
  private Location currentLocation;
  
  @Column(name = "shop_location", nullable = false)
  private String shopLocation;
  
  @Column(name = "client_location", nullable = false)
  private String clientLocation;
  
  
}