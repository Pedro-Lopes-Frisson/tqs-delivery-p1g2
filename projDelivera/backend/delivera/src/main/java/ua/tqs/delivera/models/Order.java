package ua.tqs.delivera.models;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "delivera_order")
public class Order {
  @ToString.Include
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  
  @ToString.Include
  @Column(name = "external_id")
  private Long externalId;
  
  @Column(name = "order_delivery_id")
  private OrderProfit orderProfit;
  
  @Column(name = "shop_location")
  String shopLocation;
  
  @Column(name = "client_location")
  String clientLocation;
}