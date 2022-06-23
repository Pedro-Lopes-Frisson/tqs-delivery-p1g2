package ua.tqs.frostini.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.tqs.frostini.models.emddedIds.OrderProductEmbeddedId;

import javax.persistence.*;

@Entity
@Table(name = "frostini_ordered_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProduct {
  
  @Column(name = "quantity")
  long quantity;
  
  @Column(name = "price")
  double price;
  
  @EmbeddedId
  @JsonIgnore
  private OrderProductEmbeddedId orderEmbeddedId;
  
  @MapsId("order_id")
  @ManyToOne
  @JoinColumn(name = "order_id")
  @JsonIgnore
  Order order;
  
  
  @MapsId("product_id")
  @ManyToOne
  @JoinColumn(name = "product_id")
  Product product;
  
  
}
