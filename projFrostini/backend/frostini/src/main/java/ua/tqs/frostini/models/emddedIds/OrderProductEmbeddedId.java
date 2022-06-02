package ua.tqs.frostini.models.emddedIds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.tqs.frostini.models.Order;
import ua.tqs.frostini.models.Product;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable public class OrderProductEmbeddedId implements Serializable {
  private static final long serialVersionUID = - 8042472137115735872L;
  @Column(name = "order_id")
  private Long orderId;
  
  @Column(name = "product_id")
  private Long productId;
  
}