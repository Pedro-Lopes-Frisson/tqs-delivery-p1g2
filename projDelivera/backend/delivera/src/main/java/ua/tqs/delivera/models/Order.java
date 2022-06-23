package ua.tqs.delivera.models;

import lombok.*;
import ua.tqs.delivera.datamodels.OrderDTO;

import javax.persistence.*;

@Entity @Table(name = "`order`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "store_id")
  Store store;
  
  
  @Column(name= "order_made_timestamp")
  Long orderMadeTimestamp;
  
  @Column(name= "delivered_timestamp")
  Long deliveredTimestamp;
  
  
  @Column(name = "order_state")
  String orderState = "ordered";

  public Order(OrderDTO orderDto) {
    this.externalId = orderDto.getExternalId();
    this.clientLocation = orderDto.getClientLocation();
    this.store = orderDto.getStore();
    this.orderMadeTimestamp = orderDto.getOrderMadeTimestamp();
  }
  
}