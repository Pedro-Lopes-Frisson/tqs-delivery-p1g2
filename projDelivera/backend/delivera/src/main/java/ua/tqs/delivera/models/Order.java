package ua.tqs.delivera.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  Long id;
  
  
  @Column(name = "external_id", nullable = false)
  Long externalId;
  
  @OneToOne
  @JoinColumn(name = "order_profit_id")
  OrderProfit orderProfit;
  
  
  @OneToOne
  @JoinColumn(name = "location_id")
  Location currentLocation;
  
  //@Column(name = "shop_location", nullable = false)
  // String shopLocation;
  
  @Column(name = "client_location", nullable = false)
  String clientLocation;
  
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "store_id")
  Store store;
  
  
}